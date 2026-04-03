package it.mapsgroup.gzoom.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.security.model.ErrorResponse;
import it.mapsgroup.gzoom.security.model.Messages;
import it.mapsgroup.gzoom.security.model.JwtAuthentication;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.GenericFilterBean;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


import static org.slf4j.LoggerFactory.getLogger;

/**
 * Authenticate requests.
 * <p>
 * Valid requests must include the Authorization header prefixed with <code>Bearer </code> and followed by the JWT
 * token.
 * </p>
 *
 * @author Andrea Fossi
 */
public class JwtTokenFilter extends GenericFilterBean {
    private static final Logger LOG = getLogger(JwtTokenFilter.class);

    private final AuthenticationEntryPoint entryPoint;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    public JwtTokenFilter(AuthenticationManager authenticationManager,
                          AuthenticationEntryPoint entryPoint,
                          ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.entryPoint = entryPoint;
        this.objectMapper = objectMapper;
    }





    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        boolean oneLoginSamlSSOEnabled = true;

        String path = ((HttpServletRequest)request).getRequestURI();
        if (path.contains("/api/getToken")) {
            System.out.println("Request : " + ((HttpServletRequest) request).getMethod() + " - " + getRequestUrl((HttpServletRequest)request));
            ((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
            ((HttpServletResponse) response).addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            ((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers", "*");
            ((HttpServletResponse) response).addHeader("Access-Control-Max-Age", "1728000");
            chain.doFilter(request, response);
            return;
        }
        else if(path.contains("/api/getLoginMethod") || path.contains("/api/getOneLogin-LoginUrl") || path.contains("/api/getOneLogin-LogoutUrl") || path.contains("/api/doLogout") || path.contains("/api/getExternalKey")){
            chain.doFilter(request, response);
            return;
        }


        try {
            String token = Tokens.token(req);
            JwtAuthentication jwtAuth = new JwtAuthentication(token);
            Authentication auth = authenticationManager.authenticate(jwtAuth);
            SecurityContextHolder.getContext().setAuthentication(auth);
            if (LOG.isDebugEnabled()) LOG.debug("Token valid for user: {}", auth.getName());
            chain.doFilter(request, response);
        } catch (AuthenticationException e) {
            if (entryPoint != null) {
                entryPoint.commence(req, res, e);
            }
        } catch (AccessDeniedException e) {
            LOG.error("Access denied [url={}]", req.getRequestURL(), e);
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(Messages.FORBIDDEN));
        } catch (Exception e) {
            LOG.error("Unexpected exception occurred [url={}]", req.getRequestURL(), e);
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(Messages.UNEXPECTED_ERROR));
        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    private String getRequestUrl(final HttpServletRequest req){
        final String scheme = req.getScheme();
        final int port = req.getServerPort();
        final StringBuilder url = new StringBuilder(256);
        url.append(scheme);
        url.append("://");
        url.append(req.getServerName());
        if(!(("http".equals(scheme) && (port == 0 || port == 80))
                || ("https".equals(scheme) && port == 443))){
            url.append(':');
            url.append(port);
        }
        url.append(req.getRequestURI());
        final String qs = req.getQueryString();
        if(qs != null){
            url.append('?');
            url.append(qs);
        }
        final String result = url.toString();
        return result;
    }
}
