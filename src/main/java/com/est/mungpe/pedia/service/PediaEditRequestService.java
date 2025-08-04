package com.est.mungpe.pedia.service;



import com.est.mungpe.pedia.dto.GetAllPediaEditRequestResponse;
import com.est.mungpe.pedia.dto.GetEditRequestDto;
import com.est.mungpe.pedia.dto.PediaBasicResponse;
import com.est.mungpe.pedia.dto.PediaEditRequestDto;

public interface PediaEditRequestService {
    PediaBasicResponse saveEditRequest(PediaEditRequestDto request);

    GetAllPediaEditRequestResponse getAllEditRequest(int page);

    GetEditRequestDto getEditRequest(long id);
}
