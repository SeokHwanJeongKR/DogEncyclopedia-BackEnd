package com.est.mungpe.post.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardMessageResponse {

    private String message;
    private Boolean result;


}
