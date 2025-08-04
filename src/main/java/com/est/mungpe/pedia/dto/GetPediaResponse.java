package com.est.mungpe.pedia.dto;

import com.est.mungpe.image.domain.Image;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GetPediaResponse {

    private Long id;
    private String name;                   // 종류
    private String origin;                 //원산지
    private String size;                   // 크기

    private int parentingLevel;            //육아 난이도
    private int sheddingLevel;            //털빠짐 레벨
    private int walkLevel;                 //산책 레벨
    private String recommendedExercise;    //추천 운동량
    private int barkingLevel;              //짖음 정도

    //성격 설명
    private String personality;

    //질병 설명
        private String diseases;

    //간단 설명
    private String history;

    private String message;
    private boolean result;

    private LocalDateTime updatedAt;

    private List<Image> images;

}
