package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortContent;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortContentEx;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortContentMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortContentDao extends AbstractDao {
    private static final Logger LOG = getLogger(WorkEffortContentDao.class);
    private final WorkEffortContentMapper workEffortContentMapper;

    @Autowired
    public WorkEffortContentDao(WorkEffortContentMapper workEffortContentMapper) {
        this.workEffortContentMapper = workEffortContentMapper;
    }

    @Transactional
    public List<WorkEffortContentEx> getWorkEffortContentExList() {
        LOG.info("getWorkEffortContentExList");

        List<WorkEffortContentEx> workEffortContents = this.workEffortContentMapper.getWorkEffortContentExList();
        LOG.info("size = {}", workEffortContents.size());
        return workEffortContents;
    }

    @Transactional
    public List<WorkEffortContentEx> getWorkEffortContentListExFilter(InfoPage infoPage) {
        if(infoPage.getFilter().length == 0){
            infoPage.setFilters(null);
        }
        return this.workEffortContentMapper.getWorkEffortContentExListFilter(infoPage.getFilter(), infoPage.getFilterGenericLabel(), infoPage.getOrganizationId(), infoPage.getMatchModeSearch(), infoPage.getSecondaryLang());
    }

    @Transactional
    public WorkEffortContentEx getWorkEffortContentEx(String dataResourceId) {
        LOG.info("getWorkEffortContentEx");

        WorkEffortContentEx workEffortContentEx = this.workEffortContentMapper.getWorkEffortContentEx(dataResourceId);
        LOG.info("WorkEffortContentEx = {}", (workEffortContentEx != null));
        return workEffortContentEx;
    }

    @Transactional
    public List<WorkEffortContent> getWorkEffortContentByWorkEffortId(String workEffortId){
        LOG.info("getWorkEffortContentByWorkEffortId");
        List<WorkEffortContent> workEffortContentList = this.workEffortContentMapper.getWorkEffortContentByWorkEffortId(workEffortId);
        LOG.info("result = {}", workEffortContentList);
        return workEffortContentList;
    }

    @Transactional
    public boolean create(WorkEffortContent workEffortContent, String userLoginId) {
        LOG.info("create workEffortContent");
        setCreatedTimestamp(workEffortContent);
        workEffortContent.setCreatedByUserLogin(userLoginId);
        int result = this.workEffortContentMapper.insert(workEffortContent);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean update(WorkEffortContent workEffortContent, String userLoginId) {
        LOG.info("update workEffortContent");
        setUpdateTimestamp(workEffortContent);
        workEffortContent.setLastModifiedByUserLogin(userLoginId);
        int result = this.workEffortContentMapper.updateByPrimaryKey(workEffortContent);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(WorkEffortContent workEffortContent) {
        LOG.info("delete workEffortContent");
        int result = this.workEffortContentMapper.deleteByPrimaryKey(workEffortContent.getWorkEffortId(), workEffortContent.getContentId(), workEffortContent.getWorkEffortContentTypeId(), workEffortContent.getFromDate());
        LOG.info("result = {}", result);
        return result > 0;
    }
}
