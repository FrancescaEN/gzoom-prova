package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.mapper.PartyRoleViewMapper;
import it.mapsgroup.gzoom.mybatis.dto.PartyRoleView;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PartyRoleViewDao {
    private static final Logger LOG = getLogger(PartyRoleViewDao.class);
    private final PartyRoleViewMapper partyRoleViewMapper;

    @Autowired
    public PartyRoleViewDao(PartyRoleViewMapper partyRoleViewMapper) {
        this.partyRoleViewMapper = partyRoleViewMapper;
    }

    @Transactional
    public List<PartyRoleView> getPartyRoleView(String statusId, String organizationId){
        LOG.info("select by statusId and organizationId order by partyName");

        List<PartyRoleView> partyRoleViewList = this.partyRoleViewMapper.selectByStatusIdAndOrganizationIdOrderByPartyName(statusId, organizationId);
        LOG.info("size = {}", partyRoleViewList.size());
        return partyRoleViewList;
    }

    @Transactional
    public List<PartyRoleView> getPartyRoleViewRoleTypeId(String statusId, String organizationId, String roleTypeId){
        LOG.info("select by statusId and organizationId and roleTypeId order by partyName");

        List<PartyRoleView> partyRoleViewList = this.partyRoleViewMapper.selectByStatusIdAndOrganizationIdAndRoleTypeIdOrderByPartyName(statusId, organizationId, roleTypeId);
        LOG.info("size = {}", partyRoleViewList.size());
        return partyRoleViewList;
    }

    @Transactional
    public List<PartyRoleView> getPartyRoleViewByRoleTypeId(String roleTypeId){
        LOG.info("getPartyRoleViewByRoleTypeId");

        List<PartyRoleView> partyRoleViewList = this.partyRoleViewMapper.selectByRoleTypeId(roleTypeId);
        LOG.info("size = {}", partyRoleViewList.size());
        return partyRoleViewList;
    }
}
