package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountClassDao;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountClass;
import it.mapsgroup.gzoom.mybatis.dto.GlResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlAccountClassService {
    private final GlAccountClassDao glAccountClassDao;

    @Autowired
    public GlAccountClassService(GlAccountClassDao glAccountClassDao) {
        this.glAccountClassDao = glAccountClassDao;
    }
    public Result<GlAccountClass> getByAccountTypeEnumId(String accountTypeEnumId) {
        List<GlAccountClass> list = this.glAccountClassDao.getByAccountTypeEnumId(accountTypeEnumId);
        return new Result<>(list, list.size());
    }

}
