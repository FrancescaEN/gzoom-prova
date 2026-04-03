package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortMeasRatScDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasRatSc;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasRatScExUomRatingScale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;


/**
 * @author Leonardo Minaudo
 */
@Service
public class WorkEffortMeasRatScService {
    private  final WorkEffortMeasRatScDao workEffortMeasRatScDao;

    @Autowired
    public WorkEffortMeasRatScService(WorkEffortMeasRatScDao workEffortMeasRatScDao) {
        this.workEffortMeasRatScDao = workEffortMeasRatScDao;
    }

    public Result<WorkEffortMeasRatScExUomRatingScale> getRatingScaleWEM(String workEffortMeasureId) {
        List<WorkEffortMeasRatScExUomRatingScale> list = this.workEffortMeasRatScDao.getRatingScaleWEM(workEffortMeasureId);
        return new Result<>(list, list.size());
    }

    public boolean createWorkEffortMeasRatSc(WorkEffortMeasRatSc req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortMeasureId(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.WORK_EFFORT_MEASURE_ID, Messages.IS_REQUIRED) );
        Validators.assertNotBlank(req.getUomId(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.UOM_ID,  Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomRatingValue().toString(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.UOM_RATING_VALUE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomCode(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.UOM_CODE , Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomCodeLang(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC , Messages.UOM_CODE_LANG , Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomDescr(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.UOM_DESCR, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomDescrLang(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC , Messages.UOM_DESCR_LANG , Messages.IS_REQUIRED));

        WorkEffortMeasRatSc record = workEffortMeasRatScDao.findById(req.getWorkEffortMeasureId(), req.getUomId(), req.getUomRatingValue());
        Validators.assertTrue(record == null, msg.getMessageTable(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.EXISTING));
        return this.workEffortMeasRatScDao.create(req, principal().getUserLoginId());
    }

    public int updateWorkEffortMeasRatSc(WorkEffortMeasRatSc req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortMeasureId(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.WORK_EFFORT_MEASURE_ID, Messages.IS_REQUIRED) );
        Validators.assertNotBlank(req.getUomId(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.UOM_ID,  Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomRatingValue().toString(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.UOM_RATING_VALUE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomCode(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.UOM_CODE , Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomCodeLang(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC , Messages.UOM_CODE_LANG , Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomDescr(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.UOM_DESCR, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomDescrLang(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC , Messages.UOM_DESCR_LANG , Messages.IS_REQUIRED));

        WorkEffortMeasRatSc record = workEffortMeasRatScDao.findById(req.getWorkEffortMeasureId(), req.getUomId(), req.getUomRatingValue());
        Validators.assertNotNull(record, msg.getMessageTable(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.INVALID));
        return this.workEffortMeasRatScDao.update(req, principal().getUserLoginId());
    }

    public int deleteWorkEffortMeasRatSc(String workEffortMeasureId, String uomId, Double uomRatingValue) {
        Messages msg = new Messages();
        Validators.assertNotBlank(workEffortMeasureId, msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.WORK_EFFORT_MEASURE_ID, Messages.IS_REQUIRED) );
        Validators.assertNotBlank(uomId, msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.UOM_ID,  Messages.IS_REQUIRED));
        Validators.assertNotBlank(uomRatingValue.toString(), msg.getMessageColumn(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.UOM_RATING_VALUE, Messages.IS_REQUIRED));

        WorkEffortMeasRatSc record = workEffortMeasRatScDao.findById(workEffortMeasureId, uomId, uomRatingValue);
        Validators.assertNotNull(record, msg.getMessageTable(Messages.WORK_EFFORT_MEAS_RAT_SC, Messages.INVALID));
        return this.workEffortMeasRatScDao.delete(workEffortMeasureId, uomId, uomRatingValue);
    }

}
