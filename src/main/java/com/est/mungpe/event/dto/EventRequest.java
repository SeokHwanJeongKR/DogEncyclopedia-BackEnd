package com.est.mungpe.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EventRequest {

    private String title;
    private String content;


}
