package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.mybatis.dao.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PersonService {
    private final PersonDao personDao;

    @Autowired
    public PersonService(PersonDao personDao) {
        this.personDao = personDao;
    }

    public int anonymizePerson(Instant expirationDate) {
        return this.personDao.anonymizePerson(expirationDate);
    }
}
