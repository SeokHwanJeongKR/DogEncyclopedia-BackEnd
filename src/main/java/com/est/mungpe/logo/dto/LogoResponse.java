package com.est.mungpe.logo.dto;

import com.est.mungpe.logo.domain.Logo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogoResponse {

    private String message;

    private boolean result;



}
