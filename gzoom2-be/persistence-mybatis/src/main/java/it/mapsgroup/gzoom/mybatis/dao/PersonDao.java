package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.Person;
import it.mapsgroup.gzoom.mybatis.mapper.PersonMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PersonDao extends AbstractDao {
    private static final Logger LOG = getLogger(PersonDao.class);
    private final PersonMapper personMapper;

    @Autowired
    public PersonDao(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    @Transactional
    public List<Person> getPersons() {
        LOG.info("getPersons");

        List<Person> personList = this.personMapper.selectAll();
        LOG.info("size = {}", personList.size());
        return personList;
    }

    @Transactional
    public int anonymizePerson(Instant expirationDate) {
        LOG.info("anonymizePerson update");
        int result = this.personMapper.anonymizePerson(expirationDate);
        LOG.info("result = {}", result);
        return result;
    }

}
