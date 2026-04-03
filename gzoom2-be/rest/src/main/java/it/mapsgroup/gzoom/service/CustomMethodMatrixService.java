package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.CustomMethodMatrixDao;
import it.mapsgroup.gzoom.mybatis.dto.CustomMethodMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomMethodMatrixService {
    private final CustomMethodMatrixDao customMethodMatrixDao;

    @Autowired
    public CustomMethodMatrixService(CustomMethodMatrixDao customMethodMatrixDao) {
        this.customMethodMatrixDao = customMethodMatrixDao;
    }

    public Result<CustomMethodMatrix> getCustomMethodMatrixList(String uomRangeId) {
        List<CustomMethodMatrix> list = customMethodMatrixDao.getCustomMethodMatrixList(uomRangeId);
        return new Result<>(list, list.size());
    }

    public String createCustomMethodMatrix(CustomMethodMatrix req) {
        this.validateCustomMethodMatrix(req, "CREATE");
        return customMethodMatrixDao.create(req);
    }

    public boolean updateCustomMethodMatrix(CustomMethodMatrix req) {
        this.validateCustomMethodMatrix(req, "UPDATE");
        return customMethodMatrixDao.update(req);
    }

    public boolean deleteCustomMethodMatrix (String customMethodMatrixId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(customMethodMatrixId, msg.getMessageColumn(Messages.CUSTOM_METHOD_MATRIX, Messages.CUSTOM_METHOD_MATRIX_ID, Messages.IS_REQUIRED));
        CustomMethodMatrix record = customMethodMatrixDao.get(customMethodMatrixId);
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.CUSTOM_METHOD_MATRIX, Messages.CUSTOM_METHOD_MATRIX_ID, Messages.INVALID));

        return customMethodMatrixDao.delete(customMethodMatrixId);
    }

    private void validateCustomMethodMatrix(CustomMethodMatrix req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.CUSTOM_METHOD_MATRIX, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getCustomMethodId(), msg.getMessageColumn(Messages.CUSTOM_METHOD_MATRIX, Messages.CUSTOM_METHOD_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getRowInputValue(), msg.getMessageColumn(Messages.CUSTOM_METHOD_MATRIX, Messages.ROW_INPUT_VALUE, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getColumnInputValue(), msg.getMessageColumn(Messages.CUSTOM_METHOD_MATRIX, Messages.COLUMN_INPUT_VALUE, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getOutputValue(), msg.getMessageColumn(Messages.CUSTOM_METHOD_MATRIX, Messages.OUTPUT_VALUE, Messages.IS_REQUIRED));

        if ( method.equalsIgnoreCase("UPDATE")) {
            CustomMethodMatrix record = customMethodMatrixDao.get(req.getCustomMethodMatrixId());
            Validators.assertNotBlank(req.getCustomMethodMatrixId(), msg.getMessageColumn(Messages.CUSTOM_METHOD_MATRIX, Messages.CUSTOM_METHOD_MATRIX_ID, Messages.IS_REQUIRED));
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.CUSTOM_METHOD_MATRIX, Messages.CUSTOM_METHOD_MATRIX_ID, Messages.INVALID));
        }

    }
}
