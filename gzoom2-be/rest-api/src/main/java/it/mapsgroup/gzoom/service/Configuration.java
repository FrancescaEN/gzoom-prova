package it.mapsgroup.gzoom.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface Configuration {

    /**
     * Tells whether some localization data (translations and formats) is provided for the given locale.
     *
     * @param locale The locale of the translation and formats.
     * @return True if locale is supported, false otherwise
     */
    boolean isLocaleSupported(Locale locale);

    /**
     * Retrieves the translation map for a certain locale or null if locale is not supported.
     *
     * @param locale The locale
     * @return The translation map for a certain locale or null if locale is not supported.
     */
    Map<String, String> getTranslations(Locale locale);
    Map<String, Object> getTranslations_v2(Locale locale);

    /**
     * Retrieves the formats of the given locale or null if locale is not supported.
     *
     * @param locale The locale
     * @return The formats of the given locale or null if locale is not supported.
     */
    Map<String, Object> getFormats(Locale locale);
    
    /**
     * Retrieves the label calendar of the given locale or null if locale is not supported.
     *
     * @param locale The locale
     * @return The formats of the given locale or null if locale is not supported.
     */
    Map<String, Object> getPrimeng(Locale locale);

    /**
     * Retrieves the URL path to the REST API services.
     *
     * @return The URL path to the REST API services.
     */
    String getRestPath();

    /**
     * The number of days before the threshold date that identify the deadline date.
     *
     * @return The number of date before the threshold that identify the deadline date
     */
    int getDeadlineDays();

    /**
     * The supported languages
     * @return
     */
    List<String> getLanguages();

    /**
     * The languages type
     * @return
     */
    String getLanguageType();

    /**
     *  Organization multi type
     * @return
     */
    String getOrganizationMultiType();

    /**
     *  Return Mail host
     * @return
     */
    String getMailHost();

    /**
     *  Return if 2fauth is enabled
     * @return
     */
    boolean isForce2FAuth();

    /**
     *  Return if 2fauth is activable or disable
     * @return
     */
    boolean isManage2FAuth();

    /**
     *  Return if possible change password
     * @return
     */
    boolean isEnableChangePassword();

    /**
     *  Return if ldap is enabled
     * @return
     */
    boolean isLdap();

    /**
     *  Return if Refresh of material view is enabled
     * @return
     */
    boolean isRefreshMaterialView();

    /**
     *  Return if title and Logo are switched
     * @return
     */
    boolean isSwitchTitleLogo();

}
