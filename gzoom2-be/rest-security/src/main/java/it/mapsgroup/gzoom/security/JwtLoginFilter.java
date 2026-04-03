package it.mapsgroup.gzoom.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.mybatis.dao.UserPreferenceDaoMB;
import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import it.mapsgroup.gzoom.security.model.Credentials;
import it.mapsgroup.gzoom.security.model.ErrorResponse;
import it.mapsgroup.gzoom.security.model.JwtAuthentication;
import it.mapsgroup.gzoom.security.model.LoginResponse;
import it.mapsgroup.gzoom.security.model.Messages;
import it.mapsgroup.gzoom.service.Configuration;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.io.IOException;
import java.time.Instant;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Filters incoming requests to '/login' and treats them as authentication requests using username and password.
 *
 * @author Andrea Fossi.
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;
    private final Configuration config;
    private static final Logger LOG = getLogger(JwtLoginFilter.class);
    private final UserPreferenceDaoMB userPreferenceDao;

    public JwtLoginFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, Configuration config, UserPreferenceDaoMB userPreferenceDao) {
        super(PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST,"/login"));
        this.config = config;
        this.userPreferenceDao = userPreferenceDao;
        setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Credentials cred = objectMapper.readValue(request.getReader(), Credentials.class);
        UsernamePasswordAuthenticationToken usrPwdAuth = new UsernamePasswordAuthenticationToken(cred.getUsername(), cred.getPassword());
        return getAuthenticationManager().authenticate(usrPwdAuth);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = ((JwtAuthentication) authResult).getJwtToken();
        UserPreference userPreference = this.userPreferenceDao.getUserPreference(authResult.getName(), "2FAEnabled");
        if(config.isForce2FAuth()){
            if(userPreference == null){
                UserPreference newUserPreference = new UserPreference();
                newUserPreference.setUserLoginId(authResult.getName());
                newUserPreference.setUserPrefValue("Y");
                newUserPreference.setUserPrefTypeId("2FAEnabled");
                newUserPreference.setUserPrefGroupTypeId("GLOBAL_PREFERENCES");
                newUserPreference.setCreatedStamp(Instant.now());
                newUserPreference.setCreatedTxStamp(Instant.now());
                userPreferenceDao.create(authResult.getName(),newUserPreference);
            }else{
                userPreference.setUserPrefValue("Y");
                userPreferenceDao.update(authResult.getName(),userPreference);
            }
        }else{
            if(userPreference == null){
                UserPreference newUserPreference = new UserPreference();
                newUserPreference.setUserLoginId(authResult.getName());
                newUserPreference.setUserPrefValue("N");
                newUserPreference.setUserPrefTypeId("2FAEnabled");
                newUserPreference.setUserPrefGroupTypeId("GLOBAL_PREFERENCES");
                newUserPreference.setCreatedStamp(Instant.now());
                newUserPreference.setCreatedTxStamp(Instant.now());
                userPreferenceDao.create(authResult.getName(),newUserPreference);
            }
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (LOG.isInfoEnabled()) LOG.info("Successfully authenticated user: {}", authResult.getName());
        objectMapper.writeValue(response.getOutputStream(), new LoginResponse(token));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (failed instanceof InternalAuthenticationServiceException) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(Messages.UNEXPECTED_ERROR));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(Messages.INVALID_USERNAME_OR_PASSWORD));
        }
    }
}
