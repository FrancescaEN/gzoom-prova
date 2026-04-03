package it.mapsgroup.gzoom.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.security.model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andrea Fossi
 */
@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Autowired
    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // checks which exception is exactly
        if (authException instanceof InternalAuthenticationServiceException)
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        else
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // streams the message
        objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(authException.getMessage()));
    }
}
