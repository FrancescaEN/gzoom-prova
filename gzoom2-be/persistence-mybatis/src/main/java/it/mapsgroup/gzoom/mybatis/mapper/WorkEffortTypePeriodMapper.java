package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypePeriod;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WorkEffortTypePeriodMapper {

    @Delete({
        "delete from work_effort_type_period",
        "where work_effort_type_period_id = #{workEffortTypePeriodId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String workEffortTypePeriodId);


    @Insert({
        "insert into work_effort_type_period (work_effort_type_period_id, work_effort_type_id, ",
        "custom_time_period_id, gl_fiscal_type_enum_id, ",
        "status_type_id, per_lav_from, ",
        "per_lav_thru, des_proc, ",
        "status_enum_id, organization_id, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{workEffortTypePeriodId,jdbcType=VARCHAR}, #{workEffortTypeId,jdbcType=VARCHAR}, ",
        "#{customTimePeriodId,jdbcType=VARCHAR}, #{glFiscalTypeEnumId,jdbcType=VARCHAR}, ",
        "#{statusTypeId,jdbcType=VARCHAR}, #{perLavFrom,jdbcType=TIMESTAMP}, ",
        "#{perLavThru,jdbcType=TIMESTAMP}, #{desProc,jdbcType=VARCHAR}, ",
        "#{statusEnumId,jdbcType=VARCHAR}, #{organizationId,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(WorkEffortTypePeriod row);


    @Select({
        "select",
        "work_effort_type_period_id, work_effort_type_id, custom_time_period_id, gl_fiscal_type_enum_id, ",
        "status_type_id, per_lav_from, per_lav_thru, des_proc, status_enum_id, organization_id, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from work_effort_type_period",
        "where work_effort_type_period_id = #{workEffortTypePeriodId,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffortTypePeriodResultMap", value = {
        @Result(column="work_effort_type_period_id", property="workEffortTypePeriodId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="work_effort_type_id", property="workEffortTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_time_period_id", property="customTimePeriodId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_fiscal_type_enum_id", property="glFiscalTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_type_id", property="statusTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="per_lav_from", property="perLavFrom", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="per_lav_thru", property="perLavThru", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="des_proc", property="desProc", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_enum_id", property="statusEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="organization_id", property="organizationId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    WorkEffortTypePeriod selectByPrimaryKey(String workEffortTypePeriodId);


    @Select({
        "select",
        "work_effort_type_period_id, work_effort_type_id, custom_time_period_id, gl_fiscal_type_enum_id, ",
        "status_type_id, per_lav_from, per_lav_thru, des_proc, status_enum_id, organization_id, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from work_effort_type_period"
    })
    @ResultMap("workEffortTypePeriodResultMap")
    List<WorkEffortTypePeriod> selectAll();

    @Update({
        "update work_effort_type_period",
        "set work_effort_type_id = #{workEffortTypeId,jdbcType=VARCHAR},",
          "custom_time_period_id = #{customTimePeriodId,jdbcType=VARCHAR},",
          "gl_fiscal_type_enum_id = #{glFiscalTypeEnumId,jdbcType=VARCHAR},",
          "status_type_id = #{statusTypeId,jdbcType=VARCHAR},",
          "per_lav_from = #{perLavFrom,jdbcType=TIMESTAMP},",
          "per_lav_thru = #{perLavThru,jdbcType=TIMESTAMP},",
          "des_proc = #{desProc,jdbcType=VARCHAR},",
          "status_enum_id = #{statusEnumId,jdbcType=VARCHAR},",
          "organization_id = #{organizationId,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
        "where work_effort_type_period_id = #{workEffortTypePeriodId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(WorkEffortTypePeriod row);

    List<WorkEffortTypePeriod> getWorkEffortTypePeriodByWorkEffortTypeId(@Param("workEffortTypeId") String workEffortTypeId);
}