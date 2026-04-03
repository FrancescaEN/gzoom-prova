package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.mybatis.dto.JobLog;
import it.mapsgroup.gzoom.mybatis.dto.JobLogEx;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface JobLogMapper {

    @Insert({
        "insert into job_log (job_log_id, job_id, ",
        "user_login_id, service_type_id, ",
        "service_name, description, ",
        "log_date, log_end_date, ",
        "record_elaborated, warning_messages, ",
        "blocking_errors, elab_ref1, ",
        "session_id, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{jobLogId,jdbcType=VARCHAR}, #{jobId,jdbcType=VARCHAR}, ",
        "#{userLoginId,jdbcType=VARCHAR}, #{serviceTypeId,jdbcType=VARCHAR}, ",
        "#{serviceName,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{logDate,jdbcType=TIMESTAMP}, #{logEndDate,jdbcType=TIMESTAMP}, ",
        "#{recordElaborated,jdbcType=NUMERIC}, #{warningMessages,jdbcType=NUMERIC}, ",
        "#{blockingErrors,jdbcType=NUMERIC}, #{elabRef1,jdbcType=VARCHAR}, ",
        "#{sessionId,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(JobLog row);


    @Select({
        "select",
        "job_log_id, job_id, user_login_id, service_type_id, service_name, description, ",
        "log_date, log_end_date, record_elaborated, warning_messages, blocking_errors, ",
        "elab_ref1, session_id, last_modified_by_user_login, created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from job_log",
        "where job_log_id = #{jobLogId,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="job_log_id", property="jobLogId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_login_id", property="userLoginId", jdbcType=JdbcType.VARCHAR),
        @Result(column="service_type_id", property="serviceTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="service_name", property="serviceName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="log_date", property="logDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="log_end_date", property="logEndDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="record_elaborated", property="recordElaborated", jdbcType=JdbcType.NUMERIC),
        @Result(column="warning_messages", property="warningMessages", jdbcType=JdbcType.NUMERIC),
        @Result(column="blocking_errors", property="blockingErrors", jdbcType=JdbcType.NUMERIC),
        @Result(column="elab_ref1", property="elabRef1", jdbcType=JdbcType.VARCHAR),
        @Result(column="session_id", property="sessionId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    JobLog selectByPrimaryKey(String jobLogId);


    @Select({
        "select",
        "job_log_id, job_id, user_login_id, service_type_id, service_name, description, ",
        "log_date, log_end_date, record_elaborated, warning_messages, blocking_errors, ",
        "elab_ref1, session_id, last_modified_by_user_login, created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from job_log"
    })
    @Results({
        @Result(column="job_log_id", property="jobLogId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_login_id", property="userLoginId", jdbcType=JdbcType.VARCHAR),
        @Result(column="service_type_id", property="serviceTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="service_name", property="serviceName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="log_date", property="logDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="log_end_date", property="logEndDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="record_elaborated", property="recordElaborated", jdbcType=JdbcType.NUMERIC),
        @Result(column="warning_messages", property="warningMessages", jdbcType=JdbcType.NUMERIC),
        @Result(column="blocking_errors", property="blockingErrors", jdbcType=JdbcType.NUMERIC),
        @Result(column="elab_ref1", property="elabRef1", jdbcType=JdbcType.VARCHAR),
        @Result(column="session_id", property="sessionId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    List<JobLog> selectAll();


    @Update({
        "update job_log",
        "set job_id = #{jobId,jdbcType=VARCHAR},",
          "user_login_id = #{userLoginId,jdbcType=VARCHAR},",
          "service_type_id = #{serviceTypeId,jdbcType=VARCHAR},",
          "service_name = #{serviceName,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "log_date = #{logDate,jdbcType=TIMESTAMP},",
          "log_end_date = #{logEndDate,jdbcType=TIMESTAMP},",
          "record_elaborated = #{recordElaborated,jdbcType=NUMERIC},",
          "warning_messages = #{warningMessages,jdbcType=NUMERIC},",
          "blocking_errors = #{blockingErrors,jdbcType=NUMERIC},",
          "elab_ref1 = #{elabRef1,jdbcType=VARCHAR},",
          "session_id = #{sessionId,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where job_log_id = #{jobLogId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(JobLog row);

    List<JobLogEx> getJobLogEx(@Param("LIMIT") Integer limit, @Param("OFFSET") Integer offset, @Param("filters") Filter[] filters, @Param("SORTORDER") Integer sortOrder, @Param("SORTFIELD") String sortField, @Param("matchModeSearch") String matchModeSearch, @Param("SECONDARYLANGUAGE") Boolean secondaryLanguage);
}