package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountWithWorkEffortPurposeTypeView;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GlAccountWithWorkEffortPurposeTypeViewMapper {

    @Insert({
        "insert into gl_account_with_work_effort_purpose_type_view (uom_descr, gl_account_type_description, ",
        "uom_range_description, gl_account_id, ",
        "account_code, account_name, ",
        "account_name_lang, input_enum_id, ",
        "detail_enum_id, we_measure_type_enum_id, ",
        "we_score_range_enum_id, we_score_conv_enum_id, ",
        "we_alert_rule_enum_id, uom_range_id, ",
        "we_without_perf, resp_center_id, ",
        "work_effort_type_id_res, current_status_id, ",
        "organization_party_id)",
        "values (#{uomDescr,jdbcType=VARCHAR}, #{glAccountTypeDescription,jdbcType=VARCHAR}, ",
        "#{uomRangeDescription,jdbcType=VARCHAR}, #{glAccountId,jdbcType=VARCHAR}, ",
        "#{accountCode,jdbcType=VARCHAR}, #{accountName,jdbcType=VARCHAR}, ",
        "#{accountNameLang,jdbcType=VARCHAR}, #{inputEnumId,jdbcType=VARCHAR}, ",
        "#{detailEnumId,jdbcType=VARCHAR}, #{weMeasureTypeEnumId,jdbcType=VARCHAR}, ",
        "#{weScoreRangeEnumId,jdbcType=VARCHAR}, #{weScoreConvEnumId,jdbcType=VARCHAR}, ",
        "#{weAlertRuleEnumId,jdbcType=VARCHAR}, #{uomRangeId,jdbcType=VARCHAR}, ",
        "#{weWithoutPerf,jdbcType=VARCHAR}, #{respCenterId,jdbcType=VARCHAR}, ",
        "#{workEffortTypeIdRes,jdbcType=VARCHAR}, #{currentStatusId,jdbcType=VARCHAR}, ",
        "#{organizationPartyId,jdbcType=VARCHAR})"
    })
    int insert(GlAccountWithWorkEffortPurposeTypeView row);


    @Select({
        "select",
        "uom_descr, gl_account_type_description, uom_range_description, gl_account_id, ",
        "account_code, account_name, account_name_lang, input_enum_id, detail_enum_id, ",
        "we_measure_type_enum_id, we_score_range_enum_id, we_score_conv_enum_id, we_alert_rule_enum_id, ",
        "uom_range_id, we_without_perf, resp_center_id, work_effort_type_id_res, current_status_id, ",
        "organization_party_id",
        "from gl_account_with_work_effort_purpose_type_view"
    })
    @Results({
        @Result(column="uom_descr", property="uomDescr", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_account_type_description", property="glAccountTypeDescription", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_range_description", property="uomRangeDescription", jdbcType=JdbcType.VARCHAR),
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_code", property="accountCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_name", property="accountName", jdbcType=JdbcType.VARCHAR),
        @Result(column="account_name_lang", property="accountNameLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="input_enum_id", property="inputEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="detail_enum_id", property="detailEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_measure_type_enum_id", property="weMeasureTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_score_range_enum_id", property="weScoreRangeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_score_conv_enum_id", property="weScoreConvEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_alert_rule_enum_id", property="weAlertRuleEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_range_id", property="uomRangeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_without_perf", property="weWithoutPerf", jdbcType=JdbcType.VARCHAR),
        @Result(column="resp_center_id", property="respCenterId", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_type_id_res", property="workEffortTypeIdRes", jdbcType=JdbcType.VARCHAR),
        @Result(column="current_status_id", property="currentStatusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="organization_party_id", property="organizationPartyId", jdbcType=JdbcType.VARCHAR)
    })
    List<GlAccountWithWorkEffortPurposeTypeView> selectAll();

    @Select({
            "select",
            "uom_descr, gl_account_type_description, uom_range_description, gl_account_id, ",
            "account_code, account_name, account_name_lang, input_enum_id, detail_enum_id, ",
            "we_measure_type_enum_id, we_score_range_enum_id, we_score_conv_enum_id, we_alert_rule_enum_id, ",
            "uom_range_id, we_without_perf, resp_center_id, work_effort_type_id_res, current_status_id, ",
            "organization_party_id",
            "from gl_account_with_work_effort_purpose_type_view",
            "where organization_party_id = #{organizationId}"
    })
    @ResultType(GlAccountWithWorkEffortPurposeTypeView.class)
    List<GlAccountWithWorkEffortPurposeTypeView> getGlAccountWithWorkEffortPurposeTypeView(@Param("organizationId") String organizationId);

    @ResultType(GlAccountWithWorkEffortPurposeTypeView.class)
    List<GlAccountWithWorkEffortPurposeTypeView> getGlAccountWithWorkEffortPurposeTypeViewFilter(@Param("organizationId") String organizationId, @Param("val") String value, @Param("secondaryLang") boolean secondaryLanguage );

}