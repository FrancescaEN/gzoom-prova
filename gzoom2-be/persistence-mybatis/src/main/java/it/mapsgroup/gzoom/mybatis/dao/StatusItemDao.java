package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.StatusItem;
import it.mapsgroup.gzoom.mybatis.dto.StatusItemExType;
import it.mapsgroup.gzoom.mybatis.mapper.StatusItemMapper;
import it.mapsgroup.gzoom.mybatis.dto.StatusItemExt;
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
public class StatusItemDao extends AbstractDao {
    private static final Logger LOG = getLogger(StatusItemDao.class);
    private final StatusItemMapper statusItemMapper;

    @Autowired
    public StatusItemDao(StatusItemMapper statusItemMapper) {
        this.statusItemMapper = statusItemMapper;
    }

    @Transactional
    public List<StatusItemExt> getStatusItems(String userLoginId) {
        LOG.info("find all statusItem");

        List<StatusItemExt> statusItems = this.statusItemMapper.getStatusItems(userLoginId);
        LOG.info("size = {}", statusItems.size());
        return statusItems;
    }

    @Transactional
    public List<StatusItem> getTimesheetStatusDropdownFilter(String parentTypeId) {
        LOG.info("find statusItem for Timesheet");

        List<StatusItem> statusItems = this.statusItemMapper.getTimesheetStatusDropdownFilter(parentTypeId);
        LOG.info("size = {}", statusItems.size());
        return statusItems;
    }

    @Transactional
    public StatusItem getStatusItem(String statusItemId) {
        LOG.info("find statusItem by id");

        StatusItem statusItem = this.statusItemMapper.selectByPrimaryKey(statusItemId);
        LOG.info("StatusItem = {}", (statusItem != null));
        return statusItem;
    }

    @Transactional
    public List<StatusItemExType> getStatusItemStateTo() {
        LOG.info("getStatusItemStateTo");

        List<StatusItemExType> list = this.statusItemMapper.getStatusItemStateTo();
        LOG.info("size = {}", list.size());
        return list;
    }


    @Transactional
    public List<StatusItem> findByStatusTypeId(String statusTypeId) {
        LOG.info("find statusItem by statusTypeId");

        List<StatusItem> statusItems = this.statusItemMapper.findByStatusTypeId(statusTypeId);
        LOG.info("size = {}", statusItems.size());
        return statusItems;
    }

    @Transactional
    public boolean create(StatusItem statusItem, String userLoginId) {
        LOG.info("create statusItem");
        setCreatedTimestamp(statusItem);
        statusItem.setCreatedByUserLogin(userLoginId);
        int result = this.statusItemMapper.insert(statusItem);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean update(StatusItem statusItem, String userLoginId) {
        LOG.info("update statusItem");
        setUpdateTimestamp(statusItem);
        statusItem.setLastModifiedByUserLogin(userLoginId);
        int result = this.statusItemMapper.updateByPrimaryKey(statusItem);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String statusId) {
        LOG.info("delete statusItem by statusId");
        int result = this.statusItemMapper.deleteByPrimaryKey(statusId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteByStatusTypeId(String statusItemId) {
        LOG.info("delete statusItem by statusTypeId");
        int result = this.statusItemMapper.deleteByStatusTypeId(statusItemId);
        LOG.info("result = {}", result);
        return result > 0;
    }


    public List<StatusItemExType> getStatusItemStateFrom(String statusTypeId) {
        LOG.info("getStatusItemStateFrom");

        List<StatusItemExType> list = this.statusItemMapper.getStatusItemStateFrom(statusTypeId);
        LOG.info("size = {}", list.size());
        return list;
    }

    @Transactional
    public List<StatusItem> getStatusItemByCode(String statusCode) {
        LOG.info("getStatusItemByCode");
        List<StatusItem> list = this.statusItemMapper.getStatusItemByCode(statusCode);
        LOG.info("size = {}", list.size());
        return list;
    }
}
