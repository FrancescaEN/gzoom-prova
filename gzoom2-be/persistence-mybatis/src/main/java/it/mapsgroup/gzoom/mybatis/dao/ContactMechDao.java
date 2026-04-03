package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.ContactMech;
import it.mapsgroup.gzoom.mybatis.dto.ContactMechEx;
import it.mapsgroup.gzoom.mybatis.mapper.ContactMechMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ContactMechDao  extends AbstractDao {
    private static final Logger LOG = getLogger(ContactMechDao.class);
    private final ContactMechMapper contactMechMapper;

    @Autowired
    public ContactMechDao(ContactMechMapper contactMechMapper) {
        this.contactMechMapper = contactMechMapper;
    }

    @Transactional
    public int anonymizeContactMech(Instant expirationDate) {
        LOG.info("anonymizeContactMech update");
        int result = this.contactMechMapper.anonymizeContactMech(expirationDate);
        LOG.info("result = {}", result);
        return result;
    }

    @Transactional
    public ContactMechEx getEmailInfoString(String username) {
        LOG.info("getEmailInfoString get");
        List<ContactMechEx> result = this.contactMechMapper.getEmailInfoString(username);
        LOG.info("size = {}", result.size());
        return result.get(0);
    }

    @Transactional
    public ContactMech getInfoString(String username) {
        LOG.info("getInfoString get");
        List<ContactMech> result = this.contactMechMapper.getInfoString(username);
        LOG.info("size = {}", result.size());
        return result.get(0);
    }
}
