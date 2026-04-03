package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.PartyRole;
import it.mapsgroup.gzoom.mybatis.dto.PartyRoleEx;
import it.mapsgroup.gzoom.mybatis.mapper.PartyRoleMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PartyRoleDao extends AbstractDao{
    private static final Logger LOG = getLogger(PartyRoleDao.class);
    private final SequenceGenerator sequenceGenerator;
    private final UserLoginDao userLoginDao;
    private final PartyRoleMapper partyRoleMapper;

    @Autowired
    public PartyRoleDao(SequenceGenerator sequenceGenerator, UserLoginDao userLoginDao, PartyRoleMapper partyRoleMapper) {
        this.sequenceGenerator = sequenceGenerator;
        this.userLoginDao = userLoginDao;
        this.partyRoleMapper = partyRoleMapper;
    }

    @Transactional
    public List<PartyRoleEx> getPartyRoleOrgUnit(String userPreferenceOrganizationUnitId) {
        LOG.info("getPartyRoleOrgUnit");

        List<PartyRoleEx> partyRoleList = this.partyRoleMapper.getPartyRoleOrgUnit(userPreferenceOrganizationUnitId);
        LOG.info("size = {}", partyRoleList.size());
        return partyRoleList;
    }

    @Transactional
    public List<PartyRoleEx> getPartyRoleExOrgUnit(String userPreferenceOrganizationUnitId, String userLoginId, String accountTypeEnumId) {
        LOG.info("getPartyRoleExOrgUnit");
        List<PartyRoleEx> partyRoleList = this.partyRoleMapper.getPartyRoleExOrgUnit(userPreferenceOrganizationUnitId, userLoginId, accountTypeEnumId);
        LOG.info("size = {}", partyRoleList.size());
        return partyRoleList;
    }

    @Transactional
    public List<PartyRole> getPartyRole(
            boolean isSecondaryLang,
            String search,
            String matchModeSearch,
            String[] roleTypeId
    ) {
        LOG.info("getPartyRole");
        List<PartyRole> partyRoles = this.partyRoleMapper.getPartyRole(isSecondaryLang, search, matchModeSearch, roleTypeId);
        LOG.info("size = {}", partyRoles.size());
        return partyRoles;
    }

}
