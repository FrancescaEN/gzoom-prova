package com.mapsengineering.workeffortext.scorecard;

import java.util.Map;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;

import com.mapsengineering.base.services.ServiceLogger;
import com.mapsengineering.base.util.JobLogger;

/**
 * Avanzamento % con soglie
 */
public class PercLimitsConverter extends ValueConverter {
	
    public static final String MODULE = PercLimitsConverter.class.getName();

    private JobLogger jLogger;
    
    /**
     * Constructor
     * @param delegator
     */
    public PercLimitsConverter(Delegator delegator) {
        super(delegator);
        jLogger = new JobLogger(MODULE);
    }

    @Override
	public JobLogger getJobLogger() {
		return jLogger;
	}

	@Override
	public double convert(Map<String, Object> currentKpi, String workEffortId, String accountName) throws Exception {
	    Double VC = getDoubleValue(currentKpi, E.actualValue.name());
	    Double VT = getDoubleValue(currentKpi, E.targetValue.name());
	    Double VE = getDoubleValue(currentKpi, E.limitExcellentValue.name());
	    Double VS = getDoubleValue(currentKpi, E.limitMaxValue.name());
	    Double VI = getDoubleValue(currentKpi, E.limitMinValue.name());
        // il debitCreditFlag puo' essere D o C, -> D = "Valori superiori",
	    // ma con le soglie il flag viene ignorato, se possibile.
	    // Debug.log(" VI " + VI + " VT " + VT + " VS " + VS + " VE " + VE + " VC " + VC + " debitCreditFlag " + currentKpi.get("debitCreditFlag"));
        
        boolean miglioraConValoriPiuAlti = false;
        boolean miglioraConValoriPiuBassi = false;

        // determinazione configurazione dei valori di soglia
        if ((UtilValidate.isNotEmpty(VI) && UtilValidate.isNotEmpty(VT) && VI < VT) || 
            (UtilValidate.isNotEmpty(VI) && UtilValidate.isNotEmpty(VS) && VI < VS) || 
            (UtilValidate.isNotEmpty(VI) && UtilValidate.isNotEmpty(VE) && VI < VE) || 
            (UtilValidate.isNotEmpty(VT) && UtilValidate.isNotEmpty(VS) && VT < VS) || 
            (UtilValidate.isNotEmpty(VT) && UtilValidate.isNotEmpty(VE) && VT < VE) || 
            (UtilValidate.isNotEmpty(VS) && UtilValidate.isNotEmpty(VE) && VS < VE))
        {
            // Debug.log(" miglioraConValoriPiuAlti " + miglioraConValoriPiuAlti + " -> true");
            miglioraConValoriPiuAlti = true;
        }
        
        if ((UtilValidate.isNotEmpty(VI) && UtilValidate.isNotEmpty(VT) && VI > VT) || 
                (UtilValidate.isNotEmpty(VI) && UtilValidate.isNotEmpty(VS) && VI > VS) || 
                (UtilValidate.isNotEmpty(VI) && UtilValidate.isNotEmpty(VE) && VI > VE) || 
                (UtilValidate.isNotEmpty(VT) && UtilValidate.isNotEmpty(VS) && VT > VS) || 
                (UtilValidate.isNotEmpty(VT) && UtilValidate.isNotEmpty(VE) && VT > VE) || 
                (UtilValidate.isNotEmpty(VS) && UtilValidate.isNotEmpty(VE) && VS > VE))
        {
            // Debug.log(" miglioraConValoriPiuBassi " + miglioraConValoriPiuBassi + " -> true");
            miglioraConValoriPiuBassi = true;
        }
        
        // controllo configurazione dei valori di soglia
        if (miglioraConValoriPiuAlti && miglioraConValoriPiuBassi)
        {
          
            // Debug.log(" entrambi true -> errore");
            // Messaggio errore impostazione soglia
            GenericValue wrk = getDelegator().findOne(E.WorkEffort.name(), UtilMisc.toMap(E.workEffortId.name(), workEffortId), false);
            String wrkDesc = workEffortId + ScoreCard.TRATT + wrk.getString(E.workEffortName.name());
            String msg = String.format("Found error for workEffort %s in setting limitMin %s, target %s, limitMax %s, limitEx %s", wrkDesc, VI, VT, VS, VE);
            jLogger.addMessage(ServiceLogger.makeLogError(msg, "019", workEffortId, accountName, null));
            return 0d;
            
        } else if (!miglioraConValoriPiuAlti && !miglioraConValoriPiuBassi) {
            // Debug.log(" currentKpi.get(\"debitCreditFlag\") " + currentKpi.get("debitCreditFlag"));
            if ("D".equals(currentKpi.get("debitCreditFlag"))) {
                miglioraConValoriPiuAlti = true;
            } else {
                miglioraConValoriPiuBassi = true;
            }
        }
//        Debug.log(" miglioraConValoriPiuAlti " + miglioraConValoriPiuAlti);
//        Debug.log(" miglioraConValoriPiuBassi " + miglioraConValoriPiuBassi);
        Double VU = null;
        Double punteggio = null;
        // caso miglioraConValoriPiuAlti
        if(miglioraConValoriPiuAlti) {
            // primo controllo con soglia piu bassa = VI
            // Debug.log(" primo controllo con soglia piu bassa = VI = " + VI);
            if (UtilValidate.isNotEmpty(VI)) {
                if (VC < VI) {
                    // Debug.log(" VC < VI punteggio = -1 ");
                    punteggio = -1d;
                } else {
                    // Debug.log(" VC > VI -> controllo  VT " + VT + " VS " + VS + " VE " + VE + " VI " + VI);
                    if (UtilValidate.isNotEmpty(VT)) VU = VT;
                    else if (UtilValidate.isNotEmpty(VS)) VU = VS;
                    else if (UtilValidate.isNotEmpty(VE)) VU = VE;
                    else VU = VI;
                    // Debug.log(" primo controllo con soglia piu bassa = VU = " + VU);
                    if (VU == VI) punteggio = 0d;
                    else punteggio =  ((VC - VI) / (VU - VI)) * ScoreCard.HUNDRED;
                }
                
            }
            // secondo controllo con soglia piu bassa = VT
            // Debug.log(" secondo controllo con soglia piu bassa = VT = " + VT);
            if (UtilValidate.isNotEmpty(VT)) {
                if (VC < VT && UtilValidate.isEmpty(punteggio)) {
                    // Debug.log(" secondo controllo VC < VT punteggio = -1 ");
                    punteggio = -1d;
                } else if (VC >= VT) {
                    // Debug.log(" secondo controllo VC > VT controllo VS " + VS + " VE " + VE + " VT " + VT);
                    if (UtilValidate.isNotEmpty(VS)) VU = VS;
                    else if (UtilValidate.isNotEmpty(VE)) VU = VE;
                    else VU = VT;
                    // Debug.log(" secondo controllo con soglia piu bassa = VU = " + VU);
                    if (VU == VT) punteggio = 100d;
                    else punteggio = ((VC - VT) / (VU - VT)) * ScoreCard.HUNDRED + ScoreCard.HUNDRED;
                }
                
            }
            // terzo controllo con soglia piu bassa = VS
            // Debug.log(" terzo controllo con soglia piu bassa = VS = " + VS);
            if (UtilValidate.isNotEmpty(VS)) {
                if (VC < VS && UtilValidate.isEmpty(punteggio)) {
                    // Debug.log(" terzo controllo VC < VS punteggio = -1 ");
                    punteggio = -1d;
                } else if (VC >= VS) {
                    // Debug.log(" terzo controllo controllo VE " + VE + " VS " + VS);
                    if (UtilValidate.isNotEmpty(VE)) VU = VE;
                    else VU = VS;
                    // Debug.log(" terzo controllo con soglia piu bassa = VU = " + VU);
                    if (VU == VS) punteggio = 200d;
                    else punteggio = ((VC - VS) / (VU - VS)) * ScoreCard.HUNDRED + ScoreCard.HUNDRED + ScoreCard.HUNDRED;
                }
                
            }
            // quarto e ultimo controllo con soglia piu bassa = VE
            // Debug.log(" quarto controllo con soglia piu bassa = VE = " + VE);
            if (UtilValidate.isNotEmpty(VE)) {
                if (VC < VE && UtilValidate.isEmpty(punteggio)) {
                    // Debug.log(" quarto controllo VC < VE punteggio = -1 ");
                    punteggio = -1d;
                } else if (VC >= VE) {
                    VU = VE;
                    punteggio = 300d;
                    // Debug.log(" quarto controllo con soglia piu bassa = VU = " + VU);
                }
            }
        }
        if(miglioraConValoriPiuBassi) {
            // primo controllo con soglia piu bassa = VI
            // Debug.log(" primo controllo con soglia piu bassa = VI = " + VI);
            if (UtilValidate.isNotEmpty(VI)) {
                if (VC > VI) {
                    // Debug.log(" primo controllo VC > VI punteggio = -1 ");
                    punteggio = -1d;
                } else {
                    // Debug.log(" primo controllo controllo  VT " + VT + " VS " + VS + " VE " + VE);
                    if (UtilValidate.isNotEmpty(VT)) VU = VT;
                    else if (UtilValidate.isNotEmpty(VS)) VU = VS;
                    else if (UtilValidate.isNotEmpty(VE)) VU = VE;
                    else VU = VI;
                    // Debug.log(" primo controllo con soglia piu bassa = VU = " + VU);
                    if (VU == VI) punteggio = 0d;
                    else punteggio = ((VI - VC) / (VI - VU)) * ScoreCard.HUNDRED;
                }
            }
            // secondo controllo con soglia piu bassa = VT
            // Debug.log(" secondo controllo con soglia piu bassa = VT = " + VT);
            if (UtilValidate.isNotEmpty(VT)) {
                if (VC > VT && UtilValidate.isEmpty(punteggio)) {
                    // Debug.log(" secondo controllo VC > VT punteggio = -1 ");
                    punteggio = -1d;
                } else if (VC <= VT) {
                    // Debug.log(" secondo controllo controllo VS " + VS + " VE " + VE);
                    if (UtilValidate.isNotEmpty(VS)) VU = VS;
                    else if (UtilValidate.isNotEmpty(VE)) VU = VE;
                    else VU = VT;
                    // Debug.log(" secondo controllo con soglia piu bassa = VU = " + VU);
                    if (VU == VT) punteggio = 100d;
                    else punteggio = ((VT - VC) / (VT - VU)) * ScoreCard.HUNDRED + ScoreCard.HUNDRED;
                }
            }
            // terzo controllo con soglia piu bassa = VS
            // Debug.log(" terzo controllo con soglia piu bassa = VS = " + VS);
            if (UtilValidate.isNotEmpty(VS)) {
                if (VC > VS && UtilValidate.isEmpty(punteggio)) {
                    // Debug.log(" terzo controllo VC > VS punteggio = -1 ");
                    punteggio = -1d;
                } else if (VC <= VS) {
                    // Debug.log(" terzo controllo controllo VE " + VE + " VS " + VS);
                    if (UtilValidate.isNotEmpty(VE)) VU = VE;
                    else VU = VS;
                    // Debug.log(" terzo controllo con soglia piu bassa = VU = " + VU);
                    if (VU == VS) punteggio = 200d;
                    else punteggio = ((VS - VC) / (VS - VU)) * ScoreCard.HUNDRED + ScoreCard.HUNDRED + ScoreCard.HUNDRED;
                }
            }
            // quarto e ultimo controllo con soglia piu bassa = VE
            // Debug.log(" quarto controllo con soglia piu bassa = VE = " + VE);
            if (UtilValidate.isNotEmpty(VE)) {
                if (VC > VE  && UtilValidate.isEmpty(punteggio)) {
                    // Debug.log(" quarto controllo VC > VE punteggio = -1 ");
                    punteggio = -1d;
                } else if (VC <= VE){
                    VU = VE;
                    punteggio = 300d;
                    // Debug.log(" quarto controllo con soglia piu bassa = VU = " + VU);
                }
            }
        }
        
        // Debug.log(" punteggio " + punteggio);
        return punteggio;
	}

}
