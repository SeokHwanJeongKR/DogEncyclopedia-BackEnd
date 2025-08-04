package com.est.mungpe.system.utill;

import com.est.mungpe.member.dto.CustomUserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityUtill {
    //로그인 한 User id를 가져온다.
    public static Long getCurrentMemberId() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication = {}", authentication);

        Object principal = authentication.getPrincipal();
        log.info("principal = {}", principal);


        if (authentication == null || !(principal instanceof CustomUserPrincipal)) {
            return null;
        }

        return ((CustomUserPrincipal) authentication.getPrincipal()).getId();
    }

    public static boolean isLogged() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication = {}", authentication);

        Object principal = authentication.getPrincipal();
        log.info("principal = {}", principal);

        if (authentication == null) {
            return false;
        }

        return true;
    }
}

