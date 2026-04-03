package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.CustomMethodDao;
import it.mapsgroup.gzoom.mybatis.dao.CustomMethodMatrixDao;
import it.mapsgroup.gzoom.mybatis.dto.CustomMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomMethodService {
    private final CustomMethodDao customMethodDao;
    private final CustomMethodMatrixDao customMethodMatrixDao;

    @Autowired
    public CustomMethodService(CustomMethodDao customMethodDao, CustomMethodMatrixDao customMethodMatrixDao) {
        this.customMethodDao = customMethodDao;
        this.customMethodMatrixDao = customMethodMatrixDao;
    }

    public Result<CustomMethod> getCustomMethodList() {
        List<CustomMethod> list = this.customMethodDao.getCustomMethodList();
        return new Result<>(list, list.size());
    }

    public boolean createCustomMethod(CustomMethod req) {
        this.validateCustomMethod(req, "CREATE");
        return customMethodDao.create(req);
    }

    public boolean updateCustomMethod(CustomMethod req) {
        this.validateCustomMethod(req, "UPDATE");
        return customMethodDao.update(req);
    }

    @Transactional
    public boolean deleteCustomMethod(String customMethodId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(customMethodId, msg.getMessageColumn(Messages.CUSTOM_METHOD, Messages.CUSTOM_METHOD_ID, Messages.IS_REQUIRED));
        CustomMethod record = customMethodDao.get(customMethodId);
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.CUSTOM_METHOD, Messages.CUSTOM_METHOD_ID, Messages.INVALID));

        this.customMethodMatrixDao.deleteWithCustomMethodId(customMethodId);
        return customMethodDao.delete(customMethodId);
    }

    private void validateCustomMethod(CustomMethod req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.CUSTOM_METHOD, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getCustomMethodId(), msg.getMessageColumn(Messages.CUSTOM_METHOD, Messages.CUSTOM_METHOD_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getCustomMethodTypeId(), msg.getMessageColumn(Messages.CUSTOM_METHOD, Messages.CUSTOM_METHOD_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getCustomMethodName(), msg.getMessageColumn(Messages.CUSTOM_METHOD, Messages.CUSTOM_METHOD_NAME, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getDescription(), msg.getMessageColumn(Messages.CUSTOM_METHOD, Messages.DESCRIPTION, Messages.IS_REQUIRED));


        CustomMethod record = customMethodDao.get(req.getCustomMethodId());

        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertTrue(record == null, msg.getMessageTable(Messages.CUSTOM_METHOD, Messages.EXISTING));
        }
        if ( method.equalsIgnoreCase("UPDATE")) {
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.CUSTOM_METHOD, Messages.CUSTOM_METHOD_ID, Messages.INVALID));
        }

    }
}
