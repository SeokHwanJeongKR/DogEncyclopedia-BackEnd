package com.est.mungpe.post.dto;


import com.est.mungpe.image.domain.Image;
import com.est.mungpe.pedia.dto.GetPediaResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MatchBoardAndImage {

    private GetBoardMessageReponse board;
    private Image image;
}
