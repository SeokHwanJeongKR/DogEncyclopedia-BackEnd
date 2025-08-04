package com.est.mungpe.logo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Logo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String url;

    @Builder
    public Logo(String url) {
        this.url = url;
    }

    public void update(String url) {
        this.url = url;
    }

}
