package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.QrtzTriggers;
import it.mapsgroup.gzoom.mybatis.mapper.QrtzTriggersMapper;
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
public class QrtzTriggersDao {
    private static final Logger LOG = getLogger(QrtzTriggersDao.class);
    private final QrtzTriggersMapper qrtzTriggersMapper;

    @Autowired
    public QrtzTriggersDao(QrtzTriggersMapper qrtzTriggersMapper) {
        this.qrtzTriggersMapper = qrtzTriggersMapper;
    }

    @Transactional
    public List<QrtzTriggers> selectAll() {
        LOG.info("find all qrtzTriggers");
        List<QrtzTriggers> jobDetails = this.qrtzTriggersMapper.selectAll();
        LOG.info("size = {}", jobDetails.size());
        return jobDetails;
    }

    @Transactional
    public List<QrtzTriggers> selectByJobName(String jobName) {
        LOG.info("find qrtzTriggers by jobName");
        List<QrtzTriggers> jobDetails = this.qrtzTriggersMapper.selectByJobName(jobName);
        LOG.info("size = {}", jobDetails.size());
        return jobDetails;
    }
}
