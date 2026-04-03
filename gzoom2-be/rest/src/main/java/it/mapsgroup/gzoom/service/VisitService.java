package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.mybatis.dao.VisitDao;
import it.mapsgroup.gzoom.mybatis.dto.Visit;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class VisitService {
	private static final Logger LOG = getLogger(VisitService.class);

	private final PermissionService permissionService;

    private final VisitDao visitDao;

	@Autowired
    public VisitService(PermissionService permissionService, VisitDao visitDao) {
        this.permissionService = permissionService;
        this.visitDao = visitDao;
    }

    public int deleteVisitor(String id) {
        Messages msg = new Messages();
        Visit record = visitDao.getVisitById(id);
        Validators.assertNotBlank(id, msg.getMessageColumn(Messages.VISIT, Messages.VISIT_ID, Messages.IS_REQUIRED));

        return visitDao.delete(record.getVisitId());
    }

    public int deleteOld(Instant minusYears) {
        return visitDao.deleteOld(minusYears);
    }


    
}
