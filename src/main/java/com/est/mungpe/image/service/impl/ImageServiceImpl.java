package com.est.mungpe.image.service.impl;

import com.est.mungpe.image.domain.Image;
import com.est.mungpe.image.domain.ImageDomainType;
import com.est.mungpe.image.repository.ImageRepository;
import com.est.mungpe.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;


    @Override
    public Image savePostImage(String url, Long postId, ImageDomainType type) {
        Image newImage = Image.builder()
                .imageUrl(url)
                .postNumber(postId)
                .type(type)
                .build();

        imageRepository.save(newImage);

        return newImage;

    }

    @Override
    public List<Image> getImageByPostNum(Long postId,ImageDomainType domainType) {

        List<Image> images = imageRepository.findByPostNumberAndType(postId,domainType);

        return images;
    }


    @Override
    public Image getFirstIamgeByPostNum(Long postId,ImageDomainType domainType) {

        List<Image> images = imageRepository.findByPostNumberAndType(postId,domainType);

        Image image = images.get(0);

        return image;
    }
    @Override
    public void deleteImage(Image image) {
        imageRepository.delete(image);
    }

}
