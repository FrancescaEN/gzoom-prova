package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.EmailValidatorService;
import it.mapsgroup.gzoom.mybatis.dao.ContactMechDao;
import it.mapsgroup.gzoom.mybatis.dto.ContactMech;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class ContactMechService {
    private final ContactMechDao contactMechDao;
    private final EmailValidatorService emailValidator;

    @Autowired
    public ContactMechService(ContactMechDao contactMechDao, EmailValidatorService emailValidator) {
        this.contactMechDao = contactMechDao;
        this.emailValidator = emailValidator;
    }

    public int anonymizeContactMech(Instant expirationDate) {
        return this.contactMechDao.anonymizeContactMech(expirationDate);
    }

    public ContactMech getInfoString() {
        return this.contactMechDao.getInfoString(principal().getUserLoginId());
    }

}
