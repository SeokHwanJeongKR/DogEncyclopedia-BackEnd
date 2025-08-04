package com.est.mungpe.member.controller;



import com.est.mungpe.member.dto.MemberInfoResponse;
import com.est.mungpe.member.service.MemberService;
import com.est.mungpe.system.utill.SecurityUtill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberInfoResponse> getMember() {

        log.info("getMember 컨트롤러 도착");

        Long memberId = SecurityUtill.getCurrentMemberId();
        log.info("memberId = {}", memberId);

        MemberInfoResponse result = memberService.getMemberInfo(memberId);

        return ResponseEntity.ok(result);
    }
}




