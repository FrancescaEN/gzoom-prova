package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountInterfaceExt;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountInterfaceExtMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlAccountInterfaceDao extends AbstractDao {
    private static final Logger LOG = getLogger(GlAccountInterfaceDao.class);
    private final GlAccountInterfaceExtMapper glAccountInterfaceExtMapper;
    private final SequenceGenerator sequenceGenerator;

    @Autowired
    public GlAccountInterfaceDao(GlAccountInterfaceExtMapper glAccountInterfaceExtMapper, SequenceGenerator sequenceGenerator) {
        this.glAccountInterfaceExtMapper = glAccountInterfaceExtMapper;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Transactional
    public boolean createEx(GlAccountInterfaceExt record, String userLoginId) {
        LOG.info("create glAccountInterfaceExt");
        String id = sequenceGenerator.getNextSeqId("GlAccountInterfaceExt");
        record.setId(id);
        int result = this.glAccountInterfaceExtMapper.insert(record);
        LOG.info("created records: {}", result);
        return result > 0;
    }

}
