package it.mapsgroup.gzoom.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import it.mapsgroup.gzoom.mybatis.dao.ContactMechDao;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginDao;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginHistoryDao;
import it.mapsgroup.gzoom.ofbiz.service.ChangePasswordServiceOfBiz;
import it.mapsgroup.gzoom.ofbiz.service.LoginServiceOfBiz;
import it.mapsgroup.gzoom.security.model.ErrorResponse;
import it.mapsgroup.gzoom.security.model.LogoutResponse;
import it.mapsgroup.gzoom.security.model.Messages;
import it.mapsgroup.gzoom.security.service.EmailServiceCheckUser;
import it.mapsgroup.gzoom.security.service.KeycloakAdminClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Filters logout requests.
 *
 * @author Andrea Fossi
 */

public class JwtLogoutFilter extends GenericFilterBean {
    private static final Logger LOG = getLogger(JwtLogoutFilter.class);
    private final AuthenticationEntryPoint entryPoint;
    private final PermitsStorage permitsStorage;
    private final ObjectMapper objectMapper;
    private final LoginServiceOfBiz loginService;
    private final UserLoginDao userLoginDao;
    private final EmailServiceCheckUser emailServiceCheckUser;
    private final HashingStrategy hashingStrategy;
    private final UserLoginHistoryDao userLoginHistoryDao;

    private final ContactMechDao contactMechDao;
    private final String  securityModeKeycloakLoginEnable;
    private final KeycloakAdminClient keycloakAdminClient;


    public JwtLogoutFilter(AuthenticationEntryPoint entryPoint, PermitsStorage permitsStorage, ObjectMapper objectMapper,
                           LoginServiceOfBiz loginService, UserLoginDao userLoginDao, EmailServiceCheckUser emailServiceCheckUser, ChangePasswordServiceOfBiz changeService, HashingStrategy hashingStrategy, UserLoginHistoryDao userLoginHistoryDao, ContactMechDao contactMechDao, String securityModeKeycloakLoginEnable, KeycloakAdminClient keycloakAdminClient) {
        this.entryPoint = entryPoint;
        this.permitsStorage = permitsStorage;
        this.objectMapper = objectMapper;
        this.loginService = loginService;
        this.userLoginDao = userLoginDao;
        this.emailServiceCheckUser = emailServiceCheckUser;
        this.hashingStrategy = hashingStrategy;
        this.userLoginHistoryDao = userLoginHistoryDao;
        this.contactMechDao = contactMechDao;
        this.securityModeKeycloakLoginEnable = securityModeKeycloakLoginEnable;
        this.keycloakAdminClient = keycloakAdminClient;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST,"/logout").matches(req)) {
            try {
                String username = null;
                boolean keycloakEnabled = securityModeKeycloakLoginEnable != null
                        && securityModeKeycloakLoginEnable.equalsIgnoreCase("true");

                if (keycloakEnabled) {
                    String logoutToken = req.getParameter("logout_token");
                    if (logoutToken == null) {
                        LOG.error("Keycloak backchannel logout called without logout_token");
                        throw new BadCredentialsException("Missing logout_token");
                    }
                    SignedJWT jwt = SignedJWT.parse(logoutToken);
                    username = keycloakAdminClient.getUsernameFromSub(jwt.getJWTClaimsSet().getSubject());
                    if (username == null) {
                        throw new BadCredentialsException("Failed to get username");
                    }
                    if (LOG.isInfoEnabled())
                        LOG.info("Keycloak backchannel logout for user: {}", username);
                }
                else {
                    String token = Tokens.token(req);
                    PermitsStorage.Permit permit = permitsStorage.prune(token);
                    if (permit == null) {
                        throw new BadCredentialsException(Messages.INVALID_AUTHORIZATION_TOKEN);
                    }
                    username = permit.username;
                    if (LOG.isInfoEnabled()) LOG.info("Logged out user: {}", permit.username);
                    // everything is fine, user was authenticated, returns ok
                }
                try {
                    loginService.logout(username);
                } catch (Exception e) {
                    LOG.warn("Exception while logout user from legacy... ", e);
                }
                new ObjectMapper().writeValue(response.getOutputStream(), new LogoutResponse(0));
            } catch (AuthenticationException e) {
                if (entryPoint != null) {
                    entryPoint.commence(req, res, e);
                }
            } catch (Exception e) {
                LOG.error("Unexpected exception occurred while filtering logout request [url={}]", req.getRequestURL(), e);
                res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(Messages.UNEXPECTED_ERROR));
            } finally {
                SecurityContextHolder.clearContext();
            }
        }else {
            chain.doFilter(request, response);
        }
    }
}
