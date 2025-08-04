package com.est.mungpe.comment.service;

import com.est.mungpe.comment.domain.Comment;
import com.est.mungpe.comment.dto.AllCommentResponse;
import com.est.mungpe.comment.dto.CommentBasicResponse;
import com.est.mungpe.comment.dto.CommentDto;
import com.est.mungpe.comment.dto.CreateCommentRequest;
import com.est.mungpe.comment.repository.CommentRepository;
import com.est.mungpe.exception.ComentNotFound;
import com.est.mungpe.exception.ExceptionMessage;
import com.est.mungpe.exception.MemberNotFound;
import com.est.mungpe.exception.PostNotFound;
import com.est.mungpe.member.domain.Member;
import com.est.mungpe.member.repository.MemberRepository;
import com.est.mungpe.post.domain.Board;
import com.est.mungpe.post.postRepository.BoardRepository;
import com.est.mungpe.system.utill.SecurityUtill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;


    public CommentBasicResponse createComment(CreateCommentRequest request) throws Exception {
        Long loginedId = SecurityUtill.getCurrentMemberId();

        Member member = memberRepository.findById(loginedId)
                .orElseThrow(() -> new MemberNotFound(ExceptionMessage.MEMBER_NOT_FOUND)
                );

        String nickname = member.getNickname();
        String profileImageUrl = member.getProfileImageUrl();

        Board post = boardRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostNotFound(ExceptionMessage.POST_NOT_FOUND));

        Comment parentComment = null;

        if (request.getParentCommentId() != null) {
             parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ComentNotFound(ExceptionMessage.COMMENT_NOT_FOUND));
        }


        Comment newComent = Comment.builder()
                .comment(request.getComment())
                .post(post)
                .userNickName(nickname)
                .userImageUrl(profileImageUrl)
                .userId(loginedId)
                .parentComment(parentComment)
                .build();
        commentRepository.save(newComent);

        return CommentBasicResponse.builder()
                .result(true)
                .message(parentComment == null
                        ? "Success created comment"
                        : "Success created child comment")
                .build();
    }

    @Transactional(readOnly = true)
    public AllCommentResponse getAllComments(long postId) throws Exception {

        Board post = boardRepository.findById(postId)
                .orElseThrow(() -> new PostNotFound(ExceptionMessage.POST_NOT_FOUND));

        List<Comment> commentList = commentRepository.findByPost(post);

        List<CommentDto> commentDtoList = commentList.stream()
                .filter(c -> c.getParentComment() == null)
                .map(this::toDto)
                .toList();

        return AllCommentResponse.builder()
                .result(true)
                .message("Success get All Comment")
                .comments(commentDtoList)
                .build();

    }

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userId(comment.getUserId())
                .userNickName(comment.getUserNickName())
                .userImageUrl(comment.getUserImageUrl())
                .createdAt(comment.getCreatedAt())
                .parentCommentId(comment.getParentComment() != null
                        ? comment.getParentComment().getId()
                        : null)
                .childrenComments(
                        comment.getChildComments() != null
                        ? comment.getChildComments().stream()
                                .map(this :: toDto)
                                .toList()
                                : null
                )
                .build();
    }

    public CommentBasicResponse updateComment(String comment, long commentId) throws Exception {
        Comment beforeComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new PostNotFound(ExceptionMessage.POST_NOT_FOUND));

        beforeComment.update(comment);

        return CommentBasicResponse.builder()
                .result(true)
                .message("Success update comment")
                .build();
    }

    public CommentBasicResponse deleteComment(long commentId) {
        commentRepository.deleteById(commentId);

        return CommentBasicResponse.builder()
                .result(true)
                .message("Success delete comment")
                .build();
    }

}


