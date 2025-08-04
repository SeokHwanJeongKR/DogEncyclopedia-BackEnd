package com.est.mungpe.system.config;

import com.est.mungpe.member.domain.Role;
import com.est.mungpe.member.service.CustomOAuth2UserService;
import com.est.mungpe.security.config.JwtTokenFilter;
import com.est.mungpe.security.config.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtTokenFilter jwtTokenFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http

                //cors 기본 설정 활성화
                .cors(Customizer.withDefaults())

                //csrf 비활성화
                .csrf(csrf -> csrf.disable())

                // form 로그인 비활성화
                .formLogin(formLogin -> formLogin.disable())

                //인증 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // ADMIN ONLY
                        .requestMatchers(HttpMethod.GET,"/api/chat/with/*","/api/chat/list","/api/pedia/editRequest/all/**"
                        ,"/api/pedia/editRequest/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,"/api/pedia/editRequest/accept","/api/event").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT,"/api/logo","/api/event/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,"/api/event/*","/api/pedia/editRequest/accept/*").hasRole("ADMIN")


                        //ADMIN && MEMEBER

                        .requestMatchers(HttpMethod.GET,"/api/chat","/api/myPage/**").hasAnyRole("ADMIN","MEMBER")

                        .requestMatchers(HttpMethod.POST,"/api/chat/send","/api/comment","/api/board","/api/board/like/*" ,"/api/myPage/**","/api/pedia"
                        ,"/api/pedia/editRequest").hasAnyRole("ADMIN","MEMBER")

                        .requestMatchers(HttpMethod.PUT,"/api/comment/*","/api/board/*","/api/pedia/*").hasAnyRole("ADMIN","MEMBER")

                        .requestMatchers(HttpMethod.DELETE,"/api/comment/*","/api/board/*","/api/pedia/*","/api/auth/logout").hasAnyRole("ADMIN","MEMBER")

                        .requestMatchers("/chat/**","/topic/**").hasAnyRole("ADMIN","MEMBER")


                        // ALL
                        .requestMatchers(HttpMethod.GET,"/api/logo","/api/comment/*","/api/search/**","/api/event/**","/api/board/**","/api/pedia/**").permitAll()

                        .requestMatchers(HttpMethod.POST,"/api/auth/reissue","/oauth2/**", "/login/**" ,"/api/member").permitAll()

                        .anyRequest().authenticated()
                )

                // Oauthlogin 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        //로그인이 성공 했을 때 작동하는 핸들러
                        .successHandler(oAuth2SuccessHandler)
                )

                // jwt 필터 설정
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}
