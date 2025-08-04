package com.est.mungpe.pedia.dto;


import com.est.mungpe.pedia.domain.PediaEditRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;


@Data
@Builder
@AllArgsConstructor
public class GetAllPediaEditRequestResponse {

    private String message;
    private boolean result;
    private Page<PediaEditRequest> requests;
    private long totalPage;

}
