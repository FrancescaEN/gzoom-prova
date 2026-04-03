package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortContent;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortContentEx;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Mapper
@Repository
public interface WorkEffortContentMapper {

    @Delete({
        "delete from work_effort_content",
        "where work_effort_id = #{workEffortId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}",
          "and work_effort_content_type_id = #{workEffortContentTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int deleteByPrimaryKey(@Param("workEffortId") String workEffortId, @Param("contentId") String contentId, @Param("workEffortContentTypeId") String workEffortContentTypeId, @Param("fromDate") Instant fromDate);


    @Insert({
        "insert into work_effort_content (work_effort_id, content_id, ",
        "work_effort_content_type_id, from_date, ",
        "thru_date, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{workEffortId,jdbcType=VARCHAR}, #{contentId,jdbcType=VARCHAR}, ",
        "#{workEffortContentTypeId,jdbcType=VARCHAR}, #{fromDate,jdbcType=TIMESTAMP}, ",
        "#{thruDate,jdbcType=TIMESTAMP}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(WorkEffortContent row);


    @Select({
        "select",
        "work_effort_id, content_id, work_effort_content_type_id, from_date, thru_date, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp, ",
        "last_modified_by_user_login, created_by_user_login",
        "from work_effort_content",
        "where work_effort_id = #{workEffortId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}",
          "and work_effort_content_type_id = #{workEffortContentTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    @Results(id = "workEffortContent", value = {
        @Result(column="work_effort_id", property="workEffortId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="work_effort_content_type_id", property="workEffortContentTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    WorkEffortContent selectByPrimaryKey(@Param("workEffortId") String workEffortId, @Param("contentId") String contentId, @Param("workEffortContentTypeId") String workEffortContentTypeId, @Param("fromDate") Instant fromDate);


    @Select({
        "select",
        "work_effort_id, content_id, work_effort_content_type_id, from_date, thru_date, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp, ",
        "last_modified_by_user_login, created_by_user_login",
        "from work_effort_content"
    })
    @ResultMap("workEffortContent")
    List<WorkEffortContent> selectAll();

    @Select({
            "select * ",
            "from work_effort_content wec",
            "where wec.work_effort_id = #{workEffortId}"
    })
    @ResultMap("workEffortContent")
    List<WorkEffortContent> getWorkEffortContentByWorkEffortId(@Param("workEffortId") String workEffortId);

    WorkEffortContentEx getWorkEffortContentEx(@Param("dataResourceId") String dataResourceId);

    List<WorkEffortContentEx> getWorkEffortContentExList();

    List<WorkEffortContentEx> getWorkEffortContentExListFilter( @Param("filters") Filter[] filters, @Param("filterGenericLabel") Filter[] filterGenericLabel, @Param("ORGANIZATIONID") String organizationId, @Param("matchModeSearch") String matchModeSearch, @Param("SECONDARYLANGUAGE") Boolean secondaryLanguage);


    @Update({
        "update work_effort_content",
        "set thru_date = #{thruDate,jdbcType=TIMESTAMP},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where work_effort_id = #{workEffortId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}",
          "and work_effort_content_type_id = #{workEffortContentTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int updateByPrimaryKey(WorkEffortContent row);
}