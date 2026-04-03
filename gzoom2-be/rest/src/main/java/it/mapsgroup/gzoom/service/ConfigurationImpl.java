package it.mapsgroup.gzoom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClientConfig;
import it.mapsgroup.gzoom.quartz.SchedulerConfig;
import it.mapsgroup.gzoom.quartz.scheduler.dto.ServiceJob;
import it.mapsgroup.gzoom.security.model.SecurityConfiguration;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static it.mapsgroup.gzoom.common.JsonLocalizationReader.readClasspathResources;
import static it.mapsgroup.gzoom.common.JsonLocalizationReader.readDirectory;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Configuration implementation.
 */
public class ConfigurationImpl implements Configuration, SecurityConfiguration, OfBizClientConfig, GzoomReportClientConfig, SchedulerConfig {
    private static final Logger LOG = getLogger(ConfigurationImpl.class);
    // Localizations
    private final Path localeDirPath;
    private final List<String> translationResources = new ArrayList<>();
    private volatile Map<String, Map<String, Object>> localizations;
    // REST
    private final int tokenExpiryMinutes;
    private final String restPath;
    // non-REST properties
    private final int deadlineDays;
    private final String configurationPath;
    private final String ofbizServerXmlrpcUrl;
    private final String gzoomServerReportUrl;
    private final int reportProbeDelay;
    private final int reportProbeRetries;
    private final List<String> languages;
    private final String languageType;
    private final String organizationMultiType;
    private final String documentPath;
    private final String ofbizPath;
    private final boolean enableScheduler;
    private final ServiceJob serviceJobList;
    private final String mailHost;
    private final boolean force2FAuth;
    private final boolean manage2FAuth;
    private final boolean enableChangePassword;
    private final boolean ldap;
    private final boolean refreshMaterialView;

    private final boolean switchTitleLogo;

    @Autowired
    public ConfigurationImpl(Environment env) {
        // document path
        this.documentPath = env.getProperty("document.path", String.class, "NONE");
        //ofbiz path
        this.ofbizPath = env.getProperty("ofbiz.path", String.class, "NONE");
        // localization
        this.localeDirPath = Paths.get(env.getProperty("gzoom.conf.dir") + "/locales");
        this.languageType = env.getProperty("language.multi.type", String.class, "NONE");
        this.languages = Arrays.asList(env.getProperty("language.locales.available").split(","));
        this.translationResources.add("/lmm/locales/it.json");
        //Add locals json if exists multiple lang
        for (String l : languages) {
            if (!this.translationResources.contains("/lmm/locales/" + l.split("_")[0] + ".json"))
                this.translationResources.add("/lmm/locales/" + l.split("_")[0] + ".json");
        }
        this.localizations = initLocalization();

        //multi tenant
        this.organizationMultiType = env.getProperty("organizzation.multi.type", String.class, "N");

        // rest properties
        this.restPath = env.getProperty("rest.path", "../rest");
        this.tokenExpiryMinutes = env.getProperty("rest.token.expiry.minutes", Integer.class, 43200);

        this.deadlineDays = env.getProperty("deadline.days", Integer.class, 5);

        this.configurationPath = env.getProperty("gzoom.conf.dir");

        this.ofbizServerXmlrpcUrl = env.getProperty("ofbiz.server.xmlrpc.url");

        this.gzoomServerReportUrl = env.getProperty("gzoom.server.report.url");

        this.reportProbeDelay = env.getProperty("gzoom.quartz.report.probe.delay", Integer.class, 60);//60 sec default
        this.reportProbeRetries = env.getProperty("gzoom.quartz.report.probe.retries", Integer.class, 20);//number of retires

        this.enableScheduler = env.getProperty("gzoom.scheduler.enable", boolean.class, false);
        this.serviceJobList = this.readServices();
        this.initScheduler(env);

        this.force2FAuth = env.getProperty("gzoom.2fAuth.force2FAuth", boolean.class, false);
        this.manage2FAuth = env.getProperty("gzoom.2fAuth.manage2FAuth", boolean.class, true);
        this.mailHost = env.getProperty("gzoom.mail.host", String.class, "N");
        this.enableChangePassword = env.getProperty("security.enableChangePassword", Boolean.class, true);
        this.ldap = env.getProperty("management.health.ldap.enabled", Boolean.class, false);
        this.refreshMaterialView = env.getProperty("gzoom.refreshMaterialView", Boolean.class, false);
        this.switchTitleLogo = env.getProperty("gzoom.switchTitleLogo", Boolean.class, false);
    }

    @Override
    public int getTokenExpiryMinutes() {
        return tokenExpiryMinutes;
    }

    @Override
    public String getRestPath() {
        return restPath;
    }

    @Override
    public int getDeadlineDays() {
        return deadlineDays;
    }

    @Override
    public boolean isLocaleSupported(Locale locale) {
        return localizationOf(locale) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> getTranslations(Locale locale) {
        Map<String, Object> map = localizationOf(locale);
        if (map == null)
            return null;
        return (Map<String, String>) map.get("translations");
    }

    @Override
    public Map<String, Object> getTranslations_v2(Locale locale) {
        Map<String, Object> map = localizationOf(locale);
        if (map == null)
            return null;
        return (Map<String, Object>) map.get("translations_v2");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getFormats(Locale locale) {
        Map<String, Object> map = localizationOf(locale);
        if (map == null)
            return null;
        return (Map<String, Object>) map.get("formats");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getPrimeng(Locale locale) {
        Map<String, Object> map = localizationOf(locale);
        if (map == null)
            return null;
        return (Map<String, Object>) map.get("primeng");
    }

    private Map<String, Object> localizationOf(Locale locale) {
        // copy reference, this will synchronize writing of the volatile reference
        Map<String, Map<String, Object>> map = localizations;

        // attempt #1: using the locale complete formatting
        if (map.containsKey(locale.toString()))
            return map.get(locale.toString());
        // attempt #2: with language and country only
        // this assumes the passed locale used variant, script or extensions that are
        // not considered here
        String l = Locale.of(locale.getLanguage(), locale.getCountry()).toString();
        if (map.containsKey(l))
            return map.get(l);
        // attempt #3: with the language only
        if (map.containsKey(locale.getLanguage()))
            return map.get(locale.getLanguage());

        return null;
    }

    private Map<String, Map<String, Object>> initLocalization() {
        Map<String, Map<String, Object>> cpLoc = readClasspathResources(translationResources);
        Map<String, Map<String, Object>> dirLoc = readDirectory(localeDirPath);


        dirLoc.forEach((key, dirLocLang) -> {
            if (cpLoc.containsKey(key)) {
                Map<String, Object> cpLocLang = cpLoc.get(key);
                dirLocLang.forEach((key2, dirLocLangItem) ->
                {
                    if (cpLocLang.containsKey(key2))
                        ((Map<String, Object>) cpLocLang.get(key2)).putAll((Map<String, Object>) dirLocLangItem);
                    else cpLocLang.put(key2, dirLocLangItem);
                });
            } else {
                cpLoc.put(key, dirLocLang);
            }
        });

        return cpLoc;
    }


    public String getConfigurationPath() {
        return configurationPath;
    }

    @Override
    public URL getServerXmlRpcUrl() {
        try {
            return new URI(ofbizServerXmlrpcUrl).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("ofbizServerXmlrpcUrl is wrong");
        }
    }

    @Override
    public URL getServerReportUrl() {
        try {
            return new URI(gzoomServerReportUrl).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("gzoomServerReportUrl is wrong");
        }
    }

    @Override
    public int getReportProbeDelay() {
        return this.reportProbeDelay;
    }

    @Override
    public int getReportProbeRetries() {
        return this.reportProbeRetries;
    }

    public List<String> getLanguages() {
        return this.languages;
    }

    public String getLanguageType() {
        return this.languageType;
    }

    public String getOrganizationMultiType() {
        return this.organizationMultiType;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public String getOfbizPath() {
        return ofbizPath;
    }

    public boolean isEnableScheduler() {
        return enableScheduler;
    }

    private void initScheduler(Environment env) {
         this.serviceJobList.getServicesRequired().forEach(serviceJobInfo -> serviceJobInfo.getParameters().forEach(parameterJob -> {
             String propKey = "gzoom.scheduler." + serviceJobInfo.getKey() + "." + parameterJob.getKey();
             String prop = env.getProperty(propKey);
             if (prop != null) {
                 switch (parameterJob.getType()) {
                     case "int":
                         try {
                             parameterJob.setDefaultValue(Integer.parseInt(prop));
                         } catch (NumberFormatException e) {
                             LOG.error(propKey + ": " + e.getMessage());
                         }

                         break;
                     case "float":
                         try {
                             parameterJob.setDefaultValue(Float.parseFloat(prop));
                         } catch (NumberFormatException e) {
                             LOG.error(propKey + ": " + e.getMessage());
                         }
                         break;

                     default:
                         parameterJob.setDefaultValue(prop);
                         break;
                 }
             }
         }));
    }

    public ServiceJob getServiceJob() {
        return this.serviceJobList;
    }

    /**
     * This retrieves the list of schedulable services present in the application.
     *
     * @return ServiceJob
     */
    private ServiceJob readServices() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassPathResource classPathResource = new ClassPathResource("/scheduler/services.json");
            return objectMapper.readValue(classPathResource.getInputStream(), ServiceJob.class);
        } catch (IOException e) {
            LOG.error("No services found: {}", e);
            return new ServiceJob();
        }
    }

    public String getMailHost() {
        return mailHost;
    }

    public boolean isForce2FAuth() {
        return force2FAuth;
    }

    public boolean isManage2FAuth() {
        return manage2FAuth;
    }

    public boolean isEnableChangePassword() {
        return enableChangePassword;
    }

    public boolean isLdap() {
        return ldap;
    }
    public boolean isRefreshMaterialView() {
        return refreshMaterialView;
    }
    public boolean isSwitchTitleLogo() {
        return switchTitleLogo;
    }

}
