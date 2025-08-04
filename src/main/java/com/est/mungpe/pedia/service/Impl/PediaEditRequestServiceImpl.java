package com.est.mungpe.pedia.service.Impl;

import com.est.mungpe.exception.EditRequestNotFound;
import com.est.mungpe.exception.ExceptionMessage;
import com.est.mungpe.pedia.domain.PediaEditRequest;
import com.est.mungpe.pedia.dto.GetAllPediaEditRequestResponse;
import com.est.mungpe.pedia.dto.GetEditRequestDto;
import com.est.mungpe.pedia.dto.PediaBasicResponse;
import com.est.mungpe.pedia.dto.PediaEditRequestDto;
import com.est.mungpe.pedia.repository.PediaEditRequestRepository;
import com.est.mungpe.pedia.service.PediaEditRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PediaEditRequestServiceImpl implements PediaEditRequestService {

    private final PediaEditRequestRepository pediaEditRequestRepository;

    @Override
    public PediaBasicResponse saveEditRequest(PediaEditRequestDto request) {

        PediaEditRequest pediaEditRequest = PediaEditRequest.builder()
                .pediaId(request.getPediaId())
                .target(request.getTarget())
                .originalInfo(request.getOriginalInfo())
                .newInfo(request.getNewInfo())
                .build();
        pediaEditRequestRepository.save(pediaEditRequest);

        return PediaBasicResponse.builder()
                .result(true)
                .message("Success Create Pedia Edit Request")
                .build();
    }

    @Override
    public GetAllPediaEditRequestResponse getAllEditRequest(int page) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("createdAt").ascending());

        Page<PediaEditRequest> pediaEditRequests = pediaEditRequestRepository.findByCompletedFalse(pageable);

        long totalPage = pediaEditRequests.getTotalPages();

        return GetAllPediaEditRequestResponse.builder()
                .requests(pediaEditRequests)
                .totalPage(totalPage)
                .message("Success Get All Pedia Edit Request")
                .result(true)
                .build();

    }

    @Override
    public GetEditRequestDto getEditRequest(long id) {
        PediaEditRequest pediaEditRequest = pediaEditRequestRepository.findById(id).orElseThrow(() ->
                new EditRequestNotFound(ExceptionMessage.EDIT_REQUEST_NOT_FOUND));


        return GetEditRequestDto.builder()
                .requestId(pediaEditRequest.getId())
                .pediaId(pediaEditRequest.getPediaId())
                .target(pediaEditRequest.getTarget())
                .originalInfo(pediaEditRequest.getOriginalInfo())
                .newInfo(pediaEditRequest.getNewInfo())
                .message("Success Get Pedia Edit Request")
                .result(true)
                .build();
    }
}
