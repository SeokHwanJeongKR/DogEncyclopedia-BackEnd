package com.est.mungpe.elasitc.dto;


import com.est.mungpe.elasitc.domain.Type;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "documents")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SearchDto {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String title;
    private String content;
    private Type type;

}
