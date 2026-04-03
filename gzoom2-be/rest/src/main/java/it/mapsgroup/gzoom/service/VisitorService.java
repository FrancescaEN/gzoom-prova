package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.mybatis.dao.VisitorDao;
import it.mapsgroup.gzoom.mybatis.dto.Visitor;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class VisitorService {
	private static final Logger LOG = getLogger(VisitorService.class);

	private final PermissionService permissionService;

    private final VisitorDao visitorDao;

	@Autowired
    public VisitorService( PermissionService permissionService, VisitorDao visitorDao) {
        this.permissionService = permissionService;
        this.visitorDao = visitorDao;
    }

    public int deleteVisitor(String id) {
        Messages msg = new Messages();
        Visitor record = visitorDao.getVisitorById(id);
        Validators.assertNotBlank(id, msg.getMessageColumn(Messages.VISITOR, Messages.VISITOR_ID, Messages.IS_REQUIRED));

        return visitorDao.delete(record.getVisitorId());
    }

    public int deleteOld(Instant minusYears) {
        return visitorDao.deleteOld(minusYears);
    }

    
}
