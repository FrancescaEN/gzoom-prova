package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeAccount;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface WorkEffortPurposeAccountMapper {
    @Delete({
        "delete from work_effort_purpose_account",
        "where work_effort_purpose_type_id = #{workEffortPurposeTypeId,jdbcType=VARCHAR}",
          "and gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("workEffortPurposeTypeId") String workEffortPurposeTypeId, @Param("glAccountId") String glAccountId);

    @Delete({
            "delete from work_effort_purpose_account",
            "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    int deleteByGlAccountId(@Param("glAccountId") String glAccountId);

    @Insert({
        "insert into work_effort_purpose_account (work_effort_purpose_type_id, gl_account_id, ",
        "comments, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{workEffortPurposeTypeId,jdbcType=VARCHAR}, #{glAccountId,jdbcType=VARCHAR}, ",
        "#{comments,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(WorkEffortPurposeAccount row);

    @Select({
            "select",
            "count(*)",
            "from work_effort_purpose_account wepa",
            "join gl_account ga on ga.gl_account_id = wepa.gl_account_id",
            "where ga.gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    @ResultType(int.class)
    int countByGlAccountId(String glAccountId);

    @Select({
        "select",
        "work_effort_purpose_type_id, gl_account_id, comments, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp",
        "from work_effort_purpose_account",
        "where work_effort_purpose_type_id = #{workEffortPurposeTypeId,jdbcType=VARCHAR}",
          "and gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffortPurposeAccount", value = {
        @Result(column="work_effort_purpose_type_id", property="workEffortPurposeTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="comments", property="comments", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    WorkEffortPurposeAccount selectByPrimaryKey(@Param("workEffortPurposeTypeId") String workEffortPurposeTypeId, @Param("glAccountId") String glAccountId);

    @Select({
        "select",
        "work_effort_purpose_type_id, gl_account_id, comments, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp",
        "from work_effort_purpose_account"
    })
    @Results({
        @Result(column="work_effort_purpose_type_id", property="workEffortPurposeTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="comments", property="comments", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    List<WorkEffortPurposeAccount> selectAll();

    @Update({
        "update work_effort_purpose_account",
        "set comments = #{comments,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where work_effort_purpose_type_id = #{workEffortPurposeTypeId,jdbcType=VARCHAR}",
          "and gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(WorkEffortPurposeAccount row);
}