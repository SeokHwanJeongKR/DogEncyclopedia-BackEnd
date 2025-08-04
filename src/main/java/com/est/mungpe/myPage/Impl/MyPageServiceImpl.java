package com.est.mungpe.myPage.Impl;


import com.est.mungpe.image.domain.Image;
import com.est.mungpe.image.domain.ImageDomainType;
import com.est.mungpe.image.service.ImageService;
import com.est.mungpe.myPage.MyPageService;
import com.est.mungpe.post.domain.Board;
import com.est.mungpe.post.dto.GetAllBoardResponse;
import com.est.mungpe.post.dto.GetBoardMessageReponse;
import com.est.mungpe.post.dto.MatchBoardAndImage;
import com.est.mungpe.post.postRepository.BoardRepository;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final ImageService imageService;

    @Override
    public GetAllBoardResponse getAllMyBoards(int page) {

        Long currentMemberId = SecurityUtill.getCurrentMemberId();

        Pageable pageable = PageRequest.of(page -1 , 3, Sort.by("updatedAt").descending());


        List<MatchBoardAndImage> boardAndImages = new ArrayList<>();

        Page<Board> allByMemberId = boardRepository.findAllByMemberId(currentMemberId, pageable);

        int totalPage = allByMemberId.getTotalPages();

        for (Board board : allByMemberId) {

            try {
                Long id = board.getId();

                //포스트 조회
                GetBoardMessageReponse targetBoard = boardService.getPost(id);
                log.info("targetBoard = {}", targetBoard);

                //첫째 이미지 조회
                Image firstImage = imageService.getFirstIamgeByPostNum(id, ImageDomainType.POST);
                log.info("firstImage = {}", firstImage);

                //포스트랑 이미지 합치기
                boardAndImages.add(new MatchBoardAndImage(targetBoard,firstImage));

                log.info("firstImage = {}", firstImage.getImageUrl());

            } catch (Exception e) {
                log.warn("전체 조회 중 에러 발생 - postId: {}", board.getId(), e);
            }

        }

        log.info("boardAndImages = {}", boardAndImages);

        return GetAllBoardResponse.builder()
                .message("Success Get All Post And Images")
                .result(true)
                .boardAndImages(boardAndImages)
                .totalPage(totalPage)
                .build();

    }

    @Override
    public GetAllBoardResponse getAllMyLikedBoard(int page) {

        Long currentMemberId = SecurityUtill.getCurrentMemberId();

        Pageable pageable = PageRequest.of(page -1 , 3, Sort.by("updatedAt").descending());


        List<MatchBoardAndImage> boardAndImages = new ArrayList<>();

        Page<Board> allByMemberId = boardRepository.findAllLikedByMemberId(currentMemberId, pageable);

        int totalPage = allByMemberId.getTotalPages();

        for (Board board : allByMemberId) {

            try {
                Long id = board.getId();

                //포스트 조회
                GetBoardMessageReponse targetBoard = boardService.getPost(id);
                log.info("targetBoard = {}", targetBoard);

                //첫째 이미지 조회
                Image firstImage = imageService.getFirstIamgeByPostNum(id, ImageDomainType.POST);
                log.info("firstImage = {}", firstImage);

                //포스트랑 이미지 합치기
                boardAndImages.add(new MatchBoardAndImage(targetBoard,firstImage));

                log.info("firstImage = {}", firstImage.getImageUrl());

            } catch (Exception e) {
                log.warn("전체 조회 중 에러 발생 - postId: {}", board.getId(), e);
            }

        }

        log.info("boardAndImages = {}", boardAndImages);

        return GetAllBoardResponse.builder()
                .message("Success Get All My Liked Post And Images")
                .result(true)
                .boardAndImages(boardAndImages)
                .totalPage(totalPage)
                .build();

    }
}
