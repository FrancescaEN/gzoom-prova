package it.mapsgroup.gzoom.service.reminder;

import static org.slf4j.LoggerFactory.getLogger;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.mapsgroup.gzoom.mybatis.dao.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mapsgroup.gzoom.model.Report;
import it.mapsgroup.gzoom.mybatis.dto.Reminder;
import it.mapsgroup.gzoom.quartz.oldScheduler.ProbeSchedulerService;
import it.mapsgroup.gzoom.mybatis.dto.PartyContactMech;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeContentExt;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import it.mapsgroup.gzoom.report.service.ReportCallbackType;
import it.mapsgroup.gzoom.service.report.ReportAddService;

@Service
public class ReminderService {
	private static final Logger LOG = getLogger(ReminderService.class);
	
	private final ReminderDao reminderDao;
	private final ReportAddService reportAddService;
	private final WorkEffortTypeDao workEffortTypeDao;
	private final PartyContactMechDao partyContactMechDao;
	private final ProbeSchedulerService probeSchedulerService;

	
	@Autowired
	public ReminderService (ReminderDao reminderDao, ReportAddService reportAddService, CommEventContentAssocDao commEventContentAssocDao,
			CommunicationEventDao communicationEventDao, WorkEffortTypeDao workEffortTypeDao, PartyContactMechDao partyContactMechDao, ProbeSchedulerService probeSchedulerService) {
		this.reminderDao = reminderDao;
		this.reportAddService = reportAddService;
		this.workEffortTypeDao = workEffortTypeDao;
		this.partyContactMechDao = partyContactMechDao;
		this.probeSchedulerService = probeSchedulerService;
	}	
	
	/**     
     * servizio di invio mail per il sollecito
     * @param report
     * @return
     */
    public String sendMail(Report report) {    
    	String selectName = ReminderReportContentIdEnum.getQuery(report.getReportContentId());
    	try {
			reminder(reportAddService.getReportParameters(report), selectName);
		} catch (Exception e) {
			LOG.error("Exception "+ e);
		} 
    	return "";
    }
    
    
    /**     
     * servizio di invio mail per il sollecito
     * @param
     * @return
     */
    public String reminderPeriodoScheduled() { 
    	LOG.info("*** start reminderPeriodoScheduled");
    	return reminderScheduled("REMINDER_PERIODO");
    }
    
    
    /**     
     * servizio di invio mail per il sollecito
     * @param
     * @return
     */
    public String reminderWorkEffortExpiry() { 
    	LOG.info("*** start reminderWorkEffortExpiry");
    	return reminderScheduled("REMINDER_SCADENZA");
    }
    
    
    private String reminderScheduled(String name) {
    	List<WorkEffortTypeContentExt> list = workEffortTypeDao.getWorkEffortTypeReminderActive(name);
    	LOG.info("*** start reminderScheduled list=" + list.size());
    	
    	//TODO aggiungere parametri che mancano
    	HashMap<String, Object> param = new HashMap<>();
    	param.put("reportContentId", name);
    	param.put("userLoginId", "admin");
    	param.put("outputFormat", "pdf");
    	param.put("langLocale", "");
    	param.put("defaultOrganizationPartyId", "Company");
    	
    	list.forEach(wt -> {    		
    		LOG.info("Scheduled reminder "+wt.getWorkEffortTypeId());
    		param.put("workEffortTypeId", wt.getWorkEffortTypeId());
    		param.put("birtOutputFileName",  wt.getContent().getContentName());
    		param.put("localDispatcherName",  ContextPermissionPrefixEnum.getPermissionPrefix(wt.getWorkEffortType().getParentTypeId()));    		
    		try {
				reminder(param, ReminderReportContentIdEnum.getQuery(name)); 
			} catch (Exception e) {
				LOG.error("Exception "+ e);
			}
    	});
    	
    	return "";
    }
	
	@SuppressWarnings("unchecked")
	public void reminder (HashMap<String, Object> reportParameters, String selectName) throws Exception {
		LOG.info("start reminder");
		
		String parentTypeId = (String)reportParameters.get("localDispatcherName"); 
		String contentName = (String)reportParameters.get("birtOutputFileName");  
		String workEffortTypeId = (String)reportParameters.get("workEffortTypeId");
		String userLoginId = (String)reportParameters.get("userLoginId");
		
		if (selectName.isEmpty()) {
			LOG.error("query null");
            throw new Exception("query null");
		}
		
		List<PartyContactMech> contactMechIdFromList = getContactMechIdFrom(workEffortTypeId);
		LOG.info("reminder contactMechIdFrom="+contactMechIdFromList.size());
		
		if (contactMechIdFromList.size() <= 0) {
			LOG.error("list email address is null");
            throw new Exception("list email address is null");
		}
		
		Date monitoringDate = (Date)reportParameters.get("monitoringDate") == null ? Calendar.getInstance().getTime() :  (Date)reportParameters.get("monitoringDate");
		reportParameters.put("monitoringDate", monitoringDate);

		Method setNameMethod = ReminderDao.class.getMethod(selectName, String.class, String.class, String.class, Date.class);
		List<Reminder> list = (List<Reminder>)setNameMethod.invoke(reminderDao, userLoginId, parentTypeId, (String)reportParameters.get("workEffortTypeId"), monitoringDate);
		LOG.info("send mail service= : " +selectName+ " list=" + list.size() +" whit workEffortTypeId="+(String)reportParameters.get("workEffortTypeId") + " monitoringDate="+monitoringDate);
		
		HashMap<String, Object> appMap = (HashMap<String, Object>) reportParameters.clone();
    	list.forEach(reminder -> {
    		LOG.info("reminder element : " + reminder.toString());    		
    		
    		appMap.put("partyId", reminder.getPartyId());
    		LOG.info("generated report  appMap= "+ appMap.toString());
    		//TODO - Aggiornare il secondo contentName con il resourceName
    		String activityId = reportAddService.add(reportParameters, contentName, contentName, userLoginId);
    		LOG.info("reminder activityId= "+ activityId);    		
    		
    		Map<String, Object> ele = new HashMap<>();
    		ele.put("contactMechIdFrom", contactMechIdFromList.get(0).getContactMechId());
    		ele.put("contactMechIdTo", reminder.getContactMechIdTo());
    		ele.put("partyId", reminder.getPartyId());
    		ele.put("partyIdFrom", contactMechIdFromList.get(0).getPartyId());	
    		ele.put("content", reminder.getContent());
    		ele.put("subject", reminder.getSubject());
    		ele.put("userLoginId", userLoginId);
    		probeSchedulerService.scheduleReportProbe(activityId, ReportCallbackType.REMINDER, ele);
    		LOG.info("reminder send mail ele= "+ ele);
    		
    	});
    	
    	if (list.size() > 0) {
    		//invio mail di riepilogo
    		//invio la mail a chi ha lanciato l'invio massivo oppure alla mail di default
			//TODO - Aggiornare il secondo contentName con il resourceName
    		String activityId = reportAddService.add(reportParameters, contentName, contentName, userLoginId);
    		LOG.info("reminder adminisrtator activityId= "+ activityId);
    		contactMechIdFromList.forEach(contactMech -> {
    			LOG.info("reminder adminisrtator contactMech="+ contactMech.getContactMechId() + " party=" + contactMech.getPartyId());
    			probeSchedulerService.scheduleReportProbe(activityId, ReportCallbackType.REMINDER, getReportParametersAdmin((String)reportParameters.get("workEffortTypeId"), contactMech.getContactMechId(), contactMech.getPartyId(), activityId, userLoginId));
    		});
    		  	
    	}
	}
	
	private Map<String, Object> getReportParametersAdmin(String workEffortTypeId, String contactMechIdFrom, String partyIdFrom, String activityId, String userLoginId) {
		Map<String, Object> ele = new HashMap<>();
		ele.put("contactMechIdTo", contactMechIdFrom);		
		ele.put("contactMechIdFrom", contactMechIdFrom);
		ele.put("partyId", partyIdFrom);	
		ele.put("partyIdFrom", partyIdFrom);	
		
		WorkEffortType wt = workEffortTypeDao.selectByPrimaryKey(workEffortTypeId);
		ele.put("subject", wt.getEtch());
		ele.put("content", wt.getNote());
		ele.put("userLoginId", userLoginId);	
		LOG.info("*** ReminderService getReportParametersAdmin: "+ ele);
		return ele;		
	}
		
	
	/**
	 * Resistuisco la lista dei party dove inviare il riepilogo mail
	 * @return
	 */
	private List<PartyContactMech> getContactMechIdFrom(String workEffortTypeId) {
		return partyContactMechDao.getContactMechWorkEffortTypeRole(workEffortTypeId);
	}

}
