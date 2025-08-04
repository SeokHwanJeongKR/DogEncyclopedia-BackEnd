package com.est.mungpe.post.controller;


import com.est.mungpe.pedia.dto.GetAllPediaResponse;
import com.est.mungpe.post.dto.*;
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
@RequestMapping("api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 글 작성
    @PostMapping
    public ResponseEntity<BoardMessageResponse> createPost
            (@RequestPart("data") BoardRequest request, @RequestPart("images") List<MultipartFile> images) throws IOException {

        log.info("Board Create 컨트롤러 도착");

        BoardMessageResponse result = boardService.createPost(request, images);
        log.info("result = {}", result);


        return  ResponseEntity.ok(result);
    }

    //글 수정
    @PutMapping(value = "{postId}", consumes = "multipart/form-data")
    public ResponseEntity<BoardMessageResponse> updatePost
            (@PathVariable Long postId, @RequestPart("data") BoardRequest request, @RequestPart("images")List<MultipartFile> images) throws IOException {
        log.info("Board Delete Update 컨트롤러 도착");

        BoardMessageResponse result = boardService.updatePost(postId,request,images);
        log.info("result = {}", result);

        return  ResponseEntity.ok(result);
    }



    //글 삭제
    @DeleteMapping(value = "{postId}")
    public ResponseEntity<BoardMessageResponse> deletePost(@PathVariable Long postId) {

        log.info("Board Delete 컨트롤러 도착");

        BoardMessageResponse result = boardService.deletePost(postId);
        log.info("result = {}", result);

        return  ResponseEntity.ok(result);
    }

    //특정 글 조회
    @GetMapping(value = "{postId}")
    public ResponseEntity<GetBoardMessageReponse> getPost(@PathVariable Long postId) {

        log.info("Get Post 컨트롤러 도착");

        GetBoardMessageReponse result = boardService.getPost(postId);
        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

    //top2 조회
    @GetMapping(path = "/top2")
    public ResponseEntity<Top2PostResponse> getTo2Posts() {

        log.info("Get Top2 컨트롤러 도착");

        Top2PostResponse result = boardService.getTop2Post();
        log.info("result = {}", result);


        return ResponseEntity.ok(result);
    }

    //전체 조회
    @GetMapping(path = "/all/{page}")
    public ResponseEntity<GetAllBoardResponse> getAllBoard(@PathVariable int page) {

        log.info("Get all 컨트롤러 도착");

        GetAllBoardResponse result = boardService.getAllBoard(page);
        log.info("result = {}", result);


        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/like/{postId}")
    public ResponseEntity<BoardLikeResponse> changePostLike(@PathVariable Long postId) {

        log.info("Change Like 컨트롤러 도착");

        BoardLikeResponse result = boardService.addLikeOrRemoveLike(postId);

        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

}
