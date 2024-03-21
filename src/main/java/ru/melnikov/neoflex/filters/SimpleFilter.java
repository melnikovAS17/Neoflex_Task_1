package ru.melnikov.neoflex.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class SimpleFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        StringBuffer s = httpServletRequest.getRequestURL();
        ServletContext servletContext = getServletContext();
        // 35 - длина URL (валидация адреса запроса)
        if(s.length() < 35) {
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("error.html");
            requestDispatcher.forward(httpServletRequest, httpServletResponse);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
