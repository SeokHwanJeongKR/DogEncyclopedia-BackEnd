package com.est.mungpe.image.service;

import com.est.mungpe.image.domain.Image;
import com.est.mungpe.image.domain.ImageDomainType;

import java.util.List;

public interface ImageService {

    Image savePostImage(String url, Long postId, ImageDomainType type);

    List<Image> getImageByPostNum(Long postId, ImageDomainType domainType);

   Image getFirstIamgeByPostNum(Long postId,ImageDomainType domainType);

    void deleteImage(Image image);
}
