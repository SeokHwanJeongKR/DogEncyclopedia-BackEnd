package com.est.mungpe.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BoardRequest {

    private String title;
    private String content;


}
