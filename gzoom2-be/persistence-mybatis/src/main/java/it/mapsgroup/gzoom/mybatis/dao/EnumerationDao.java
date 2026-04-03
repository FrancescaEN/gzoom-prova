package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.Enumeration;
import it.mapsgroup.gzoom.mybatis.mapper.EnumerationMapper;
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
public class EnumerationDao extends AbstractDao{


    private static final Logger LOG = getLogger(EnumerationDao.class);
    private final EnumerationMapper enumerationMapper;

    @Autowired
    public EnumerationDao(EnumerationMapper enumerationMapper) {
        this.enumerationMapper = enumerationMapper;
    }

    @Transactional
    public List<Enumeration> getEnumerations(String enumTypeId) {
        LOG.info("find all enumeration by enumId order by sequenceId");

        List<Enumeration> enumerationList = this.enumerationMapper.selectByEnumTypeIdOrderBySequenceId(enumTypeId);
        LOG.info("size = {}", enumerationList.size());
        return enumerationList;
    }

    @Transactional
    public List<Enumeration> getEnumerationFilter(String enumTypeId, String field, String value, boolean secondaryLanguage) {
        LOG.info("getEnumerationFilter");

        System.out.println();
        List<Enumeration> enumerationList = this.enumerationMapper.getEnumerationFilter(enumTypeId, field, value, secondaryLanguage);
        LOG.info("size = {}", enumerationList.size());
        return enumerationList;
    }
}
