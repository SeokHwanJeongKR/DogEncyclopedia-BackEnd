package com.est.mungpe.image.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Long postNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageDomainType type;



    @Builder
    public Image(String imageUrl,Long postNumber, ImageDomainType type) {
        this.imageUrl = imageUrl;
        this.postNumber = postNumber;
        this.type = type;
    }

}
