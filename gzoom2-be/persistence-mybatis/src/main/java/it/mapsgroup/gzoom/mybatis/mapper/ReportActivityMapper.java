package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;

import java.time.Instant;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ReportActivityMapper {

    @Delete({
        "delete from report_activity",
        "where activity_id = #{activityId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String activityId);


    @Insert({
        "insert into report_activity (activity_id, status, ",
        "resumed, report_data, ",
        "error, template_name, ",
        "report_name, report_locale, ",
        "object_info, mime_type_id, ",
        "completed_stamp, created_by_user_login, ",
        "last_modified_by_user_login, content_name, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{activityId,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR}, ",
        "#{resumed,jdbcType=CHAR}, #{reportData,jdbcType=VARCHAR}, ",
        "#{error,jdbcType=VARCHAR}, #{templateName,jdbcType=VARCHAR}, ",
        "#{reportName,jdbcType=VARCHAR}, #{reportLocale,jdbcType=VARCHAR}, ",
        "#{objectInfo,jdbcType=VARCHAR}, #{mimeTypeId,jdbcType=VARCHAR}, ",
        "#{completedStamp,jdbcType=TIMESTAMP}, #{createdByUserLogin,jdbcType=VARCHAR}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{contentName,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(ReportActivity row);


    @Select({
        "select",
        "activity_id, status, resumed, report_data, error, template_name, report_name, ",
        "report_locale, object_info, mime_type_id, completed_stamp, created_by_user_login, ",
        "last_modified_by_user_login, content_name, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from report_activity",
        "where activity_id = #{activityId,jdbcType=VARCHAR}"
    })
    @Results(id = "reportActivity", value = {
        @Result(column="activity_id", property="activityId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="resumed", property="resumed", jdbcType=JdbcType.CHAR),
        @Result(column="report_data", property="reportData", jdbcType=JdbcType.VARCHAR),
        @Result(column="error", property="error", jdbcType=JdbcType.VARCHAR),
        @Result(column="template_name", property="templateName", jdbcType=JdbcType.VARCHAR),
        @Result(column="report_name", property="reportName", jdbcType=JdbcType.VARCHAR),
        @Result(column="report_locale", property="reportLocale", jdbcType=JdbcType.VARCHAR),
        @Result(column="object_info", property="objectInfo", jdbcType=JdbcType.VARCHAR),
        @Result(column="mime_type_id", property="mimeTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="completed_stamp", property="completedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="content_name", property="contentName", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    ReportActivity selectByPrimaryKey(String activityId);


    @Select({
        "select",
        "activity_id, status, resumed, report_data, error, template_name, report_name, ",
        "report_locale, object_info, mime_type_id, completed_stamp, created_by_user_login, ",
        "last_modified_by_user_login, content_name, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from report_activity"
    })
    @ResultMap("reportActivity")
    List<ReportActivity> selectAll();

    @Select({
            "select",
            "activity_id, status, resumed, report_data, error, template_name, report_name, ",
            "report_locale, object_info, mime_type_id, completed_stamp, created_by_user_login, ",
            "last_modified_by_user_login, content_name, last_updated_stamp, last_updated_tx_stamp, ",
            "created_stamp, created_tx_stamp",
            "from report_activity",
            "where created_by_user_login = #{userLoginId,jdbcType=VARCHAR}",
            "order by created_stamp desc"
    })
    @ResultMap("reportActivity")
    List<ReportActivity> selectByCreatedByUserLoginOrderByCreatedStamp(@Param("userLoginId")String userLoginId);

    @ResultType(ReportActivity.class)
    List<ReportActivity> getActivitiesByStates(@Param("states") List<String> states);

    @Update({
        "update report_activity",
        "set status = #{status,jdbcType=VARCHAR},",
          "resumed = #{resumed,jdbcType=CHAR},",
          "report_data = #{reportData,jdbcType=VARCHAR},",
          "error = #{error,jdbcType=VARCHAR},",
          "template_name = #{templateName,jdbcType=VARCHAR},",
          "report_name = #{reportName,jdbcType=VARCHAR},",
          "report_locale = #{reportLocale,jdbcType=VARCHAR},",
          "object_info = #{objectInfo,jdbcType=VARCHAR},",
          "mime_type_id = #{mimeTypeId,jdbcType=VARCHAR},",
          "completed_stamp = #{completedStamp,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "content_name = #{contentName,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where activity_id = #{activityId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(ReportActivity row);

    @Update({
            "update report_activity",
            "set status = #{dest,jdbcType=VARCHAR},",
            "error = #{error,jdbcType=VARCHAR},",
            "object_info = #{objectInfo,jdbcType=VARCHAR},",
            "completed_stamp = #{completedStamp,jdbcType=TIMESTAMP}",
            "where activity_id = #{activityId,jdbcType=VARCHAR}",
            "and status = #{src,jdbcType=VARCHAR}"
    })
    int updateState(@Param("activityId") String activityId, @Param("src") String src, @Param("dest") String dest, @Param("error") String error, @Param("objectInfo") String objectInfo, @Param("completedStamp")Instant completedStamp);

    @Update({
            "update report_activity",
            "set status = #{dest,jdbcType=VARCHAR},",
            "resumed = #{resumed,jdbcType=VARCHAR}",
            "where status = #{src,jdbcType=VARCHAR}"
    })
    int resumeRunning(@Param("src") String src, @Param("dest") String dest, @Param("resumed") String resumed);

}