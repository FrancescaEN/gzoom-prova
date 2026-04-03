package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.QrtzJobDetails;
import it.mapsgroup.gzoom.mybatis.mapper.QrtzJobDetailsMapper;
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
public class QrtzJobDetailsDao {
    private static final Logger LOG = getLogger(QrtzJobDetailsDao.class);
    private final QrtzJobDetailsMapper qrtzJobDetailsMapper;

    @Autowired
    public QrtzJobDetailsDao(QrtzJobDetailsMapper qrtzJobDetailsMapper) {
        this.qrtzJobDetailsMapper = qrtzJobDetailsMapper;
    }

    @Transactional
    public List<QrtzJobDetails> selectBySchedName(String schedName) {
        LOG.info("find qrtzJobDetails by schedName");
        List<QrtzJobDetails> jobDetails = this.qrtzJobDetailsMapper.selectBySchedName(schedName);
        LOG.info("size = {}", jobDetails.size());
        return jobDetails;
    }

    @Transactional
    public List<QrtzJobDetails> selectByClassName(String className) {
        LOG.info("find qrtzJobDetails by className");
        List<QrtzJobDetails> jobDetails = this.qrtzJobDetailsMapper.selectByClassName(className);
        LOG.info("size = {}", jobDetails.size());
        return jobDetails;
    }


    @Transactional
    public boolean updateDesc(String jobName, String desc) {
        LOG.info("update qrtzJobDetails description");
        int result = this.qrtzJobDetailsMapper.updateDescription(jobName, desc);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
