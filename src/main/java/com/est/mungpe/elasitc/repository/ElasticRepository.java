package com.est.mungpe.elasitc.repository;

import com.est.mungpe.elasitc.domain.Type;
import com.est.mungpe.elasitc.dto.SearchDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticRepository extends ElasticsearchRepository<SearchDto, String> {
    List<SearchDto> findByTitleContainingAndType(String keyword, Type type);
    List<SearchDto> findByContentContainingAndType(String keyword, Type type);
}