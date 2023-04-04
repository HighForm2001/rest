package com.ocbc_rpp.rest.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfTokenFilter extends OncePerRequestFilter {
    private final CsrfTokenRepository csrfTokenRepository;

    public CsrfTokenFilter(CsrfTokenRepository repository) {
        this.csrfTokenRepository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrfToken = CsrfUtil.getTokenFromRequest(request);
        if (csrfToken==null){
            csrfToken = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(csrfToken,request,response);
        }
        response.setHeader("X-XSRF-TOKEN",csrfToken.getToken());
        filterChain.doFilter(request, response);
    }
}