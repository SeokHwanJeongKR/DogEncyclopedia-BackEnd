package com.est.mungpe.pedia.dto;

import lombok.Data;

@Data
public class EditRequestAcceptRequest {
    private Long requestId;
    private String newInfo;
}
