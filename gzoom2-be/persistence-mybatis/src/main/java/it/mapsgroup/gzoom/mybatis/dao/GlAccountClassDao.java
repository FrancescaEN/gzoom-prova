package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountClass;
import it.mapsgroup.gzoom.mybatis.dto.GlResourceType;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountClassMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlAccountClassDao extends AbstractDao{
    private static final Logger LOG = getLogger(GlAccountClassDao.class);
    private final GlAccountClassMapper glAccountClassMapper;

    @Autowired
    public GlAccountClassDao(GlAccountClassMapper glAccountClassMapper) {
        this.glAccountClassMapper = glAccountClassMapper;
    }

    @Transactional
    public List<GlAccountClass> getByAccountTypeEnumId(String accountTypeEnumId) {
        LOG.info("find glAccountClass by accountTypeEnumId");
        List<GlAccountClass> glAccountClassList = this.glAccountClassMapper.selectByAccountTypeEnumId(accountTypeEnumId);
        LOG.info("size = {}", glAccountClassList.size());
        return glAccountClassList;
    }
}
