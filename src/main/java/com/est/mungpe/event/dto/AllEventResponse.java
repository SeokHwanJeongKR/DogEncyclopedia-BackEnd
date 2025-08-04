package com.est.mungpe.event.dto;


import com.est.mungpe.event.domain.Event;
import com.est.mungpe.post.dto.PostImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AllEventResponse {

    private String message;
    private boolean result;
    private List<Event> events;
    private List<PostImageDto> images;
}
