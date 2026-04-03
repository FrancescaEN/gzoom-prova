package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import it.mapsgroup.gzoom.mybatis.mapper.QueryConfigMapper;
import it.mapsgroup.gzoom.mybatis.service.FilterService;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class QueryConfigDao extends AbstractDao {
    private static final Logger LOG = getLogger(QueryConfigDao.class);
    private final QueryConfigMapper queryConfigMapper;
    private final FilterService filterService;
    private final SequenceGenerator sequenceGenerator;

    @Autowired
    public QueryConfigDao(QueryConfigMapper queryConfigMapper, FilterService filterService, SequenceGenerator sequenceGenerator) {
        this.queryConfigMapper = queryConfigMapper;
        this.filterService = filterService;
        this.sequenceGenerator = sequenceGenerator;
    }


    @Transactional
    public QueryConfig getQueryConfig(String queryConfigId) {
        LOG.info("find queryConfig by id");

        QueryConfig queryConfig = this.queryConfigMapper.selectByPrimaryKey(queryConfigId);
        LOG.info("QueryConfig = {}", (queryConfig != null));
        return queryConfig;
    }

    @Transactional
    public List<QueryConfig> getAllQueryConfig(String parentTypeId, String queryType, String userLoginId) {
        LOG.info("find all queryConfig");


        Map<String, Object> filterPermission = null;

        if (parentTypeId!=null && !parentTypeId.equals("")) {
            String permission = ContextPermissionPrefixEnum.getPermissionPrefix(parentTypeId);

            // se ho uno dei permessi uso la lista filtrata di elementi
            filterPermission = filterService.setMapFilter(userLoginId, ContextPermissionPrefixEnum.valueOf(parentTypeId));
        }
        else parentTypeId = null;

        List<QueryConfig> queryConfigs = this.queryConfigMapper.selectByActiveTrueOrderByQueryCode(parentTypeId, queryType, filterPermission);
        LOG.info("size = {}", queryConfigs.size());
        return queryConfigs;
    }

    @Transactional
    public List<QueryConfig> selectAllOrderByQueryCode() {
        LOG.info("find all queryConfig order by queryCode");

        List<QueryConfig> queryConfigs = this.queryConfigMapper.selectAllOrderByQueryCode();
        LOG.info("size = {}", queryConfigs.size());
        return queryConfigs;
    }

    @Transactional
    public List<QueryConfig> selectByQueryTypeOrderByQueryCode(String queryType) {
        LOG.info("find queryConfig by queryType order by queryCode");

        List<QueryConfig> queryConfigs = this.queryConfigMapper.selectByQueryTypeOrderByQueryCode(queryType);
        LOG.info("size = {}", queryConfigs.size());
        return queryConfigs;
    }

    @Transactional
    public boolean update(QueryConfig queryConfig) {
        LOG.info("update queryConfig");
        setUpdateTimestamp(queryConfig);
        int result = this.queryConfigMapper.updateByPrimaryKey(queryConfig);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean updateInfoBase(QueryConfig queryConfig) {
        LOG.info("update queryConfig");
        setUpdateTimestamp(queryConfig);
        int result = this.queryConfigMapper.updateInfoBase(queryConfig);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean updateConditions(QueryConfig queryConfig) {
        LOG.info("update conditions queryConfig");
        setUpdateTimestamp(queryConfig);
        int result = this.queryConfigMapper.updateConditions(queryConfig);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String id) {
        LOG.info("delete queryConfig");
        int result = this.queryConfigMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public String create(QueryConfig queryConfig) {
        LOG.info("create queryConfig");
        setCreatedTimestamp(queryConfig);
        String newId = this.sequenceGenerator.getNextSeqId("QueryConfig");
        queryConfig.setQueryId(newId);
        int result = this.queryConfigMapper.insert(queryConfig);
        LOG.info("result = {}", result);
        return (result > 0)? newId : null;
    }
}
