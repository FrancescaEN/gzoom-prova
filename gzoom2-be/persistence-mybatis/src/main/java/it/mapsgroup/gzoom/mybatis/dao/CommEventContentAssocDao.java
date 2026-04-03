package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.CommEventContentAssoc;
import it.mapsgroup.gzoom.mybatis.mapper.CommEventContentAssocMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CommEventContentAssocDao extends AbstractDao{
    private static final Logger LOG = getLogger(CommEventContentAssocDao.class);
    private final CommEventContentAssocMapper commEventContentAssocMapper;

    @Autowired
    public CommEventContentAssocDao(CommEventContentAssocMapper commEventContentAssocMapper) {
        this.commEventContentAssocMapper = commEventContentAssocMapper;
    }

    /**
     * create element CommunicationEvent
     * @param record
     * @return
     */
    @Transactional
    public boolean create(CommEventContentAssoc record) {
        LOG.info("create roleType");
        setCreatedTimestamp(record);
        record.setFromDate(Instant.now());
        int result = this.commEventContentAssocMapper.insert(record);
        LOG.info("result = {}", result);
        return result > 0;
    }

}
