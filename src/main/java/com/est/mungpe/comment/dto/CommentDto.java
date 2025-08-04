package com.est.mungpe.comment.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentDto {
    private long id;
    private String comment;
    private String userNickName;
    private String userImageUrl;
    private Long userId;
    private LocalDateTime createdAt;
    private Long parentCommentId;
    private List<CommentDto> childrenComments;
}
