package com.est.mungpe.member.dto;

import com.est.mungpe.member.domain.Provider;
import com.est.mungpe.member.domain.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Builder
public class MemberInfoResponse {

    private Long id;

    private String email;

    private String nickname;

    private String profileImageUrl;

    private Provider provider;

    private LocalDateTime createdAt;

    private Role role;

    private String message;

    private boolean result;
}
