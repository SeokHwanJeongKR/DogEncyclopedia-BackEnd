package com.est.mungpe.elasitc.service;


import com.est.mungpe.elasitc.domain.Type;
import com.est.mungpe.elasitc.dto.SearchBothResponse;
import com.est.mungpe.elasitc.dto.SearchDto;
import com.est.mungpe.elasitc.repository.ElasticRepository;
import com.est.mungpe.pedia.dto.GetPediaResponse;
import com.est.mungpe.pedia.service.PediaService;
import com.est.mungpe.post.dto.GetBoardMessageReponse;
import com.est.mungpe.post.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticService {

    private final ElasticRepository elasticRepository;
    private final BoardService boardService;
    private final PediaService pediaService;

    public SearchBothResponse searchBoth(String keyword) {

        // 반환용 게시판 리스트
        List<GetBoardMessageReponse> boardResponses = new ArrayList<>();

        // 게시판 검색
        List<SearchDto> boardResults = mergeResults(keyword, Type.BOARDDATA);

        for (SearchDto boardResult : boardResults) {

            Long postId = Long.valueOf(boardResult.getId().split("BOARDDATA_")[1]);

            GetBoardMessageReponse post = boardService.getPost(postId);

            boardResponses.add(post);
        }

        // 반환용 백과 리스트
        List<GetPediaResponse> pediaResponses  = new ArrayList<>();

        // 백과 검색
        List<SearchDto> pediaResults = mergeResults(keyword, Type.PEDIADATA);

        for (SearchDto pediaResult : pediaResults) {

            Long postId = Long.valueOf(pediaResult.getId().split("PEDIADATA_")[1]);

            GetPediaResponse pedia = pediaService.getPedia(postId);

            pediaResponses.add(pedia);

        }


        log.info("keyword = {}", keyword);
        log.info("boardResults = {}", boardResults);
        log.info("pediaResults = {}", pediaResults);
        log.info("boardResponses = {}", boardResponses);
        log.info("pediaResponses = {}", pediaResponses);

        SearchBothResponse result = SearchBothResponse.builder()
                .message("Success Get Both Document")
                .result(true)
                .boardResults(boardResponses)
                .pediaResults(pediaResponses)
                .build();
        return result;
    }


    public List<SearchDto> mergeResults(String keyword,Type type) {

        Set<SearchDto> mergedSet = new LinkedHashSet<>();

        List<SearchDto> byTitleContainingAndType = elasticRepository.findByTitleContainingAndType(keyword, type);
        List<SearchDto> byContentContainingAndType = elasticRepository.findByContentContainingAndType(keyword, type);

        mergedSet.addAll(byTitleContainingAndType);
        mergedSet.addAll(byContentContainingAndType);

        List<SearchDto> mergedList = new ArrayList<>(mergedSet);
        log.info("mergedList = {}", mergedList);

        return mergedList;
    }


}
