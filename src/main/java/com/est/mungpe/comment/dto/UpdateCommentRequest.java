package com.est.mungpe.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCommentRequest {

    private String comment;
}
