package com.ocbc_rpp.rest.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.security.SecureRandom;
import java.util.Base64;

public class CsrfUtil {
    private static final CsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    private static final SecureRandom random = new SecureRandom();
    public static CsrfToken generateToken(){
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getEncoder().encodeToString(bytes);
        return new DefaultCsrfToken("X-XSRF-TOKEN","_csrf",token);
    }

    public static void addTokenToResponse(HttpServletRequest request, HttpServletResponse response){
//        CsrfToken existingToken = csrfTokenRepository.loadToken(request);
//        if (existingToken == null){
//            CsrfToken token = generateToken();
//            csrfTokenRepository.saveToken(token,request,response);
//        }
        CsrfToken token = generateToken();
        csrfTokenRepository.saveToken(token,request,response);
    }

    public static CsrfToken getTokenFromRequest(HttpServletRequest request){
        return csrfTokenRepository.loadToken(request);
    }

    @Bean
    public static CsrfTokenRepository getCsrfTokenRepository(){
        return csrfTokenRepository;
    }
}
