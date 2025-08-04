package com.est.mungpe.event.service;

import com.est.mungpe.event.dto.AllEventResponse;
import com.est.mungpe.event.dto.EventMessageResponse;
import com.est.mungpe.event.dto.EventRequest;
import com.est.mungpe.event.dto.GetEventMessageReponse;
import com.est.mungpe.post.dto.BoardMessageResponse;
import com.est.mungpe.post.dto.BoardRequest;
import com.est.mungpe.post.dto.GetBoardMessageReponse;
import com.est.mungpe.post.dto.Top2PostResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EventService {

    EventMessageResponse createEvent(EventRequest request, List<MultipartFile> sourceImage) throws IOException;

    EventMessageResponse updateEvent(Long postId, EventRequest request, List<MultipartFile> sourceImage) throws IOException;

    EventMessageResponse deleteEvent(Long postId);

    GetEventMessageReponse getEvent(Long postId);

    AllEventResponse getAllEvent();

}
