package com.est.mungpe.post.dto;

import com.est.mungpe.image.domain.Image;
import com.est.mungpe.member.domain.Member;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GetBoardMessageReponse {

    private Long id;
    private String title;
    private String content;
    private List<Image> images;
    private String message;
    private Boolean result;
    private Long memberId;
    private String memberNickname;
    private String profileUrl;
    private LocalDateTime updatedAt;
    private Boolean isLiked;
    private int likeCount;

}
