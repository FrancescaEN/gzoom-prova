package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.GlFiscalType;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GlFiscalTypeMapper {

    @Delete({
            "delete from gl_fiscal_type",
            "where gl_fiscal_type_id = #{glFiscalTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String glFiscalTypeId);


    @Insert({
            "insert into gl_fiscal_type (gl_fiscal_type_id, description, ",
            "last_updated_stamp, last_updated_tx_stamp, ",
            "created_stamp, created_tx_stamp, ",
            "periodical_absolute_enum_id, gl_fiscal_type_enum_id, ",
            "is_financial_used, is_account_used, ",
            "is_indicator_used, description_lang, ",
            "last_modified_by_user_login, created_by_user_login)",
            "values (#{glFiscalTypeId,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
            "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
            "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
            "#{periodicalAbsoluteEnumId,jdbcType=VARCHAR}, #{glFiscalTypeEnumId,jdbcType=VARCHAR}, ",
            "#{isFinancialUsed,jdbcType=CHAR}, #{isAccountUsed,jdbcType=CHAR}, ",
            "#{isIndicatorUsed,jdbcType=CHAR}, #{descriptionLang,jdbcType=VARCHAR}, ",
            "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(GlFiscalType row);


    @Select({
            "select",
            "gl_fiscal_type_id, description, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
            "created_tx_stamp, periodical_absolute_enum_id, gl_fiscal_type_enum_id, is_financial_used, ",
            "is_account_used, is_indicator_used, description_lang, last_modified_by_user_login, ",
            "created_by_user_login",
            "from gl_fiscal_type",
            "where gl_fiscal_type_id = #{glFiscalTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "glFiscalType", value = {
            @Result(column = "gl_fiscal_type_id", property = "glFiscalTypeId", jdbcType = JdbcType.VARCHAR, id = true),
            @Result(column = "description", property = "description", jdbcType = JdbcType.VARCHAR),
            @Result(column = "last_updated_stamp", property = "lastUpdatedStamp", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "last_updated_tx_stamp", property = "lastUpdatedTxStamp", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "created_stamp", property = "createdStamp", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "created_tx_stamp", property = "createdTxStamp", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "periodical_absolute_enum_id", property = "periodicalAbsoluteEnumId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "gl_fiscal_type_enum_id", property = "glFiscalTypeEnumId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "is_financial_used", property = "isFinancialUsed", jdbcType = JdbcType.CHAR),
            @Result(column = "is_account_used", property = "isAccountUsed", jdbcType = JdbcType.CHAR),
            @Result(column = "is_indicator_used", property = "isIndicatorUsed", jdbcType = JdbcType.CHAR),
            @Result(column = "description_lang", property = "descriptionLang", jdbcType = JdbcType.VARCHAR),
            @Result(column = "last_modified_by_user_login", property = "lastModifiedByUserLogin", jdbcType = JdbcType.VARCHAR),
            @Result(column = "created_by_user_login", property = "createdByUserLogin", jdbcType = JdbcType.VARCHAR)
    })
    GlFiscalType selectByPrimaryKey(String glFiscalTypeId);


    @Select({
            "select",
            "gl_fiscal_type_id, description, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
            "created_tx_stamp, periodical_absolute_enum_id, gl_fiscal_type_enum_id, is_financial_used, ",
            "is_account_used, is_indicator_used, description_lang, last_modified_by_user_login, ",
            "created_by_user_login",
            "from gl_fiscal_type",
            "order by gl_fiscal_type_id"
    })
    @ResultMap("glFiscalType")
    List<GlFiscalType> selectAllOrderByPrimaryKey();


    @Select({
            "select *",
            "from gl_fiscal_type",
            "where is_indicator_used = #{isIndicatorUsed}"
    })
    @ResultType(GlFiscalType.class)
    List<GlFiscalType> getGlFiscalTypeByIsIndicatorUsed(String isIndicatorUsed);

    @Select({
            """
            <script>
            SELECT gft.*
            FROM gl_fiscal_type gft
            INNER JOIN gl_account_type_gl_fiscal_type gtft 
                ON gtft.gl_fiscal_type_id = gft.gl_fiscal_type_id
            WHERE gtft.gl_account_type_id = (
                SELECT gl_account_type_id 
                FROM gl_account 
                WHERE gl_account_id = #{glAccountId, jdbcType=VARCHAR}
            )
            AND NOT EXISTS (
                SELECT 1
                FROM acctg_trans at
                INNER JOIN acctg_trans_entry ate ON ate.acctg_trans_id = at.acctg_trans_id
                    AND ate.work_effort_revision_id IS NULL
                WHERE ate.gl_account_id = #{glAccountId, jdbcType=VARCHAR}
                AND at.transaction_date = (
                    SELECT thru_date 
                    FROM custom_time_period 
                    WHERE custom_time_period_id = #{customTimePeriodId, jdbcType=VARCHAR}
                )
                <if test='roleTypeId != null'>
                    AND at.role_type_id = #{roleTypeId, jdbcType=VARCHAR}
                </if>
                <if test='partyId != null'>
                    AND at.party_id = #{partyId, jdbcType=VARCHAR}
                </if>
                <if test='voucherRef != null'>
                    AND ate.voucher_ref = #{voucherRef, jdbcType=VARCHAR}
                </if>
                <if test='voucherRef == null'>
                    AND ate.voucher_ref IS NULL
                </if>
                AND ate.gl_fiscal_type_id = gft.gl_fiscal_type_id
            )
            <if test='accountTypeEnumId == "FINANCIAL"'>
                AND gft.is_financial_used = 'Y'
            </if>
            <if test='accountTypeEnumId == "ACCOUNT"'>
                AND gft.is_account_used = 'Y'
            </if>
            <if test='accountTypeEnumId == "INDICATOR"'>
                AND gft.is_indicator_used = 'Y'
            </if>
            </script>
            """
    })
    @ResultType(GlFiscalType.class)
    List<GlFiscalType> getGlFiscalTypeForNewIndicatorMovement(
            @Param("accountTypeEnumId") String accountTypeEnumId,
            @Param("glAccountId") String glAccountId,
            @Param("customTimePeriodId") String customTimePeriodId,
            @Param("roleTypeId") String roleTypeId,
            @Param("partyId") String partyId,
            @Param("voucherRef") String voucherRef
    );

    @Select({
            "select *",
            "from gl_fiscal_type",
            "where is_financial_used = #{isFinancialUsed}"
    })
    @ResultType(GlFiscalType.class)
    List<GlFiscalType> getGlFiscalTypeByIsFinancialUsed(String isFinancialUsed);

    @Select({
            "select *",
            "from gl_fiscal_type",
            "where is_account_used = #{isAccountUsed}"
    })
    @ResultType(GlFiscalType.class)
    List<GlFiscalType> getGlFiscalTypeByIsAccountUsed(String isAccountUsed);

    @Update({
            "update gl_fiscal_type",
            "set description = #{description,jdbcType=VARCHAR},",
            "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
            "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
            "periodical_absolute_enum_id = #{periodicalAbsoluteEnumId,jdbcType=VARCHAR},",
            "gl_fiscal_type_enum_id = #{glFiscalTypeEnumId,jdbcType=VARCHAR},",
            "is_financial_used = #{isFinancialUsed,jdbcType=CHAR},",
            "is_account_used = #{isAccountUsed,jdbcType=CHAR},",
            "is_indicator_used = #{isIndicatorUsed,jdbcType=CHAR},",
            "description_lang = #{descriptionLang,jdbcType=VARCHAR},",
            "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
            "where gl_fiscal_type_id = #{glFiscalTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(GlFiscalType row);
}