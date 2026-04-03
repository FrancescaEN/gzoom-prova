package com.mapsengineering.accountingext.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;

import com.mapsengineering.accountingext.util.BaseCustomMethodCalculatorUtil;
import com.mapsengineering.accountingext.util.WorkEffortCustomMethodCalculatorUtil;
import com.mapsengineering.accountingext.util.WorkEffortFieldEnum;
import com.mapsengineering.base.comparator.PartialMapComparator;
import com.mapsengineering.base.jdbc.FtlQuery;
import com.mapsengineering.base.jdbc.JdbcQueryIterator;

/**
 * GlAccount Model on WorkEffort
 *
 */
public class AccinpWeAggregFiller extends AccinpUoFlagFiller {

    public static final String MODULE = AccinpWeAggregFiller.class.getName();

    /**
     * Constructor
     * @param delegator
     * @param thruDate
     * @param glFiscalTypeIdInput
     * @param factorFieldNames
     */
    AccinpWeAggregFiller(Delegator delegator, Timestamp thruDate, String glFiscalTypeIdInput, List<String> factorFieldNames) {
        super(delegator, thruDate, glFiscalTypeIdInput, factorFieldNames);
    }
    
    /**
     * get condition, return condition entryVoucherRef
     * @param glAccountIdRef
     * @param extraCondition
     * @param conditionValues
     * @return
     * @throws GenericEntityException
     */
    protected EntityCondition getValuesCondition(String glAccountIdRef, Map<String, Object> extraCondition, EntityCondition conditionValues) throws GenericEntityException {
        List<EntityCondition> valueCond = FastList.newInstance();
        valueCond.add(conditionValues);
        valueCond.add(EntityCondition.makeCondition(E.entryVoucherRef.name(), extraCondition.get(E.workEffortMeasureId.name())));

        return EntityCondition.makeCondition(valueCond);
    }

    protected double getFieldValue(Map<String, Object> extraCondition) {
    	double result = 0d;
        List<String> listKeys = FastList.newInstance();
        listKeys.add(E.workEffortMeasureId.name());

        PartialMapComparator<String, Object> partialMapComparator = new PartialMapComparator<String, Object>(listKeys);
        for (Map<String, Object> mappa : getExtraResultMapList()) {
        	int res = partialMapComparator.compare(mappa, extraCondition);
            if (res == 0) {
                result = (Double)mappa.get(E.amount.name());
            }

        }

        return result;
    }
    
    protected double getFieldValue(Map<String, Object> extraCondition, String fieldName) {
    	double result = 0d;
        List<String> listKeys = FastList.newInstance();
        listKeys.add(E.workEffortMeasureId.name());

        PartialMapComparator<String, Object> partialMapComparator = new PartialMapComparator<String, Object>(listKeys);
        for (Map<String, Object> mappa : getExtraResultMapList()) {
        	int res = partialMapComparator.compare(mappa, extraCondition);
            if (res == 0) {
            	result = mappa.get(fieldName) != null ? (Double)mappa.get(fieldName) : 0d;
            }
        }

        return result;
    }
    
    protected BigDecimal getAmount(ResultSet ele, String aggregType) throws SQLException {
    	if (BaseCustomMethodCalculatorUtil.MEDIA.equals(aggregType)) {
    		return getAverage(ele);
    	}else if (BaseCustomMethodCalculatorUtil.NUMDEN.equals(aggregType)) {
    		return getNumDen(ele);
    	}
        return ele.getBigDecimal(BaseCustomMethodCalculatorUtil.AMOUNT);
    }
    
    protected BigDecimal getNumDen(ResultSet ele) throws SQLException {
    	BigDecimal numAmount = ele.getBigDecimal(BaseCustomMethodCalculatorUtil.A);
		BigDecimal denAmount = ele.getBigDecimal(BaseCustomMethodCalculatorUtil.B);
		Integer decimalScale = ele.getInt(BaseCustomMethodCalculatorUtil.DECIMAL_SCALE);
		
		if (UtilValidate.isEmpty(denAmount) || BigDecimal.ZERO.compareTo(denAmount) == 0) {
			return null;
		}
		if (UtilValidate.isEmpty(numAmount) || BigDecimal.ZERO.compareTo(numAmount) == 0) {
			return BigDecimal.ZERO;
		}
		// A / B * 100
        BigDecimal result = numAmount
            .divide(denAmount, 10, RoundingMode.HALF_UP) // Divisione con 10 decimali temporanei
            .multiply(BigDecimal.valueOf(100))
            .setScale(decimalScale, RoundingMode.HALF_UP); // Arrotonda a 2 decimali
        return result;
    }

    @Override
    public List<Map<String, Object>> getExtraParametersList(String glAccountId, Map<String, ? extends Object> context) throws GenericEntityException {
        inizializeExtraResultMap();
        List<Map<String, Object>> extraConditionList = FastList.newInstance();

        WorkEffortCustomMethodCalculatorUtil workEffortCustomMethodCalculatorUtil = new WorkEffortCustomMethodCalculatorUtil(getDelegator(), context);        
        try {
        	Map<String, Object> queryContext = workEffortCustomMethodCalculatorUtil.mapContextUpdate(glAccountId, getThruDate(), getGlFiscalTypeIdInput(), getCustomMethodName(), context);
            String aggregType = (String) queryContext.get(WorkEffortFieldEnum.aggregType.name());
        	JdbcQueryIterator queryWorkEffortList = new FtlQuery(getDelegator(), workEffortCustomMethodCalculatorUtil.getQuery(), queryContext).iterate();
            Debug.log(" - queryWorkEffortList " + queryWorkEffortList);
            try {
                while (queryWorkEffortList.hasNext()) {
                    ResultSet ele = queryWorkEffortList.next();
                    String voucherRef = getVoucheRef(ele);
                    BigDecimal amount = getAmount(ele, aggregType);
                    String msg = " - For " + voucherRef + " = " + amount;
                    getjLogger().printLogInfo(msg);
                    

                    Map<String, Object> extraCondition = FastMap.newInstance();
                    extraCondition.put(E.workEffortMeasureId.name(), voucherRef);
                    extraConditionList.add(extraCondition);

                    Map<String, Object> extraResultMap = FastMap.newInstance();
                    extraResultMap.put(E.workEffortMeasureId.name(), voucherRef);
                    Double outputValue = getValue(amount, aggregType);
                    extraResultMap.put(E.amount.name(), outputValue);
                    if (BaseCustomMethodCalculatorUtil.NUMDEN.equals(aggregType)) {
                    	Double numAmount = ele.getDouble(BaseCustomMethodCalculatorUtil.A);
                        Double denAmount = ele.getDouble(BaseCustomMethodCalculatorUtil.B);
                		extraResultMap.put("NUM", numAmount);
                		extraResultMap.put("DEN", denAmount);
                    }
                    getExtraResultMapList().add(extraResultMap);
                }

            } finally {
                queryWorkEffortList.close();
            }
        } catch (IOException e) {
            Debug.logError(e, MODULE);
        } catch (SQLException e) {
            Debug.logError(e, MODULE);
        }

        return extraConditionList;
    }
    
    private Double getValue(BigDecimal amount, String aggregType) {
    	if (BaseCustomMethodCalculatorUtil.MEDIA.equals(aggregType) && amount == null) {
    		return Double.NaN;
    	}
    	return new Double(amount.doubleValue());
    }

    private String getVoucheRef(ResultSet ele) throws SQLException {
        return ele.getString(WorkEffortFieldEnum.VOUCHER_REF.name());
    }

    /**
     * Call super.getParametersToStore and super.getWeExtraParametersToStore
     * Sono gli unici parametri che poi vengono passati al servizio di scrittura del movimento
     * @throws GenericEntityException 
     */
    public Map<String, Object> getParametersToStore(String glAccountId, Map<String, Object> extraCondition, String organizationId) throws GenericEntityException {
        Map<String, Object> extraParametersToStore = FastMap.newInstance();
        extraParametersToStore.putAll(super.getParametersToStore(glAccountId, extraCondition, organizationId));
        extraParametersToStore.putAll(super.getWeExtraParametersToStore(extraCondition));
        extraParametersToStore.put(BaseCustomMethodCalculatorUtil.perfAmountActual, getFieldValue(extraCondition, "NUM"));
        extraParametersToStore.put(BaseCustomMethodCalculatorUtil.perfAmountTarget, getFieldValue(extraCondition, "DEN"));
        return extraParametersToStore;
    }
    
    /**
     * @return resultMap like {"amount" = {A = 70, B = 10}, "origAmount" = {A =
     *         80, B = 18}} or null if there is not row to process for factor
     *         calculation.
     */
    public Map<String, Map<String, Object>> fillFactorMap(GenericValue inputCalc, String glAccountId, Map<String, Object> extraCondition) throws GeneralException {
        Iterator<String> fieldNameIt = getResultMap().keySet().iterator();
        int counter = 0;

        while (fieldNameIt.hasNext()) {
            String factorFieldName = fieldNameIt.next();
            getResultMap().get(factorFieldName).put(BaseCustomMethodCalculatorUtil.DUMMY_A, getFieldValue(extraCondition));
        }
        getjLogger().addRecordElaborated(counter);
        return getResultMap();
    }

}
