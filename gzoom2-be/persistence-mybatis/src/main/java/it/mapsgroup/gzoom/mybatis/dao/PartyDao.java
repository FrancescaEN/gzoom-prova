package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.mybatis.mapper.PartyMapper;
import it.mapsgroup.gzoom.mybatis.service.FilterService;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PartyDao extends AbstractDao{
    private static final Logger LOG = getLogger(PartyDao.class);
    private final SequenceGenerator sequenceGenerator;
    private final PartyMapper partyMapper;
    private final FilterService filterService;
    private final UserLoginDao userLoginDao;
    private final WorkEffortTypeContentDao workEffortTypeContentDao;

    @Autowired
    public PartyDao(SequenceGenerator sequenceGenerator, PartyMapper partyMapper, FilterService filterService, UserLoginDao userLoginDao, WorkEffortTypeContentDao workEffortTypeContentDao) {
        this.sequenceGenerator = sequenceGenerator;
        this.partyMapper = partyMapper;
        this.filterService = filterService;
        this.userLoginDao = userLoginDao;
        this.workEffortTypeContentDao = workEffortTypeContentDao;
    }

    @Transactional
    public boolean create(Party record) {
        LOG.info("create party");
        setCreatedTimestamp(record);
        String newId = this.sequenceGenerator.getNextSeqId("Party");
        record.setPartyId(newId);
        int result = this.partyMapper.insert(record);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public Party findByPartyId(String id) {
        LOG.info("find party by id");

        Party party = this.partyMapper.selectByPrimaryKey(id);
        LOG.info("Party = {}", (party != null));
        return party;
    }


    @Transactional
    public List<Party> getParties(String userLoginId, String parentTypeId) {
        LOG.info("getParties");

        Map<String, Object> filterPermission = this.filterService.setMapFilter(userLoginId, ContextPermissionPrefixEnum.valueOf(parentTypeId));
        List<Party> parties = this.partyMapper.getParties(userLoginId, parentTypeId, filterPermission);
        LOG.info("size = {}", parties.size());
        return parties;
    }

    @Transactional
    public List<Party> selectByRoleTypeId(String roleTypeId) {
        LOG.info("find party by roleTypeId");
        List<Party> parties = this.partyMapper.selectByRoleTypeId(roleTypeId);
        LOG.info("size = {}", parties.size());
        return parties;
    }

    @Transactional
    public List<Party> selectByRoleTypeIdAndNotInGlAccount(String roleTypeId, String glAccountId) {
        LOG.info("selectByRoleTypeIdAndNotInGlAccount");
        List<Party> parties = this.partyMapper.selectByRoleTypeIdAndNotInGlAccount(roleTypeId, glAccountId);
        LOG.info("size = {}", parties.size());
        return parties;
    }

    @Transactional
    public List<Party> getPartiesByRoleTypeIdAndOrganizationId(String roleTypeId, String organizationId) {
        LOG.info("getPartiesByRoleTypeIdAndOrganizationId");
        List<Party> parties = this.partyMapper.getPartiesByRoleTypeIdAndOrganizationId(roleTypeId, organizationId);
        LOG.info("size = {}", parties.size());
        return parties;
    }

    @Transactional
    public List<PersonEx> getPartiesExposed() {
        LOG.info("getPartiesExposed");

        List<PersonEx> personExList = this.partyMapper.getPartiesExposed();
        LOG.info("size = {}", personExList.size());
        return personExList;
    }

    @Transactional
    public List<Party> getRoleTypePartys(String roleTypeId, String roleTypeIdFrom, String workEffortTypeId) {
        LOG.info("getRoleTypePartys");

        String[] roleTypeIdFromArray = {"20DIR","30SETT"};
        if(roleTypeIdFrom!=null)
            roleTypeIdFromArray = roleTypeIdFrom.split(",");

        List<Party> parties = this.partyMapper.getRoleTypePartys(roleTypeId, roleTypeIdFrom, roleTypeIdFromArray, workEffortTypeId, !this.checkPartyRole(workEffortTypeId).isEmpty());
        LOG.info("size = {}", parties.size());
        return parties;
    }

    @Transactional
    public List<PartyRole> checkPartyRole(String workEffortTypeId) {
        LOG.info("checkPartyRole");

        List<PartyRole> partyRoles = this.partyMapper.checkPartyRole(workEffortTypeId);
        LOG.info("size = {}", partyRoles.size());
        return partyRoles;
    }

    @Transactional
    public List<Party> getRoleTypePartysBetween(String roleTypeId) {
        LOG.info("getRoleTypePartysBetween");

        List<Party> parties = this.partyMapper.getRoleTypePartysBetween(roleTypeId.split(",")[0], roleTypeId.split(",")[1]);
        LOG.info("size = {}", parties.size());
        return parties;
    }

    @Transactional
    public List<PartyEx> getOrgUnits(String userLoginId, String parentTypeId, String roleTypeId, String workEffortTypeId, String company, List<String> languages) {
        LOG.info("getOrgUnits");

        Map<String, Object> filterPermission = this.filterService.setMapFilter(userLoginId, ContextPermissionPrefixEnum.valueOf(parentTypeId));
        String[] roleType = new String[]{};
        if (roleTypeId != null)
            roleType = roleTypeId.split(",");

        String orderUoBy = "MAINCODE";
        Map<String, String> paramsMap = this.workEffortTypeContentDao.getWorkEffortTypeContentParams(parentTypeId, "WEFLD_MAIN");
        if (paramsMap != null && !paramsMap.isEmpty() && paramsMap.get("orderUoBy") != null) {
            orderUoBy = paramsMap.get("orderUoBy");
        }
        List<PartyEx> partyExList = this.partyMapper.getOrgUnits(userLoginId, parentTypeId, filterPermission, roleType, workEffortTypeId, company, isLanguageLang(userLoginId, languages), !checkPartyRole(workEffortTypeId).isEmpty(), orderUoBy);
        LOG.info("size = {}", partyExList.size());
        return partyExList;
    }

    @Transactional
    public List<PartyEx> getUOGestore(String organizationId, String userLoginId, boolean isFullAdmin) {
        LOG.info("getUOGestore");
        List<PartyEx> partyExList = this.partyMapper.getUOGestore(organizationId, userLoginId, isFullAdmin);
        LOG.info("size = {}", partyExList.size());
        return partyExList;
    }

    @Transactional
    public List<PartyEx> getUOGestoreByRespCenterRoleTypeId(String organizationId, String respCenterRoleTypeId, String userLoginId, boolean isFullAdmin) {
        LOG.info("getUOGestoreByRespCenterRoleTypeId");
        List<PartyEx> partyExList = this.partyMapper.getUOGestoreByRespCenterRoleTypeId(organizationId, respCenterRoleTypeId, userLoginId, isFullAdmin);
        LOG.info("size = {}", partyExList.size());
        return partyExList;
    }

    @Transactional
    public List<Party> getPartyByOrganizationId(String organizationId) {
        LOG.info("getPartyByOrganizationId");
        List<Party> parties = this.partyMapper.getPartyByOrganizationId(organizationId);
        LOG.info("size = {}", parties.size());
        return parties;
    }

    @Transactional
    public List<Party> getManagers() {
        LOG.info("getManagers");
        List<Party> parties = this.partyMapper.getManagers();
        LOG.info("size = {}", parties.size());
        return parties;
    }

    @Transactional
    public HeaderPortalPage getHeaderPortalPage(String userLoginId) {
        LOG.info("getHeaderPortalPage");
        HeaderPortalPage headerPortalPage = this.partyMapper.getHeaderPortalPage(userLoginId);
        return headerPortalPage;
    }

    private boolean isLanguageLang(String userLoginId, List<String> languages) {
        boolean languageLang = false;
        if (languages != null && languages.size() > 1) {
            UserLogin profile = userLoginDao.getUserLogin(userLoginId);
            if (profile != null && profile.getLastLocale() != null && !profile.getLastLocale().isEmpty()){
                String [] localeStr = profile.getLastLocale().split("_");
                Locale locale = Locale.of(localeStr[0],localeStr[1]);
                languageLang = locale.toString().equals(languages.get(1));
            }
        }
        return languageLang;
    }

    @Transactional
    public int anonymizeParty(Instant expirationDate) {
        LOG.info("anonymizeParty update");
        int result = this.partyMapper.anonymizeParty(expirationDate);
        LOG.info("result = {}", result);
        return result;
    }
}
