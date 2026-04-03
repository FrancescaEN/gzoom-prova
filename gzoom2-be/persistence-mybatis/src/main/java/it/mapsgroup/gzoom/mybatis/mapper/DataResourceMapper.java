package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.DataResource;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DataResourceMapper {

    @Delete({
        "delete from data_resource",
        "where data_resource_id = #{dataResourceId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String dataResourceId);


    @Insert({
        "insert into data_resource (data_resource_id, data_resource_type_id, ",
        "data_template_type_id, data_category_id, ",
        "data_source_id, status_id, ",
        "data_resource_name, locale_string, ",
        "mime_type_id, character_set_id, ",
        "object_info, survey_id, ",
        "survey_response_id, related_detail_id, ",
        "is_public, created_date, ",
        "created_by_user_login, last_modified_date, ",
        "last_modified_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{dataResourceId,jdbcType=VARCHAR}, #{dataResourceTypeId,jdbcType=VARCHAR}, ",
        "#{dataTemplateTypeId,jdbcType=VARCHAR}, #{dataCategoryId,jdbcType=VARCHAR}, ",
        "#{dataSourceId,jdbcType=VARCHAR}, #{statusId,jdbcType=VARCHAR}, ",
        "#{dataResourceName,jdbcType=VARCHAR}, #{localeString,jdbcType=VARCHAR}, ",
        "#{mimeTypeId,jdbcType=VARCHAR}, #{characterSetId,jdbcType=VARCHAR}, ",
        "#{objectInfo,jdbcType=VARCHAR}, #{surveyId,jdbcType=VARCHAR}, ",
        "#{surveyResponseId,jdbcType=VARCHAR}, #{relatedDetailId,jdbcType=VARCHAR}, ",
        "#{isPublic,jdbcType=CHAR}, #{createdDate,jdbcType=TIMESTAMP}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{lastModifiedDate,jdbcType=TIMESTAMP}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(DataResource row);


    @Select({
        "select",
        "data_resource_id, data_resource_type_id, data_template_type_id, data_category_id, ",
        "data_source_id, status_id, data_resource_name, locale_string, mime_type_id, ",
        "character_set_id, object_info, survey_id, survey_response_id, related_detail_id, ",
        "is_public, created_date, created_by_user_login, last_modified_date, last_modified_by_user_login, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from data_resource",
        "where data_resource_id = #{dataResourceId,jdbcType=VARCHAR}"
    })
    @Results(id = "dataResource", value = {
        @Result(column="data_resource_id", property="dataResourceId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="data_resource_type_id", property="dataResourceTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_template_type_id", property="dataTemplateTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_category_id", property="dataCategoryId", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_source_id", property="dataSourceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_id", property="statusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_resource_name", property="dataResourceName", jdbcType=JdbcType.VARCHAR),
        @Result(column="locale_string", property="localeString", jdbcType=JdbcType.VARCHAR),
        @Result(column="mime_type_id", property="mimeTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="character_set_id", property="characterSetId", jdbcType=JdbcType.VARCHAR),
        @Result(column="object_info", property="objectInfo", jdbcType=JdbcType.VARCHAR),
        @Result(column="survey_id", property="surveyId", jdbcType=JdbcType.VARCHAR),
        @Result(column="survey_response_id", property="surveyResponseId", jdbcType=JdbcType.VARCHAR),
        @Result(column="related_detail_id", property="relatedDetailId", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_public", property="isPublic", jdbcType=JdbcType.CHAR),
        @Result(column="created_date", property="createdDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_date", property="lastModifiedDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    DataResource selectByPrimaryKey(String dataResourceId);


    @Select({
        "select",
        "data_resource_id, data_resource_type_id, data_template_type_id, data_category_id, ",
        "data_source_id, status_id, data_resource_name, locale_string, mime_type_id, ",
        "character_set_id, object_info, survey_id, survey_response_id, related_detail_id, ",
        "is_public, created_date, created_by_user_login, last_modified_date, last_modified_by_user_login, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from data_resource"
    })
    @ResultMap("dataResource")
    List<DataResource> selectAll();

    @Select({
            "select *",
            "from data_resource dr",
            "where dr.data_resource_id = #{dataResourceId}"
    })
    @ResultMap("dataResource")
    List<DataResource> getDataResourceByContentId(@Param("contentId") String dataResourceId);


    @Update({
        "update data_resource",
        "set data_resource_type_id = #{dataResourceTypeId,jdbcType=VARCHAR},",
          "data_template_type_id = #{dataTemplateTypeId,jdbcType=VARCHAR},",
          "data_category_id = #{dataCategoryId,jdbcType=VARCHAR},",
          "data_source_id = #{dataSourceId,jdbcType=VARCHAR},",
          "status_id = #{statusId,jdbcType=VARCHAR},",
          "data_resource_name = #{dataResourceName,jdbcType=VARCHAR},",
          "locale_string = #{localeString,jdbcType=VARCHAR},",
          "mime_type_id = #{mimeTypeId,jdbcType=VARCHAR},",
          "character_set_id = #{characterSetId,jdbcType=VARCHAR},",
          "object_info = #{objectInfo,jdbcType=VARCHAR},",
          "survey_id = #{surveyId,jdbcType=VARCHAR},",
          "survey_response_id = #{surveyResponseId,jdbcType=VARCHAR},",
          "related_detail_id = #{relatedDetailId,jdbcType=VARCHAR},",
          "is_public = #{isPublic,jdbcType=CHAR},",
          "last_modified_date = #{lastModifiedDate,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where data_resource_id = #{dataResourceId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(DataResource row);
}