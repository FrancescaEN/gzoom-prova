package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortView;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortViewEx;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WorkEffortViewMapper {


    @Select({
        "select",
        "work_effort_id, source_reference_id, work_effort_name, work_effort_name_lang, ",
        "description, description_lang, estimated_start_date, estimated_completion_date, ",
        "current_status_id, work_effort_type_id, org_unit_id, org_unit_role_type_id, ",
        "we_assoc_type_id, is_posted, work_effort_snapshot_id, work_effort_revision_id, ",
        "work_effort_revision_descr, snap_shot_description, snap_shot_date, work_effort_type_period_id, ",
        "uom_range_score_id, organization_id, effort_uom_id, product_id, empl_position_type_id, ",
        "local_name_content_id, estimated_total_effort, weight_kpi, weight_review, weight_sons, ",
        "weight_assoc_work_effort, last_correct_score_date, total_enum_id_kpi, total_enum_id_sons, ",
        "total_enum_id_assoc, process_id, scheduled_start_date, scheduled_completion_date, ",
        "special_terms, data_soll, we_type_uom_range_score_id, we_type_description, we_type_description_lang, ",
        "we_type_icon_id, layout_type_enum_id, etch, purpose_etch, child_template_id, ",
        "enable_multi_year_flag, we_status_descr, we_status_descr_lang, we_activation, ",
        "we_status_type_id, we_context_id, we_context_description, we_is_template, we_is_root, ",
        "we_is_in_only_one_card, we_hierarchy_type_id, we_int_org_code, we_int_org_descr, ",
        "we_int_org_descr_lang, we_org_role_descr, we_org_party_code, we_org_party_descr, ",
        "we_org_party_descr_lang, we_type_period_id, we_type_period_desc, we_etch, we_parent_type_etch, ",
        "we_parent_etch, we_parent_name, we_parent_id",
        "from work_effort_view"
    })
    @Results(id = "workEffortView", value = {
        @Result(column="work_effort_id", property="workEffortId", jdbcType=JdbcType.VARCHAR),
        @Result(column="source_reference_id", property="sourceReferenceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_name", property="workEffortName", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_name_lang", property="workEffortNameLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="estimated_start_date", property="estimatedStartDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="estimated_completion_date", property="estimatedCompletionDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="current_status_id", property="currentStatusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_type_id", property="workEffortTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="org_unit_id", property="orgUnitId", jdbcType=JdbcType.VARCHAR),
        @Result(column="org_unit_role_type_id", property="orgUnitRoleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_assoc_type_id", property="weAssocTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_posted", property="isPosted", jdbcType=JdbcType.CHAR),
        @Result(column="work_effort_snapshot_id", property="workEffortSnapshotId", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_revision_id", property="workEffortRevisionId", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_revision_descr", property="workEffortRevisionDescr", jdbcType=JdbcType.VARCHAR),
        @Result(column="snap_shot_description", property="snapShotDescription", jdbcType=JdbcType.VARCHAR),
        @Result(column="snap_shot_date", property="snapShotDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="work_effort_type_period_id", property="workEffortTypePeriodId", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_range_score_id", property="uomRangeScoreId", jdbcType=JdbcType.VARCHAR),
        @Result(column="organization_id", property="organizationId", jdbcType=JdbcType.VARCHAR),
        @Result(column="effort_uom_id", property="effortUomId", jdbcType=JdbcType.VARCHAR),
        @Result(column="product_id", property="productId", jdbcType=JdbcType.VARCHAR),
        @Result(column="empl_position_type_id", property="emplPositionTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="local_name_content_id", property="localNameContentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="estimated_total_effort", property="estimatedTotalEffort", jdbcType=JdbcType.DOUBLE),
        @Result(column="weight_kpi", property="weightKpi", jdbcType=JdbcType.DOUBLE),
        @Result(column="weight_review", property="weightReview", jdbcType=JdbcType.DOUBLE),
        @Result(column="weight_sons", property="weightSons", jdbcType=JdbcType.DOUBLE),
        @Result(column="weight_assoc_work_effort", property="weightAssocWorkEffort", jdbcType=JdbcType.DOUBLE),
        @Result(column="last_correct_score_date", property="lastCorrectScoreDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="total_enum_id_kpi", property="totalEnumIdKpi", jdbcType=JdbcType.VARCHAR),
        @Result(column="total_enum_id_sons", property="totalEnumIdSons", jdbcType=JdbcType.VARCHAR),
        @Result(column="total_enum_id_assoc", property="totalEnumIdAssoc", jdbcType=JdbcType.VARCHAR),
        @Result(column="process_id", property="processId", jdbcType=JdbcType.VARCHAR),
        @Result(column="scheduled_start_date", property="scheduledStartDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="scheduled_completion_date", property="scheduledCompletionDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="special_terms", property="specialTerms", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_soll", property="dataSoll", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="we_type_uom_range_score_id", property="weTypeUomRangeScoreId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_type_description", property="weTypeDescription", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_type_description_lang", property="weTypeDescriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_type_icon_id", property="weTypeIconId", jdbcType=JdbcType.VARCHAR),
        @Result(column="layout_type_enum_id", property="layoutTypeEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="etch", property="etch", jdbcType=JdbcType.VARCHAR),
        @Result(column="purpose_etch", property="purposeEtch", jdbcType=JdbcType.VARCHAR),
        @Result(column="child_template_id", property="childTemplateId", jdbcType=JdbcType.VARCHAR),
        @Result(column="enable_multi_year_flag", property="enableMultiYearFlag", jdbcType=JdbcType.CHAR),
        @Result(column="we_status_descr", property="weStatusDescr", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_status_descr_lang", property="weStatusDescrLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_activation", property="weActivation", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_status_type_id", property="weStatusTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_context_id", property="weContextId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_context_description", property="weContextDescription", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_is_template", property="weIsTemplate", jdbcType=JdbcType.CHAR),
        @Result(column="we_is_root", property="weIsRoot", jdbcType=JdbcType.CHAR),
        @Result(column="we_is_in_only_one_card", property="weIsInOnlyOneCard", jdbcType=JdbcType.CHAR),
        @Result(column="we_hierarchy_type_id", property="weHierarchyTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_int_org_code", property="weIntOrgCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_int_org_descr", property="weIntOrgDescr", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_int_org_descr_lang", property="weIntOrgDescrLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_org_role_descr", property="weOrgRoleDescr", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_org_party_code", property="weOrgPartyCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_org_party_descr", property="weOrgPartyDescr", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_org_party_descr_lang", property="weOrgPartyDescrLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_type_period_id", property="weTypePeriodId", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_type_period_desc", property="weTypePeriodDesc", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_etch", property="weEtch", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_parent_type_etch", property="weParentTypeEtch", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_parent_etch", property="weParentEtch", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_parent_name", property="weParentName", jdbcType=JdbcType.VARCHAR),
        @Result(column="we_parent_id", property="weParentId", jdbcType=JdbcType.VARCHAR)
    })
    List<WorkEffortView> selectAll();



    @Select({
            "select wew.WORK_EFFORT_ID , wew.WORK_EFFORT_NAME , wew.WORK_EFFORT_NAME_LANG , wew.ETCH ,",
            "p.party_name as party_name, p.party_name_lang as party_name_lang",
            "from work_effort_view wew",
            "join party p on p.party_id = wew.org_unit_id",
            "where wew.work_effort_snapshot_id is null and wew.organization_id = #{organizationId,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffortViewEx", value = {
            @Result(column="work_effort_id", property="workEffortId", jdbcType=JdbcType.VARCHAR),
            @Result(column="work_effort_name", property="workEffortName", jdbcType=JdbcType.VARCHAR),
            @Result(column="work_effort_name_lang", property="workEffortNameLang", jdbcType=JdbcType.VARCHAR),
            @Result(column="etch", property="etch", jdbcType=JdbcType.VARCHAR),
            @Result(column="party_name", property="party.partyName", jdbcType=JdbcType.VARCHAR),
            @Result(column="party_name_lang", property="party.partyNameLang", jdbcType=JdbcType.VARCHAR),
            @Result(column="wet_description", property="workEffortType.description", jdbcType=JdbcType.VARCHAR),
            @Result(column="wet_description_lang", property="workEffortType.descriptionLang", jdbcType=JdbcType.VARCHAR)
    })
    List<WorkEffortViewEx> selectAllWhereWorkEffortRevisionIdIsNullAndByOrganizationId(@Param("organizationId") String organizationId);

    List<WorkEffortView> getWorkEffortViewFilter(@Param("organizationId") String organizationId, @Param("field") String field, @Param("val") String value);

}