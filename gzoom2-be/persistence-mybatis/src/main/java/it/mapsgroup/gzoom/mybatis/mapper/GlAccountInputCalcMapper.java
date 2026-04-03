package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.CustomMethod;
import it.mapsgroup.gzoom.mybatis.dto.GlAccount;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountInputCalc;
import java.util.List;

import it.mapsgroup.gzoom.mybatis.dto.GlFiscalType;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GlAccountInputCalcMapper {
    @Delete({
        "delete from gl_account_input_calc",
        "where gl_account_input_calc_id = #{glAccountInputCalcId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String glAccountInputCalcId);

    @Delete({
            "delete from gl_account_input_calc",
            "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    int deleteByGlAccountId(String glAccountId);

    @Insert({
        "insert into gl_account_input_calc (gl_account_input_calc_id, gl_account_id, ",
        "input_sequence_num, gl_account_id_ref, ",
        "factor_calculator, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, gl_fiscal_type_id)",
        "values (#{glAccountInputCalcId,jdbcType=VARCHAR}, #{glAccountId,jdbcType=VARCHAR}, ",
        "#{inputSequenceNum,jdbcType=VARCHAR}, #{glAccountIdRef,jdbcType=VARCHAR}, ",
        "#{factorCalculator,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{glFiscalTypeId,jdbcType=VARCHAR})"
    })
    int insert(GlAccountInputCalc row);

    @Select({
        "select",
        "gl_account_input_calc_id, gl_account_id, input_sequence_num, gl_account_id_ref, ",
        "factor_calculator, last_modified_by_user_login, created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, gl_fiscal_type_id",
        "from gl_account_input_calc",
        "where gl_account_input_calc_id = #{glAccountInputCalcId,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="gl_account_input_calc_id", property="glAccountInputCalcId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR),
        @Result(column="input_sequence_num", property="inputSequenceNum", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_account_id_ref", property="glAccountIdRef", jdbcType=JdbcType.VARCHAR),
        @Result(column="factor_calculator", property="factorCalculator", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gl_fiscal_type_id", property="glFiscalTypeId", jdbcType=JdbcType.VARCHAR)
    })
    GlAccountInputCalc selectByPrimaryKey(String glAccountInputCalcId);

    @Select({
        "select",
        "gl_account_input_calc_id, gl_account_id, input_sequence_num, gl_account_id_ref, ",
        "factor_calculator, last_modified_by_user_login, created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, gl_fiscal_type_id",
        "from gl_account_input_calc"
    })
    @Results({
        @Result(column="gl_account_input_calc_id", property="glAccountInputCalcId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR),
        @Result(column="input_sequence_num", property="inputSequenceNum", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_account_id_ref", property="glAccountIdRef", jdbcType=JdbcType.VARCHAR),
        @Result(column="factor_calculator", property="factorCalculator", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gl_fiscal_type_id", property="glFiscalTypeId", jdbcType=JdbcType.VARCHAR)
    })
    List<GlAccountInputCalc> selectAll();

    @Select({
            "select",
            "gl_account_input_calc_id, gl_account_id, input_sequence_num, gl_account_id_ref, ",
            "factor_calculator, last_modified_by_user_login, created_by_user_login, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp, gl_fiscal_type_id",
            "from gl_account_input_calc",
            "where gl_account_id = #{glAccountId}"
    })
    @Results({
            @Result(column="gl_account_input_calc_id", property="glAccountInputCalcId", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR),
            @Result(column="input_sequence_num", property="inputSequenceNum", jdbcType=JdbcType.VARCHAR),
            @Result(column="gl_account_id_ref", property="glAccountIdRef", jdbcType=JdbcType.VARCHAR),
            @Result(column="factor_calculator", property="factorCalculator", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
            @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gl_fiscal_type_id", property="glFiscalTypeId", jdbcType=JdbcType.VARCHAR),
            @Result(property = "glAccountRef", column = "gl_account_id_ref", one = @One(select = "selectGlAccountForGlAccountInputCalc")),
    })
    List<GlAccountInputCalc> selectByGlAccountId(String glAccountId);

    @Select({
            "select",
            "gl_account_input_calc_id, gl_account_id, input_sequence_num, gl_account_id_ref, ",
            "factor_calculator, last_modified_by_user_login, created_by_user_login, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp, gl_fiscal_type_id",
            "from gl_account_input_calc",
            "where gl_account_id_ref = #{glAccountIdRef}"
    })
    @Results({
            @Result(column="gl_account_input_calc_id", property="glAccountInputCalcId", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR),
            @Result(column="input_sequence_num", property="inputSequenceNum", jdbcType=JdbcType.VARCHAR),
            @Result(column="gl_account_id_ref", property="glAccountIdRef", jdbcType=JdbcType.VARCHAR),
            @Result(column="factor_calculator", property="factorCalculator", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
            @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gl_fiscal_type_id", property="glFiscalTypeId", jdbcType=JdbcType.VARCHAR),
            @Result(property = "glAccount", column = "gl_account_id", one = @One(select = "selectGlAccountForGlAccountInputCalc")),
    })
    List<GlAccountInputCalc> selectByGlAccountIdRef(String glAccountIdRef);


    @Select({"select * from gl_Account where gl_account_id = #{glAccountId}"})
    @ResultType(GlAccount.class)
    GlAccount selectGlAccountForGlAccountInputCalc(String glAccountId);


    @Select({
            "select count(*)",
            "from gl_account_input_calc",
            "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    @ResultType(int.class)
    int countByGlAccountId(String glAccountId);

    @Update({
        "update gl_account_input_calc",
        "set gl_account_id = #{glAccountId,jdbcType=VARCHAR},",
          "input_sequence_num = #{inputSequenceNum,jdbcType=VARCHAR},",
          "gl_account_id_ref = #{glAccountIdRef,jdbcType=VARCHAR},",
          "factor_calculator = #{factorCalculator,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "gl_fiscal_type_id = #{glFiscalTypeId,jdbcType=VARCHAR}",
        "where gl_account_input_calc_id = #{glAccountInputCalcId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(GlAccountInputCalc row);

    @Update({
            "update gl_account_input_calc",
            "set gl_account_id_ref = #{glAccountIdRef,jdbcType=VARCHAR},",
            "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
            "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
            "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
            "where gl_account_input_calc_id = #{glAccountInputCalcId,jdbcType=VARCHAR}"
    })
    int updateGlAccountIdRefByPrimaryKey(GlAccountInputCalc row);
}