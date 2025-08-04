package com.est.mungpe.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostImageDto {
    private Long postId;
    private String imageUrl;
}
