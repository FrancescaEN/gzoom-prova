package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.StatusItem;
import it.mapsgroup.gzoom.mybatis.dto.StatusItemExType;
import it.mapsgroup.gzoom.mybatis.dto.StatusItemExt;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StatusItemMapper {

    @Delete({
        "delete from status_item",
        "where status_id = #{statusId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String statusId);

    @Delete({
            "delete from status_item",
            "where status_type_id = #{statusTypeId,jdbcType=VARCHAR}"
    })
    int deleteByStatusTypeId(String statusTypeId);


    @Insert({
        "insert into status_item (status_id, status_type_id, ",
        "status_code, sequence_id, ",
        "description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, act_st_enum_id, ",
        "description_lang, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{statusId,jdbcType=VARCHAR}, #{statusTypeId,jdbcType=VARCHAR}, ",
        "#{statusCode,jdbcType=VARCHAR}, #{sequenceId,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{actStEnumId,jdbcType=VARCHAR}, ",
        "#{descriptionLang,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(StatusItem row);


    @Select({
        "select",
        "status_id, status_type_id, status_code, sequence_id, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, act_st_enum_id, description_lang, ",
        "last_modified_by_user_login, created_by_user_login",
        "from status_item",
        "where status_id = #{statusId,jdbcType=VARCHAR}"
    })
    @Results( id  = "statusItemResultMap" , value = {
        @Result(column="status_id", property="statusId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="status_type_id", property="statusTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_code", property="statusCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="sequence_id", property="sequenceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="act_st_enum_id", property="actStEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    StatusItem selectByPrimaryKey(String statusId);


    @Select({
        "select",
        "status_id, status_type_id, status_code, sequence_id, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, act_st_enum_id, description_lang, ",
        "last_modified_by_user_login, created_by_user_login",
        "from status_item"
    })
    @Results({
        @Result(column="status_id", property="statusId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="status_type_id", property="statusTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_code", property="statusCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="sequence_id", property="sequenceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="act_st_enum_id", property="actStEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    List<StatusItem> selectAll();

    @Select({
            "select *",
            "from status_item",
            "where status_type_id = #{statusTypeId}",
            "order by sequence_id"
    })
    @ResultType(StatusItem.class)
    List<StatusItem> findByStatusTypeId(String statusTypeId);

    @Update({
        "update status_item",
        "set status_type_id = #{statusTypeId,jdbcType=VARCHAR},",
          "status_code = #{statusCode,jdbcType=VARCHAR},",
          "sequence_id = #{sequenceId,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "act_st_enum_id = #{actStEnumId,jdbcType=VARCHAR},",
          "description_lang = #{descriptionLang,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where status_id = #{statusId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(StatusItem row);

    List<StatusItemExType> getStatusItemStateTo();

    List<StatusItemExt> getStatusItems(@Param("userLoginId")  String userLoginId);

    List<StatusItem> getTimesheetStatusDropdownFilter(@Param("userLoginId")  String userLoginId);

    List<StatusItemExType> getStatusItemStateFrom(String statusTypeId);

    @Select({
            "select * from status_item where status_code = #{statusCode,jdbcType=VARCHAR}"
    })
    @ResultType(StatusItem.class)
    List<StatusItem> getStatusItemByCode(String statusCode);
}