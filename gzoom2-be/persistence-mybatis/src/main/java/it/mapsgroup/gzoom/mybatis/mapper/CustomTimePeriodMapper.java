package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.CustomTimePeriod;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CustomTimePeriodMapper {

    @Delete({
        "delete from custom_time_period",
        "where custom_time_period_id = #{customTimePeriodId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String customTimePeriodId);


    @Insert({
        "insert into custom_time_period (custom_time_period_id, parent_period_id, ",
        "period_type_id, period_num, ",
        "period_name, from_date, ",
        "thru_date, is_closed, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "custom_time_period_code, custom_time_period_code_lang, ",
        "period_name_lang, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{customTimePeriodId,jdbcType=VARCHAR}, #{parentPeriodId,jdbcType=VARCHAR}, ",
        "#{periodTypeId,jdbcType=VARCHAR}, #{periodNum,jdbcType=NUMERIC}, ",
        "#{periodName,jdbcType=VARCHAR}, #{fromDate,jdbcType=TIMESTAMP}, ",
        "#{thruDate,jdbcType=TIMESTAMP}, #{isClosed,jdbcType=CHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{customTimePeriodCode,jdbcType=VARCHAR}, #{customTimePeriodCodeLang,jdbcType=VARCHAR}, ",
        "#{periodNameLang,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(CustomTimePeriod row);


    @Select({
        "select",
        "custom_time_period_id, parent_period_id, period_type_id, period_num, period_name, ",
        "from_date, thru_date, is_closed, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, custom_time_period_code, custom_time_period_code_lang, ",
        "period_name_lang, last_modified_by_user_login, created_by_user_login",
        "from custom_time_period",
        "where custom_time_period_id = #{customTimePeriodId,jdbcType=VARCHAR}"
    })
    @Results(id = "customTimePeriodResultMap", value = {
        @Result(column="custom_time_period_id", property="customTimePeriodId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_period_id", property="parentPeriodId", jdbcType=JdbcType.VARCHAR),
        @Result(column="period_type_id", property="periodTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="period_num", property="periodNum", jdbcType=JdbcType.NUMERIC),
        @Result(column="period_name", property="periodName", jdbcType=JdbcType.VARCHAR),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="is_closed", property="isClosed", jdbcType=JdbcType.CHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="custom_time_period_code", property="customTimePeriodCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_time_period_code_lang", property="customTimePeriodCodeLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="period_name_lang", property="periodNameLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    CustomTimePeriod selectByPrimaryKey(String customTimePeriodId);


    @Select({
        "select",
        "custom_time_period_id, parent_period_id, period_type_id, period_num, period_name, ",
        "from_date, thru_date, is_closed, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, custom_time_period_code, custom_time_period_code_lang, ",
        "period_name_lang, last_modified_by_user_login, created_by_user_login",
        "from custom_time_period",
            "order by custom_time_period_id"
    })
    @ResultMap("customTimePeriodResultMap")
    List<CustomTimePeriod> selectAllOrderByPrimaryKey();

    @Select({
            "select",
            "custom_time_period_id, parent_period_id, period_type_id, period_num, period_name, ",
            "from_date, thru_date, is_closed, last_updated_stamp, last_updated_tx_stamp, ",
            "created_stamp, created_tx_stamp, custom_time_period_code, custom_time_period_code_lang, ",
            "period_name_lang, last_modified_by_user_login, created_by_user_login",
            "from custom_time_period",
            "where period_type_id = #{periodTypeId,jdbcType=VARCHAR} ",
            "order by thru_date"
    })
    @ResultMap("customTimePeriodResultMap")
    List<CustomTimePeriod> selectByPeriodTypeIdOrderByThruDate(String periodTypeId);


    @Update({
        "update custom_time_period",
        "set parent_period_id = #{parentPeriodId,jdbcType=VARCHAR},",
          "period_type_id = #{periodTypeId,jdbcType=VARCHAR},",
          "period_num = #{periodNum,jdbcType=NUMERIC},",
          "period_name = #{periodName,jdbcType=VARCHAR},",
          "from_date = #{fromDate,jdbcType=TIMESTAMP},",
          "thru_date = #{thruDate,jdbcType=TIMESTAMP},",
          "is_closed = #{isClosed,jdbcType=CHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "custom_time_period_code = #{customTimePeriodCode,jdbcType=VARCHAR},",
          "custom_time_period_code_lang = #{customTimePeriodCodeLang,jdbcType=VARCHAR},",
          "period_name_lang = #{periodNameLang,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where custom_time_period_id = #{customTimePeriodId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(CustomTimePeriod row);

    List<CustomTimePeriod> getTimesheetCustomTimePeriodDropdownFilter(@Param("userLoginId") String userLoginId);

    @Select({
            "select ctp.* from custom_time_period ctp \n" +
                    "join acctg_trans at2 on at2.transaction_date = ctp.thru_date \n" +
                    "join acctg_trans_entry ate on ate.acctg_trans_id = at2.acctg_trans_id and ate.acctg_trans_id = #{acctgTransId,jdbcType=VARCHAR} and ate.acctg_trans_entry_seq_id = #{acctgTransEntrySeqId,jdbcType=VARCHAR}\n" +
                    "join gl_account ga on ga.gl_account_id = ate.gl_account_id \n" +
                    "and  ga.period_type_id = ctp.period_type_id "
    })
    @ResultType(CustomTimePeriod.class)
    CustomTimePeriod getCustomTimePeriodForIndicatorMovement(String acctgTransId, String acctgTransEntrySeqId);
}