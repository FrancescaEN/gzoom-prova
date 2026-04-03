package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssocType;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WorkEffortAssocTypeMapper {
    @Delete({
        "delete from work_effort_assoc_type",
        "where work_effort_assoc_type_id = #{workEffortAssocTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String workEffortAssocTypeId);

    @Insert({
        "insert into work_effort_assoc_type (work_effort_assoc_type_id, parent_type_id, ",
        "has_table, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "has_response, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{workEffortAssocTypeId,jdbcType=VARCHAR}, #{parentTypeId,jdbcType=VARCHAR}, ",
        "#{hasTable,jdbcType=CHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{hasResponse,jdbcType=CHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(WorkEffortAssocType row);

    @Select({
        "select",
        "work_effort_assoc_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, has_response, last_modified_by_user_login, ",
        "created_by_user_login",
        "from work_effort_assoc_type",
        "where work_effort_assoc_type_id = #{workEffortAssocTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffortAssocType", value = {
        @Result(column="work_effort_assoc_type_id", property="workEffortAssocTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_type_id", property="parentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="has_table", property="hasTable", jdbcType=JdbcType.CHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="has_response", property="hasResponse", jdbcType=JdbcType.CHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    WorkEffortAssocType selectByPrimaryKey(String workEffortAssocTypeId);

    @Select({
        "select",
        "work_effort_assoc_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, has_response, last_modified_by_user_login, ",
        "created_by_user_login",
        "from work_effort_assoc_type",
            "order by work_effort_assoc_type_id"
    })
    @ResultMap("workEffortAssocType")
    List<WorkEffortAssocType> selectAllOrderByPrimaryKey();

    @Update({
        "update work_effort_assoc_type",
        "set parent_type_id = #{parentTypeId,jdbcType=VARCHAR},",
          "has_table = #{hasTable,jdbcType=CHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "has_response = #{hasResponse,jdbcType=CHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where work_effort_assoc_type_id = #{workEffortAssocTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(WorkEffortAssocType row);
}