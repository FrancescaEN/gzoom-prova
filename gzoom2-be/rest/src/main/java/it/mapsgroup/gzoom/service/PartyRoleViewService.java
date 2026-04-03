package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.PartyRoleViewDao;
import it.mapsgroup.gzoom.mybatis.dto.PartyRoleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leonardo Minaudo
 */
@Service
public class PartyRoleViewService {
    private final PartyRoleViewDao partyRoleViewDao;

    @Autowired
    public PartyRoleViewService(PartyRoleViewDao partyRoleViewDao) {
        this.partyRoleViewDao = partyRoleViewDao;
    }

    public Result<PartyRoleView> getPartyRoleView(String statusId, String organizationId) {
        List<PartyRoleView> list = this.partyRoleViewDao.getPartyRoleView(statusId, organizationId);
        return new Result<>(list, list.size());
    }

    public Result<PartyRoleView> getPartyRoleViewRoleTypeId(String statusId, String organizationId, String roleTypeId) {
        List<PartyRoleView> list = this.partyRoleViewDao.getPartyRoleViewRoleTypeId(statusId, organizationId, roleTypeId);
        return new Result<>(list, list.size());
    }

    public Result<PartyRoleView> getPartyRoleViewByRoleTypeId(String roleTypeId) {
        List<PartyRoleView> list = this.partyRoleViewDao.getPartyRoleViewByRoleTypeId(roleTypeId);
        return new Result<>(list, list.size());
    }
}