package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.CustomMethod;
import it.mapsgroup.gzoom.mybatis.mapper.CustomMethodMapper;
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
public class CustomMethodDao extends AbstractDao {
    private static final Logger LOG = getLogger(CustomMethodDao.class);
    private final CustomMethodMapper customMethodMapper;

    @Autowired
    public CustomMethodDao(CustomMethodMapper customMethodMapper) {
        this.customMethodMapper = customMethodMapper;
    }

    @Transactional
    public List<CustomMethod> getCustomMethodList () {
        LOG.info("find all customMethod");

        List<CustomMethod> customMethods = this.customMethodMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", customMethods.size());
        return customMethods;
    }

    /**
     * This function gets a CustomMethod given its id.
     *
     * @param id uom range id of the customMethod
     * @return the corresponding CustomMethod record
     */
    @Transactional
    public CustomMethod get(String id) {
        LOG.info("find customMethod by id");

        CustomMethod customMethod = this.customMethodMapper.selectByPrimaryKey(id);
        LOG.info("CustomMethod = {}", (customMethod != null));
        return customMethod;
    }

    /**
     * This function creates a new record customMethod.
     *
     * @param customMethod customMethod to add
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(CustomMethod customMethod) {
        LOG.info("create customMethod");
        setCreatedTimestamp(customMethod);
        int result = this.customMethodMapper.insert(customMethod);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of customMethod.
     *
     * @param customMethod customMethod to update
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(CustomMethod customMethod) {
        LOG.info("update customMethod");
        setUpdateTimestamp(customMethod);
        int result = this.customMethodMapper.updateByPrimaryKey(customMethod);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a customMethod.
     *
     * @param id id of the customMethod to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete customMethod");
        int result = this.customMethodMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
