package com.est.mungpe.security.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenBody {

    private Long memberId;
    private String role;

}
