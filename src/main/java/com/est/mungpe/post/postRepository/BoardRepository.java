package com.est.mungpe.post.postRepository;

import com.est.mungpe.post.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findTop2ByOrderByUpdatedAtDesc();

    Page<Board> findAll(Pageable pageable);

    Page<Board> findAllByMemberId(Long memberId, Pageable pageable);

    @Query("select b from Board b join b.likedMemberIds l where l = :memberId")
    Page<Board> findAllLikedByMemberId(Long memberId, Pageable pageable);
}
