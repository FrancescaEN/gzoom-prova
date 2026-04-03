package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeContent;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeExt;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WorkEffortTypeContentMapper {

    @Delete({
        "delete from work_effort_type_content",
        "where work_effort_type_id = #{workEffortTypeId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("workEffortTypeId") String workEffortTypeId, @Param("contentId") String contentId);


    @Insert({
        "insert into work_effort_type_content (work_effort_type_id, content_id, ",
        "we_type_content_type_id, sequence_num, ",
        "etch, work_effort_purpose_type_id, ",
        "params, is_visible, ",
        "use_filter, etch_lang, ",
        "only_admin, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, is_mandatory)",
        "values (#{workEffortTypeId,jdbcType=VARCHAR}, #{contentId,jdbcType=VARCHAR}, ",
        "#{weTypeContentTypeId,jdbcType=VARCHAR}, #{sequenceNum,jdbcType=NUMERIC}, ",
        "#{etch,jdbcType=VARCHAR}, #{workEffortPurposeTypeId,jdbcType=VARCHAR}, ",
        "#{params,jdbcType=VARCHAR}, #{isVisible,jdbcType=CHAR}, ",
        "#{useFilter,jdbcType=CHAR}, #{etchLang,jdbcType=VARCHAR}, ",
        "#{onlyAdmin,jdbcType=CHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{isMandatory,jdbcType=CHAR})"
    })
    int insert(WorkEffortTypeContent row);


    @Select({
        "select",
        "work_effort_type_id, content_id, we_type_content_type_id, sequence_num, etch, ",
        "work_effort_purpose_type_id, params, is_visible, use_filter, etch_lang, only_admin, ",
        "last_modified_by_user_login, created_by_user_login, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, is_mandatory",
        "from work_effort_type_content",
        "where work_effort_type_id = #{workEffortTypeId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffortTypeContent", value = {
        @Result(column="work_effort_type_id", property="workEffortTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="we_type_content_type_id", property="weTypeContentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="sequence_num", property="sequenceNum", jdbcType=JdbcType.NUMERIC),
        @Result(column="etch", property="etch", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_purpose_type_id", property="workEffortPurposeTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="params", property="params", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_visible", property="isVisible", jdbcType=JdbcType.CHAR),
        @Result(column="use_filter", property="useFilter", jdbcType=JdbcType.CHAR),
        @Result(column="etch_lang", property="etchLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="only_admin", property="onlyAdmin", jdbcType=JdbcType.CHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="is_mandatory", property="isMandatory", jdbcType=JdbcType.CHAR)
    })
    WorkEffortTypeContent selectByPrimaryKey(@Param("workEffortTypeId") String workEffortTypeId, @Param("contentId") String contentId);


    @Select({
        "select",
        "work_effort_type_id, content_id, we_type_content_type_id, sequence_num, etch, ",
        "work_effort_purpose_type_id, params, is_visible, use_filter, etch_lang, only_admin, ",
        "last_modified_by_user_login, created_by_user_login, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, is_mandatory",
        "from work_effort_type_content"
    })
    @ResultMap("workEffortTypeContent")
    List<WorkEffortTypeContent> selectAll();

    List<WorkEffortTypeExt> getWorkEffortTypeContents(@Param("parentTypeId") String parentTypeId, @Param("reportContentId") String reportContentId, @Param("workEffortTypeId") String workEffortTypeId);


    @Update({
        "update work_effort_type_content",
        "set we_type_content_type_id = #{weTypeContentTypeId,jdbcType=VARCHAR},",
          "sequence_num = #{sequenceNum,jdbcType=NUMERIC},",
          "etch = #{etch,jdbcType=VARCHAR},",
          "work_effort_purpose_type_id = #{workEffortPurposeTypeId,jdbcType=VARCHAR},",
          "params = #{params,jdbcType=VARCHAR},",
          "is_visible = #{isVisible,jdbcType=CHAR},",
          "use_filter = #{useFilter,jdbcType=CHAR},",
          "etch_lang = #{etchLang,jdbcType=VARCHAR},",
          "only_admin = #{onlyAdmin,jdbcType=CHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "is_mandatory = #{isMandatory,jdbcType=CHAR}",
        "where work_effort_type_id = #{workEffortTypeId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(WorkEffortTypeContent row);
}