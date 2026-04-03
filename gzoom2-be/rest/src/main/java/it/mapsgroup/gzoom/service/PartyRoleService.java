package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.PartyRoleDao;
import it.mapsgroup.gzoom.mybatis.dto.PartyRole;
import it.mapsgroup.gzoom.mybatis.dto.PartyRoleEx;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alex Tivoli
 */
@Service
public class PartyRoleService {
    private final PartyRoleDao partyRoleDao;
    private final PermissionService permissionService;

    @Autowired
    public PartyRoleService(PartyRoleDao partyRoleDao, PermissionService permissionService) {
        this.partyRoleDao = partyRoleDao;
        this.permissionService = permissionService;
    }

    public Result<PartyRoleEx> getPartyRoleOrgUnit(String userLoginId) {
        String userPreferenceOrganizationUnitId = this.permissionService.userPrefereceOrganizationUnitId(userLoginId);
        List<PartyRoleEx> list = this.partyRoleDao.getPartyRoleOrgUnit(userPreferenceOrganizationUnitId);
        return new Result<>(list, list.size());
    }

    public Result<PartyRoleEx> getPartyRoleExOrgUnit(String userLoginId, String accountTypeEnumId) {
        String userPreferenceOrganizationUnitId = this.permissionService.userPrefereceOrganizationUnitId(userLoginId);
        List<PartyRoleEx> list = this.partyRoleDao.getPartyRoleExOrgUnit(userPreferenceOrganizationUnitId, userLoginId, accountTypeEnumId);
        return new Result<>(list, list.size());
    }

    public Result<PartyRole> getPartyRole(
            boolean isSecondaryLang,
            String search,
            String matchModeSearch,
            String[] roleTypeId
    ) {
        List<PartyRole> list = this.partyRoleDao.getPartyRole(isSecondaryLang, search, matchModeSearch, roleTypeId);
        return new Result<>(list, list.size());
    }

}