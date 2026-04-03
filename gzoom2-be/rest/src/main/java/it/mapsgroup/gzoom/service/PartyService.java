package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.PartyDao;
import it.mapsgroup.gzoom.mybatis.dao.UserCtxPermissionViewDao;
import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.mybatis.dao.PersonDao;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import it.mapsgroup.gzoom.mybatis.util.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static it.mapsgroup.gzoom.security.Principals.principal;


/**
 * Profile service.
 *
 */
@Service
public class PartyService {

    private final PartyDao partyDao;
    private final PersonDao personDao;
    private final DtoMapper dtoMapper;
    private final Configuration config;
    private final PermissionService permissionService;
    private final UserPreferenceService userPreferenceService;
private final UserCtxPermissionViewDao userCtxPermissionViewDao;
    @Autowired
    public PartyService(PartyDao partyDao, PersonDao personDao, DtoMapper dtoMapper, Configuration config, PermissionService permissionService, UserPreferenceService userPreferenceService, UserCtxPermissionViewDao userCtxPermissionViewDao) {
        this.partyDao = partyDao;
        this.personDao = personDao;
        this.dtoMapper = dtoMapper;
        this.config = config;
        this.permissionService = permissionService;
        this.userPreferenceService = userPreferenceService;
        this.userCtxPermissionViewDao = userCtxPermissionViewDao;
    }

    public Result<Party> getPartyByRoleTypeId(String roleTypeId) {
        List<Party> list = this.partyDao.selectByRoleTypeId(roleTypeId);
        return new Result<>(list, list.size());
    }

    public Result<Party> getByRoleTypeIdAndNotInGlAccount(String roleTypeId,String glAccountId) {
        List<Party> list = this.partyDao.selectByRoleTypeIdAndNotInGlAccount(roleTypeId, glAccountId);
        return new Result<>(list, list.size());
    }

    public Result<Party> getPartiesByRoleTypeIdAndCurrentOrganizationId(String roleTypeId) {
        List<Party> list = this.partyDao.getPartiesByRoleTypeIdAndOrganizationId(roleTypeId, this.permissionService.userPrefereceOrganizationUnitId(principal().getUserLoginId()));
        return new Result<>(list, list.size());
    }

    public Result<Person> getPersons() {
        List<Person> list = personDao.getPersons();
        return new Result<>(list, list.size());
    }
    
    public Result<Party> getPartys(String userLoginId, String parentTypeId) {
        List<Party> list = partyDao.getParties(userLoginId, parentTypeId);
        return new Result<>(list, list.size());
    }
    
    public Result<PartyEx> getOrgUnits(String userLoginId, String parentTypeId, String roleTypeId, String workEffortTypeId, String company) {
        List<PartyEx> list = partyDao.getOrgUnits(userLoginId, parentTypeId, roleTypeId, workEffortTypeId, company, config.getLanguages());
        return new Result<>(list, list.size());
    }

    public Result<PartyEx> getUOGestore(String context, String organizationId) {
        String permission = ContextPermissionPrefixEnum.getPermissionPrefix(context);
        List<PartyEx> list = partyDao.getUOGestore(organizationId, principal().getUserLoginId(), this.userCtxPermissionViewDao.hasPermission(principal().getUserLoginId(), ContextPermissionPrefixEnum.valueOf(context), Permission.ADMIN));
        return new Result<>(list, list.size());
    }

    public Result<PartyEx> getUOGestoreByRespCenterRoleTypeId(String context, String respCenterRoleTypeId) {
        String permission = ContextPermissionPrefixEnum.getPermissionPrefix(context);
        List<PartyEx> list = partyDao.getUOGestoreByRespCenterRoleTypeId(this.userPreferenceService.getOrganizationId(), respCenterRoleTypeId,  principal().getUserLoginId(), this.userCtxPermissionViewDao.hasPermission(principal().getUserLoginId(), ContextPermissionPrefixEnum.valueOf(context), Permission.ADMIN));
        return new Result<>(list, list.size());
    }

    public Result<Party> getPartyByOrganizationId(String userLoginId) {
        String organizationId = this.permissionService.userPrefereceOrganizationUnitId(userLoginId);
        List<Party> list = partyDao.getPartyByOrganizationId(organizationId);
        return new Result<>(list, list.size());
    }

    public Result<Party> getManagers() {
        List<Party> list = partyDao.getManagers();
        return new Result<>(list, list.size());
    }
    
    public Result<Party> getRoleTypePartys(String roleTypeId, String roleTypeIdFrom, String workEffortTypeId) {
        List<Party> list = partyDao.getRoleTypePartys(roleTypeId,roleTypeIdFrom,workEffortTypeId);
        return new Result<>(list, list.size());
    }

    public Result<Party> getRoleTypePartysBetween(String roleTypeId) {
        List<Party> list = partyDao.getRoleTypePartysBetween(roleTypeId);
        return new Result<>(list, list.size());
    }

    public Result<it.mapsgroup.gzoom.model.Person> getPartiesExposed() {
        List<PersonEx> list = partyDao.getPartiesExposed();
        List<it.mapsgroup.gzoom.model.Person> ret = list.stream().map(p -> dtoMapper.copy(p, new it.mapsgroup.gzoom.model.Person())).collect(Collectors.toList());
        return new Result<>(ret, ret.size());
    }

    public Party findByPartyId(String id) {
        Party party = partyDao.findByPartyId(id);
        return party;
    }

    public HeaderPortalPage getHeaderPortalPage() {
        HeaderPortalPage headerPortalPage = partyDao.getHeaderPortalPage(principal().getUserLoginId());
        return headerPortalPage;
    }

    public int anonymizeParty(Instant expirationDate) {
        return this.partyDao.anonymizeParty(expirationDate);
    }
}
