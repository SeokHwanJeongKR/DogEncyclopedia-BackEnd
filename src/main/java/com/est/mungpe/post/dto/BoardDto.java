package com.est.mungpe.post.dto;

import com.est.mungpe.member.domain.Member;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<Long> likedMemberIds;

}
