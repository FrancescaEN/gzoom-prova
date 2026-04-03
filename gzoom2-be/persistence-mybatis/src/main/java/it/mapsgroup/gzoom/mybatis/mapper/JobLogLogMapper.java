package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.JobLogLog;
import it.mapsgroup.gzoom.mybatis.dto.JobLogLogEx;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface JobLogLogMapper {

    @Insert({
        "insert into job_log_log (job_log_log_id, job_log_id, ",
        "log_type_enum_id, log_code, ",
        "log_message, value_ref1, ",
        "value_ref2, value_ref3, ",
        "log_message_long, value_pk1, ",
        "last_modified_by_user_login, created_by_user_login, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{jobLogLogId,jdbcType=VARCHAR}, #{jobLogId,jdbcType=VARCHAR}, ",
        "#{logTypeEnumId,jdbcType=VARCHAR}, #{logCode,jdbcType=VARCHAR}, ",
        "#{logMessage,jdbcType=VARCHAR}, #{valueRef1,jdbcType=VARCHAR}, ",
        "#{valueRef2,jdbcType=VARCHAR}, #{valueRef3,jdbcType=VARCHAR}, ",
        "#{logMessageLong,jdbcType=VARCHAR}, #{valuePk1,jdbcType=VARCHAR}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(JobLogLog row);


    @Select({
        "select",
        "job_log_log_id, job_log_id, log_type_enum_id, log_code, log_message, value_ref1, ",
        "value_ref2, value_ref3, log_message_long, value_pk1, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp",
        "from job_log_log",
        "where job_log_log_id = #{jobLogLogId,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="job_log_log_id", property="jobLogLogId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="job_log_id", property="jobLogId", jdbcType=JdbcType.VARCHAR),
        @Result(column="log_type_enum_id", property="logTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="log_code", property="logCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="log_message", property="logMessage", jdbcType=JdbcType.VARCHAR),
        @Result(column="value_ref1", property="valueRef1", jdbcType=JdbcType.VARCHAR),
        @Result(column="value_ref2", property="valueRef2", jdbcType=JdbcType.VARCHAR),
        @Result(column="value_ref3", property="valueRef3", jdbcType=JdbcType.VARCHAR),
        @Result(column="log_message_long", property="logMessageLong", jdbcType=JdbcType.VARCHAR),
        @Result(column="value_pk1", property="valuePk1", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    JobLogLog selectByPrimaryKey(String jobLogLogId);


    @Select({
        "select",
        "job_log_log_id, job_log_id, log_type_enum_id, log_code, log_message, value_ref1, ",
        "value_ref2, value_ref3, log_message_long, value_pk1, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp",
        "from job_log_log"
    })
    @Results({
        @Result(column="job_log_log_id", property="jobLogLogId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="job_log_id", property="jobLogId", jdbcType=JdbcType.VARCHAR),
        @Result(column="log_type_enum_id", property="logTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="log_code", property="logCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="log_message", property="logMessage", jdbcType=JdbcType.VARCHAR),
        @Result(column="value_ref1", property="valueRef1", jdbcType=JdbcType.VARCHAR),
        @Result(column="value_ref2", property="valueRef2", jdbcType=JdbcType.VARCHAR),
        @Result(column="value_ref3", property="valueRef3", jdbcType=JdbcType.VARCHAR),
        @Result(column="log_message_long", property="logMessageLong", jdbcType=JdbcType.VARCHAR),
        @Result(column="value_pk1", property="valuePk1", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    List<JobLogLog> selectAll();


    @Update({
        "update job_log_log",
        "set job_log_id = #{jobLogId,jdbcType=VARCHAR},",
          "log_type_enum_id = #{logTypeEnumId,jdbcType=VARCHAR},",
          "log_code = #{logCode,jdbcType=VARCHAR},",
          "log_message = #{logMessage,jdbcType=VARCHAR},",
          "value_ref1 = #{valueRef1,jdbcType=VARCHAR},",
          "value_ref2 = #{valueRef2,jdbcType=VARCHAR},",
          "value_ref3 = #{valueRef3,jdbcType=VARCHAR},",
          "log_message_long = #{logMessageLong,jdbcType=VARCHAR},",
          "value_pk1 = #{valuePk1,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where job_log_log_id = #{jobLogLogId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(JobLogLog row);

    List<JobLogLogEx> getJobLogLogEx(@Param("jobLogId") String jobLogId);
}