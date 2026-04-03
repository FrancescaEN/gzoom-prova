package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeContent;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeExt;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortTypeContentMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortTypeContentDao extends AbstractDao {
    private static final Logger LOG = getLogger(WorkEffortTypeContentDao.class);
    private final WorkEffortTypeContentMapper workEffortTypeContentMapper;

    @Autowired
    public WorkEffortTypeContentDao(WorkEffortTypeContentMapper workEffortTypeContentMapper) {
        this.workEffortTypeContentMapper = workEffortTypeContentMapper;
    }

    public List<WorkEffortTypeExt> getWorkEffortTypeContents(String parentTypeId, String reportContentId, String reportName, String workEffortTypeId) {
        LOG.info("getWorkEffortTypeContents");

        List<WorkEffortTypeExt> ret = this.workEffortTypeContentMapper.getWorkEffortTypeContents(parentTypeId, reportContentId, workEffortTypeId);
        LOG.info("size {}", ret.size() );
        return ret;
    }

    @Transactional
    public WorkEffortTypeContent getWorkEffortTypeContent(String workEffortTypeId, String contentId) {
        LOG.info("getWorkEffortTypeContent");

        WorkEffortTypeContent workEffortTypeContent = this.workEffortTypeContentMapper.selectByPrimaryKey(workEffortTypeId, contentId);
        LOG.info("WorkEffortTypeContent {}", (workEffortTypeContent != null) );
        return workEffortTypeContent;
    }

    @Transactional
    public Map<String, String> getWorkEffortTypeContentParams(String workEffortTypeId, String contentId) {
        WorkEffortTypeContent workEffortTypeContent = getWorkEffortTypeContent(workEffortTypeId, contentId);
        if (workEffortTypeContent != null && workEffortTypeContent.getParams() != null && workEffortTypeContent.getParams().length() > 0) {
            return Arrays.asList(workEffortTypeContent.getParams().trim().split(";")).stream().map(s -> s.split("=")).collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].replaceAll("\"", "")));
        }
        return new HashMap<String, String>();
    }
}
