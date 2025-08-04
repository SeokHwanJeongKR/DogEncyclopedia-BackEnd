package com.est.mungpe.member.service;

import com.est.mungpe.member.domain.Member;
import com.est.mungpe.member.dto.MemberInfoResponse;

public interface MemberService {
    MemberInfoResponse getMemberInfo(Long id);

    Member getMemberById(Long id);

    Member getMemberByEmail(String mail);
}
