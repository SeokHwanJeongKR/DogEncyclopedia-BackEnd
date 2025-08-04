package com.est.mungpe.image.repository;

import com.est.mungpe.image.domain.Image;
import com.est.mungpe.image.domain.ImageDomainType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostNumberAndType(Long postId, ImageDomainType type);
}
