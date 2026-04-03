package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.StatusType;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StatusTypeMapper {

    @Delete({
        "delete from status_type",
        "where status_type_id = #{statusTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String statusTypeId);


    @Insert({
        "insert into status_type (status_type_id, parent_type_id, ",
        "has_table, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "is_reserved, portal_type_id, ",
        "last_modified_by_user_login, created_by_user_login)",
        "values (#{statusTypeId,jdbcType=VARCHAR}, #{parentTypeId,jdbcType=VARCHAR}, ",
        "#{hasTable,jdbcType=CHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{isReserved,jdbcType=CHAR}, #{portalTypeId,jdbcType=VARCHAR}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(StatusType row);


    @Select({
        "select",
        "status_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, is_reserved, portal_type_id, ",
        "last_modified_by_user_login, created_by_user_login",
        "from status_type",
        "where status_type_id = #{statusTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "statusType", value = {
            @Result(column="status_type_id", property="statusTypeId", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="parent_type_id", property="parentTypeId", jdbcType=JdbcType.VARCHAR),
            @Result(column="has_table", property="hasTable", jdbcType=JdbcType.CHAR),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="is_reserved", property="isReserved", jdbcType=JdbcType.CHAR),
            @Result(column="portal_type_id", property="portalTypeId", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
            @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    StatusType selectByPrimaryKey(String statusTypeId);

    @Select({
        "select",
        "status_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, is_reserved, portal_type_id, ",
        "last_modified_by_user_login, created_by_user_login",
        "from status_type",
            "order by status_type_id"
    })
    @ResultType(StatusType.class)
    List<StatusType> selectAllOrderByPrimaryKey();


    @Update({
        "update status_type",
        "set parent_type_id = #{parentTypeId,jdbcType=VARCHAR},",
          "has_table = #{hasTable,jdbcType=CHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "is_reserved = #{isReserved,jdbcType=CHAR},",
          "portal_type_id = #{portalTypeId,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where status_type_id = #{statusTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(StatusType row);
}