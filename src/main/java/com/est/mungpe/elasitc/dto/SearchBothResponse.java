package com.est.mungpe.elasitc.dto;

import com.est.mungpe.pedia.dto.GetPediaResponse;
import com.est.mungpe.post.dto.GetBoardMessageReponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SearchBothResponse {

    String message;

    Boolean result;

    // 게시판 결과
    List<GetBoardMessageReponse> boardResults;
    // 백과 검색
    List<GetPediaResponse> pediaResults;

}
