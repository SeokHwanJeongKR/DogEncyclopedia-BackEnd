package com.est.mungpe.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCommentRequest {

    private String comment;
    private long postId;
    private Long parentCommentId;
}
