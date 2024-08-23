package com.study.SpringSecurity.security.filter;

import com.study.SpringSecurity.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.http.HttpHeaders;

@Component
public class JwtAccessTokenFilter extends GenericFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String bearerAccessToken = request.getHeader("Authorization");

        if(bearerAccessToken != null) {
            String accessToken = jwtProvider.removeBearer(bearerAccessToken);
            Claims claims = null;
            try {
                claims = jwtProvider.parseToken(accessToken);
            } catch (Exception e) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            Long userId = (Long) claims.get("userId");

            System.out.println("예외 발생하지 않음");
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
