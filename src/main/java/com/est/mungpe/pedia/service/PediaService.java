package com.est.mungpe.pedia.service;

import com.est.mungpe.pedia.dto.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PediaService {
    PediaBasicResponse createPedia(PediaBasicRequest request, List<MultipartFile> sourceImage) throws IOException;

    @Transactional(readOnly = false)
    PediaBasicResponse updatePedia(Long postId, PediaBasicRequest request, List<MultipartFile> sourceImage) throws IOException;

    PediaBasicResponse deletePedia(Long postId);

    GetPediaResponse getPedia(Long postId);

    Top12PediaResponse getTop12Pedia();


    GetAllPediaResponse getAllPedia(int page);

    PediaBasicResponse responseUpateAboutEditRequest(EditRequestAcceptRequest request);

    PediaBasicResponse deleteEditRequest(long requestId);
}
