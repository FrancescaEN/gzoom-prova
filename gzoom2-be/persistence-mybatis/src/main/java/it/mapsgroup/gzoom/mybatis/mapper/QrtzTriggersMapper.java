package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.QrtzTriggers;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QrtzTriggersMapper {

    @Delete({
        "delete from qrtz_triggers",
        "where sched_name = #{schedName,jdbcType=VARCHAR}",
          "and trigger_name = #{triggerName,jdbcType=VARCHAR}",
          "and trigger_group = #{triggerGroup,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("schedName") String schedName, @Param("triggerName") String triggerName, @Param("triggerGroup") String triggerGroup);


    @Insert({
        "insert into qrtz_triggers (sched_name, trigger_name, ",
        "trigger_group, job_name, ",
        "job_group, description, ",
        "next_fire_time, prev_fire_time, ",
        "priority, trigger_state, ",
        "trigger_type, start_time, ",
        "end_time, calendar_name, ",
        "misfire_instr, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, job_data)",
        "values (#{schedName,jdbcType=VARCHAR}, #{triggerName,jdbcType=VARCHAR}, ",
        "#{triggerGroup,jdbcType=VARCHAR}, #{jobName,jdbcType=VARCHAR}, ",
        "#{jobGroup,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{nextFireTime,jdbcType=BIGINT}, #{prevFireTime,jdbcType=BIGINT}, ",
        "#{priority,jdbcType=INTEGER}, #{triggerState,jdbcType=VARCHAR}, ",
        "#{triggerType,jdbcType=VARCHAR}, #{startTime,jdbcType=BIGINT}, ",
        "#{endTime,jdbcType=BIGINT}, #{calendarName,jdbcType=VARCHAR}, ",
        "#{misfireInstr,jdbcType=SMALLINT}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{jobData,jdbcType=BINARY})"
    })
    int insert(QrtzTriggers row);


    @Select({
        "select",
        "sched_name, trigger_name, trigger_group, job_name, job_group, description, next_fire_time, ",
        "prev_fire_time, priority, trigger_state, trigger_type, start_time, end_time, ",
        "calendar_name, misfire_instr, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, job_data",
        "from qrtz_triggers",
        "where sched_name = #{schedName,jdbcType=VARCHAR}",
          "and trigger_name = #{triggerName,jdbcType=VARCHAR}",
          "and trigger_group = #{triggerGroup,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="sched_name", property="schedName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="trigger_name", property="triggerName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="trigger_group", property="triggerGroup", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="job_name", property="jobName", jdbcType=JdbcType.VARCHAR),
        @Result(column="job_group", property="jobGroup", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="next_fire_time", property="nextFireTime", jdbcType=JdbcType.BIGINT),
        @Result(column="prev_fire_time", property="prevFireTime", jdbcType=JdbcType.BIGINT),
        @Result(column="priority", property="priority", jdbcType=JdbcType.INTEGER),
        @Result(column="trigger_state", property="triggerState", jdbcType=JdbcType.VARCHAR),
        @Result(column="trigger_type", property="triggerType", jdbcType=JdbcType.VARCHAR),
        @Result(column="start_time", property="startTime", jdbcType=JdbcType.BIGINT),
        @Result(column="end_time", property="endTime", jdbcType=JdbcType.BIGINT),
        @Result(column="calendar_name", property="calendarName", jdbcType=JdbcType.VARCHAR),
        @Result(column="misfire_instr", property="misfireInstr", jdbcType=JdbcType.SMALLINT),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_data", property="jobData", jdbcType=JdbcType.BINARY)
    })
    QrtzTriggers selectByPrimaryKey(@Param("schedName") String schedName, @Param("triggerName") String triggerName, @Param("triggerGroup") String triggerGroup);


    @Select({
        "select",
        "sched_name, trigger_name, trigger_group, job_name, job_group, description, next_fire_time, ",
        "prev_fire_time, priority, trigger_state, trigger_type, start_time, end_time, ",
        "calendar_name, misfire_instr, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, job_data",
        "from qrtz_triggers"
    })
    @Results({
        @Result(column="sched_name", property="schedName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="trigger_name", property="triggerName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="trigger_group", property="triggerGroup", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="job_name", property="jobName", jdbcType=JdbcType.VARCHAR),
        @Result(column="job_group", property="jobGroup", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="next_fire_time", property="nextFireTime", jdbcType=JdbcType.BIGINT),
        @Result(column="prev_fire_time", property="prevFireTime", jdbcType=JdbcType.BIGINT),
        @Result(column="priority", property="priority", jdbcType=JdbcType.INTEGER),
        @Result(column="trigger_state", property="triggerState", jdbcType=JdbcType.VARCHAR),
        @Result(column="trigger_type", property="triggerType", jdbcType=JdbcType.VARCHAR),
        @Result(column="start_time", property="startTime", jdbcType=JdbcType.BIGINT),
        @Result(column="end_time", property="endTime", jdbcType=JdbcType.BIGINT),
        @Result(column="calendar_name", property="calendarName", jdbcType=JdbcType.VARCHAR),
        @Result(column="misfire_instr", property="misfireInstr", jdbcType=JdbcType.SMALLINT),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_data", property="jobData", jdbcType=JdbcType.BINARY)
    })
    List<QrtzTriggers> selectAll();

    @Select({
            "select",
            "sched_name, trigger_name, trigger_group, job_name, job_group, description, next_fire_time, ",
            "prev_fire_time, priority, trigger_state, trigger_type, start_time, end_time, ",
            "calendar_name, misfire_instr, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
            "created_tx_stamp, job_data",
            "from qrtz_triggers",
            "where job_name=#{jobName,jdbcType=VARCHAR}"
    })
    @ResultType(QrtzTriggers.class)
    List<QrtzTriggers> selectByJobName(String jobName);


    @Update({
        "update qrtz_triggers",
        "set job_name = #{jobName,jdbcType=VARCHAR},",
          "job_group = #{jobGroup,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "next_fire_time = #{nextFireTime,jdbcType=BIGINT},",
          "prev_fire_time = #{prevFireTime,jdbcType=BIGINT},",
          "priority = #{priority,jdbcType=INTEGER},",
          "trigger_state = #{triggerState,jdbcType=VARCHAR},",
          "trigger_type = #{triggerType,jdbcType=VARCHAR},",
          "start_time = #{startTime,jdbcType=BIGINT},",
          "end_time = #{endTime,jdbcType=BIGINT},",
          "calendar_name = #{calendarName,jdbcType=VARCHAR},",
          "misfire_instr = #{misfireInstr,jdbcType=SMALLINT},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "job_data = #{jobData,jdbcType=BINARY}",
        "where sched_name = #{schedName,jdbcType=VARCHAR}",
          "and trigger_name = #{triggerName,jdbcType=VARCHAR}",
          "and trigger_group = #{triggerGroup,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(QrtzTriggers row);
}