package com.est.mungpe.member.service.Impl;

import com.est.mungpe.exception.ExceptionMessage;
import com.est.mungpe.exception.MemberNotFound;
import com.est.mungpe.member.domain.Member;
import com.est.mungpe.member.dto.MemberInfoResponse;
import com.est.mungpe.member.repository.MemberRepository;
import com.est.mungpe.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public MemberInfoResponse getMemberInfo(Long id) {

        Member member = memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFound(ExceptionMessage.MEMBER_NOT_FOUND)
        );

        MemberInfoResponse memberInfo = MemberInfoResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .provider(member.getProvider())
                .profileImageUrl(member.getProfileImageUrl())
                .message("유저 정보 가져오기 성공")
                .result(true)
                .build();

        return memberInfo;
    }

    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFound(ExceptionMessage.MEMBER_NOT_FOUND)
        );
    }

    @Override
    public Member getMemberByEmail(String mail) {
        return memberRepository.findByEmail(mail).orElseThrow(
                () -> new MemberNotFound(ExceptionMessage.MEMBER_NOT_FOUND)
        );
    }
}
