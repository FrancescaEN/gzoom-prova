package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.GlAccount;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Mapper
@Repository
public interface GlAccountMapper {

    @Delete({
        "delete from gl_account",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String glAccountId);


    @Insert({
        "insert into gl_account (gl_account_id, gl_account_type_id, ",
        "gl_account_class_id, gl_resource_type_id, ",
        "gl_xbrl_class_id, parent_gl_account_id, ",
        "account_code, account_name, ",
        "description, product_id, ",
        "external_id, posted_balance, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "debit_credit_default, default_uom_id, ",
        "periodical_absolute_enum_id, referenced_account_id, ",
        "account_type_enum_id, local_name_content_id, ",
        "child_folder_file, has_competence, ",
        "detect_org_unit_id_flag, current_status_id, ",
        "input_enum_id, period_type_id, ",
        "from_date, thru_date, ",
        "data_source_id, detail_enum_id, ",
        "calc_custom_method_id, prio_calc, ",
        "we_measure_type_enum_id, we_score_range_enum_id, ",
        "we_score_conv_enum_id, we_alert_rule_enum_id, ",
        "uom_range_id, we_without_perf, ",
        "source, trend_enum_id, ",
        "resp_center_id, resp_center_role_type_id, ",
        "empl_position_type_id, account_name_lang, ",
        "description_lang, source_lang, ",
        "role_type_id, sequence_id, ",
        "target_period_enum_id, we_without_target, ",
        "last_modified_by_user_login, created_by_user_login)",
        "values (#{glAccountId,jdbcType=VARCHAR}, #{glAccountTypeId,jdbcType=VARCHAR}, ",
        "#{glAccountClassId,jdbcType=VARCHAR}, #{glResourceTypeId,jdbcType=VARCHAR}, ",
        "#{glXbrlClassId,jdbcType=VARCHAR}, #{parentGlAccountId,jdbcType=VARCHAR}, ",
        "#{accountCode,jdbcType=VARCHAR}, #{accountName,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{productId,jdbcType=VARCHAR}, ",
        "#{externalId,jdbcType=VARCHAR}, #{postedBalance,jdbcType=NUMERIC}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{debitCreditDefault,jdbcType=VARCHAR}, #{defaultUomId,jdbcType=VARCHAR}, ",
        "#{periodicalAbsoluteEnumId,jdbcType=VARCHAR}, #{referencedAccountId,jdbcType=VARCHAR}, ",
        "#{accountTypeEnumId,jdbcType=VARCHAR}, #{localNameContentId,jdbcType=VARCHAR}, ",
        "#{childFolderFile,jdbcType=VARCHAR}, #{hasCompetence,jdbcType=CHAR}, ",
        "#{detectOrgUnitIdFlag,jdbcType=CHAR}, #{currentStatusId,jdbcType=VARCHAR}, ",
        "#{inputEnumId,jdbcType=VARCHAR}, #{periodTypeId,jdbcType=VARCHAR}, ",
        "#{fromDate,jdbcType=TIMESTAMP}, #{thruDate,jdbcType=TIMESTAMP}, ",
        "#{dataSourceId,jdbcType=VARCHAR}, #{detailEnumId,jdbcType=VARCHAR}, ",
        "#{calcCustomMethodId,jdbcType=VARCHAR}, #{prioCalc,jdbcType=NUMERIC}, ",
        "#{weMeasureTypeEnumId,jdbcType=VARCHAR}, #{weScoreRangeEnumId,jdbcType=VARCHAR}, ",
        "#{weScoreConvEnumId,jdbcType=VARCHAR}, #{weAlertRuleEnumId,jdbcType=VARCHAR}, ",
        "#{uomRangeId,jdbcType=VARCHAR}, #{weWithoutPerf,jdbcType=VARCHAR}, ",
        "#{source,jdbcType=VARCHAR}, #{trendEnumId,jdbcType=VARCHAR}, ",
        "#{respCenterId,jdbcType=VARCHAR}, #{respCenterRoleTypeId,jdbcType=VARCHAR}, ",
        "#{emplPositionTypeId,jdbcType=VARCHAR}, #{accountNameLang,jdbcType=VARCHAR}, ",
        "#{descriptionLang,jdbcType=VARCHAR}, #{sourceLang,jdbcType=VARCHAR}, ",
        "#{roleTypeId,jdbcType=VARCHAR}, #{sequenceId,jdbcType=NUMERIC}, ",
        "#{targetPeriodEnumId,jdbcType=VARCHAR}, #{weWithoutTarget,jdbcType=VARCHAR}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(GlAccount row);


    @Select({
        "select",
        "gl_account_id, gl_account_type_id, gl_account_class_id, gl_resource_type_id, ",
        "gl_xbrl_class_id, parent_gl_account_id, account_code, account_name, description, ",
        "product_id, external_id, posted_balance, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, debit_credit_default, default_uom_id, periodical_absolute_enum_id, ",
        "referenced_account_id, account_type_enum_id, local_name_content_id, child_folder_file, ",
        "has_competence, detect_org_unit_id_flag, current_status_id, input_enum_id, period_type_id, ",
        "from_date, thru_date, data_source_id, detail_enum_id, calc_custom_method_id, ",
        "prio_calc, we_measure_type_enum_id, we_score_range_enum_id, we_score_conv_enum_id, ",
        "we_alert_rule_enum_id, uom_range_id, we_without_perf, source, trend_enum_id, ",
        "resp_center_id, resp_center_role_type_id, empl_position_type_id, account_name_lang, ",
        "description_lang, source_lang, role_type_id, sequence_id, target_period_enum_id, ",
        "we_without_target, last_modified_by_user_login, created_by_user_login",
        "from gl_account",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    @Results(id = "glAccountMapper", value = {
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="gl_account_type_id", property="glAccountTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_account_class_id", property="glAccountClassId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_resource_type_id", property="glResourceTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_xbrl_class_id", property="glXbrlClassId", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_gl_account_id", property="parentGlAccountId", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_code", property="accountCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_name", property="accountName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="product_id", property="productId", jdbcType=JdbcType.VARCHAR),
        @Result(column="external_id", property="externalId", jdbcType=JdbcType.VARCHAR),
        @Result(column="posted_balance", property="postedBalance", jdbcType=JdbcType.NUMERIC),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="debit_credit_default", property="debitCreditDefault", jdbcType=JdbcType.VARCHAR),
        @Result(column="default_uom_id", property="defaultUomId", jdbcType=JdbcType.VARCHAR),
        @Result(column="periodical_absolute_enum_id", property="periodicalAbsoluteEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="referenced_account_id", property="referencedAccountId", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_type_enum_id", property="accountTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="local_name_content_id", property="localNameContentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="child_folder_file", property="childFolderFile", jdbcType=JdbcType.VARCHAR),
        @Result(column="has_competence", property="hasCompetence", jdbcType=JdbcType.CHAR),
        @Result(column="detect_org_unit_id_flag", property="detectOrgUnitIdFlag", jdbcType=JdbcType.CHAR),
        @Result(column="current_status_id", property="currentStatusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="input_enum_id", property="inputEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="period_type_id", property="periodTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="data_source_id", property="dataSourceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="detail_enum_id", property="detailEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="calc_custom_method_id", property="calcCustomMethodId", jdbcType=JdbcType.VARCHAR),
        @Result(column="prio_calc", property="prioCalc", jdbcType=JdbcType.NUMERIC),
        @Result(column="we_measure_type_enum_id", property="weMeasureTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_score_range_enum_id", property="weScoreRangeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_score_conv_enum_id", property="weScoreConvEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_alert_rule_enum_id", property="weAlertRuleEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_range_id", property="uomRangeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_without_perf", property="weWithoutPerf", jdbcType=JdbcType.VARCHAR),
        @Result(column="source", property="source", jdbcType=JdbcType.VARCHAR),
        @Result(column="trend_enum_id", property="trendEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="resp_center_id", property="respCenterId", jdbcType=JdbcType.VARCHAR),
        @Result(column="resp_center_role_type_id", property="respCenterRoleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="empl_position_type_id", property="emplPositionTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_name_lang", property="accountNameLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="source_lang", property="sourceLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="sequence_id", property="sequenceId", jdbcType=JdbcType.NUMERIC),
        @Result(column="target_period_enum_id", property="targetPeriodEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_without_target", property="weWithoutTarget", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    GlAccount selectByPrimaryKey(String glAccountId);

    @Select({
            "select *",
            "from gl_account",
            "where account_type_enum_id = #{accountTypeEnumId,jdbcType=VARCHAR}",
            "order by gl_account_id"
    })
    @ResultType(GlAccountMapper.class)
    List<GlAccount> selectByAccountTypeEnumId(String accountTypeEnumId);

    @Select({
            "select ga.*",
            "from gl_account ga",
            "join gl_account_organization gao on gao.gl_account_id = ga.gl_account_id and gao.organization_party_id = #{organizationId,jdbcType=VARCHAR}"
    })
    @ResultType(GlAccountMapper.class)
    List<GlAccount> selectByOrganizationPartyId(String organizationId);


    @ResultType(GlAccountMapper.class)
    List<GlAccount> selectGlAccount(String userLoginId,
                                    boolean isAdmin,
                                    String organizationId,
                                    String accountTypeEnumId,
                                    String isReservedAccount,
                                    boolean isSecondaryLang,
                                    String search,
                                    String matchModeSearch,
                                    String[] workEffortPurposeTypeId,
                                    String orderBy,
                                    String orderType,
                                    String glAccountTypeId,
                                    String inputEnumId,
                                    String detectOrgUnitIdFlag,
                                    String debitCreditDefault,
                                    String periodTypeId,
                                    String currentStatusId,
                                    String respCenterId);

    @ResultType(GlAccount.class)
    List<GlAccount> selectGlAccountByFilterParams(
                                    boolean isSecondaryLang,
                                    String search,
                                    String matchModeSearch,
                                    String[] workEffortPurposeTypeId,
                                    String glAccountTypeId);

    @Select({
        "select",
        "gl_account_id, gl_account_type_id, gl_account_class_id, gl_resource_type_id, ",
        "gl_xbrl_class_id, parent_gl_account_id, account_code, account_name, description, ",
        "product_id, external_id, posted_balance, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, debit_credit_default, default_uom_id, periodical_absolute_enum_id, ",
        "referenced_account_id, account_type_enum_id, local_name_content_id, child_folder_file, ",
        "has_competence, detect_org_unit_id_flag, current_status_id, input_enum_id, period_type_id, ",
        "from_date, thru_date, data_source_id, detail_enum_id, calc_custom_method_id, ",
        "prio_calc, we_measure_type_enum_id, we_score_range_enum_id, we_score_conv_enum_id, ",
        "we_alert_rule_enum_id, uom_range_id, we_without_perf, source, trend_enum_id, ",
        "resp_center_id, resp_center_role_type_id, empl_position_type_id, account_name_lang, ",
        "description_lang, source_lang, role_type_id, sequence_id, target_period_enum_id, ",
        "we_without_target, last_modified_by_user_login, created_by_user_login",
        "from gl_account"
    })
    @Results({
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="gl_account_type_id", property="glAccountTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_account_class_id", property="glAccountClassId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_resource_type_id", property="glResourceTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_xbrl_class_id", property="glXbrlClassId", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_gl_account_id", property="parentGlAccountId", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_code", property="accountCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_name", property="accountName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="product_id", property="productId", jdbcType=JdbcType.VARCHAR),
        @Result(column="external_id", property="externalId", jdbcType=JdbcType.VARCHAR),
        @Result(column="posted_balance", property="postedBalance", jdbcType=JdbcType.NUMERIC),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="debit_credit_default", property="debitCreditDefault", jdbcType=JdbcType.VARCHAR),
        @Result(column="default_uom_id", property="defaultUomId", jdbcType=JdbcType.VARCHAR),
        @Result(column="periodical_absolute_enum_id", property="periodicalAbsoluteEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="referenced_account_id", property="referencedAccountId", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_type_enum_id", property="accountTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="local_name_content_id", property="localNameContentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="child_folder_file", property="childFolderFile", jdbcType=JdbcType.VARCHAR),
        @Result(column="has_competence", property="hasCompetence", jdbcType=JdbcType.CHAR),
        @Result(column="detect_org_unit_id_flag", property="detectOrgUnitIdFlag", jdbcType=JdbcType.CHAR),
        @Result(column="current_status_id", property="currentStatusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="input_enum_id", property="inputEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="period_type_id", property="periodTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="data_source_id", property="dataSourceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="detail_enum_id", property="detailEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="calc_custom_method_id", property="calcCustomMethodId", jdbcType=JdbcType.VARCHAR),
        @Result(column="prio_calc", property="prioCalc", jdbcType=JdbcType.NUMERIC),
        @Result(column="we_measure_type_enum_id", property="weMeasureTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_score_range_enum_id", property="weScoreRangeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_score_conv_enum_id", property="weScoreConvEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_alert_rule_enum_id", property="weAlertRuleEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_range_id", property="uomRangeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_without_perf", property="weWithoutPerf", jdbcType=JdbcType.VARCHAR),
        @Result(column="source", property="source", jdbcType=JdbcType.VARCHAR),
        @Result(column="trend_enum_id", property="trendEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="resp_center_id", property="respCenterId", jdbcType=JdbcType.VARCHAR),
        @Result(column="resp_center_role_type_id", property="respCenterRoleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="empl_position_type_id", property="emplPositionTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_name_lang", property="accountNameLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="source_lang", property="sourceLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="sequence_id", property="sequenceId", jdbcType=JdbcType.NUMERIC),
        @Result(column="target_period_enum_id", property="targetPeriodEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_without_target", property="weWithoutTarget", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    List<GlAccount> selectAll();


    @Update({
        "update gl_account",
        "set gl_account_type_id = #{glAccountTypeId,jdbcType=VARCHAR},",
          "gl_account_class_id = #{glAccountClassId,jdbcType=VARCHAR},",
          "gl_resource_type_id = #{glResourceTypeId,jdbcType=VARCHAR},",
          "gl_xbrl_class_id = #{glXbrlClassId,jdbcType=VARCHAR},",
          "parent_gl_account_id = #{parentGlAccountId,jdbcType=VARCHAR},",
          "account_code = #{accountCode,jdbcType=VARCHAR},",
          "account_name = #{accountName,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "product_id = #{productId,jdbcType=VARCHAR},",
          "external_id = #{externalId,jdbcType=VARCHAR},",
          "posted_balance = #{postedBalance,jdbcType=NUMERIC},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "debit_credit_default = #{debitCreditDefault,jdbcType=VARCHAR},",
          "default_uom_id = #{defaultUomId,jdbcType=VARCHAR},",
          "periodical_absolute_enum_id = #{periodicalAbsoluteEnumId,jdbcType=VARCHAR},",
          "referenced_account_id = #{referencedAccountId,jdbcType=VARCHAR},",
          "account_type_enum_id = #{accountTypeEnumId,jdbcType=VARCHAR},",
          "local_name_content_id = #{localNameContentId,jdbcType=VARCHAR},",
          "child_folder_file = #{childFolderFile,jdbcType=VARCHAR},",
          "has_competence = #{hasCompetence,jdbcType=CHAR},",
          "detect_org_unit_id_flag = #{detectOrgUnitIdFlag,jdbcType=CHAR},",
          "current_status_id = #{currentStatusId,jdbcType=VARCHAR},",
          "input_enum_id = #{inputEnumId,jdbcType=VARCHAR},",
          "period_type_id = #{periodTypeId,jdbcType=VARCHAR},",
          "from_date = #{fromDate,jdbcType=TIMESTAMP},",
          "thru_date = #{thruDate,jdbcType=TIMESTAMP},",
          "data_source_id = #{dataSourceId,jdbcType=VARCHAR},",
          "detail_enum_id = #{detailEnumId,jdbcType=VARCHAR},",
          "calc_custom_method_id = #{calcCustomMethodId,jdbcType=VARCHAR},",
          "prio_calc = #{prioCalc,jdbcType=NUMERIC},",
          "we_measure_type_enum_id = #{weMeasureTypeEnumId,jdbcType=VARCHAR},",
          "we_score_range_enum_id = #{weScoreRangeEnumId,jdbcType=VARCHAR},",
          "we_score_conv_enum_id = #{weScoreConvEnumId,jdbcType=VARCHAR},",
          "we_alert_rule_enum_id = #{weAlertRuleEnumId,jdbcType=VARCHAR},",
          "uom_range_id = #{uomRangeId,jdbcType=VARCHAR},",
          "we_without_perf = #{weWithoutPerf,jdbcType=VARCHAR},",
          "source = #{source,jdbcType=VARCHAR},",
          "trend_enum_id = #{trendEnumId,jdbcType=VARCHAR},",
          "resp_center_id = #{respCenterId,jdbcType=VARCHAR},",
          "resp_center_role_type_id = #{respCenterRoleTypeId,jdbcType=VARCHAR},",
          "empl_position_type_id = #{emplPositionTypeId,jdbcType=VARCHAR},",
          "account_name_lang = #{accountNameLang,jdbcType=VARCHAR},",
          "description_lang = #{descriptionLang,jdbcType=VARCHAR},",
          "source_lang = #{sourceLang,jdbcType=VARCHAR},",
          "role_type_id = #{roleTypeId,jdbcType=VARCHAR},",
          "sequence_id = #{sequenceId,jdbcType=NUMERIC},",
          "target_period_enum_id = #{targetPeriodEnumId,jdbcType=VARCHAR},",
          "we_without_target = #{weWithoutTarget,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(GlAccount row);

    @Update({
            "update gl_account",
            "set last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
            "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
            "calc_custom_method_id = #{calcCustomMethodId,jdbcType=VARCHAR},",
            "prio_calc = #{prioCalc,jdbcType=NUMERIC},",
            "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
            "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    int updateCalcCustomMethodAndPrioCalc(GlAccount row);

    @Select({
            "select uom.decimal_scale",
            "from gl_account gl",
            "inner join uom uom on uom.uom_id = gl.default_uom_id",
            "where gl.gl_account_id = #{glAccountId}"
    })
    @ResultType(BigInteger.class)
    BigInteger getDecimalPrecision(String glAccountId);

    List<GlAccount> selectGlAccountByOrgId(@Param("isAdmin") boolean isAdmin, @Param("organizationId") String organizationId, @Param("userLoginId") String userLoginId);

    @ResultType(GlAccount.class)
    List<GlAccount> getGlAccountMovements(
            String userLoginId,
            boolean isAdmin,
            String accountTypeEnumId,
            String isReservedAccount,
            String organizationId,
            String inputEnumId,
            String detectOrgUnitIdFlag
            );
}