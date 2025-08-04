package com.est.mungpe.pedia.dto;


import com.est.mungpe.pedia.domain.Pedia;
import com.est.mungpe.post.domain.Board;
import com.est.mungpe.post.dto.PostImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Top12PediaResponse {

    private String message;
    private boolean result;
    private List<Pedia> pedias;
    private List<PostImageDto> images;
}
