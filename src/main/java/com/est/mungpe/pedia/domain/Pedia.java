package com.est.mungpe.pedia.domain;

import com.est.mungpe.pedia.dto.PediaBasicRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;                   // 종류
    private String origin;                 //원산지
    private String size;                   // 크기

    private int parentingLevel;            //육아 난이도
    private int sheddingLevel;            //털빠짐 레벨
    private int walkLevel;                 //산책 레벨
    private String recommendedExercise;    //추천 운동량
    private int barkingLevel;              //짖음 정도

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    //성격 설명
    @Column(columnDefinition = "TEXT")
    private String personality;

    //질병 설명
    @Column(columnDefinition = "TEXT")
    private String diseases;

    //간단 설명
    @Column(columnDefinition = "TEXT")
    private String history;

    @Builder
    public Pedia(String name, String origin, String size, int parentingLevel, int sheddingLevel,
                 int walkLevel, String recommendedExercise, int barkingLevel, String personality, String diseases, String history) {
        this.name = name;
        this.origin = origin;
        this.size = size;
        this.parentingLevel = parentingLevel;
        this.sheddingLevel = sheddingLevel;
        this.walkLevel = walkLevel;
        this.recommendedExercise = recommendedExercise;
        this.barkingLevel = barkingLevel;
        this.personality = personality;
        this.diseases  = diseases;
        this.history = history;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(PediaBasicRequest request) {
        this.name = request.getName();
        this.origin = request.getOrigin();
        this.size = request.getSize();
        this.parentingLevel = request.getParentingLevel();
        this.sheddingLevel = request.getSheddingLevel();
        this.walkLevel = request.getWalkLevel();
        this.recommendedExercise = request.getRecommendedExercise();
        this.barkingLevel = request.getBarkingLevel();
        this.personality = request.getPersonality();
        this.diseases  = request.getDiseases();
        this.history = request.getHistory();
        this.updatedAt = LocalDateTime.now();
    }
}
