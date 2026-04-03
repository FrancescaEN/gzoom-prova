package com.mapsengineering.base.birt.util;

import it.mapsgroup.gzoom.mybatis.dto.Party;
import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import it.mapsgroup.gzoom.service.NodeService;
import it.mapsgroup.gzoom.service.PartyService;
import it.mapsgroup.gzoom.service.UserPreferenceService;
import it.memelabs.smartnebula.spring.boot.config.ApplicationContextProvider;
import org.slf4j.Logger;


import static org.slf4j.LoggerFactory.getLogger;

public class AppHeaderLogo {
    private static final Logger LOG = getLogger(AppHeaderLogo.class);

    private NodeService nodeService;


    /**
     * cerca il logo associato alla company, se non lo trova ritorna il percorso del logo di default
     * @param delegator
     * @param partyContentTypeId
     * @param defaultOrganizationPartyId
     * @return
     */
    public static String getAppContentUrl(Object delegator, String partyContentTypeId, String defaultOrganizationPartyId) {
        LOG.error(" TODO getAppContentUrl NOT EXISTS");
        return null;
    }

    /**
     * compone la url del logo, da richiamare poi nell ftl
     * @param contentId
     * @return
     */
    private static String getImgUrlFromContent(String contentId) {
        StringBuilder sb = new StringBuilder();
        sb.append("stream?contentId=");
        sb.append(contentId);
        return sb.toString();
    }

    /**
     * Cerco il logo associato alla Company per utilizzarlo nei report.
     *
     */
    public static String getReportContentUrl(String partyContentTypeId, String defaultOrganizationPartyId) {
        String fileName = null;
        NodeService nodeService = ApplicationContextProvider.getApplicationContext().getBean(NodeService.class);
        String partyContentEx = nodeService.getObjectInfo(defaultOrganizationPartyId, partyContentTypeId);

        if (partyContentEx != null) {
            LOG.info("stream partyContent path: " + partyContentEx);

        } else {
            LOG.info("No valid partyContent path found, use default... ");
        }
        return partyContentEx;
    }

    /**
     * Cerco il logo associato alla Company per utilizzarlo nei report.
      *
     */
    public static String getReportContentUrl(Object delegator, String partyContentTypeId, String defaultOrganizationPartyId) {
        LOG.info(" TODO getReportContentUrl with delegator TO DELETE ");
        return getReportContentUrl(partyContentTypeId, defaultOrganizationPartyId);
    }

    public static String getCompany(String userLoginId){
        return getCompany(userLoginId, null);
    }

    public static String getCompany(String userLoginId, String langLocale) {

        Boolean isSecondaryLang = langLocale != null && langLocale.equals("_LANG");

        UserPreferenceService userPreferenceService = ApplicationContextProvider.getApplicationContext().getBean(UserPreferenceService.class);
        PartyService partyService = ApplicationContextProvider.getApplicationContext().getBean(PartyService.class);
        UserPreference up = userPreferenceService.getUserPreference(userLoginId);
        if(up != null){
            Party party = partyService.getParty(up.getUserPrefValue());
            if(party != null){
                return isSecondaryLang? party.getPartyNameLang() : party.getPartyName();
            }
        }
        return "";
    }


}
