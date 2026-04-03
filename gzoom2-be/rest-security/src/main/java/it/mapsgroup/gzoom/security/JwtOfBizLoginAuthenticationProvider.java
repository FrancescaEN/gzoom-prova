package it.mapsgroup.gzoom.security;

import it.mapsgroup.gzoom.ofbiz.service.LoginResponseOfBiz;
import it.mapsgroup.gzoom.ofbiz.service.LoginServiceOfBiz;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginDao;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.security.model.JwtAuthentication;
import it.mapsgroup.gzoom.security.model.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Database-based login authentication provider.
 * <p>
 * This provider attempts to validate username and password and creates a JWT token if succeeds.
 * </p>
 *
 * @author Andrea Fossi
 * @author Fabio G. Strozzi
 */
@Component("jwtOfBizLoginAuthenticationProvider")
public class JwtOfBizLoginAuthenticationProvider implements AuthenticationProvider {
    private static final org.slf4j.Logger LOG = getLogger(JwtOfBizLoginAuthenticationProvider.class);

    private final JwtService jwtService;
    private final UserLoginDao userLoginDao;
    private final PermitsStorage permitsStorage;
    private final LoginServiceOfBiz loginService;

    @Autowired
    public JwtOfBizLoginAuthenticationProvider(JwtService jwtService,
                                               UserLoginDao userLoginDao,
                                               PermitsStorage permitsStorage,
                                               LoginServiceOfBiz loginService) {
        this.jwtService = jwtService;
        this.userLoginDao = userLoginDao;
        this.permitsStorage = permitsStorage;
        this.loginService = loginService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken userPwd = (UsernamePasswordAuthenticationToken) authentication;
        String username = (String) authentication.getPrincipal();
        try {
            UserLogin profile = userLoginDao.getUserLogin(username);

            if (profile == null || (profile.getEnabled() != null && !("Y").equals(profile.getEnabled()))) {
                    // no user or user not enabled, exit the chain of providers as
                // no other things are possible at all
                LOG.error("Attempting to login using a non-existing or disabled account [username={}]", username);
                throw new DisabledException(Messages.INVALID_ACCOUNT);
            }

            //  if (profile.getAuthenticationType() != DB) {
            // skips to next authentication provider as this does not support non-DB accounts
            //      return null;
            //  }

            // verifies the credentials
            //boolean auth = hashingStrategy.verify(profile.getSalt(), (String) userPwd.getCredentials(), profile.getPassword());
            LoginResponseOfBiz response = loginService.login(username, (String) userPwd.getCredentials());
            if (!StringUtils.hasLength(response.getExternalLoginKey()))
                throw new BadCredentialsException(Messages.INVALID_USERNAME_OR_PASSWORD);

            // password matches
            profile.setExternalLoginKey(response.getExternalLoginKey());
            String token = jwtService.generate(profile);
            permitsStorage.save(token, profile.getUsername());
            return new JwtAuthentication(token, profile);
        } catch (AuthenticationException e) {
            LOG.error("Invalid credentials [user={}]", username);
            throw e;
        } catch (Exception e) {
            LOG.error("Db-based authentication failed due to internal server error", e);
            throw new InternalAuthenticationServiceException("Invalid username or password", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
