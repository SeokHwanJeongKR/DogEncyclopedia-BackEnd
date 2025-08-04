package com.est.mungpe.event.service.Impl;

import com.est.mungpe.event.domain.Event;
import com.est.mungpe.event.dto.AllEventResponse;
import com.est.mungpe.event.dto.EventMessageResponse;
import com.est.mungpe.event.dto.EventRequest;
import com.est.mungpe.event.dto.GetEventMessageReponse;
import com.est.mungpe.event.repository.EventRepository;
import com.est.mungpe.event.service.EventService;
import com.est.mungpe.exception.EventtNotFound;
import com.est.mungpe.exception.ExceptionMessage;
import com.est.mungpe.image.domain.Image;
import com.est.mungpe.image.domain.ImageDomainType;
import com.est.mungpe.image.service.ImageConvertService;
import com.est.mungpe.image.service.ImageService;
import com.est.mungpe.image.service.S3StorageService;
import com.est.mungpe.member.domain.Member;
import com.est.mungpe.member.service.MemberService;
import com.est.mungpe.post.dto.*;
import com.est.mungpe.system.utill.SecurityUtill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final S3StorageService s3StorageService;
    private final ImageService imageService;
    private final ImageConvertService imageConvertService;
    private final MemberService memberService;

    @Override
    @Transactional(readOnly = false)
    public EventMessageResponse createEvent(EventRequest request, List<MultipartFile> sourceImage) throws IOException {

        // 포스트 저장
        Event newEvent = saveEvent(request);

        log.info("newEvent = {}", newEvent);

        List<byte[]> files = imageConvertService.convert(sourceImage);

        for (byte[] fileData : files) {

            //유니크 파일 경로 만들기
            String fileName = UUID.randomUUID().toString();

            //s3 업로드
            String imageUrl = s3StorageService.upload(fileData,fileName);
            log.info("imageUrl = {}", imageUrl);

            // product image 객체 생성
            imageService.savePostImage(imageUrl, newEvent.getId(), ImageDomainType.EVENT);
        }

        return EventMessageResponse.builder()
                .message("Success Created Event")
                .result(true)
                .build();
    }

    @Override
    @Transactional(readOnly = false)
    public EventMessageResponse updateEvent(Long postId, EventRequest request, List<MultipartFile> sourceImage) throws IOException {

        log.info("request.getTitle() = {}", request.getTitle());
        log.info("request.getContent() = {}", request.getContent());

        Event event = eventRepository.findById(postId)
                .orElseThrow(() -> new EventtNotFound(ExceptionMessage.EVENT_NOT_FOUND));

        log.info("event = {}", event);

        event.update(request.getTitle(),request.getContent());
        eventRepository.save(event);

        List<Image> beforeImages = imageService.getImageByPostNum(postId,ImageDomainType.EVENT);

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
            imageService.savePostImage(imageUrl, event.getId(), ImageDomainType.EVENT);
        }

        return EventMessageResponse.builder()
                .message("Success updated Event")
                .result(true)
                .build();
    }

    @Override
    @Transactional(readOnly = false)
    public EventMessageResponse deleteEvent(Long postId) {

        Event event = eventRepository.findById(postId)
                .orElseThrow(() -> new EventtNotFound(ExceptionMessage.EVENT_NOT_FOUND));

        List<Image> beforeImages = imageService.getImageByPostNum(postId,ImageDomainType.EVENT);

        //기존 이미지 삭제
        for (Image image : beforeImages) {
            String imageUrl = image.getImageUrl();
            String key = imageUrl.replaceFirst("https://mungpedia.s3.ap-northeast-2.amazonaws.com/", "");
            s3StorageService.delete(key);
            imageService.deleteImage(image);
        }

        eventRepository.delete(event);

        return EventMessageResponse.builder()
                .message("Success deleted Event")
                .result(true)
                .build();

    }

    @Override
    public GetEventMessageReponse getEvent(Long postId) {
        Event event = eventRepository.findById(postId)
                .orElseThrow(() -> new EventtNotFound(ExceptionMessage.EVENT_NOT_FOUND));

        Member member = event.getMember();

        List<Image> images = imageService.getImageByPostNum(postId,ImageDomainType.EVENT);

        return GetEventMessageReponse.builder()
                .message("Success getPost")
                .result(true)
                .images(images)
                .title(event.getTitle())
                .updatedAt(event.getUpdatedAt())
                .content(event.getContent())
                .memberNickname(member.getNickname())
                .profileUrl(member.getProfileImageUrl())
                .build();
    }

    public Event saveEvent(EventRequest request) {

        Long memberId = SecurityUtill.getCurrentMemberId();

        Member member = memberService.getMemberById(memberId);

        Event newEvent = Event.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .build();

        eventRepository.save(newEvent);

        return newEvent;
    }


    @Override
    public AllEventResponse getAllEvent() {

        List<Event> allEvnet = eventRepository.findAll();

        List<PostImageDto> Responseimages = new ArrayList<>();

        for (Event event : allEvnet) {

            try {
                Long id = event.getId();

                Image firstImage = imageService.getFirstIamgeByPostNum(id,ImageDomainType.EVENT);

                if (firstImage == null) {
                    Responseimages.add(null);
                } else {
                    Responseimages.add(new PostImageDto(firstImage.getPostNumber(), firstImage.getImageUrl()));
                }

                log.info("firstImage = {}", firstImage.getImageUrl());

            } catch (Exception e) {
                log.warn("이미지 조회 중 에러 발생 - postId: {}", event.getId(), e);
                Responseimages.add(null);
            }

        }

        return AllEventResponse.builder()
                .message("Success Get All Events And Images")
                .result(true)
                .events(allEvnet)
                .images(Responseimages)
                .build();
    }
}
