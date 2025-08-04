package com.est.mungpe.member.service;

import com.est.mungpe.member.domain.Member;
import com.est.mungpe.member.domain.Provider;
import com.est.mungpe.member.domain.Role;
import com.est.mungpe.member.dto.CustomUserPrincipal;
import com.est.mungpe.member.dto.MemberInfoResponse;
import com.est.mungpe.member.dto.OAuthAttributes;
import com.est.mungpe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        // 제공자 정보가 들어 있다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // loadUser 안에는 사용자 정보, 고유 식별자, 권한정보가 들어있다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 고유 id, 이름, 사진, 이메일, 지역등에 대한 정보가 들어 있다.
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 제공자 정보 , 및 id에 대한 정보를 통합한다.
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, attributes);

        // 기존 id가 없다면 새로운 Member등록을 하고 , 기존 id가 있다면 Email을 반환한다.
        Member member = saveOrUpdate(oAuthAttributes);
        log.info("member = {}", member);


        // 사용자 정보를 기반으로 CustomUserPrincipal 생성하여 반환
        CustomUserPrincipal principal = CustomUserPrincipal.from(member,attributes);

        return principal;
    }
    private Member saveOrUpdate(OAuthAttributes attributes) {

        Member member;

        if (attributes.getEmail().equals("jsver12@gmail.com")) {
            member = Member.builder()
                    .email(attributes.getEmail())
                    .nickname(attributes.getName())
                    .provider(Provider.valueOf(attributes.getProvider()))
                    .createdAt(LocalDateTime.now())
                    .profileImageUrl(attributes.getPicture())
                    .role(Role.ADMIN)
                    .build();

        } else {
            member = Member.builder()
                    .email(attributes.getEmail())
                    .nickname(attributes.getName())
                    .provider(Provider.valueOf(attributes.getProvider()))
                    .createdAt(LocalDateTime.now())
                    .profileImageUrl(attributes.getPicture())
                    .role(Role.MEMBER)
                    .build();
        }

        System.out.println("saveOrUpdate 도착");

        return memberRepository.findByEmail(attributes.getEmail())
                .orElseGet(() -> memberRepository.save(member));
    }

    private Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }



}
