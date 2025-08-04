package com.est.mungpe.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentBasicResponse {

    private boolean result;
    private String message;

}
