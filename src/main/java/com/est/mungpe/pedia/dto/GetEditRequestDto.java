package com.est.mungpe.pedia.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetEditRequestDto{
    private String message;
    private boolean result;
    private Long requestId;
    private Long pediaId;
    private String target;
    private String originalInfo;
    private String newInfo;
}
