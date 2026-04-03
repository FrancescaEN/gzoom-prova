package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Localization;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginDao;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Localization service.
 *
 * @author Fabio G. Strozzi
 */
@Service
public class LocaleService {
    private final Configuration config;
    private final UserLoginDao userLoginDao;

    @Autowired
    public LocaleService(Configuration config, UserLoginDao userLoginDao ) {
        this.config = config;
        this.userLoginDao = userLoginDao;
    }

    public Locale getLocalization(HttpServletRequest req) {
        return this.getLocalization(req,null);
    }

    public Locale getLocalization(HttpServletRequest req, String user) {
        String header = req.getHeader(HttpHeaders.ACCEPT_LANGUAGE);

        if(user!=null) {
            UserLogin profile = userLoginDao.getUserLogin(user);
            if (profile!=null && profile.getLastLocale()!=null && !profile.getLastLocale().equals("")){
                return getLocation(user);
            }
        }

        return header != null ? req.getLocale() : null;
    }

    public Locale getLocation (String user) {
        UserLogin profile = userLoginDao.getUserLogin(user);
        if (profile!=null && profile.getLastLocale()!=null && !profile.getLastLocale().equals("")){
            String [] locale = profile.getLastLocale().split("_");
            return Locale.of(locale[0],locale[1]);
        }

        return null;
    }

    public Localization getLocalization(Locale locale) {
        if (!config.isLocaleSupported(locale)) {
            return Localization.DEFAULT;
        }

        Map<String, String> trans = config.getTranslations(locale);
        Map<String, Object> trans_v2 = config.getTranslations_v2(locale);
        Map<String, Object> formats = config.getFormats(locale);
        Map<String, Object> primeng = config.getPrimeng(locale);

        return new Localization(locale.getLanguage(), trans, trans_v2, formats, primeng);
    }

    public Result<String> getLanguages() {
        List<String> list = config.getLanguages();
        return new Result<>(list, list.size());
    }

    public String getLanguageType() {
        return config.getLanguageType();
    }

    public String getMailHost() {
        return config.getMailHost();
    }

    public boolean isManage2FAuth() {
        return config.isManage2FAuth();
    }

    public boolean isForce2FAuth() {
        return config.isForce2FAuth();
    }

    public boolean isEnableChangePassword() {
        return config.isEnableChangePassword();
    }

    public boolean isLdap() {
        return config.isLdap();
    }

    public boolean isRefreshMaterialView() {
        return config.isRefreshMaterialView();
    }
    public boolean isSwitchTitleLogo() {
        return config.isSwitchTitleLogo();
    }


}
