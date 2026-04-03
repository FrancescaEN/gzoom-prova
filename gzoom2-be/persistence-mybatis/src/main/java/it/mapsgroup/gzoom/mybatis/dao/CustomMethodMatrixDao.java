package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.CustomMethodMatrix;
import it.mapsgroup.gzoom.mybatis.mapper.CustomMethodMatrixMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CustomMethodMatrixDao extends AbstractDao {
    private static final Logger LOG = getLogger(CustomMethodMatrixDao.class);
    private final SequenceGenerator sequenceGenerator;
    private final CustomMethodMatrixMapper customMethodMatrixMapper;

    @Autowired
    public CustomMethodMatrixDao(SequenceGenerator sequenceGenerator, CustomMethodMatrixMapper customMethodMatrixMapper) {
        this.sequenceGenerator = sequenceGenerator;
        this.customMethodMatrixMapper = customMethodMatrixMapper;
    }



    @Transactional
    public List<CustomMethodMatrix> getCustomMethodMatrixList(String customMethodId) {
        LOG.info("find customMethodMatrix by customMethodId");

        List<CustomMethodMatrix> customMethodMatrixs = this.customMethodMatrixMapper.selectByCustomMethodIdOrderByPrimaryKey(customMethodId);
        LOG.info("size = {}", customMethodMatrixs.size());
        return customMethodMatrixs;
    }

    /**
     * This function gets a CustomMethodMatrix given its id.
     *
     * @param customMethodMatrixId customMethodMatrixId of the customMethodMatrix
     * @return the corresponding CustomMethodMatrix record
     */
    @Transactional
    public CustomMethodMatrix get(String customMethodMatrixId) {
        LOG.info("find customMethodMatrix by id");

        CustomMethodMatrix customMethodMatrix = this.customMethodMatrixMapper.selectByPrimaryKey(customMethodMatrixId);
        LOG.info("CustomMethodMatrix = {}", (customMethodMatrix != null));
        return customMethodMatrix;
    }

    /**
     * This function creates a new record customMethodMatrix.
     *
     * @param customMethodMatrix customMethodMatrix to add
     * @return newId if the operation was successful
     */
    @Transactional
    public String create(CustomMethodMatrix customMethodMatrix) {
        LOG.info("create customMethodMatrix");
        String newId = this.sequenceGenerator.getNextSeqId("CustomMethodMatrix");
        customMethodMatrix.setCustomMethodMatrixId(newId);
        setCreatedTimestamp(customMethodMatrix);
        int result = this.customMethodMatrixMapper.insert(customMethodMatrix);
        LOG.info("result = {}", result);
        return (result > 0)? newId : null;
    }

    /**
     * Update of customMethodMatrix.
     *
     * @param customMethodMatrix customMethodMatrix to update
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(CustomMethodMatrix customMethodMatrix) {
        LOG.info("update customMethodMatrix");
        setUpdateTimestamp(customMethodMatrix);
        int result = this.customMethodMatrixMapper.updateByPrimaryKey(customMethodMatrix);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a customMethodMatrix.
     *
     * @param customMethodMatrixId customMethodMatrixId of the customMethodMatrix to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String customMethodMatrixId) {
        LOG.info("delete customMethodMatrix");
        int result = this.customMethodMatrixMapper.deleteByPrimaryKey(customMethodMatrixId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteWithCustomMethodId(String customMethodId) {
        LOG.info("delete customMethodMatrix");
        int result = this.customMethodMatrixMapper.deleteByCustomMethodId(customMethodId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
