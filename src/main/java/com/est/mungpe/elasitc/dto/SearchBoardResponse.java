package com.est.mungpe.elasitc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SearchBoardResponse {

    String message;

    Boolean result;

    // 게시판 결과
    List<SearchDto> boardResults;


}
