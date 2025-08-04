package com.est.mungpe.comment.controller;

import com.est.mungpe.comment.dto.AllCommentResponse;
import com.est.mungpe.comment.dto.CommentBasicResponse;
import com.est.mungpe.comment.dto.CreateCommentRequest;
import com.est.mungpe.comment.dto.UpdateCommentRequest;
import com.est.mungpe.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "{postId}")
    public ResponseEntity<AllCommentResponse> getAllComments(@PathVariable long postId) throws Exception {

        log.info("Get All Comments Controller 도착");

        AllCommentResponse result = commentService.getAllComments(postId);

        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<CommentBasicResponse> createComment(@RequestBody CreateCommentRequest request) throws Exception {

        log.info("Create Comment Controller 도착");

        CommentBasicResponse result = commentService.createComment(request);

        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "{commentId}")
    public ResponseEntity<CommentBasicResponse> updateComment(@PathVariable long commentId, @RequestBody UpdateCommentRequest request) throws Exception {

        log.info("Update Comment Controller 도착");

        CommentBasicResponse result = commentService.updateComment(request.getComment(), commentId);

        log.info("request.getComment() = {}", request.getComment());


        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "{commentId}")
    public ResponseEntity<CommentBasicResponse> deleteComment(@PathVariable long commentId) {

        log.info("Delete Comment Controller 도착");

        CommentBasicResponse result = commentService.deleteComment(commentId);

        log.info("result = {}", result);

        return ResponseEntity.ok(result);

    }

}

