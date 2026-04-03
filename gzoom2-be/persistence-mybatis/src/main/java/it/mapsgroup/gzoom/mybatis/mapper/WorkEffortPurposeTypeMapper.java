package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeTypeEx;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WorkEffortPurposeTypeMapper {

    @Delete({
        "delete from work_effort_purpose_type",
        "where work_effort_purpose_type_id = #{workEffortPurposeTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String workEffortPurposeTypeId);


    @Insert({
        "insert into work_effort_purpose_type (work_effort_purpose_type_id, parent_type_id, ",
        "description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, work_effort_purpose_type_code, ",
        "purpose_type_enum_id, last_modified_by_user_login, ",
        "created_by_user_login, description_lang)",
        "values (#{workEffortPurposeTypeId,jdbcType=VARCHAR}, #{parentTypeId,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{workEffortPurposeTypeCode,jdbcType=VARCHAR}, ",
        "#{purposeTypeEnumId,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{descriptionLang,jdbcType=VARCHAR})"
    })
    int insert(WorkEffortPurposeType row);


    @Select({
        "select",
        "work_effort_purpose_type_id, parent_type_id, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, work_effort_purpose_type_code, ",
        "purpose_type_enum_id, last_modified_by_user_login, created_by_user_login, description_lang",
        "from work_effort_purpose_type",
        "where work_effort_purpose_type_id = #{workEffortPurposeTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffortPurposeType", value = {
        @Result(column="work_effort_purpose_type_id", property="workEffortPurposeTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_type_id", property="parentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="work_effort_purpose_type_code", property="workEffortPurposeTypeCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="purpose_type_enum_id", property="purposeTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR)
    })
    WorkEffortPurposeType selectByPrimaryKey(String workEffortPurposeTypeId);


    @Select({
        "select",
        "work_effort_purpose_type_id, parent_type_id, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, work_effort_purpose_type_code, ",
        "purpose_type_enum_id, last_modified_by_user_login, created_by_user_login, description_lang",
        "from work_effort_purpose_type",
            "order by work_effort_purpose_type_id"
    })
    @ResultMap("workEffortPurposeType")
    List<WorkEffortPurposeType> selectAllOrderByPrimaryKey();

    @Select({
            "select",
            "work_effort_purpose_type_id, parent_type_id, description, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp, work_effort_purpose_type_code, ",
            "purpose_type_enum_id, last_modified_by_user_login, created_by_user_login, description_lang",
            "from work_effort_purpose_type",
            "where purpose_type_enum_id = #{purposeTypeEnumId,jdbcType=VARCHAR}"
    })
    @ResultMap("workEffortPurposeType")
    List<WorkEffortPurposeType> selectByPurposeTypeEnumId(String purposeTypeEnumId);

    List<WorkEffortPurposeTypeEx> getPurposeTabType(String glAccountId, boolean exists);

    @Update({
        "update work_effort_purpose_type",
        "set parent_type_id = #{parentTypeId,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "work_effort_purpose_type_code = #{workEffortPurposeTypeCode,jdbcType=VARCHAR},",
          "purpose_type_enum_id = #{purposeTypeEnumId,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "description_lang = #{descriptionLang,jdbcType=VARCHAR}",
        "where work_effort_purpose_type_id = #{workEffortPurposeTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(WorkEffortPurposeType row);
}