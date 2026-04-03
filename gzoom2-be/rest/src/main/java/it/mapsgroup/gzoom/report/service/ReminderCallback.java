package it.mapsgroup.gzoom.report.service;

import it.mapsgroup.gzoom.mybatis.dao.CommEventContentAssocDao;
import it.mapsgroup.gzoom.mybatis.dao.CommunicationEventDao;
import it.mapsgroup.gzoom.mybatis.dao.ContentDao;
import it.mapsgroup.gzoom.mybatis.dto.CommEventContentAssoc;
import it.mapsgroup.gzoom.mybatis.dto.CommunicationEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Assuntina Magnante
 */
@Component
public class ReminderCallback extends ReportCallback {
    private static final Logger LOG = getLogger(ReminderCallback.class);

    private final CommunicationEventDao communicationEventDao;
    private final ContentDao contentDao;
    private final CommEventContentAssocDao commEventContentAssocDao;

    @Autowired
    public ReminderCallback(ReportCallbackManager callBackManager, CommunicationEventDao communicationEventDao,
    		CommEventContentAssocDao commEventContentAssocDao, ContentDao contentDao) {
        super(callBackManager);        
        this.communicationEventDao = communicationEventDao;
        this.commEventContentAssocDao = commEventContentAssocDao;
        this.contentDao = contentDao;
    }

    @Override
    public ReportCallbackType getType() {
        return ReportCallbackType.REMINDER;
    }

    @Override
    public void run(String reportActivityId, Map<String, Object> params) {
        LOG.info("ReminderCallback reportActivityId=" + reportActivityId + " params=", params.toString());
        createCommunicationEvent(reportActivityId, params);
        LOG.info("End ReminderCallback reportActivityId="+reportActivityId);

    }
    
    
    private void createCommunicationEvent(String reportActivityId, Map<String, Object> ele) {
		LOG.info("reminder create communication event");
		
		CommunicationEvent communicationEvent = new CommunicationEvent();		
		
		communicationEvent.setContactMechIdFrom((String)ele.get("contactMechIdFrom"));	
		communicationEvent.setContactMechIdTo((String)ele.get("contactMechIdTo"));
		communicationEvent.setPartyIdTo((String)ele.get("partyId"));
		communicationEvent.setPartyIdFrom((String)ele.get("partyIdFrom"));		
		communicationEvent.setSubject((String)ele.get("subject"));
		communicationEvent.setContent((String)ele.get("content"));
		communicationEvent.setStatusId("COM_IN_PROGRESS");
		communicationEvent.setContentMimeTypeId("text/plain");
		communicationEvent.setCommunicationEventTypeId("AUTO_EMAIL_COMM");
		
		String communicationEventId = communicationEventDao.create(communicationEvent);
		LOG.info("create communication event communicationEventId="+communicationEventId);
		
		String contentId = contentDao.getContentByActivityId(reportActivityId).getContentId();
		CommEventContentAssoc commEventContentAssoc = new CommEventContentAssoc();
		commEventContentAssoc.setContentId(contentId);
		commEventContentAssoc.setCommunicationEventId(communicationEventId);
		commEventContentAssocDao.create(commEventContentAssoc);		
		LOG.info("create commEventContentAssoc communicationEventId="+communicationEventId + " contentId="+contentId);
		
	}
}
