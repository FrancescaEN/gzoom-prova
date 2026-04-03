package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortContentType;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WorkEffortContentTypeMapper {

    @Delete({
        "delete from work_effort_content_type",
        "where work_effort_content_type_id = #{workEffortContentTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String workEffortContentTypeId);


    @Insert({
        "insert into work_effort_content_type (work_effort_content_type_id, parent_type_id, ",
        "description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, content_type_id, ",
        "in_inquiry, description_lang, ",
        "last_modified_by_user_login, created_by_user_login)",
        "values (#{workEffortContentTypeId,jdbcType=VARCHAR}, #{parentTypeId,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{contentTypeId,jdbcType=VARCHAR}, ",
        "#{inInquiry,jdbcType=CHAR}, #{descriptionLang,jdbcType=VARCHAR}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(WorkEffortContentType row);


    @Select({
        "select",
        "work_effort_content_type_id, parent_type_id, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, content_type_id, in_inquiry, ",
        "description_lang, last_modified_by_user_login, created_by_user_login",
        "from work_effort_content_type",
        "where work_effort_content_type_id = #{workEffortContentTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffortContentType", value = {
        @Result(column="work_effort_content_type_id", property="workEffortContentTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_type_id", property="parentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="content_type_id", property="contentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="in_inquiry", property="inInquiry", jdbcType=JdbcType.CHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    WorkEffortContentType selectByPrimaryKey(String workEffortContentTypeId);


    @Select({
        "select",
        "work_effort_content_type_id, parent_type_id, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, content_type_id, in_inquiry, ",
        "description_lang, last_modified_by_user_login, created_by_user_login",
        "from work_effort_content_type",
            "order by work_effort_content_type_id"
    })
    @ResultMap("workEffortContentType")
    List<WorkEffortContentType> selectAllOrderByPrimaryKey();

    @Select({
            "select wect.* ",
            "from work_effort_content_type wect",
            "inner join work_effort_type_content_type wetct on wetct.work_effort_content_type_id = wect.work_effort_content_type_id",
            "inner join work_effort we on we.work_effort_type_id = wetct.work_effort_type_id",
            "where we.work_effort_id = #{workEffortId,jdbcType=VARCHAR}"
    })
    @ResultMap("workEffortContentType")
    List<WorkEffortContentType> getContentTypeList(@Param("workEffortId") String workEffortId);

    @Select({
            "select wect.* ",
            "from work_effort_content_type wect",
            "inner join work_effort_type_content_type wetct on wetct.work_effort_content_type_id = wect.work_effort_content_type_id",
            "inner join work_effort we on we.work_effort_type_id = wetct.work_effort_type_id",
            "where we.work_effort_id = #{workEffortId,jdbcType=VARCHAR}",
            "and wect.work_effort_content_type_id = #{workEffortContentTypeId,jdbcType=VARCHAR}"
    })
    @ResultMap("workEffortContentType")
    WorkEffortContentType getContentTypeId(@Param("workEffortId") String workEffortId, @Param("workEffortContentTypeId") String workEffortContentTypeId);


    @Update({
        "update work_effort_content_type",
        "set parent_type_id = #{parentTypeId,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "content_type_id = #{contentTypeId,jdbcType=VARCHAR},",
          "in_inquiry = #{inInquiry,jdbcType=CHAR},",
          "description_lang = #{descriptionLang,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where work_effort_content_type_id = #{workEffortContentTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(WorkEffortContentType row);
}