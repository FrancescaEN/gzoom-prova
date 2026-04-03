package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.PortalPageDao;
import it.mapsgroup.gzoom.mybatis.dto.PortalPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortalPageService {
    private final PortalPageDao portalPageDao;

    @Autowired
    public PortalPageService(PortalPageDao portalPageDao) {
        this.portalPageDao = portalPageDao;
    }

    public Result<PortalPage> findByParentPortalPageId( String parentPortalPage) {
        List<PortalPage> list = this.portalPageDao.findByParentPortalPageId(parentPortalPage);
        return new Result<>(list, list.size());
    }

}
