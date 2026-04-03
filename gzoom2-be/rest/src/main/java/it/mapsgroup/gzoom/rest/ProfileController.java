package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.model.Localization;
import it.mapsgroup.gzoom.model.Permissions;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.service.LocaleService;
import it.mapsgroup.gzoom.service.ProfileService;
import it.mapsgroup.gzoom.common.Exec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author Fabio G. Strozzi
 */
@RestController
@RequestMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProfileController {

    private final LocaleService localeService;
    private final ProfileService profileService;

    @Autowired
    public ProfileController(LocaleService localeService, ProfileService profileService) {
        this.localeService = localeService;
        this.profileService = profileService;
    }

    @RequestMapping(value = "/profile/i18n", method = RequestMethod.GET)
    @ResponseBody
    public Localization i18n(HttpServletRequest req) {
        return Exec.exec("profile-18n", () -> {
            Locale locale = localeService.getLocalization(req);
            return locale != null ? localeService.getLocalization(locale) : Localization.DEFAULT;
        });
    }

    @RequestMapping(value = "/profile/i18n/{user}/", method = RequestMethod.GET)
    @ResponseBody
    public Localization i18n(HttpServletRequest req,@PathVariable(value = "user") String user) {
        return Exec.exec("profile-18n", () -> {
            Locale locale = localeService.getLocalization(req,user);
            return locale != null ? localeService.getLocalization(locale) : Localization.DEFAULT;
        });
    }

    @RequestMapping(value = "/profile/i18n/languages", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> languages() {
        return Exec.exec("get-languages", () -> localeService.getLanguages());
    }

    @RequestMapping(value = "/profile/i18n/language-type", method = RequestMethod.GET)
    @ResponseBody
    public String languageType() {
        return Exec.exec("get-language-type", () -> localeService.getLanguageType());
    }
    
    @RequestMapping(value = "/account/permissions", method = RequestMethod.GET)
    @ResponseBody
    public Permissions getUserPermission() {
        return Exec.exec("user-permission", () -> profileService.getUserPermission());
    }

    @RequestMapping(value = "/profile/organization-multi-type", method = RequestMethod.GET)
    @ResponseBody
    public String getOranizationMultiType() {
        return Exec.exec("get-organization-multi-type", () -> profileService.getOrganizationMultiType());
    }

    @RequestMapping(value = "/profile/mail-host", method = RequestMethod.GET)
    @ResponseBody
    public String getMailHost() {
        return Exec.exec("get-mail-host", localeService::getMailHost);
    }

    @RequestMapping(value = "/profile/force-2fauth", method = RequestMethod.GET)
    @ResponseBody
    public boolean isForce2FAuth() {
        return Exec.exec("is-force2FAuth", localeService::isForce2FAuth);
    }

    @RequestMapping(value = "/profile/manage-2fauth", method = RequestMethod.GET)
    @ResponseBody
    public boolean isManage2FAuth() {
        return Exec.exec("is-manage2FAuth", localeService::isManage2FAuth);
    }

    @RequestMapping(value = "/profile/enableChangePassword", method = RequestMethod.GET)
    @ResponseBody
    public boolean enableChangePassword() {
        return Exec.exec("is-enableChangePassword", localeService::isEnableChangePassword);
    }

    @RequestMapping(value = "/profile/isLdap", method = RequestMethod.GET)
    @ResponseBody
    public boolean isLdap() {
        return Exec.exec("is-Ldap", localeService::isLdap);
    }

    @RequestMapping(value = "/profile/switchTitleLogo", method = RequestMethod.GET)
    @ResponseBody
    public boolean isSwitchTitleLogo() {
        return Exec.exec("is-switchTitleLogo", localeService::isSwitchTitleLogo);
    }
}
