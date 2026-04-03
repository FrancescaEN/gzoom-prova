package it.mapsgroup.gzoom.service;


import it.mapsgroup.gzoom.mybatis.dao.MaterializedViewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Leonardo Minaudo
 */
@Service
public class MaterializedViewService {
    private final MaterializedViewDao materializedViewDao;

    @Autowired
    public MaterializedViewService(MaterializedViewDao materializedViewDao, Configuration config) {
        this.materializedViewDao = materializedViewDao;
    }

    public void refreshMaterialView() {
        this.materializedViewDao.refreshMaterialView();
    }

}
