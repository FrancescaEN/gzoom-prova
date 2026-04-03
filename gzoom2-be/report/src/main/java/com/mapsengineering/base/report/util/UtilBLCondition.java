package com.mapsengineering.base.report.util;

public class UtilBLCondition {

    public String getSqlCondition(String orgUnitId,String workEffortTypeId, String workEffortId, String AssessWorkEffortTypeId){
        String ret = "";
        // se workEffortId non valorizzato la stampa è lanciata da menu stampe altrimenti da scheda
        if(workEffortId != null && !workEffortId.isEmpty())
        {
            if(workEffortTypeId.equals(AssessWorkEffortTypeId)){
                ret  =" AND org_unit_id_sup = (select  org_unit_id from work_effort where work_effort_id = '" + workEffortId + "')";
            }
            else{
                ret = "AND FIL_W.WORK_EFFORT_ID = '"+ workEffortId + "'";
            }
        }
        else
        {
            //da menu stampa con UO selezionata e NON assessorato
            if(orgUnitId  != null && ! orgUnitId.isEmpty()  && ! workEffortTypeId.equals(AssessWorkEffortTypeId))
            {
                ret = " AND (FIL_W.ORG_UNIT_ID = '"+ orgUnitId + "' AND EXP.WORK_EFFORT_TYPE_ID_STR = '" + workEffortTypeId + "')";
            }
            else if (orgUnitId  != null && ! orgUnitId.isEmpty()  && workEffortTypeId.equals(AssessWorkEffortTypeId)){
                ret = " AND org_unit_id_sup = '"+ orgUnitId + "' ";
            }
            else{
                ret = "AND EXP.WORK_EFFORT_TYPE_ID_STR = '" + workEffortTypeId + "'";
            }
        }
        return ret;
    }
}
