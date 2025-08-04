package com.est.mungpe.event.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventMessageResponse {

    private String message;
    private Boolean result;

}
