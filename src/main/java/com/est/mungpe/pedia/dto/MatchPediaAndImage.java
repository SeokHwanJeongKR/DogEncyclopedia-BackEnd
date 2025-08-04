package com.est.mungpe.pedia.dto;


import com.est.mungpe.image.domain.Image;
import com.est.mungpe.pedia.domain.Pedia;
import com.est.mungpe.post.dto.PostImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MatchPediaAndImage {

    private GetPediaResponse pedia;
    private Image image;
}
