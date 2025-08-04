package com.est.mungpe.post.service.Impl;

import com.est.mungpe.elasitc.domain.Type;
import com.est.mungpe.elasitc.dto.SearchDto;
import com.est.mungpe.elasitc.repository.ElasticRepository;
import com.est.mungpe.exception.ElasticSearchException;
import com.est.mungpe.exception.ExceptionMessage;
import com.est.mungpe.exception.PostNotFound;
import com.est.mungpe.image.domain.Image;
import com.est.mungpe.image.domain.ImageDomainType;
import com.est.mungpe.member.domain.Member;
import com.est.mungpe.member.service.MemberService;
import com.est.mungpe.post.domain.Board;
import com.est.mungpe.post.dto.*;
import com.est.mungpe.post.postRepository.BoardRepository;

import com.est.mungpe.image.service.ImageConvertService;
import com.est.mungpe.image.service.ImageService;
import com.est.mungpe.image.service.S3StorageService;
import com.est.mungpe.post.service.BoardService;
import com.est.mungpe.system.utill.SecurityUtill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final S3StorageService s3StorageService;
    private final ImageService imageService;
    private final ImageConvertService imageConvertService;
    private final MemberService memberService;
    private final ElasticRepository elasticRepository;

    @Override
    @Transactional(readOnly = false)
    public BoardMessageResponse createPost(BoardRequest request, List<MultipartFile> sourceImage) throws IOException {

        // 포스트 저장
        Board newPost = savePost(request);

        log.info("newPost = {}", newPost);

        List<byte[]> files = imageConvertService.convert(sourceImage);

        for (byte[] fileData : files) {

            //유니크 파일 경로 만들기
            String fileName = UUID.randomUUID().toString();

            //s3 업로드
            String imageUrl = s3StorageService.upload(fileData,fileName);
            log.info("imageUrl = {}", imageUrl);

            // product image 객체 생성
            imageService.savePostImage(imageUrl, newPost.getId(), ImageDomainType.POST);
        }

        //엘라스틱 서치에 추가
        SearchDto dto = new SearchDto();
        dto.setId(Type.BOARDDATA + "_" + newPost.getId().toString());
        dto.setTitle(newPost.getTitle());
        dto.setContent(newPost.getContent());
        dto.setType(Type.BOARDDATA); // 게시판 데이터
        elasticRepository.save(dto);

        return BoardMessageResponse.builder()
                .message("Success Created Post")
                .result(true)
                .build();
    }

    @Override
    @Transactional(readOnly = false)
    public BoardMessageResponse updatePost(Long postId, BoardRequest request, List<MultipartFile> sourceImage) throws IOException {

        log.info("request.getTitle() = {}", request.getTitle());
        log.info("request.getContent() = {}", request.getContent());

        Board post = boardRepository.findById(postId)
                .orElseThrow(() -> new PostNotFound(ExceptionMessage.POST_NOT_FOUND));

        log.info("post = {}", post);

        post.update(request.getTitle(),request.getContent());
        boardRepository.save(post);

        List<Image> beforeImages = imageService.getImageByPostNum(postId,ImageDomainType.POST);

        //기존 이미지 삭제
        for (Image image : beforeImages) {
            String imageUrl = image.getImageUrl();
            String key = imageUrl.replaceFirst("https://mungpedia.s3.ap-northeast-2.amazonaws.com/", "");
            s3StorageService.delete(key);
            imageService.deleteImage(image);
        }



        // 새로운 이미지 컨버트
        List<byte[]> newImage = imageConvertService.convert(sourceImage);

        for (byte[] fileData : newImage) {

            //유니크 파일 경로 만들기
            String fileName = UUID.randomUUID().toString();

            //s3 업로드
            String imageUrl = s3StorageService.upload(fileData,fileName);
            log.info("imageUrl = {}", imageUrl);

            // product image 객체 생성
            imageService.savePostImage(imageUrl, post.getId(), ImageDomainType.POST);
        }


        //엘라스틱 서치에 수정
        String elasticId = Type.BOARDDATA + "_" + post.getId().toString();
        SearchDto searchDto = elasticRepository.findById(elasticId)
                .orElseThrow(() -> new ElasticSearchException(ExceptionMessage.ELASTIC_NOT_FOUND));
        searchDto.setTitle(post.getTitle());
        searchDto.setContent(post.getContent());
        elasticRepository.save(searchDto);


        return BoardMessageResponse.builder()
                .message("Success updated Post")
                .result(true)
                .build();
    }

    @Override
    @Transactional(readOnly = false)
    public BoardMessageResponse deletePost(Long postId) {

        Board post = boardRepository.findById(postId)
                .orElseThrow(() -> new PostNotFound(ExceptionMessage.POST_NOT_FOUND));

        List<Image> beforeImages = imageService.getImageByPostNum(postId,ImageDomainType.POST);

        //기존 이미지 삭제
        for (Image image : beforeImages) {
            String imageUrl = image.getImageUrl();
            String key = imageUrl.replaceFirst("https://mungpedia.s3.ap-northeast-2.amazonaws.com/", "");
            s3StorageService.delete(key);
            imageService.deleteImage(image);
        }

        // Elasticsearch 색인 삭제
        String elasticId = Type.BOARDDATA + "_" + postId.toString();
        elasticRepository.deleteById(elasticId);

        boardRepository.delete(post);

        return BoardMessageResponse.builder()
                .message("Success deleted Post")
                .result(true)
                .build();

    }

    @Override
    public GetBoardMessageReponse getPost(Long postId) {
        Board post = boardRepository.findById(postId)
                .orElseThrow(() -> new PostNotFound(ExceptionMessage.POST_NOT_FOUND));

        Member member = post.getMember();

        List<Image> images = imageService.getImageByPostNum(postId,ImageDomainType.POST);

        boolean isLiked;

        log.info("SecurityUtill.isLogged() = {}", SecurityUtill.isLogged());

        if (SecurityUtill.isLogged()) {

            Long loginedMemberId = SecurityUtill.getCurrentMemberId();

            log.info("loginedMemberId = {}", loginedMemberId);;

            log.info("post.getLikedMemberIds() = {}", post.getLikedMemberIds());;

            isLiked = post.isLiked(loginedMemberId);
            log.info("isLiked = {}", isLiked);

        } else {
            isLiked = false;
            log.info("로그인을 하지 않음 isLiked = {}", isLiked);
        }

        int likeCount = post.getLikeCount();

        return GetBoardMessageReponse.builder()
                .message("Success getPost")
                .result(true)
                .images(images)
                .id(post.getId())
                .title(post.getTitle())
                .updatedAt(post.getUpdatedAt())
                .content(post.getContent())
                .memberId(member.getId())
                .memberNickname(member.getNickname())
                .profileUrl(member.getProfileImageUrl())
                .isLiked(isLiked)
                .likeCount(likeCount)
                .build();
    }

    public Board savePost(BoardRequest request) {

        Long memberId = SecurityUtill.getCurrentMemberId();

        Member member = memberService.getMemberById(memberId);

        Board newBoard = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .build();

        boardRepository.save(newBoard);

        return newBoard;
    }


    @Override
    public Top2PostResponse getTop2Post() {

        List<Board> top2Post = boardRepository.findTop2ByOrderByUpdatedAtDesc();

        List<BoardDto> boardDtos = top2Post.stream()
                .map(board -> BoardDto.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .createdAt(board.getCreatedAt())
                        .updatedAt(board.getUpdatedAt())
                        .likedMemberIds(board.getLikedMemberIds())
                        .build()
                )
                .toList();

        List<PostImageDto> Responseimages = new ArrayList<>();

        for (Board board : top2Post) {

            try {
                Long id = board.getId();

                Image firstImage = imageService.getFirstIamgeByPostNum(id,ImageDomainType.POST);

                if (firstImage == null) {
                    Responseimages.add(null);
                } else {
                    Responseimages.add(new PostImageDto(firstImage.getPostNumber(), firstImage.getImageUrl()));
                }

                log.info("firstImage = {}", firstImage.getImageUrl());

            } catch (Exception e) {
                log.warn("이미지 조회 중 에러 발생 - postId: {}", board.getId(), e);
                Responseimages.add(null);
            }

        }



        return Top2PostResponse.builder()
                .message("Success Get Top2 Post And Images")
                .result(true)
                .posts(boardDtos)
                .images(Responseimages)
                .build();
    }

    @Override
    public GetAllBoardResponse getAllBoard(int page) {
        log.info("page = {}", page);

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("updatedAt").descending());

        Page<Board> allBoard = boardRepository.findAll(pageable);
        log.info("allPedia = {}", allBoard);

        long totalPage = allBoard.getTotalPages();

        //피디아랑 이미지 매칭한 List
        List<MatchBoardAndImage> boardAndImages = new ArrayList<>();

        for (Board board : allBoard) {

            try {
                Long id = board.getId();

                GetBoardMessageReponse targetBoard = getPost(id);
                log.info("targetBoard = {}", targetBoard);


                Image firstImage = imageService.getFirstIamgeByPostNum(id,ImageDomainType.POST);
                log.info("firstImage = {}", firstImage);

                boardAndImages.add(new MatchBoardAndImage(targetBoard,firstImage));

                log.info("firstImage = {}", firstImage.getImageUrl());

            } catch (Exception e) {
                log.warn("전체 조회 중 에러 발생 - postId: {}", board.getId(), e);
            }

        }

        log.info("boardAndImages = {}", boardAndImages);

        return GetAllBoardResponse.builder()
                .message("Success Get All Pedia And Images")
                .result(true)
                .boardAndImages(boardAndImages)
                .totalPage(totalPage)
                .build();
    }



    @Override
    @Transactional(readOnly = false)
    public BoardLikeResponse addLikeOrRemoveLike(Long postId) {
        Board post = boardRepository.findById(postId).orElseThrow(
                () -> new PostNotFound(ExceptionMessage.POST_NOT_FOUND)
        );

        Long currentMemberId = SecurityUtill.getCurrentMemberId();

        boolean checkedLiked = post.isLiked(currentMemberId);

        String resultMessage = "";

        int resultCount = 0;
        boolean resultLiked = false;

        if (!checkedLiked) {
            post.addLike(currentMemberId);
            resultMessage = "Success Add Like Post";
            resultCount = post.getLikeCount();
            log.info("add after likeCount = {}", resultCount);
            resultLiked = post.isLiked(currentMemberId);

        } else {
            post.removeLike(currentMemberId);
            resultMessage = "Success Remove Like Post";
            resultCount = post.getLikeCount();
            log.info("remove after likeCount = {}", resultCount);
            resultLiked = post.isLiked(currentMemberId);
        }



        return BoardLikeResponse.builder()
                .message(resultMessage)
                .result(true)
                .isLiked(resultLiked)
                .likeCount(resultCount)
                .build();
    }




}
