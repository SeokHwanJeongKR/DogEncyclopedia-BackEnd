package com.est.mungpe.myPage;

import com.est.mungpe.post.dto.GetAllBoardResponse;

public interface MyPageService {
    GetAllBoardResponse getAllMyBoards(int page);

    GetAllBoardResponse getAllMyLikedBoard(int page);
}
