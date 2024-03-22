package com.austin.flashcard.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * @Description:
 * @Author: Austin
 * @Create: 3/14/2024 3:24 PM
 */
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getRequestURI().contains("webjars")) {
            log.info("request details-> schema:{}, serverName:{}, serverPort:{}, requestURI:{}, queryString:{}, remoteAddr:{}, fullUrl:{}",
                    request.getScheme(), request.getServerName(), request.getServerPort(), request.getRequestURI(),
                    request.getQueryString(), request.getRemoteAddr(), UrlUtils.buildFullRequestUrl(request));
            var headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    log.info("Header:{} : {}", name, request.getHeader(name));
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}
