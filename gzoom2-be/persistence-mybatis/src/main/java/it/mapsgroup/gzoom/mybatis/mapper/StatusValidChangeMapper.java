package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.StatusValidChange;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StatusValidChangeMapper {

    @Delete({
        "delete from status_valid_change",
        "where status_id = #{statusId,jdbcType=VARCHAR}",
          "and status_id_to = #{statusIdTo,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("statusId") String statusId, @Param("statusIdTo") String statusIdTo);

    @Delete(value = {
            "delete svc from status_valid_change svc",
            "join status_item si on svc.status_id = si.status_id",
            "join status_type st on st.status_type_id  = si.status_type_id ",
            "where st.status_type_id = #{statusTypeId,jdbcType=VARCHAR}"
    }, databaseId = "mysql")
    @Delete(value = {
            "delete svc from status_valid_change svc",
            "join status_item si on svc.status_id = si.status_id or svc.status_id_to = si.status_id ",
            "join status_type st on st.status_type_id  = si.status_type_id ",
            "where st.status_type_id = #{statusTypeId,jdbcType=VARCHAR}",
            "group by svc.status_id, svc.status_id_to"
    }, databaseId = "mssql")
    @Delete(value = {
            "delete from status_valid_change",
            "where (status_id, status_id_to) in (",
            "            select svc.status_id, svc.status_id_to",
            "            from status_valid_change svc",
            "            inner join status_item si on svc.status_id = si.status_id or svc.status_id_to = si.status_id ",
            "            inner join status_type st on st.status_type_id = si.status_type_id",
            "            where st.status_type_id = #{statusTypeId,jdbcType=VARCHAR} " +
                    "group by svc.status_id, svc.status_id_to)"
    })
    int deleteByStatusTypeId(String statusTypeId);

    @Insert({
        "insert into status_valid_change (status_id, status_id_to, ",
        "condition_expression, transition_name, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "last_modified_by_user_login, created_by_user_login)",
        "values (#{statusId,jdbcType=VARCHAR}, #{statusIdTo,jdbcType=VARCHAR}, ",
        "#{conditionExpression,jdbcType=VARCHAR}, #{transitionName,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(StatusValidChange row);


    @Select({
        "select",
        "status_id, status_id_to, condition_expression, transition_name, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, last_modified_by_user_login, ",
        "created_by_user_login",
        "from status_valid_change",
        "where status_id = #{statusId,jdbcType=VARCHAR}",
          "and status_id_to = #{statusIdTo,jdbcType=VARCHAR}"
    })
    @ResultType(StatusValidChange.class)
    StatusValidChange selectByPrimaryKey(@Param("statusId") String statusId, @Param("statusIdTo") String statusIdTo);


    @Select({
        "select",
        "status_id, status_id_to, condition_expression, transition_name, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, last_modified_by_user_login, ",
        "created_by_user_login",
        "from status_valid_change"
    })
    @ResultType(StatusValidChange.class)
    List<StatusValidChange> selectAll();


    @Select({
            """
            SELECT DISTINCT svc.*
            FROM status_valid_change svc
            JOIN status_item si ON (svc.status_id = si.status_id OR svc.status_id_to = si.status_id)
            WHERE si.STATUS_TYPE_ID = #{statusTypeId,jdbcType=VARCHAR}
            """
    })
    @ResultType(StatusValidChange.class)
    List<StatusValidChange> findByStatusTypeId(String statusTypeId);


    @Update({
        "update status_valid_change",
        "set condition_expression = #{conditionExpression,jdbcType=VARCHAR},",
          "transition_name = #{transitionName,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where status_id = #{statusId,jdbcType=VARCHAR}",
          "and status_id_to = #{statusIdTo,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(StatusValidChange row);
}