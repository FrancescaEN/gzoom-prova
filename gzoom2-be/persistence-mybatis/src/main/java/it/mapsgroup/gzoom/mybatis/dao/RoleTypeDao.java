package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.RoleType;
import it.mapsgroup.gzoom.mybatis.mapper.RoleTypeMapper;
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
public class RoleTypeDao extends AbstractDao{
    private static final Logger LOG = getLogger(RoleTypeDao.class);
    private final RoleTypeMapper roleTypeMapper;

    @Autowired
    public RoleTypeDao(RoleTypeMapper roleTypeMapper) {
        this.roleTypeMapper = roleTypeMapper;
    }

    @Transactional
    public List<RoleType> getRoleTypes() {
        LOG.info("find all roleType");

        List<RoleType> roleTypes = this.roleTypeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", roleTypes.size());
        return roleTypes;
    }

    @Transactional
    public List<RoleType> getRoleTypeByParentTypeId(String parentTypeId) {
        LOG.info("find by parentTypeId");
        List<RoleType> roleTypes = this.roleTypeMapper.selectByParentTypeId(parentTypeId);
        LOG.info("size = {}", roleTypes.size());
        return roleTypes;
    }

    @Transactional
    public List<RoleType> getRoleTypeByOUAndLikeGOAL() {
        LOG.info("getRoleTypeByOUAndLikeGOAL");
        List<RoleType> roleTypes = this.roleTypeMapper.getRoleTypeByOUAndLikeGOAL();
        LOG.info("size = {}", roleTypes.size());
        return roleTypes;
    }

    @Transactional
    public RoleType getRoleType(String roleTypeId) {
        LOG.info("find roleType by id");

        RoleType roleType = this.roleTypeMapper.selectByPrimaryKey(roleTypeId);
        LOG.info("RoleType = {}", (roleType != null));
        return roleType;
    }

    @Transactional
    public boolean update(RoleType roleType, String userLoginId) {
        LOG.info("update roleType");
        setUpdateTimestamp(roleType);
        roleType.setLastModifiedByUserLogin(userLoginId);
        int result = this.roleTypeMapper.updateByPrimaryKey(roleType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String id) {
        LOG.info("delete roleType");
        int result = this.roleTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean create(RoleType roleType, String userLoginId) {
        LOG.info("create roleType");
        setCreatedTimestamp(roleType);
        roleType.setCreatedByUserLogin(userLoginId);
        int result = this.roleTypeMapper.insert(roleType);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
