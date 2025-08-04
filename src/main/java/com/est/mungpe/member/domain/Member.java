package com.est.mungpe.member.domain;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "memberId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    @Setter
    private boolean isBlocked = false;


    @Builder
    public Member(String email, String nickname, Provider provider, LocalDateTime createdAt,String profileImageUrl, Role role) {
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
        this.createdAt = createdAt;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }

}
