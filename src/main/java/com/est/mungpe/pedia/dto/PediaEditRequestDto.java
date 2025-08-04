package com.est.mungpe.pedia.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PediaEditRequestDto {
    private Long pediaId;
    private String target;
    private String originalInfo;
    private String newInfo;
}