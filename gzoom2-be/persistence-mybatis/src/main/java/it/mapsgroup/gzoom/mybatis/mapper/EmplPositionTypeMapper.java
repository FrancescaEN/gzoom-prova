package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.EmplPositionType;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EmplPositionTypeMapper {

    @Delete({
        "delete from empl_position_type",
        "where empl_position_type_id = #{emplPositionTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String emplPositionTypeId);


    @Insert({
        "insert into empl_position_type (empl_position_type_id, parent_type_id, ",
        "has_table, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "template_id, last_modified_by_user_login, ",
        "created_by_user_login, description_lang)",
        "values (#{emplPositionTypeId,jdbcType=VARCHAR}, #{parentTypeId,jdbcType=VARCHAR}, ",
        "#{hasTable,jdbcType=CHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{templateId,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{descriptionLang,jdbcType=VARCHAR})"
    })
    int insert(EmplPositionType row);


    @Select({
        "select",
        "empl_position_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, template_id, last_modified_by_user_login, ",
        "created_by_user_login, description_lang",
        "from empl_position_type",
        "where empl_position_type_id = #{emplPositionTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "emplPositionType", value = {
        @Result(column="empl_position_type_id", property="emplPositionTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_type_id", property="parentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="has_table", property="hasTable", jdbcType=JdbcType.CHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="template_id", property="templateId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR)
    })
    EmplPositionType selectByPrimaryKey(String emplPositionTypeId);


    @Select({
        "select",
        "empl_position_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, template_id, last_modified_by_user_login, ",
        "created_by_user_login, description_lang",
        "from empl_position_type",
            "order by empl_position_type_id"
    })
    @ResultMap("emplPositionType")
    List<EmplPositionType> selectAllOrderByPrimaryKey();


    @Update({
        "update empl_position_type",
        "set parent_type_id = #{parentTypeId,jdbcType=VARCHAR},",
          "has_table = #{hasTable,jdbcType=CHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "template_id = #{templateId,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "description_lang = #{descriptionLang,jdbcType=VARCHAR}",
        "where empl_position_type_id = #{emplPositionTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(EmplPositionType row);
}