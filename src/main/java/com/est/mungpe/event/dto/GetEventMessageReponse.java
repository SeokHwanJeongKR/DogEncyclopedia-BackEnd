package com.est.mungpe.event.dto;

import com.est.mungpe.image.domain.Image;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GetEventMessageReponse {

    private String title;
    private String content;
    private List<Image> images;
    private String message;
    private Boolean result;
    private String memberNickname;
    private String profileUrl;
    private LocalDateTime updatedAt;

}
