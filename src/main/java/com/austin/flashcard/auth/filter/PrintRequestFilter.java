package com.austin.flashcard.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * @Description:
 * @Author: Austin
 * @Create: 3/14/2024 3:24 PM
 */
@Slf4j
public class PrintRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var session = request.getSession(false);
        log.info("received a request:{}, session:{}, isNew:{}", request.getRequestURL());
        if(!Objects.isNull(session)){
            log.info("session ID:{}, session is new:{}", session.getId(), session.isNew());
        }
        filterChain.doFilter(request, response);
    }

}
