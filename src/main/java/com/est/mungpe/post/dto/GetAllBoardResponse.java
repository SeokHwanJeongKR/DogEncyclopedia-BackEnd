package com.est.mungpe.post.dto;


import com.est.mungpe.pedia.dto.MatchPediaAndImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetAllBoardResponse {

    private String message;
    private boolean result;
    private List<MatchBoardAndImage> boardAndImages;
    private long totalPage;
}
