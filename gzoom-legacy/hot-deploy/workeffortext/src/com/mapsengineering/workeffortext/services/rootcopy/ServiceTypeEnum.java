package com.mapsengineering.workeffortext.services.rootcopy;

import org.ofbiz.base.util.UtilValidate;

import com.mapsengineering.workeffortext.services.E;


/**
 * Manage enumeration type for GlAccount
 *
 */
public enum ServiceTypeEnum {

    /*SNAPSHOT_MASSIVE("SNAPSHOT_MASSIVE", "MAS_WRK_ROOT_COPY")
    SNAPSHOT("SNAPSHOT", "WRK_ROOT_SNAP")
    CLONE("CLONE", "WRK_ROOT_CLONE")
    COPY_ALL("COPY_ALL", "WRK_ROOT_COPY_ALL")
    COPY("COPY", "WRK_ROOT_COPY")*/    
    
    /*SNAPSHOT_MASSIVE("MAS_WRK_ROOT_COPY", true, true, true),
    SNAPSHOT("WRK_ROOT_SNAP", true, true, true),
    CLONE("WRK_ROOT_CLONE", true, true, false),
    COPY_ALL("WRK_ROOT_COPY_ALL", false, false, false),
    COPY("WRK_ROOT_COPY", false, false, false);*/
    
	/** SNAPSHOT MASSIVO, è il caso di storicizzazione massiva lanciata da voce di menu e che opera su tutte le schede con enabled_snapshot = Y (escludendo le altre) */
    SNAPSHOT_MASSIVE("MAS_WRK_ROOT_COPY", true, true),
    /** PUNTUALE, è il caso di storicizzazione puntuale della singola scheda (servizio richiamato anche massivamente dalla storicizzazione), attivo se duplicate_admit = 'SNAPSHOT' ed enabled_snapshot = Y */
    SNAPSHOT("WRK_ROOT_SNAP", true, true),
    /** CLONE è il caso di duplicazione completa della scheda sullo stesso tipo obiettivo, analogo allo snapshot puntuale ma senza revision_id e snpshot_id, attivo se se duplicate_admit = 'CLONE' ed enabled_snapshot = Y */
    CLONE("WRK_ROOT_CLONE", true, false),
    /** ALL, è il caso di copia completa della scheda (incluso creazione automatica misure e note e codice, esclusa responsabile) su un tipo scheda collegato attivo se duplicate_admit = 'COPY' e copy = Y */
    COPY_ALL("WRK_ROOT_COPY_ALL", false, false),
    /** COPY, è il caso di copia parziale della scheda (esclusi creazione automatiche a parte il codice, escluse misure e note e soggetti collegati) lanciata da voce di menu e che opera su tutte le schede con copy = Y */
    COPY("WRK_ROOT_COPY", false, false); 
    
    private String serviceTypeId;
    private boolean useEnableSnapshot;
    private boolean useWorkEffortRevisionId;
    
    /**
     * 
     * @param serviceTypeId
     * @param useEnableSnapshot, wether if workEffortType use enableSnapshop to enable service
     * @param snapshot
     */
    private ServiceTypeEnum(String serviceTypeId, boolean useEnableSnapshot, boolean useWorkEffortRevisionId) {
        this.serviceTypeId = serviceTypeId;
        this.useEnableSnapshot = useEnableSnapshot;
        this.useWorkEffortRevisionId = useWorkEffortRevisionId;
    }

    /**
     * Return Enumeration type
     * @param indicator
     * @return
     */
    public static ServiceTypeEnum getEnumeration(String snapshot, String duplicateAdmit) {
        if (E.Y.name().equals(snapshot)) {
            return returnCaseSnapshot(duplicateAdmit);
        } else if (UtilValidate.isNotEmpty(duplicateAdmit)) {
            return returnCaseDuplicate(duplicateAdmit);
        }
        return COPY;
    }

    private static ServiceTypeEnum returnCaseSnapshot(String duplicateAdmit) {
        if (E.SNAPSHOT.name().equals(duplicateAdmit)) {
            return SNAPSHOT;
        }
        return SNAPSHOT_MASSIVE;
    }
    
    private static ServiceTypeEnum returnCaseDuplicate(String duplicateAdmit) {
        if (E.CLONE.name().equals(duplicateAdmit)) {
            return CLONE;
        }
        return COPY_ALL;
    }

    /**
     * @return the serviceTypeId
     */
    public String getServiceTypeId() {
        return serviceTypeId;
    }

    /**
     * @return the useEnableSnapshot
     */
    public boolean isUseEnableSnapshot() {
        return useEnableSnapshot;
    }

    /**
     * @return the useWorkEffortRevisionId
     */
    public boolean isUseWorkEffortRevisionId() {
        return useWorkEffortRevisionId;
    }
}