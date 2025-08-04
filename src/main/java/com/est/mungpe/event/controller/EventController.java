package com.est.mungpe.event.controller;


import com.est.mungpe.event.dto.AllEventResponse;
import com.est.mungpe.event.dto.EventMessageResponse;
import com.est.mungpe.event.dto.EventRequest;
import com.est.mungpe.event.dto.GetEventMessageReponse;
import com.est.mungpe.event.service.EventService;
import com.est.mungpe.post.dto.BoardMessageResponse;
import com.est.mungpe.post.dto.BoardRequest;
import com.est.mungpe.post.dto.GetBoardMessageReponse;
import com.est.mungpe.post.dto.Top2PostResponse;
import com.est.mungpe.post.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // 글 작성
    @PostMapping
    public ResponseEntity<EventMessageResponse> createEvent
            (@RequestPart("data") EventRequest request, @RequestPart("images") List<MultipartFile> images) throws IOException {

        log.info("Event Create 컨트롤러 도착");

        EventMessageResponse result = eventService.createEvent(request, images);
        log.info("result = {}", result);


        return  ResponseEntity.ok(result);
    }

    //글 수정
    @PutMapping(value = "{postId}", consumes = "multipart/form-data")
    public ResponseEntity<EventMessageResponse> updateEvent
            (@PathVariable Long postId, @RequestPart("data") EventRequest request, @RequestPart("images")List<MultipartFile> images) throws IOException {
        log.info("Event Delete Update 컨트롤러 도착");

        EventMessageResponse result = eventService.updateEvent(postId,request,images);
        log.info("result = {}", result);

        return  ResponseEntity.ok(result);
    }



    //글 삭제
    @DeleteMapping(value = "{postId}")
    public ResponseEntity<EventMessageResponse> deleteEvent(@PathVariable Long postId) {

        log.info("Event Delete 컨트롤러 도착");

        EventMessageResponse result = eventService.deleteEvent(postId);
        log.info("result = {}", result);

        return  ResponseEntity.ok(result);
    }

    //특정 글 조회
    @GetMapping(value = "{postId}")
    public ResponseEntity<GetEventMessageReponse> getEvent(@PathVariable Long postId) {

        log.info("Get Event 컨트롤러 도착");

        GetEventMessageReponse result = eventService.getEvent(postId);
        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }


    //전체 조회
    @GetMapping(path = "/allEvent")
    public ResponseEntity<AllEventResponse> getAllEvent() {

        log.info("Get ALLEvent 컨트롤러 도착");

        AllEventResponse result = eventService.getAllEvent();
        log.info("result = {}", result);


        return ResponseEntity.ok(result);
    }


}
