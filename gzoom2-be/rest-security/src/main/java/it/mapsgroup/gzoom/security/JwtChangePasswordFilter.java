package it.mapsgroup.gzoom.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.mybatis.dao.ContactMechDao;
import it.mapsgroup.gzoom.mybatis.dao.PartyContentDao;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginDao;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginHistoryDao;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginPasswordHistoryDao;
import it.mapsgroup.gzoom.mybatis.dto.ContactMechEx;
import it.mapsgroup.gzoom.mybatis.dto.PartyContentEx;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginHistory;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginPasswordHistory;
import it.mapsgroup.gzoom.security.model.ErrorResponse;
import it.mapsgroup.gzoom.security.model.LogoutResponse;
import it.mapsgroup.gzoom.security.model.Messages;
import it.mapsgroup.gzoom.security.model.RequestResetPsw;
import it.mapsgroup.gzoom.security.service.EmailServiceCheckUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Filters logout requests.
 *
 * @author Andrea Fossi
 */

public class JwtChangePasswordFilter extends GenericFilterBean {
    private static final Logger LOG = getLogger(JwtChangePasswordFilter.class);
    private final AuthenticationEntryPoint entryPoint;
    private final ObjectMapper objectMapper;
    private final UserLoginDao userLoginDao;
    private final EmailServiceCheckUser emailServiceCheckUser;
    private final UserLoginHistoryDao userLoginHistoryDao;
    private final ContactMechDao contactMechDao;
    private final PartyContentDao partyContentDao;
    private final UserLoginPasswordHistoryDao userLoginPasswordHistoryDao;


    public JwtChangePasswordFilter(AuthenticationEntryPoint entryPoint, ObjectMapper objectMapper, UserLoginDao userLoginDao, EmailServiceCheckUser emailServiceCheckUser, UserLoginHistoryDao userLoginHistoryDao, ContactMechDao contactMechDao, PartyContentDao partyContentDao, UserLoginPasswordHistoryDao userLoginPasswordHistoryDao) {
        this.entryPoint = entryPoint;
        this.objectMapper = objectMapper;
        this.userLoginDao = userLoginDao;
        this.emailServiceCheckUser = emailServiceCheckUser;
        this.userLoginHistoryDao = userLoginHistoryDao;
        this.contactMechDao = contactMechDao;
        this.partyContentDao = partyContentDao;
        this.userLoginPasswordHistoryDao = userLoginPasswordHistoryDao;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if(PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST,"/check-user").matches(req)) {
            try {
                RequestResetPsw requestResetPsw = objectMapper.readValue(request.getReader(), RequestResetPsw.class);

                ContactMechEx contactMechEx = contactMechDao.getEmailInfoString(requestResetPsw.getUsername());

                if(contactMechEx.getInfoString() == null){
                    objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(Messages.INVALID_USERNAME));
                }else{

                    this.userLoginHistoryDao.updatePreToken(contactMechEx.getUserLogin().getUserLoginId());

                    String token = TokenGenerator.generateToken(10);
                    UserLoginHistory userLoginHistory = new UserLoginHistory();
                    userLoginHistory.setUserLoginId(contactMechEx.getUserLogin().getUserLoginId());
                    userLoginHistory.setPasswordUsed(token);

                    Instant now = Instant.now();
                    userLoginHistory.setFromDate(now);

                    Duration duration = Duration.ofMinutes(15);
                    Instant futureInstant = now.plus(duration);
                    userLoginHistory.setThruDate(futureInstant);

                    this.userLoginHistoryDao.create(userLoginHistory);

                    String path = "";
                    PartyContentEx partyContentEx = partyContentDao.getPartyContent("Company", "REPORT_SMALL");
                    if (partyContentEx != null) {
                        path = partyContentEx.getDataResource().getObjectInfo();
                    }

                    String htmlBody = "<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "  <meta charset=\"UTF-8\">\n" +
                            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                            "  <title>Recupera Password</title>\n" +
                            "  <style>\n" +
                            "    body {\n" +
                            "      font-family: Arial, sans-serif;\n" +
                            "      background-color: #f0f8ff;\n" +
                            "      margin: 0;\n" +
                            "      padding: 0;\n" +
                            "    }\n" +
                            "\n" +
                            "    .container {\n" +
                            "      max-width: 600px;\n" +
                            "      margin: 0 auto;\n" +
                            "      padding: 20px;\n" +
                            "      background-color: #e0efff;\n" +
                            "      border-radius: 5px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .card {\n" +
                            "      background-color: #fff;\n" +
                            "      padding: 20px;\n" +
                            "      border-radius: 5px;\n" +
                            "      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
                            "    }\n" +
                            "\n" +
                            "    .card h2 {\n" +
                            "      color: #333;\n" +
                            "      font-size: 18px;\n" +
                            "      margin-bottom: 10px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .card p {\n" +
                            "      color: #666;\n" +
                            "      font-size: 16px;\n" +
                            "      margin-bottom: 20px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .card a {\n" +
                            "      background-color: #007bff;\n" +
                            "      color: #fff;\n" +
                            "      text-decoration: none;\n" +
                            "      padding: 10px 20px;\n" +
                            "      border-radius: 5px;\n" +
                            "      display: inline-block;\n" +
                            "      font-size: 16px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .footer {\n" +
                            "      text-align: center;\n" +
                            "      font-size: 12px;\n" +
                            "      color: #999;\n" +
                            "      margin-top: 20px;\n" +
                            "    }\n" +
                            "\n" +
                            "    .footer a {\n" +
                            "      color: #007bff;\n" +
                            "      text-decoration: none;\n" +
                            "    }\n" +
                            "  </style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "  <div class=\"container\">\n" +
                            "    <div class=\"card\">\n" +
                            "      <h2>Recupera la tua password</h2>\n" +
                            "      <p>Ciao!</p>\n" +
                            "      <p>Hai richiesto di recuperare la password per il tuo account. Clicca sul pulsante qui sotto per reimpostarla:</p>\n" +
                            "      <a href=\""+requestResetPsw.getUrl()+"/reset-password?username="+ contactMechEx.getUserLogin().getUserLoginId() +"&token="+ token +" \" class=\"card-button\">Recupera Password</a>\n" +
                            "      <p>Se non hai richiesto di recuperare la password, ignora questa email.</p>\n" +
                            "    </div>\n" +
                            "    <div class=\"footer\">\n" +
                            "      <p>In caso di problemi contattare il referente di prodotto interno.</p>\n" +
                            "    </div>\n" +
                            "  </div>\n" +
                            "</body>\n" +
                            "</html>";

                    this.emailServiceCheckUser.sendHtmlMessage(contactMechEx.getInfoString(), "Recupero Password Gzoom", htmlBody);

                    new ObjectMapper().writeValue(response.getOutputStream(), new LogoutResponse(0));
                }

            }catch (AuthenticationException e) {
                    if (entryPoint != null) {
                        entryPoint.commence(req, res, e);
                    }
            } catch (Exception e) {
                LOG.error("Unexpected exception occurred while filtering reset password request [url={}]", req.getRequestURL(), e);
                res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(Messages.UNEXPECTED_ERROR));
            }


        }  else if (PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST,"/reset-password").matches(req)) {
            try {
                    RequestResetPsw requestResetPsw = objectMapper.readValue(request.getReader(), RequestResetPsw.class);
                    UserLoginHistory userLoginHistory = userLoginHistoryDao.verifyToken(requestResetPsw.getUsername(),requestResetPsw.getToken());

                    Instant now = Instant.now();

                    if(now.isBefore(userLoginHistory.getThruDate())){
                        userLoginHistory.setThruDate(now);
                        this.userLoginHistoryDao.update(userLoginHistory);
                        UserLogin user = userLoginDao.selectByUserLoginId(userLoginHistory.getUserLoginId());
                        UserLoginPasswordHistory userLoginPasswordHistory = new UserLoginPasswordHistory();
                        if(user!=null) {
                            try {
                                String hash = HashingStrategy.getHash(requestResetPsw.getNewPassword(), "SHA-1");
                                user.setCurrentPassword(hash);
                                user.setRequirePasswordChange("N");
                                userLoginDao.update(user);

                                userLoginPasswordHistory.setUserLoginId(userLoginHistory.getUserLoginId());
                                userLoginPasswordHistory.setFromDate(Instant.now());
                                userLoginPasswordHistory.setCurrentPassword(hash);
                                userLoginPasswordHistoryDao.create(userLoginPasswordHistory);

                            } catch (Exception e) {
                                LOG.error("changePsw: {}", e);
                            }
                        }
                }else{
                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(Messages.TOKEN_EXPIRED));
                }

            }catch (AuthenticationException e) {
                if (entryPoint != null) {
                    entryPoint.commence(req, res, e);
                }
            } catch (Exception e) {
                LOG.error("Unexpected exception occurred while filtering reset password request [url={}]", req.getRequestURL(), e);
                res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(Messages.UNEXPECTED_ERROR));
            }
        }else {
            chain.doFilter(request, response);
        }
    }

}
