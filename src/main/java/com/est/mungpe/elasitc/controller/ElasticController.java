package com.est.mungpe.elasitc.controller;


import com.est.mungpe.elasitc.dto.SearchBothResponse;
import com.est.mungpe.elasitc.service.ElasticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class ElasticController {

    private final ElasticService elasticService;


    @GetMapping(path = "/both")
    public ResponseEntity<SearchBothResponse> searchBoth(@RequestParam String keyword) {

        log.info("Search Both 컨트롤러 도착");

        SearchBothResponse result = elasticService.searchBoth(keyword);

        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }


}

