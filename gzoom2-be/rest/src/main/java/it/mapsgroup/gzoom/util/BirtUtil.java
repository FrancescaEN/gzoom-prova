package it.mapsgroup.gzoom.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import it.mapsgroup.gzoom.mybatis.dao.ReportDao;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortAnalysisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysis;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssoc;

@Service
public class BirtUtil {
	
	private final ReportDao reportDao;
	private final WorkEffortAnalysisDao workEffortAnalysisDao;
	
	
	public final static String pattern_date="dd/MM/yyyy";
	public final static String from_date_default="01/01/1900";
	public final static String thru_date_default="31/12/2100";
	
	
	@Autowired
    public BirtUtil(ReportDao reportDao, WorkEffortAnalysisDao workEffortAnalysisDao) {
        this.reportDao = reportDao;
        this.workEffortAnalysisDao = workEffortAnalysisDao;
    }
	
	public void checkChildRootEquality(Map<String, Object> context) {
		String workEffortId = (String)context.get("workEffortId");
		String workEffortIdChild = (String)context.get("workEffortIdChild");
		if (workEffortIdChild != null) {
			if (workEffortId != null) {
				if (!workEffortIdChild.equals(workEffortId)) {
					context.put("workEffortId", "");
				} else {						
					List<WorkEffortAssoc> assocList = reportDao.getChildRootEquality(workEffortId);
					if (!assocList.isEmpty() && assocList.size() == 1) {						
						context.put("workEffortIdChild", "");
					} else {
						context.put("workEffortId", "");
					}
				}
			} 
		} else if (workEffortId != null) {
			List<WorkEffortAssoc> assocList = reportDao.getChildRootEquality(workEffortId);
			if (!assocList.isEmpty() && assocList.size() > 1) {
				context.put("workEffortId", "");
				context.put("workEffortIdChild", workEffortId);
				
			}			
		}
		
	}

	/**
	 * verifica workEffortId e workEffortIdChild senza settare i campi orgUnit
	 * @param context
	 */
	public void checkChildRootEqualityWithoutOrgUnit(Map<String, Object> context) {
		String orgUnitId = (String) context.get("orgUnitId");
		String orgUnitRoleTypeId = (String) context.get("orgUnitRoleTypeId");
		checkChildRootEquality(context);
		if (orgUnitId != null) {
			context.remove("orgUnitId");
		}
		if (orgUnitRoleTypeId != null) {
			context.remove("orgUnitRoleTypeId");
		}
	}


	/**
	 * converte il workEffortIdParametric in una stringa
	 * @param context
	 */
	public void convertWorkEffortIdParametricToString(Map<String, Object> context) {
		/** Gestisto il reperimento dei parametri con un try catch in quanto se dal front end non è selezionata nessuna voce,
		 il cast implicito va in errore perchè gli arriva al beckend una stringa vuota e non un array */
		ArrayList workEffortIdParametric = new ArrayList();
		try {workEffortIdParametric = (ArrayList) context.get("workEffortIdParametric"); } catch (Exception e) {}
		ArrayList workEffortIdParametric2 = new ArrayList();
		try {workEffortIdParametric2 = (ArrayList) context.get("workEffortIdParametric2"); } catch (Exception e) {}
		ArrayList workEffortIdParametric3 = new ArrayList();
		try {workEffortIdParametric3 = (ArrayList) context.get("workEffortIdParametric3"); } catch (Exception e) {}
		String workEffortConverted = "";

		if (workEffortIdParametric != null && workEffortIdParametric.size()>0) {
			workEffortConverted = "'"+String.join("','", workEffortIdParametric)+"'";
		}
		if(workEffortIdParametric2 != null && workEffortIdParametric2.size()>0) {
			workEffortConverted += !workEffortConverted.equals("")
									? "," + "'"+String.join("','", workEffortIdParametric2)+"'"
									:"'"+String.join("','", workEffortIdParametric2)+"'";
		}
		if(workEffortIdParametric3 != null && workEffortIdParametric3.size()>0) {
			workEffortConverted += !workEffortConverted.equals("")
									? "," + "'"+String.join("','", workEffortIdParametric3)+"'"
									:"'"+String.join("','", workEffortIdParametric3)+"'";
		}

		/** Rimuovo tutti i parametri e passo alla stampa una stringa con i valori separati da , */
		context.remove("workEffortIdParametric");
		context.remove("workEffortIdParametric2");
		context.remove("workEffortIdParametric3");
		context.put("workEffortIdParametric",workEffortConverted);

	}
	
	public void cleanRootValue(Map<String, Object> context) {
		String workEffortId = (String)context.get("workEffortId");
		String workEffortIdChild = (String)context.get("workEffortIdChild");

		if (workEffortId != null && workEffortIdChild != null) {
			context.put("workEffortId", "");
		}
	}
	/**
	 * 
	 * @param context
	 * @throws ParseException
	 */
	public void setMonitoringDate(Map<String, Object> context) throws ParseException {
		if (context.get("monitoringDate") == null) {
			context.put("monitoringDate", Calendar.getInstance().getTime());
		}		
		
	}	

	/**
	 * utilizzato per il problema delle stampe null su birt, setta la data al 01/01/1900 se gli arriva un valore null 
	 * @param context
	 * @throws ParseException
	 */
	public void setDefaultFromDate(Map<String, Object> context) throws ParseException {
		
		if (context.get("fromDate") == null) {
			DateFormat dateFormat = new SimpleDateFormat(pattern_date);
			Date date = dateFormat.parse(from_date_default);
			long time = date.getTime();
			Timestamp fromDateTimestamp= new Timestamp(time);
			context.put("fromDate", fromDateTimestamp);
		}		
		
	}
	
	
	/**
	 * utilizzato per il problema delle stampe null su birt, setta la data al 31/12/2100 se gli arriva un valore null 
	 * @param context
	 * @throws ParseException
	 */
	public void setDefaultThruDate(Map<String, Object> context) throws ParseException {
		
		if (context.get("thruDate") == null) {
			DateFormat dateFormat = new SimpleDateFormat(pattern_date);
			Date date = dateFormat.parse(thru_date_default);
			long time = date.getTime();
			Timestamp fromDateTimestamp= new Timestamp(time);
			context.put("thruDate", fromDateTimestamp);
		}		
		
	}
	
	/**
     * Nelle stampe analisi andiamo a settare la referenceDate: parametro obbligatorio. 
     * @param context
     * @throws ParseException
     */
	public void setReferenceDate(Map<String, Object> context) {
        if (context.get("monitoringDate") == null) {
            String workEffortAnalysisId = (String)context.get("workEffortAnalysisId");
            if (workEffortAnalysisId != null) {
                WorkEffortAnalysis workEffortAnalysis = workEffortAnalysisDao.getWorkEffortAnalysis(workEffortAnalysisId);
                context.put("monitoringDate", workEffortAnalysis.getReferenceDate());
            }
        }   
                        
    }
}
