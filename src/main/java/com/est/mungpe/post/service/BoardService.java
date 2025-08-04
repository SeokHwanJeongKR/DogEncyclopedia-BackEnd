package com.est.mungpe.post.service;

import com.est.mungpe.post.domain.Board;
import com.est.mungpe.post.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BoardService {
    BoardMessageResponse createPost(BoardRequest request, List<MultipartFile> sourceImage) throws IOException;

    BoardMessageResponse updatePost(Long postId, BoardRequest request, List<MultipartFile> sourceImage) throws IOException;

    BoardMessageResponse deletePost(Long postId);

    GetBoardMessageReponse getPost(Long postId);

    Top2PostResponse getTop2Post();

    GetAllBoardResponse getAllBoard(int page);


    BoardLikeResponse addLikeOrRemoveLike(Long postId);
}
