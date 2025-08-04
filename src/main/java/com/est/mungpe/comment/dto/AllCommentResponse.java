package com.est.mungpe.comment.dto;

import com.est.mungpe.comment.domain.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllCommentResponse {

    private boolean result;
    private String message;
    List<CommentDto> comments;
}

