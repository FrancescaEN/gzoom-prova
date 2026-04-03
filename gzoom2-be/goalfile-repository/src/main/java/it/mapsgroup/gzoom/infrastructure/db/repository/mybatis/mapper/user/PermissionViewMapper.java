package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.user;

import it.mapsgroup.gzoom.entity.goalfile.model.Context;
import it.mapsgroup.gzoom.infrastructure.user.dto.UserCtxPermissionViewDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PermissionViewMapper {
    UserCtxPermissionViewDto getPermissionView(String userLoginId,
                                               Context userCtx);
}