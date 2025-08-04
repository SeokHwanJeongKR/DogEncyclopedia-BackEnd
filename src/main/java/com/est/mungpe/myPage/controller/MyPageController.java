package com.est.mungpe.myPage.controller;

import com.est.mungpe.myPage.MyPageService;
import com.est.mungpe.post.dto.GetAllBoardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/myPage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping(path = "/all/post/{page}")
    public ResponseEntity<GetAllBoardResponse> getAllMyPosts(@PathVariable int page) {

        log.info("My Page All Get My Post 컨트롤러 도착");

        GetAllBoardResponse result = myPageService.getAllMyBoards(page);

        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }


    @GetMapping(path = "/all/likedPost/{page}")
    public ResponseEntity<GetAllBoardResponse> getAllMyLikedPosts(@PathVariable int page) {

        log.info("My Page All Get My Liked Post 컨트롤러 도착");

        GetAllBoardResponse result = myPageService.getAllMyLikedBoard(page);

        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }


}
