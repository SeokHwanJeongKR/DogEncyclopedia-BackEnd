package com.est.mungpe.comment.domain;

import com.est.mungpe.post.domain.Board;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Board post ;

    private Long userId;

    private String userNickName;

    private String userImageUrl;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    @Builder
    public Comment (String comment, Board post,Long userId, String userNickName, String userImageUrl,Comment parentComment) {
        this.comment = comment;
        this.post = post;
        this.userId = userId;
        this.userNickName = userNickName;
        this.userImageUrl = userImageUrl;
        this.parentComment = parentComment;
    }

    public void update (String comment) {
        this.comment = comment;
    }
}
