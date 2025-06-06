package ru.slisarenko.springpractick.config.sequrity.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class DeniedClientFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var header = request.getHeader(HttpHeaders.USER_AGENT);
        if(header != null && header.startsWith("curl")){
            response.sendError(HttpStatus.FORBIDDEN.value());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

        filterChain.doFilter(request, response);

    }
}
