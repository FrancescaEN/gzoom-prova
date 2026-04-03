package it.mapsgroup.gzoom.security;

import it.mapsgroup.gzoom.commons.EmailValidatorService;
import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.mybatis.dao.ContactMechDao;
import it.mapsgroup.gzoom.mybatis.dao.PartyContentDao;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginHistoryDao;
import it.mapsgroup.gzoom.mybatis.dao.UserPreferenceDaoMB;
import it.mapsgroup.gzoom.mybatis.dto.ContactMech;
import it.mapsgroup.gzoom.mybatis.dto.PartyContentEx;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginHistory;
import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import it.mapsgroup.gzoom.security.service.EmailServiceCheckUser;
import it.mapsgroup.gzoom.service.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

import static it.mapsgroup.gzoom.security.Principals.principal;

@RestController
@RequestMapping(value="/twoFAuth", produces= MediaType.APPLICATION_JSON_VALUE)
public class TwoFactoryAuth {
    private final ContactMechDao contactMechDao;
    private final UserLoginHistoryDao userLoginHistoryDao;
    private final PartyContentDao partyContentDao;
    private final EmailServiceCheckUser emailServiceCheckUser;

    private final UserPreferenceDaoMB userPreferenceDao;

    @Autowired
    public TwoFactoryAuth(ContactMechDao contactMechDao, UserLoginHistoryDao userLoginHistoryDao, PartyContentDao partyContentDao, EmailServiceCheckUser emailServiceCheckUser, UserPreferenceDaoMB userPreferenceDao) {
        this.contactMechDao = contactMechDao;
        this.userLoginHistoryDao = userLoginHistoryDao;
        this.partyContentDao = partyContentDao;
        this.emailServiceCheckUser = emailServiceCheckUser;
        this.userPreferenceDao = userPreferenceDao;
    }

    @RequestMapping(value = "/enableTwoFactoryAuth", method = RequestMethod.GET)
    public ContactMech enableTwoFactoryAuth() {

        ContactMech contactMech = this.contactMechDao.getInfoString(principal().getUserLoginId());
        String infoString = contactMech.getInfoString();
        if(EmailValidatorService.validateEmail(infoString)){

            this.userLoginHistoryDao.updatePreToken(principal().getUserLoginId());

            String token = TokenGenerator.generateToken(8);
            UserLoginHistory userLoginHistory = new UserLoginHistory();
            userLoginHistory.setUserLoginId(principal().getUserLoginId());
            userLoginHistory.setPasswordUsed(token);

            Instant now = Instant.now();
            userLoginHistory.setFromDate(Instant.now());

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
                    "  <title>Verifica OTP</title>\n" +
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
                    "      <h2>Verifica il tuo OTP</h2>\n" +
                    "      <p>Ciao!</p>\n" +
                    "      <p>Ecco il tuo codice OTP da inserire in Gzoom per abilitare l'autenticazione in due fattori: "+ token +"</p>\n" +
                    "\n" +
                    "      <p>Il codice OTP scadrà tra 15 minuti.</p>\n" +
                    "    </div>\n" +
                    "    <div class=\"footer\">\n" +
                    "      <p>Contatti:</p>\n" +
                    "      <p><a href=\"mailto:SupportoGZoom@mapsgroup.it\">Email: SupportoGZoom@mapsgroup.it</a></p>\n" +
                    "      <img alt=\"Gzoom Logo\" class=\"img-fluid\" src=\"" + path + "\" />\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</body>\n" +
                    "</html>";
            this.emailServiceCheckUser.sendHtmlMessage(infoString, "Two Authentication Factory Gzoom", htmlBody);

            return contactMech;

        }else{
            Validators.assertTrue(contactMech.getInfoString() == null, Messages.INVALID_EMAIL);
            return null;
        }

    }


    @RequestMapping(value = "/verifyOtp/{tokenDto}", method = RequestMethod.POST)
    @ResponseBody
    public boolean verifyOtp(@PathVariable String tokenDto) {

        UserLoginHistory userLoginHistory = userLoginHistoryDao.verifyToken(principal().getUserLoginId(),tokenDto);
        Instant now = Instant.now();

        if(now.isBefore(userLoginHistory.getThruDate())){
            userLoginHistory.setThruDate(now);
            this.userLoginHistoryDao.update(userLoginHistory);
            UserPreference userPreference = this.userPreferenceDao.getUserPreference(principal().getUserLoginId(), "2FAEnabled");
            userPreference.setUserPrefValue("Y");
            this.userPreferenceDao.update(userPreference.getUserLoginId(),userPreference);
            return true;
        }else{
            Validators.assertTrue(false, it.mapsgroup.gzoom.security.model.Messages.TOKEN_EXPIRED);
            return false;
        }
    }

    @RequestMapping(value = "/disableTwoFactoryAuth", method = RequestMethod.GET)
    public boolean verifyOtp() {

        UserPreference userPreference = this.userPreferenceDao.getUserPreference(principal().getUserLoginId(), "2FAEnabled");
        userPreference.setUserPrefValue("N");
        this.userPreferenceDao.update(userPreference.getUserLoginId(),userPreference);

        return true;
    }
}
