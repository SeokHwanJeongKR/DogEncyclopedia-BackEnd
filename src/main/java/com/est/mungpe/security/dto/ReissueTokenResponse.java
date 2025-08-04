package com.est.mungpe.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReissueTokenResponse {

    public String accessToken;
    public String message;
    public boolean result;
}
