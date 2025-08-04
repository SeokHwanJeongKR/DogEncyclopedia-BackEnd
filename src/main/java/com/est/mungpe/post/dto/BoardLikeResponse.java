package com.est.mungpe.post.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardLikeResponse {

    private String message;
    private Boolean result;
    private Boolean isLiked;
    private int likeCount;

}
