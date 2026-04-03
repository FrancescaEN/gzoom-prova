package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.mapper.MaterializedViewMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class MaterializedViewDao {
    private static final Logger LOG = getLogger(MaterializedViewDao.class);
    private final MaterializedViewMapper materializedViewMapper;

    @Autowired
    public MaterializedViewDao(MaterializedViewMapper materializedViewMapper) {
        this.materializedViewMapper = materializedViewMapper;
    }

    @Transactional
    public void refreshMaterialView() {
        LOG.info("Refresh all Materialized View");
        this.materializedViewMapper.refreshMaterialView();
    }

}
