package com.est.mungpe.comment.repository;

import com.est.mungpe.comment.domain.Comment;
import com.est.mungpe.post.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Board post);
}
