package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.QrtzJobDetails;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QrtzJobDetailsMapper {

    @Delete({
        "delete from qrtz_job_details",
        "where sched_name = #{schedName,jdbcType=VARCHAR}",
          "and job_name = #{jobName,jdbcType=VARCHAR}",
          "and job_group = #{jobGroup,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("schedName") String schedName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    @Insert({
        "insert into qrtz_job_details (sched_name, job_name, ",
        "job_group, description, ",
        "job_class_name, is_durable, ",
        "is_nonconcurrent, is_update_data, ",
        "requests_recovery, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, job_data)",
        "values (#{schedName,jdbcType=VARCHAR}, #{jobName,jdbcType=VARCHAR}, ",
        "#{jobGroup,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{jobClassName,jdbcType=VARCHAR}, #{isDurable,jdbcType=BIT}, ",
        "#{isNonconcurrent,jdbcType=BIT}, #{isUpdateData,jdbcType=BIT}, ",
        "#{requestsRecovery,jdbcType=BIT}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{jobData,jdbcType=BINARY})"
    })
    int insert(QrtzJobDetails row);


    @Select({
        "select",
        "sched_name, job_name, job_group, description, job_class_name, is_durable, is_nonconcurrent, ",
        "is_update_data, requests_recovery, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, job_data",
        "from qrtz_job_details",
        "where sched_name = #{schedName,jdbcType=VARCHAR}",
          "and job_name = #{jobName,jdbcType=VARCHAR}",
          "and job_group = #{jobGroup,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="sched_name", property="schedName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="job_name", property="jobName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="job_group", property="jobGroup", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="job_class_name", property="jobClassName", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_durable", property="isDurable", jdbcType=JdbcType.BIT),
        @Result(column="is_nonconcurrent", property="isNonconcurrent", jdbcType=JdbcType.BIT),
        @Result(column="is_update_data", property="isUpdateData", jdbcType=JdbcType.BIT),
        @Result(column="requests_recovery", property="requestsRecovery", jdbcType=JdbcType.BIT),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_data", property="jobData", jdbcType=JdbcType.BINARY)
    })
    QrtzJobDetails selectByPrimaryKey(@Param("schedName") String schedName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);


    @Select({
        "select",
        "sched_name, job_name, job_group, description, job_class_name, is_durable, is_nonconcurrent, ",
        "is_update_data, requests_recovery, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, job_data",
        "from qrtz_job_details",
            "where sched_name = #{schedName}",
            "order by job_name"
    })
    @Results({
        @Result(column="sched_name", property="schedName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="job_name", property="jobName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="job_group", property="jobGroup", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="job_class_name", property="jobClassName", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_durable", property="isDurable", jdbcType=JdbcType.BIT),
        @Result(column="is_nonconcurrent", property="isNonconcurrent", jdbcType=JdbcType.BIT),
        @Result(column="is_update_data", property="isUpdateData", jdbcType=JdbcType.BIT),
        @Result(column="requests_recovery", property="requestsRecovery", jdbcType=JdbcType.BIT),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_data", property="jobData", jdbcType=JdbcType.BINARY)
    })
    List<QrtzJobDetails> selectBySchedName(String schedName);

    @Select({
            "select *",
            "from qrtz_job_details",
            "where job_class_name=#{className,jdbcType=VARCHAR}"
    })
    List<QrtzJobDetails> selectByClassName(String className);


    @Update({
        "update qrtz_job_details",
        "set description = #{description,jdbcType=VARCHAR},",
          "job_class_name = #{jobClassName,jdbcType=VARCHAR},",
          "is_durable = #{isDurable,jdbcType=BIT},",
          "is_nonconcurrent = #{isNonconcurrent,jdbcType=BIT},",
          "is_update_data = #{isUpdateData,jdbcType=BIT},",
          "requests_recovery = #{requestsRecovery,jdbcType=BIT},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "job_data = #{jobData,jdbcType=BINARY}",
        "where sched_name = #{schedName,jdbcType=VARCHAR}",
          "and job_name = #{jobName,jdbcType=VARCHAR}",
          "and job_group = #{jobGroup,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(QrtzJobDetails row);

    @Update({
            "update qrtz_job_details",
            "set description = #{description,jdbcType=VARCHAR}",
            "where job_name = #{jobName,jdbcType=VARCHAR}"
    })
    int updateDescription(String jobName, String description);
}