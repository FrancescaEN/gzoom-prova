package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.RoleType;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleTypeMapper {

    @Delete({
        "delete from role_type",
        "where role_type_id = #{roleTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String roleTypeId);


    @Insert({
        "insert into role_type (role_type_id, parent_type_id, ",
        "has_table, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "short_label, icon_content_id, ",
        "prev_party_type_id, work_effort_type_id, ",
        "work_effort_assoc_type_id, work_effort_period_id, ",
        "description_lang, short_label_lang, ",
        "last_modified_by_user_login, created_by_user_login)",
        "values (#{roleTypeId,jdbcType=VARCHAR}, #{parentTypeId,jdbcType=VARCHAR}, ",
        "#{hasTable,jdbcType=CHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{shortLabel,jdbcType=VARCHAR}, #{iconContentId,jdbcType=VARCHAR}, ",
        "#{prevPartyTypeId,jdbcType=VARCHAR}, #{workEffortTypeId,jdbcType=VARCHAR}, ",
        "#{workEffortAssocTypeId,jdbcType=VARCHAR}, #{workEffortPeriodId,jdbcType=VARCHAR}, ",
        "#{descriptionLang,jdbcType=VARCHAR}, #{shortLabelLang,jdbcType=VARCHAR}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(RoleType row);


    @Select({
        "select",
        "role_type_id, parent_type_id, has_table, description, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, short_label, icon_content_id, prev_party_type_id, ",
        "work_effort_type_id, work_effort_assoc_type_id, work_effort_period_id, description_lang, ",
        "short_label_lang, last_modified_by_user_login, created_by_user_login",
        "from role_type",
        "where role_type_id = #{roleTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "roleType", value = {
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_type_id", property="parentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="has_table", property="hasTable", jdbcType=JdbcType.CHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="short_label", property="shortLabel", jdbcType=JdbcType.VARCHAR),
        @Result(column="icon_content_id", property="iconContentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="prev_party_type_id", property="prevPartyTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_type_id", property="workEffortTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_assoc_type_id", property="workEffortAssocTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_period_id", property="workEffortPeriodId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="short_label_lang", property="shortLabelLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    RoleType selectByPrimaryKey(String roleTypeId);


    @Select({
        "select",
        "role_type_id, parent_type_id, has_table, description, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, short_label, icon_content_id, prev_party_type_id, ",
        "work_effort_type_id, work_effort_assoc_type_id, work_effort_period_id, description_lang, ",
        "short_label_lang, last_modified_by_user_login, created_by_user_login",
        "from role_type",
            "order by role_type_id"
    })
    @ResultMap("roleType")
    List<RoleType> selectAllOrderByPrimaryKey();

    @Select({
            "select *",
            "from role_type",
            "where parent_type_id = #{parentTypeId,jdbcType=VARCHAR}"
    })
    @ResultType(RoleType.class)
    List<RoleType> selectByParentTypeId(String parentTypeId);


    @Select({
            "select *",
            "from role_type",
            "where parent_type_id = 'ORGANIZATION_UNIT' or parent_type_id like 'GOAL%'"
    })
    @ResultType(RoleType.class)
    List<RoleType> getRoleTypeByOUAndLikeGOAL();

    @Update({
        "update role_type",
        "set parent_type_id = #{parentTypeId,jdbcType=VARCHAR},",
          "has_table = #{hasTable,jdbcType=CHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "short_label = #{shortLabel,jdbcType=VARCHAR},",
          "icon_content_id = #{iconContentId,jdbcType=VARCHAR},",
          "prev_party_type_id = #{prevPartyTypeId,jdbcType=VARCHAR},",
          "work_effort_type_id = #{workEffortTypeId,jdbcType=VARCHAR},",
          "work_effort_assoc_type_id = #{workEffortAssocTypeId,jdbcType=VARCHAR},",
          "work_effort_period_id = #{workEffortPeriodId,jdbcType=VARCHAR},",
          "description_lang = #{descriptionLang,jdbcType=VARCHAR},",
          "short_label_lang = #{shortLabelLang,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where role_type_id = #{roleTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(RoleType row);
}