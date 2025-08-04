package com.est.mungpe.post.dto;


import com.est.mungpe.post.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Top2PostResponse {

    private String message;
    private boolean result;
    private List<BoardDto> posts;
    private List<PostImageDto> images;
}
