package it.mapsgroup.gzoom.security;

import it.mapsgroup.gzoom.security.model.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class QueryParamBearerTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_PARAM = "token";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/rest/report-download")) {

            String token = request.getParameter(TOKEN_PARAM);

            if (token != null && !token.isBlank()) {
                HttpServletRequest wrapped = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getHeader(String name) {
                        if ("Authorization".equalsIgnoreCase(name)) {
                            return "Bearer " + token;
                        }
                        return super.getHeader(name);
                    }
                };
                filterChain.doFilter(wrapped, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
