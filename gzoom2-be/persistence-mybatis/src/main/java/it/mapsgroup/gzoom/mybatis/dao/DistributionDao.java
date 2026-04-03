package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.Distribution;
import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import it.mapsgroup.gzoom.mybatis.mapper.DistributionMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class DistributionDao {
    private static final Logger LOG = getLogger(DistributionDao.class);
    private final DistributionMapper distributionMapper;

    @Autowired
    public DistributionDao(DistributionMapper distributionMapper) {
        this.distributionMapper = distributionMapper;
    }

    @Transactional
    public List<Distribution> findByQueryConfig(QueryConfig query) {

        String finalQuery;
        //GET Query
        finalQuery = query.getQueryInfo();
        //Replace Condition
        if(query.getCond0Info()!=null && !query.getCond0Info().equals(""))
            finalQuery = finalQuery.replaceAll("(?i)#COND0#",query.getCond0Info());

        if(query.getCond1Info()!=null && !query.getCond1Info().equals(""))
            finalQuery = finalQuery.replaceAll("(?i)#COND1#", query.getCond1Info());

        if(query.getCond2Info()!=null && !query.getCond2Info().equals(""))
            finalQuery = finalQuery.replaceAll("(?i)#COND2#", query.getCond2Info());

        if(query.getCond3Info()!=null && !query.getCond3Info().equals(""))
            finalQuery = finalQuery.replaceAll("(?i)#COND3#", query.getCond3Info());

        if(query.getCond4Info()!=null && !query.getCond4Info().equals(""))
            finalQuery = finalQuery.replaceAll("(?i)#COND4#", query.getCond4Info());

        if(query.getCond5Info()!=null && !query.getCond5Info().equals(""))
            finalQuery = finalQuery.replaceAll("(?i)#COND5#", query.getCond5Info());

        if(query.getCond6Info()!=null && !query.getCond6Info().equals(""))
            finalQuery = finalQuery.replaceAll("(?i)#COND6#", query.getCond6Info());

        if(query.getCond7Info()!=null && !query.getCond7Info().equals(""))
            finalQuery = finalQuery.replaceAll("(?i)#COND7#", query.getCond7Info());

        LOG.info("find by query");
        List<Distribution> distributions = this.distributionMapper.findByQuery(finalQuery);
        LOG.info("size = {}", distributions.size());
        return distributions;
    }
}
