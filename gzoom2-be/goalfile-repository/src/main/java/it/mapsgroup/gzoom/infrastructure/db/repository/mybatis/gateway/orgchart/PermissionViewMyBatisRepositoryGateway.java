package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.gateway.orgchart;

import it.mapsgroup.gzoom.entity.goalfile.model.Context;
import it.mapsgroup.gzoom.entity.user.gateway.PermissionViewRepositoryGateway;
import it.mapsgroup.gzoom.entity.user.model.PermissionView;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.user.PermissionViewMapper;
import it.mapsgroup.gzoom.infrastructure.user.util.UserCtxPermissionViewDtoPermissionMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PermissionViewMyBatisRepositoryGateway implements PermissionViewRepositoryGateway {
    private final PermissionViewMapper permissionViewMapper;
    private final UserCtxPermissionViewDtoPermissionMapper userCtxPermissionViewDtoPermissionMapper;

    public PermissionViewMyBatisRepositoryGateway(PermissionViewMapper permissionViewMapper,
                                                  UserCtxPermissionViewDtoPermissionMapper userCtxPermissionViewDtoPermissionMapper) {
        this.permissionViewMapper = permissionViewMapper;
        this.userCtxPermissionViewDtoPermissionMapper = userCtxPermissionViewDtoPermissionMapper;
    }

    public PermissionView getPermissionView(String userLoginId, Context context) {
        return this.userCtxPermissionViewDtoPermissionMapper.toPermissionView(
                this.permissionViewMapper.getPermissionView(userLoginId, context)
        );
    }
}