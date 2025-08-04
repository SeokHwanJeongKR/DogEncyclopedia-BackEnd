package com.est.mungpe.pedia.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PediaBasicResponse {

    private String message;
    private Boolean result;
}
