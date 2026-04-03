package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountInterfaceDao;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountInterfaceExt;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlAccountInterfaceService {
    private static final Logger LOG = getLogger(GlAccountInterfaceService.class);
    private final GlAccountInterfaceDao glAccountInterfaceDao;

    @Autowired
    public GlAccountInterfaceService(GlAccountInterfaceDao glAccountInterfaceDao){this.glAccountInterfaceDao = glAccountInterfaceDao;}

    public String createExt(GlAccountInterfaceExt req) {
       Validators.assertNotNull(req, Messages.NOT_NULL);
       glAccountInterfaceDao.createEx(req, principal().getUserLoginId());
       return "record "+req.getId()+" created successfully";
    }
}
