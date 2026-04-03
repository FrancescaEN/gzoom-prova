package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.Content;
import java.util.List;

import it.mapsgroup.gzoom.mybatis.dto.ContentExDataResource;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ContentMapper {

    @Delete({
        "delete from content",
        "where content_id = #{contentId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String contentId);


    @Insert({
        "insert into content (content_id, content_type_id, ",
        "owner_content_id, decorator_content_id, ",
        "instance_of_content_id, data_resource_id, ",
        "template_data_resource_id, data_source_id, ",
        "status_id, privilege_enum_id, ",
        "service_name, content_name, ",
        "description, locale_string, ",
        "mime_type_id, character_set_id, ",
        "child_leaf_count, child_branch_count, ",
        "created_date, created_by_user_login, ",
        "last_modified_date, last_modified_by_user_login, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "description_lang)",
        "values (#{contentId,jdbcType=VARCHAR}, #{contentTypeId,jdbcType=VARCHAR}, ",
        "#{ownerContentId,jdbcType=VARCHAR}, #{decoratorContentId,jdbcType=VARCHAR}, ",
        "#{instanceOfContentId,jdbcType=VARCHAR}, #{dataResourceId,jdbcType=VARCHAR}, ",
        "#{templateDataResourceId,jdbcType=VARCHAR}, #{dataSourceId,jdbcType=VARCHAR}, ",
        "#{statusId,jdbcType=VARCHAR}, #{privilegeEnumId,jdbcType=VARCHAR}, ",
        "#{serviceName,jdbcType=VARCHAR}, #{contentName,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{localeString,jdbcType=VARCHAR}, ",
        "#{mimeTypeId,jdbcType=VARCHAR}, #{characterSetId,jdbcType=VARCHAR}, ",
        "#{childLeafCount,jdbcType=NUMERIC}, #{childBranchCount,jdbcType=NUMERIC}, ",
        "#{createdDate,jdbcType=TIMESTAMP}, #{createdByUserLogin,jdbcType=VARCHAR}, ",
        "#{lastModifiedDate,jdbcType=TIMESTAMP}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{descriptionLang,jdbcType=VARCHAR})"
    })
    int insert(Content row);


    @Select({
        "select",
        "content_id, content_type_id, owner_content_id, decorator_content_id, instance_of_content_id, ",
        "data_resource_id, template_data_resource_id, data_source_id, status_id, privilege_enum_id, ",
        "service_name, content_name, description, locale_string, mime_type_id, character_set_id, ",
        "child_leaf_count, child_branch_count, created_date, created_by_user_login, last_modified_date, ",
        "last_modified_by_user_login, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, description_lang",
        "from content",
        "where content_id = #{contentId,jdbcType=VARCHAR}"
    })
    @Results(id = "content", value = {
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="content_type_id", property="contentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="owner_content_id", property="ownerContentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="decorator_content_id", property="decoratorContentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="instance_of_content_id", property="instanceOfContentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_resource_id", property="dataResourceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="template_data_resource_id", property="templateDataResourceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_source_id", property="dataSourceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_id", property="statusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="privilege_enum_id", property="privilegeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="service_name", property="serviceName", jdbcType=JdbcType.VARCHAR),
        @Result(column="content_name", property="contentName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="locale_string", property="localeString", jdbcType=JdbcType.VARCHAR),
        @Result(column="mime_type_id", property="mimeTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="character_set_id", property="characterSetId", jdbcType=JdbcType.VARCHAR),
        @Result(column="child_leaf_count", property="childLeafCount", jdbcType=JdbcType.NUMERIC),
        @Result(column="child_branch_count", property="childBranchCount", jdbcType=JdbcType.NUMERIC),
        @Result(column="created_date", property="createdDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_date", property="lastModifiedDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR)
    })
    Content selectByPrimaryKey(String contentId);


    @Select({
        "select",
        "content_id, content_type_id, owner_content_id, decorator_content_id, instance_of_content_id, ",
        "data_resource_id, template_data_resource_id, data_source_id, status_id, privilege_enum_id, ",
        "service_name, content_name, description, locale_string, mime_type_id, character_set_id, ",
        "child_leaf_count, child_branch_count, created_date, created_by_user_login, last_modified_date, ",
        "last_modified_by_user_login, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, description_lang",
        "from content"
    })
    @ResultMap("content")
    List<Content> selectAll();

    @Select({
            "select * ",
            "from content",
            "where service_name = #{activityId,jdbcType=VARCHAR}"
    })
    @ResultMap("content")
    List<Content> selectByServiceName(@Param("activityId") String activityId);

    @Select({
            "select * ",
            "from content",
            "where content_type_id = #{contentTypeId,jdbcType=VARCHAR}"
    })
    @ResultType(Content.class)
    List<Content> selectByContentTypeId(String contentTypeId);

    List<ContentExDataResource> getContentListByContentTypeId(@Param("contentTypeId") String contentTypeId);


    @Update({
        "update content",
        "set content_type_id = #{contentTypeId,jdbcType=VARCHAR},",
          "owner_content_id = #{ownerContentId,jdbcType=VARCHAR},",
          "decorator_content_id = #{decoratorContentId,jdbcType=VARCHAR},",
          "instance_of_content_id = #{instanceOfContentId,jdbcType=VARCHAR},",
          "data_resource_id = #{dataResourceId,jdbcType=VARCHAR},",
          "template_data_resource_id = #{templateDataResourceId,jdbcType=VARCHAR},",
          "data_source_id = #{dataSourceId,jdbcType=VARCHAR},",
          "status_id = #{statusId,jdbcType=VARCHAR},",
          "privilege_enum_id = #{privilegeEnumId,jdbcType=VARCHAR},",
          "service_name = #{serviceName,jdbcType=VARCHAR},",
          "content_name = #{contentName,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "locale_string = #{localeString,jdbcType=VARCHAR},",
          "mime_type_id = #{mimeTypeId,jdbcType=VARCHAR},",
          "character_set_id = #{characterSetId,jdbcType=VARCHAR},",
          "child_leaf_count = #{childLeafCount,jdbcType=NUMERIC},",
          "child_branch_count = #{childBranchCount,jdbcType=NUMERIC},",
          "last_modified_date = #{lastModifiedDate,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "description_lang = #{descriptionLang,jdbcType=VARCHAR}",
        "where content_id = #{contentId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(Content row);
    @Update({
            "update content",
            "set description = #{description,jdbcType=VARCHAR},",
            "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
            "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
            "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
            "where content_id = #{contentId,jdbcType=VARCHAR}"
    })
    int updateDescription(Content row);
}