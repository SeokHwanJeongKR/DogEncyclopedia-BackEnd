package com.est.mungpe.pedia.service.Impl;

import com.est.mungpe.elasitc.domain.Type;
import com.est.mungpe.elasitc.dto.SearchDto;
import com.est.mungpe.elasitc.repository.ElasticRepository;
import com.est.mungpe.exception.*;
import com.est.mungpe.image.domain.Image;
import com.est.mungpe.image.domain.ImageDomainType;
import com.est.mungpe.image.service.ImageConvertService;
import com.est.mungpe.image.service.ImageService;
import com.est.mungpe.image.service.S3StorageService;
import com.est.mungpe.pedia.domain.Pedia;
import com.est.mungpe.pedia.domain.PediaEditRequest;
import com.est.mungpe.pedia.dto.*;
import com.est.mungpe.pedia.repository.PediaEditRequestRepository;
import com.est.mungpe.pedia.repository.PediaRepository;
import com.est.mungpe.pedia.service.PediaEditRequestService;
import com.est.mungpe.pedia.service.PediaService;
import com.est.mungpe.post.dto.PostImageDto;
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
public class PediaServiceImpl implements PediaService {

    private final PediaRepository pediaRepository;
    private final PediaEditRequestRepository pediaEditRequestRepository;

    private final ImageConvertService imageConvertService;
    private final S3StorageService s3StorageService;
    private final ImageService imageService;
    private final ElasticRepository elasticRepository;
    private final PediaEditRequestService pediaEditRequestService;

    @Override
    @Transactional(readOnly = false)
    public PediaBasicResponse createPedia(PediaBasicRequest request, List<MultipartFile> sourceImage) throws IOException {

        Pedia newPedia = save(request);
        log.info("newPedia = {}", newPedia);

        List<byte[]> files = imageConvertService.convert(sourceImage);

        for (byte[] fileData : files) {

            //유니크 파일 경로 만들기
            String fileName = UUID.randomUUID().toString();

            //s3 업로드
            String imageUrl = s3StorageService.upload(fileData,fileName);
            log.info("imageUrl = {}", imageUrl);

            // product image 객체 생성
            imageService.savePostImage(imageUrl, newPedia.getId(), ImageDomainType.PEDIA);
        }

        String content = newPedia.getOrigin() + newPedia.getRecommendedExercise() + newPedia.getPersonality() + newPedia.getDiseases() + newPedia.getHistory();

        SearchDto dto = new SearchDto();
        dto.setId(Type.PEDIADATA + "_" + newPedia.getId().toString());
        dto.setTitle(newPedia.getName());
        dto.setContent(content);
        dto.setType(Type.PEDIADATA); // 게시판 데이터
        elasticRepository.save(dto);

        return PediaBasicResponse.builder()
                .message("Success Create Pedia")
                .result(true)
                .build();
    }

    @Override
    @Transactional(readOnly = false)
    public PediaBasicResponse updatePedia(Long postId, PediaBasicRequest request, List<MultipartFile> sourceImage) throws IOException {


        Pedia pedia = pediaRepository.findById(postId)
                .orElseThrow(() -> new PostNotFound(ExceptionMessage.POST_NOT_FOUND));

        log.info("pedia = {}", pedia);

        pedia.update(request);
        pediaRepository.save(pedia);

        List<Image> beforeImages = imageService.getImageByPostNum(postId,ImageDomainType.PEDIA);

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
            imageService.savePostImage(imageUrl, pedia.getId(), ImageDomainType.PEDIA);
        }

        //엘라스틱 서치에 수정
        String elasticId = Type.PEDIADATA + "_" + pedia.getId().toString();
        SearchDto searchDto = elasticRepository.findById(elasticId)
                .orElseThrow(() -> new ElasticSearchException(ExceptionMessage.ELASTIC_NOT_FOUND));

        String content = pedia.getOrigin() + pedia.getRecommendedExercise() + pedia.getPersonality() + pedia.getDiseases() + pedia.getHistory();

        searchDto.setTitle(pedia.getName());
        searchDto.setContent(content);
        elasticRepository.save(searchDto);

        return PediaBasicResponse.builder()
                .message("Success updated Pedia")
                .result(true)
                .build();
    }

    @Override
    @Transactional(readOnly = false)
    public PediaBasicResponse deletePedia(Long postId) {

        Pedia pedia = pediaRepository.findById(postId)
                .orElseThrow(() -> new PostNotFound(ExceptionMessage.POST_NOT_FOUND));

        List<Image> beforeImages = imageService.getImageByPostNum(postId,ImageDomainType.PEDIA);

        //기존 이미지 삭제
        for (Image image : beforeImages) {
            String imageUrl = image.getImageUrl();
            String key = imageUrl.replaceFirst("https://mungpedia.s3.ap-northeast-2.amazonaws.com/", "");
            s3StorageService.delete(key);
            imageService.deleteImage(image);
        }

        // Elasticsearch 색인 삭제
        String elasticId = Type.PEDIADATA + "_" + pedia.getId().toString();
        elasticRepository.deleteById(elasticId);


        pediaRepository.delete(pedia);

        return PediaBasicResponse.builder()
                .message("Success deleted Pedia")
                .result(true)
                .build();

    }

    @Override
    public GetPediaResponse getPedia(Long postId) {
        Pedia pedia = pediaRepository.findById(postId)
                .orElseThrow(() -> new PostNotFound(ExceptionMessage.POST_NOT_FOUND));

        log.info("pedia = {}", pedia);


        List<Image> images = imageService.getImageByPostNum(postId,ImageDomainType.PEDIA);

        return GetPediaResponse.builder()
                .message("Success getPost")
                .result(true)
                .images(images)
                .id(pedia.getId())
                .name(pedia.getName())
                .origin(pedia.getOrigin())
                .size(pedia.getSize())
                .parentingLevel(pedia.getParentingLevel())
                .sheddingLevel(pedia.getSheddingLevel())
                .walkLevel(pedia.getWalkLevel())
                .recommendedExercise(pedia.getRecommendedExercise())
                .barkingLevel(pedia.getBarkingLevel())
                .personality(pedia.getPersonality())
                .diseases(pedia.getDiseases())
                .history(pedia.getHistory())
                .updatedAt(pedia.getUpdatedAt())
                .build();
    }

    @Override
    public Top12PediaResponse getTop12Pedia() {

        List<Pedia> top12Pedia = pediaRepository.findTop12ByOrderByUpdatedAtDesc();

        List<PostImageDto> Responseimages = new ArrayList<>();

        for (Pedia pedia : top12Pedia) {

            try {
                Long id = pedia.getId();

                Image firstImage = imageService.getFirstIamgeByPostNum(id,ImageDomainType.PEDIA);

                if (firstImage == null) {
                    Responseimages.add(null);
                } else {
                    Responseimages.add(new PostImageDto(firstImage.getPostNumber(), firstImage.getImageUrl()));
                }

                log.info("firstImage = {}", firstImage.getImageUrl());

            } catch (Exception e) {
                log.warn("이미지 조회 중 에러 발생 - postId: {}", pedia.getId(), e);
                Responseimages.add(null);
            }

        }

        return Top12PediaResponse.builder()
                .message("Success Get Top12 Pedia And Images")
                .result(true)
                .pedias(top12Pedia)
                .images(Responseimages)
                .build();
    }


    @Override
    public GetAllPediaResponse getAllPedia(int page) {
        log.info("page = {}", page);

        Pageable pageable = PageRequest.of(page - 1, 16, Sort.by("updatedAt").descending());

        Page<Pedia> allPedia = pediaRepository.findAll(pageable);
        log.info("allPedia = {}", allPedia);

        long totalPage = allPedia.getTotalPages();

        //피디아랑 이미지 매칭한 List
        List<MatchPediaAndImage> pediaAndImages = new ArrayList<>();

        for (Pedia pedia : allPedia) {

            try {
                Long id = pedia.getId();

                GetPediaResponse targetPedia = getPedia(id);
                log.info("targetPedia = {}", targetPedia);

                Image firstImage = imageService.getFirstIamgeByPostNum(id,ImageDomainType.PEDIA);
                log.info("firstImage = {}", firstImage);

                pediaAndImages.add(new MatchPediaAndImage(targetPedia,firstImage));

                log.info("firstImage = {}", firstImage.getImageUrl());

            } catch (Exception e) {
                log.warn("전체 조회 중 에러 발생 - postId: {}", pedia.getId(), e);
            }

        }

        log.info("pediaAndImages = {}", pediaAndImages);

        return GetAllPediaResponse.builder()
                .message("Success Get All Pedia And Images")
                .result(true)
                .pediaAndImage(pediaAndImages)
                .totalPage(totalPage)
                .build();
    }

    @Override
    @Transactional(readOnly = false)
    public PediaBasicResponse responseUpateAboutEditRequest(EditRequestAcceptRequest request) {


        PediaEditRequest editRequest = pediaEditRequestRepository.findById(request.getRequestId()).orElseThrow(
                () -> new EditRequestNotFound(ExceptionMessage.EDIT_REQUEST_NOT_FOUND)
        );



        Long pediaId = editRequest.getPediaId();

        Pedia pedia = pediaRepository.findById(pediaId).orElseThrow(
                () -> new PediaNotFoundException(ExceptionMessage.PEDIA_NOT_FOUND)
        );

        String target = editRequest.getTarget();


        switch (target) {
            case "origin":
                pedia.setOrigin(request.getNewInfo());
                log.info("target = {}", target);
                break;
            case "size":
                pedia.setSize(request.getNewInfo());
                log.info("target = {}", target);
                break;
            case "parentingLevel":
                pedia.setParentingLevel(Integer.parseInt(request.getNewInfo()));
                log.info("target = {}", target);
                break;
            case "sheddingLevel":
                pedia.setSheddingLevel(Integer.parseInt(request.getNewInfo()));
                log.info("target = {}", target);
                break;
            case "walkLevel":
                pedia.setWalkLevel(Integer.parseInt(request.getNewInfo()));
                log.info("target = {}", target);
                break;
            case "recommendedExercise":
                pedia.setRecommendedExercise(request.getNewInfo());
                log.info("target = {}", target);
                break;
            case "barkingLevel":
                pedia.setBarkingLevel(Integer.parseInt(request.getNewInfo()));
                log.info("target = {}", target);
                break;
            case "personality":
                pedia.setPersonality(request.getNewInfo());
                log.info("target = {}", target);
                break;
            case "diseases":
                pedia.setDiseases(request.getNewInfo());
                log.info("target = {}", target);
                break;
            case "history":
                pedia.setHistory(request.getNewInfo());
                log.info("target = {}", target);
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 필드: " + target);
        }



        pediaRepository.save(pedia);

        String elasticId = Type.PEDIADATA + "_" + pedia.getId();

        SearchDto searchDto = elasticRepository.findById(elasticId)
                .orElseThrow(() -> new ElasticSearchException(ExceptionMessage.ELASTIC_NOT_FOUND));

        String content = String.join(" ",
                pedia.getOrigin(),
                pedia.getRecommendedExercise(),
                pedia.getPersonality(),
                pedia.getDiseases(),
                pedia.getHistory()
        );

        searchDto.setTitle(pedia.getName());
        searchDto.setContent(content);
        elasticRepository.save(searchDto);
        log.info("searchDto = {}", searchDto);


        editRequest.setCompleted(true);
        pediaEditRequestRepository.save(editRequest);

        return PediaBasicResponse.builder()
                .message("Success Edit Request Reponse Pedia")
                .result(true)
                .build();
    }




    public Pedia save(PediaBasicRequest request) {

        Pedia pedia = Pedia.builder()
                .name(request.getName())
                .origin(request.getOrigin())
                .size(request.getSize())
                .parentingLevel(request.getParentingLevel())
                .sheddingLevel(request.getSheddingLevel())
                .walkLevel(request.getWalkLevel())
                .recommendedExercise(request.getRecommendedExercise())
                .barkingLevel(request.getBarkingLevel())
                .personality(request.getPersonality())
                .diseases(request.getDiseases())
                .history(request.getHistory())
                .build();

        pediaRepository.save(pedia);

        return pedia;
    }


    @Override
    @Transactional(readOnly = false)
    public PediaBasicResponse deleteEditRequest(long requestId) {

        pediaEditRequestRepository.deleteById(requestId);

        return PediaBasicResponse.builder()
                .message("Success Delete Edit Request")
                .result(true)
                .build();
    }

}
