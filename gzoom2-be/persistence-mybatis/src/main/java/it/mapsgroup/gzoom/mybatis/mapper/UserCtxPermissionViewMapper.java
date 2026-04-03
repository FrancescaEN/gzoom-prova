package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.UserCtxPermissionView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserCtxPermissionViewMapper {

    @Select({
            "select * from user_ctx_permission_view " +
                    "where user_login_id = #{userLoginId}"
    })
    @ResultType(UserCtxPermissionView.class)
    List<UserCtxPermissionView> selectByUserLoginId (String userLoginId);

    @Select({
            "select count(*) from user_ctx_permission_view " +
                    "where user_login_id = #{userLoginId} and user_ctx = #{context} and ctx_permission = #{ctxPermission}"
    })
    @ResultType(int.class)
    int hasPermission (@Param("userLoginId")String userLoginId, @Param("context")String context, @Param("ctxPermission")String ctxPermission);

    @Select({
            "select count(*) from user_ctx_permission_view " +
                    "where user_login_id = #{userLoginId} and user_ctx = #{context} and ${respRole} = 1"
    })
    @ResultType(int.class)
    int hasPermissionResp (@Param("userLoginId")String userLoginId, @Param("context")String context, String respRole);
}
