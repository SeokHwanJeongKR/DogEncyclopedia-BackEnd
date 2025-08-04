package com.est.mungpe.member.dto;

import com.est.mungpe.member.domain.Member;
import com.est.mungpe.member.domain.Role;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
//사용자 정보를 조회하는 용도의 클래스
public class CustomUserPrincipal implements OAuth2User, UserDetails {

    @Setter
    private Long id;

    private String name;
    private String email;

    @Setter
    private Role role;

    @Setter
    private boolean isBlocked;

    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }


    public static CustomUserPrincipal from(Member member, Map<String, Object> attributes) {
        CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal();
        customUserPrincipal.id = member.getId();
        customUserPrincipal.email = member.getEmail();
        customUserPrincipal.role = member.getRole();
        customUserPrincipal.name = member.getNickname();
        customUserPrincipal.attributes = attributes;
        return customUserPrincipal;

    }

}
