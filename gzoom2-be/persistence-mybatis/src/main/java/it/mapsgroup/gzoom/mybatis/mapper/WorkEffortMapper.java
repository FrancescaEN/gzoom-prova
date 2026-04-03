package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffort;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortEx;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface WorkEffortMapper {

    int anonymizeWorkEffort(Instant expirationDate);

    @Delete({
            "delete from work_effort",
            "where work_effort_id = #{workEffortId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String workEffortId);


    @Insert({
            "insert into work_effort (work_effort_id, work_effort_type_id, ",
            "current_status_id, last_status_update, ",
            "work_effort_purpose_type_id, work_effort_parent_id, ",
            "scope_enum_id, priority, ",
            "percent_complete, work_effort_name, ",
            "show_as_enum_id, send_notification_email, ",
            "description, location_desc, ",
            "estimated_start_date, estimated_completion_date, ",
            "actual_start_date, actual_completion_date, ",
            "estimated_milli_seconds, estimated_setup_millis, ",
            "estimate_calc_method, actual_milli_seconds, ",
            "actual_setup_millis, total_milli_seconds_allowed, ",
            "total_money_allowed, money_uom_id, ",
            "special_terms, time_transparency, ",
            "universal_id, source_reference_id, ",
            "fixed_asset_id, facility_id, ",
            "info_url, recurrence_info_id, ",
            "temp_expr_id, runtime_data_id, ",
            "note_id, service_loader_name, ",
            "quantity_to_produce, quantity_produced, ",
            "quantity_rejected, reserv_persons, ",
            "reserv2nd_p_p_perc, reserv_nth_p_p_perc, ",
            "accommodation_map_id, accommodation_spot_id, ",
            "revision_number, created_date, ",
            "created_by_user_login, last_modified_date, ",
            "last_modified_by_user_login, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, ",
            "created_tx_stamp, sequence_num, ",
            "organization_id, effort_uom_id, ",
            "product_id, empl_position_type_id, ",
            "local_name_content_id, estimated_total_effort, ",
            "weight_kpi, weight_review, ",
            "weight_sons, org_unit_role_type_id, ",
            "org_unit_id, weight_assoc_work_effort, ",
            "work_effort_assoc_type_id, last_correct_score_date, ",
            "total_enum_id_kpi, total_enum_id_sons, ",
            "total_enum_id_assoc, process_id, ",
            "is_posted, etch, work_effort_type_period_id, ",
            "snap_shot_date, snap_shot_description, ",
            "work_effort_snapshot_id, uom_range_score_id, ",
            "work_effort_revision_id, scheduled_start_date, ",
            "scheduled_completion_date, work_effort_name_lang, ",
            "description_lang, data_soll)",
            "values (#{workEffortId,jdbcType=VARCHAR}, #{workEffortTypeId,jdbcType=VARCHAR}, ",
            "#{currentStatusId,jdbcType=VARCHAR}, #{lastStatusUpdate,jdbcType=TIMESTAMP}, ",
            "#{workEffortPurposeTypeId,jdbcType=VARCHAR}, #{workEffortParentId,jdbcType=VARCHAR}, ",
            "#{scopeEnumId,jdbcType=VARCHAR}, #{priority,jdbcType=NUMERIC}, ",
            "#{percentComplete,jdbcType=NUMERIC}, #{workEffortName,jdbcType=VARCHAR}, ",
            "#{showAsEnumId,jdbcType=VARCHAR}, #{sendNotificationEmail,jdbcType=CHAR}, ",
            "#{description,jdbcType=VARCHAR}, #{locationDesc,jdbcType=VARCHAR}, ",
            "#{estimatedStartDate,jdbcType=TIMESTAMP}, #{estimatedCompletionDate,jdbcType=TIMESTAMP}, ",
            "#{actualStartDate,jdbcType=TIMESTAMP}, #{actualCompletionDate,jdbcType=TIMESTAMP}, ",
            "#{estimatedMilliSeconds,jdbcType=DOUBLE}, #{estimatedSetupMillis,jdbcType=DOUBLE}, ",
            "#{estimateCalcMethod,jdbcType=VARCHAR}, #{actualMilliSeconds,jdbcType=DOUBLE}, ",
            "#{actualSetupMillis,jdbcType=DOUBLE}, #{totalMilliSecondsAllowed,jdbcType=DOUBLE}, ",
            "#{totalMoneyAllowed,jdbcType=NUMERIC}, #{moneyUomId,jdbcType=VARCHAR}, ",
            "#{specialTerms,jdbcType=VARCHAR}, #{timeTransparency,jdbcType=NUMERIC}, ",
            "#{universalId,jdbcType=VARCHAR}, #{sourceReferenceId,jdbcType=VARCHAR}, ",
            "#{fixedAssetId,jdbcType=VARCHAR}, #{facilityId,jdbcType=VARCHAR}, ",
            "#{infoUrl,jdbcType=VARCHAR}, #{recurrenceInfoId,jdbcType=VARCHAR}, ",
            "#{tempExprId,jdbcType=VARCHAR}, #{runtimeDataId,jdbcType=VARCHAR}, ",
            "#{noteId,jdbcType=VARCHAR}, #{serviceLoaderName,jdbcType=VARCHAR}, ",
            "#{quantityToProduce,jdbcType=DOUBLE}, #{quantityProduced,jdbcType=DOUBLE}, ",
            "#{quantityRejected,jdbcType=DOUBLE}, #{reservPersons,jdbcType=DOUBLE}, ",
            "#{reserv2ndPPPerc,jdbcType=DOUBLE}, #{reservNthPPPerc,jdbcType=DOUBLE}, ",
            "#{accommodationMapId,jdbcType=VARCHAR}, #{accommodationSpotId,jdbcType=VARCHAR}, ",
            "#{revisionNumber,jdbcType=NUMERIC}, #{createdDate,jdbcType=TIMESTAMP}, ",
            "#{createdByUserLogin,jdbcType=VARCHAR}, #{lastModifiedDate,jdbcType=TIMESTAMP}, ",
            "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
            "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
            "#{createdTxStamp,jdbcType=TIMESTAMP}, #{sequenceNum,jdbcType=NUMERIC}, ",
            "#{organizationId,jdbcType=VARCHAR}, #{effortUomId,jdbcType=VARCHAR}, ",
            "#{productId,jdbcType=VARCHAR}, #{emplPositionTypeId,jdbcType=VARCHAR}, ",
            "#{localNameContentId,jdbcType=VARCHAR}, #{estimatedTotalEffort,jdbcType=DOUBLE}, ",
            "#{weightKpi,jdbcType=DOUBLE}, #{weightReview,jdbcType=DOUBLE}, ",
            "#{weightSons,jdbcType=DOUBLE}, #{orgUnitRoleTypeId,jdbcType=VARCHAR}, ",
            "#{orgUnitId,jdbcType=VARCHAR}, #{weightAssocWorkEffort,jdbcType=DOUBLE}, ",
            "#{workEffortAssocTypeId,jdbcType=VARCHAR}, #{lastCorrectScoreDate,jdbcType=TIMESTAMP}, ",
            "#{totalEnumIdKpi,jdbcType=VARCHAR}, #{totalEnumIdSons,jdbcType=VARCHAR}, ",
            "#{totalEnumIdAssoc,jdbcType=VARCHAR}, #{processId,jdbcType=VARCHAR}, ",
            "#{isPosted,jdbcType=CHAR}, #{etch,jdbcType=VARCHAR}, #{workEffortTypePeriodId,jdbcType=VARCHAR}, ",
            "#{snapShotDate,jdbcType=TIMESTAMP}, #{snapShotDescription,jdbcType=VARCHAR}, ",
            "#{workEffortSnapshotId,jdbcType=VARCHAR}, #{uomRangeScoreId,jdbcType=VARCHAR}, ",
            "#{workEffortRevisionId,jdbcType=VARCHAR}, #{scheduledStartDate,jdbcType=TIMESTAMP}, ",
            "#{scheduledCompletionDate,jdbcType=TIMESTAMP}, #{workEffortNameLang,jdbcType=VARCHAR}, ",
            "#{descriptionLang,jdbcType=VARCHAR}, #{dataSoll,jdbcType=TIMESTAMP})"
    })
    int insert(WorkEffort row);


    @Select({
            "select",
            "work_effort_id, work_effort_type_id, current_status_id, last_status_update, ",
            "work_effort_purpose_type_id, work_effort_parent_id, scope_enum_id, priority, ",
            "percent_complete, work_effort_name, show_as_enum_id, send_notification_email, ",
            "description, location_desc, estimated_start_date, estimated_completion_date, ",
            "actual_start_date, actual_completion_date, estimated_milli_seconds, estimated_setup_millis, ",
            "estimate_calc_method, actual_milli_seconds, actual_setup_millis, total_milli_seconds_allowed, ",
            "total_money_allowed, money_uom_id, special_terms, time_transparency, universal_id, ",
            "source_reference_id, fixed_asset_id, facility_id, info_url, recurrence_info_id, ",
            "temp_expr_id, runtime_data_id, note_id, service_loader_name, quantity_to_produce, ",
            "quantity_produced, quantity_rejected, reserv_persons, reserv2nd_p_p_perc, reserv_nth_p_p_perc, ",
            "accommodation_map_id, accommodation_spot_id, revision_number, created_date, ",
            "created_by_user_login, last_modified_date, last_modified_by_user_login, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp, sequence_num, organization_id, ",
            "effort_uom_id, product_id, empl_position_type_id, local_name_content_id, estimated_total_effort, ",
            "weight_kpi, weight_review, weight_sons, org_unit_role_type_id, org_unit_id, ",
            "weight_assoc_work_effort, work_effort_assoc_type_id, last_correct_score_date, ",
            "total_enum_id_kpi, total_enum_id_sons, total_enum_id_assoc, process_id, is_posted, ",
            "etch, work_effort_type_period_id, snap_shot_date, snap_shot_description, work_effort_snapshot_id, ",
            "uom_range_score_id, work_effort_revision_id, scheduled_start_date, scheduled_completion_date, ",
            "work_effort_name_lang, description_lang, data_soll",
            "from work_effort",
            "where work_effort_id = #{workEffortId,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffort", value = {
            @Result(column="work_effort_id", property="workEffortId", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="work_effort_type_id", property="workEffortTypeId", jdbcType=JdbcType.VARCHAR),
            @Result(column="current_status_id", property="currentStatusId", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_status_update", property="lastStatusUpdate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="work_effort_purpose_type_id", property="workEffortPurposeTypeId", jdbcType=JdbcType.VARCHAR),
            @Result(column="work_effort_parent_id", property="workEffortParentId", jdbcType=JdbcType.VARCHAR),
            @Result(column="scope_enum_id", property="scopeEnumId", jdbcType=JdbcType.VARCHAR),
            @Result(column="priority", property="priority", jdbcType=JdbcType.NUMERIC),
            @Result(column="percent_complete", property="percentComplete", jdbcType=JdbcType.NUMERIC),
            @Result(column="work_effort_name", property="workEffortName", jdbcType=JdbcType.VARCHAR),
            @Result(column="show_as_enum_id", property="showAsEnumId", jdbcType=JdbcType.VARCHAR),
            @Result(column="send_notification_email", property="sendNotificationEmail", jdbcType=JdbcType.CHAR),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
            @Result(column="location_desc", property="locationDesc", jdbcType=JdbcType.VARCHAR),
            @Result(column="estimated_start_date", property="estimatedStartDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="estimated_completion_date", property="estimatedCompletionDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="actual_start_date", property="actualStartDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="actual_completion_date", property="actualCompletionDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="estimated_milli_seconds", property="estimatedMilliSeconds", jdbcType=JdbcType.DOUBLE),
            @Result(column="estimated_setup_millis", property="estimatedSetupMillis", jdbcType=JdbcType.DOUBLE),
            @Result(column="estimate_calc_method", property="estimateCalcMethod", jdbcType=JdbcType.VARCHAR),
            @Result(column="actual_milli_seconds", property="actualMilliSeconds", jdbcType=JdbcType.DOUBLE),
            @Result(column="actual_setup_millis", property="actualSetupMillis", jdbcType=JdbcType.DOUBLE),
            @Result(column="total_milli_seconds_allowed", property="totalMilliSecondsAllowed", jdbcType=JdbcType.DOUBLE),
            @Result(column="total_money_allowed", property="totalMoneyAllowed", jdbcType=JdbcType.NUMERIC),
            @Result(column="money_uom_id", property="moneyUomId", jdbcType=JdbcType.VARCHAR),
            @Result(column="special_terms", property="specialTerms", jdbcType=JdbcType.VARCHAR),
            @Result(column="time_transparency", property="timeTransparency", jdbcType=JdbcType.NUMERIC),
            @Result(column="universal_id", property="universalId", jdbcType=JdbcType.VARCHAR),
            @Result(column="source_reference_id", property="sourceReferenceId", jdbcType=JdbcType.VARCHAR),
            @Result(column="fixed_asset_id", property="fixedAssetId", jdbcType=JdbcType.VARCHAR),
            @Result(column="facility_id", property="facilityId", jdbcType=JdbcType.VARCHAR),
            @Result(column="info_url", property="infoUrl", jdbcType=JdbcType.VARCHAR),
            @Result(column="recurrence_info_id", property="recurrenceInfoId", jdbcType=JdbcType.VARCHAR),
            @Result(column="temp_expr_id", property="tempExprId", jdbcType=JdbcType.VARCHAR),
            @Result(column="runtime_data_id", property="runtimeDataId", jdbcType=JdbcType.VARCHAR),
            @Result(column="note_id", property="noteId", jdbcType=JdbcType.VARCHAR),
            @Result(column="service_loader_name", property="serviceLoaderName", jdbcType=JdbcType.VARCHAR),
            @Result(column="quantity_to_produce", property="quantityToProduce", jdbcType=JdbcType.DOUBLE),
            @Result(column="quantity_produced", property="quantityProduced", jdbcType=JdbcType.DOUBLE),
            @Result(column="quantity_rejected", property="quantityRejected", jdbcType=JdbcType.DOUBLE),
            @Result(column="reserv_persons", property="reservPersons", jdbcType=JdbcType.DOUBLE),
            @Result(column="reserv2nd_p_p_perc", property="reserv2ndPPPerc", jdbcType=JdbcType.DOUBLE),
            @Result(column="reserv_nth_p_p_perc", property="reservNthPPPerc", jdbcType=JdbcType.DOUBLE),
            @Result(column="accommodation_map_id", property="accommodationMapId", jdbcType=JdbcType.VARCHAR),
            @Result(column="accommodation_spot_id", property="accommodationSpotId", jdbcType=JdbcType.VARCHAR),
            @Result(column="revision_number", property="revisionNumber", jdbcType=JdbcType.NUMERIC),
            @Result(column="created_date", property="createdDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_modified_date", property="lastModifiedDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="sequence_num", property="sequenceNum", jdbcType=JdbcType.NUMERIC),
            @Result(column="organization_id", property="organizationId", jdbcType=JdbcType.VARCHAR),
            @Result(column="effort_uom_id", property="effortUomId", jdbcType=JdbcType.VARCHAR),
            @Result(column="product_id", property="productId", jdbcType=JdbcType.VARCHAR),
            @Result(column="empl_position_type_id", property="emplPositionTypeId", jdbcType=JdbcType.VARCHAR),
            @Result(column="local_name_content_id", property="localNameContentId", jdbcType=JdbcType.VARCHAR),
            @Result(column="estimated_total_effort", property="estimatedTotalEffort", jdbcType=JdbcType.DOUBLE),
            @Result(column="weight_kpi", property="weightKpi", jdbcType=JdbcType.DOUBLE),
            @Result(column="weight_review", property="weightReview", jdbcType=JdbcType.DOUBLE),
            @Result(column="weight_sons", property="weightSons", jdbcType=JdbcType.DOUBLE),
            @Result(column="org_unit_role_type_id", property="orgUnitRoleTypeId", jdbcType=JdbcType.VARCHAR),
            @Result(column="org_unit_id", property="orgUnitId", jdbcType=JdbcType.VARCHAR),
            @Result(column="weight_assoc_work_effort", property="weightAssocWorkEffort", jdbcType=JdbcType.DOUBLE),
            @Result(column="work_effort_assoc_type_id", property="workEffortAssocTypeId", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_correct_score_date", property="lastCorrectScoreDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="total_enum_id_kpi", property="totalEnumIdKpi", jdbcType=JdbcType.VARCHAR),
            @Result(column="total_enum_id_sons", property="totalEnumIdSons", jdbcType=JdbcType.VARCHAR),
            @Result(column="total_enum_id_assoc", property="totalEnumIdAssoc", jdbcType=JdbcType.VARCHAR),
            @Result(column="process_id", property="processId", jdbcType=JdbcType.VARCHAR),
            @Result(column="is_posted", property="isPosted", jdbcType=JdbcType.CHAR),
            @Result(column="etch", property="etch", jdbcType=JdbcType.VARCHAR),
            @Result(column="work_effort_type_period_id", property="workEffortTypePeriodId", jdbcType=JdbcType.VARCHAR),
            @Result(column="snap_shot_date", property="snapShotDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="snap_shot_description", property="snapShotDescription", jdbcType=JdbcType.VARCHAR),
            @Result(column="work_effort_snapshot_id", property="workEffortSnapshotId", jdbcType=JdbcType.VARCHAR),
            @Result(column="uom_range_score_id", property="uomRangeScoreId", jdbcType=JdbcType.VARCHAR),
            @Result(column="work_effort_revision_id", property="workEffortRevisionId", jdbcType=JdbcType.VARCHAR),
            @Result(column="scheduled_start_date", property="scheduledStartDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="scheduled_completion_date", property="scheduledCompletionDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="work_effort_name_lang", property="workEffortNameLang", jdbcType=JdbcType.VARCHAR),
            @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
            @Result(column="data_soll", property="dataSoll", jdbcType=JdbcType.TIMESTAMP)
    })
    WorkEffort selectByPrimaryKey(String workEffortId);


    @Select({
            "select",
            "work_effort_id, work_effort_type_id, current_status_id, last_status_update, ",
            "work_effort_purpose_type_id, work_effort_parent_id, scope_enum_id, priority, ",
            "percent_complete, work_effort_name, show_as_enum_id, send_notification_email, ",
            "description, location_desc, estimated_start_date, estimated_completion_date, ",
            "actual_start_date, actual_completion_date, estimated_milli_seconds, estimated_setup_millis, ",
            "estimate_calc_method, actual_milli_seconds, actual_setup_millis, total_milli_seconds_allowed, ",
            "total_money_allowed, money_uom_id, special_terms, time_transparency, universal_id, ",
            "source_reference_id, fixed_asset_id, facility_id, info_url, recurrence_info_id, ",
            "temp_expr_id, runtime_data_id, note_id, service_loader_name, quantity_to_produce, ",
            "quantity_produced, quantity_rejected, reserv_persons, reserv2nd_p_p_perc, reserv_nth_p_p_perc, ",
            "accommodation_map_id, accommodation_spot_id, revision_number, created_date, ",
            "created_by_user_login, last_modified_date, last_modified_by_user_login, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp, sequence_num, organization_id, ",
            "effort_uom_id, product_id, empl_position_type_id, local_name_content_id, estimated_total_effort, ",
            "weight_kpi, weight_review, weight_sons, org_unit_role_type_id, org_unit_id, ",
            "weight_assoc_work_effort, work_effort_assoc_type_id, last_correct_score_date, ",
            "total_enum_id_kpi, total_enum_id_sons, total_enum_id_assoc, process_id, is_posted, ",
            "etch, work_effort_type_period_id, snap_shot_date, snap_shot_description, work_effort_snapshot_id, ",
            "uom_range_score_id, work_effort_revision_id, scheduled_start_date, scheduled_completion_date, ",
            "work_effort_name_lang, description_lang, data_soll",
            "from work_effort",
            "order by work_effort_name"
    })
    @ResultMap("workEffort")
    List<WorkEffort> selectAllOrderByWorkEffortName();

    @Select({
            "select WORK_EFFORT.ACCOMMODATION_MAP_ID, WORK_EFFORT.ACCOMMODATION_SPOT_ID, WORK_EFFORT.ACTUAL_COMPLETION_DATE, WORK_EFFORT.ACTUAL_MILLI_SECONDS, WORK_EFFORT.ACTUAL_SETUP_MILLIS, WORK_EFFORT.ACTUAL_START_DATE, WORK_EFFORT.CREATED_BY_USER_LOGIN, WORK_EFFORT.CREATED_DATE, WORK_EFFORT.CREATED_STAMP, WORK_EFFORT.CREATED_TX_STAMP, WORK_EFFORT.CURRENT_STATUS_ID, WORK_EFFORT.DATA_SOLL, WORK_EFFORT.DESCRIPTION, WORK_EFFORT.DESCRIPTION_LANG, WORK_EFFORT.EFFORT_UOM_ID, WORK_EFFORT.EMPL_POSITION_TYPE_ID, WORK_EFFORT.ESTIMATE_CALC_METHOD, WORK_EFFORT.ESTIMATED_COMPLETION_DATE, WORK_EFFORT.ESTIMATED_MILLI_SECONDS, WORK_EFFORT.ESTIMATED_SETUP_MILLIS, WORK_EFFORT.ESTIMATED_START_DATE, WORK_EFFORT.ESTIMATED_TOTAL_EFFORT, WORK_EFFORT.ETCH, WORK_EFFORT.FACILITY_ID, WORK_EFFORT.FIXED_ASSET_ID, WORK_EFFORT.INFO_URL, WORK_EFFORT.IS_POSTED, WORK_EFFORT.LAST_CORRECT_SCORE_DATE, WORK_EFFORT.LAST_MODIFIED_BY_USER_LOGIN, WORK_EFFORT.LAST_MODIFIED_DATE, WORK_EFFORT.LAST_STATUS_UPDATE, WORK_EFFORT.LAST_UPDATED_STAMP, WORK_EFFORT.LAST_UPDATED_TX_STAMP, WORK_EFFORT.LOCAL_NAME_CONTENT_ID, WORK_EFFORT.LOCATION_DESC, WORK_EFFORT.MONEY_UOM_ID, WORK_EFFORT.NOTE_ID, WORK_EFFORT.ORGANIZATION_ID, WORK_EFFORT.ORG_UNIT_ID, WORK_EFFORT.ORG_UNIT_ROLE_TYPE_ID, WORK_EFFORT.PERCENT_COMPLETE, WORK_EFFORT.PRIORITY, WORK_EFFORT.PROCESS_ID, WORK_EFFORT.PRODUCT_ID, WORK_EFFORT.QUANTITY_PRODUCED, WORK_EFFORT.QUANTITY_REJECTED, WORK_EFFORT.QUANTITY_TO_PRODUCE, WORK_EFFORT.RECURRENCE_INFO_ID, WORK_EFFORT.RESERV2ND_P_P_PERC, WORK_EFFORT.RESERV_NTH_P_P_PERC, WORK_EFFORT.RESERV_PERSONS, WORK_EFFORT.REVISION_NUMBER, WORK_EFFORT.RUNTIME_DATA_ID, WORK_EFFORT.SCHEDULED_COMPLETION_DATE, WORK_EFFORT.SCHEDULED_START_DATE, WORK_EFFORT.SCOPE_ENUM_ID, WORK_EFFORT.SEND_NOTIFICATION_EMAIL, WORK_EFFORT.SEQUENCE_NUM, WORK_EFFORT.SERVICE_LOADER_NAME, WORK_EFFORT.SHOW_AS_ENUM_ID, WORK_EFFORT.SNAP_SHOT_DATE, WORK_EFFORT.SNAP_SHOT_DESCRIPTION, WORK_EFFORT.SOURCE_REFERENCE_ID, WORK_EFFORT.SPECIAL_TERMS, WORK_EFFORT.TEMP_EXPR_ID, WORK_EFFORT.TIME_TRANSPARENCY, WORK_EFFORT.TOTAL_ENUM_ID_ASSOC, WORK_EFFORT.TOTAL_ENUM_ID_KPI, WORK_EFFORT.TOTAL_ENUM_ID_SONS, WORK_EFFORT.TOTAL_MILLI_SECONDS_ALLOWED, WORK_EFFORT.TOTAL_MONEY_ALLOWED, WORK_EFFORT.UNIVERSAL_ID, WORK_EFFORT.UOM_RANGE_SCORE_ID, WORK_EFFORT.WEIGHT_ASSOC_WORK_EFFORT, WORK_EFFORT.WEIGHT_KPI, WORK_EFFORT.WEIGHT_REVIEW, WORK_EFFORT.WEIGHT_SONS, WORK_EFFORT.WORK_EFFORT_ASSOC_TYPE_ID, WORK_EFFORT.WORK_EFFORT_ID, WORK_EFFORT.WORK_EFFORT_NAME, WORK_EFFORT.WORK_EFFORT_NAME_LANG, WORK_EFFORT.WORK_EFFORT_PARENT_ID, WORK_EFFORT.WORK_EFFORT_PURPOSE_TYPE_ID, WORK_EFFORT.WORK_EFFORT_REVISION_ID, WORK_EFFORT.WORK_EFFORT_SNAPSHOT_ID, WORK_EFFORT.WORK_EFFORT_TYPE_ID, WORK_EFFORT.WORK_EFFORT_TYPE_PERIOD_ID\n" +
                    "from WORK_EFFORT WORK_EFFORT\n" +
                    "where WORK_EFFORT.WORK_EFFORT_PARENT_ID = #{workEffortParentId}\n" +
                    "order by WORK_EFFORT.WORK_EFFORT_NAME"
    })
    @ResultMap("workEffort")
    List<WorkEffort> selectByWorkEffortParentIdOrderByWorkEffortName(String workEffortParentId);


    @Select({
            "select WORK_EFFORT.ACCOMMODATION_MAP_ID, WORK_EFFORT.ACCOMMODATION_SPOT_ID, WORK_EFFORT.ACTUAL_COMPLETION_DATE, WORK_EFFORT.ACTUAL_MILLI_SECONDS, WORK_EFFORT.ACTUAL_SETUP_MILLIS, WORK_EFFORT.ACTUAL_START_DATE, WORK_EFFORT.CREATED_BY_USER_LOGIN, WORK_EFFORT.CREATED_DATE, WORK_EFFORT.CREATED_STAMP, WORK_EFFORT.CREATED_TX_STAMP, WORK_EFFORT.CURRENT_STATUS_ID, WORK_EFFORT.DATA_SOLL, WORK_EFFORT.DESCRIPTION, WORK_EFFORT.DESCRIPTION_LANG, WORK_EFFORT.EFFORT_UOM_ID, WORK_EFFORT.EMPL_POSITION_TYPE_ID, WORK_EFFORT.ESTIMATE_CALC_METHOD, WORK_EFFORT.ESTIMATED_COMPLETION_DATE, WORK_EFFORT.ESTIMATED_MILLI_SECONDS, WORK_EFFORT.ESTIMATED_SETUP_MILLIS, WORK_EFFORT.ESTIMATED_START_DATE, WORK_EFFORT.ESTIMATED_TOTAL_EFFORT, WORK_EFFORT.ETCH, WORK_EFFORT.FACILITY_ID, WORK_EFFORT.FIXED_ASSET_ID, WORK_EFFORT.INFO_URL, WORK_EFFORT.IS_POSTED, WORK_EFFORT.LAST_CORRECT_SCORE_DATE, WORK_EFFORT.LAST_MODIFIED_BY_USER_LOGIN, WORK_EFFORT.LAST_MODIFIED_DATE, WORK_EFFORT.LAST_STATUS_UPDATE, WORK_EFFORT.LAST_UPDATED_STAMP, WORK_EFFORT.LAST_UPDATED_TX_STAMP, WORK_EFFORT.LOCAL_NAME_CONTENT_ID, WORK_EFFORT.LOCATION_DESC, WORK_EFFORT.MONEY_UOM_ID, WORK_EFFORT.NOTE_ID, WORK_EFFORT.ORGANIZATION_ID, WORK_EFFORT.ORG_UNIT_ID, WORK_EFFORT.ORG_UNIT_ROLE_TYPE_ID, WORK_EFFORT.PERCENT_COMPLETE, WORK_EFFORT.PRIORITY, WORK_EFFORT.PROCESS_ID, WORK_EFFORT.PRODUCT_ID, WORK_EFFORT.QUANTITY_PRODUCED, WORK_EFFORT.QUANTITY_REJECTED, WORK_EFFORT.QUANTITY_TO_PRODUCE, WORK_EFFORT.RECURRENCE_INFO_ID, WORK_EFFORT.RESERV2ND_P_P_PERC, WORK_EFFORT.RESERV_NTH_P_P_PERC, WORK_EFFORT.RESERV_PERSONS, WORK_EFFORT.REVISION_NUMBER, WORK_EFFORT.RUNTIME_DATA_ID, WORK_EFFORT.SCHEDULED_COMPLETION_DATE, WORK_EFFORT.SCHEDULED_START_DATE, WORK_EFFORT.SCOPE_ENUM_ID, WORK_EFFORT.SEND_NOTIFICATION_EMAIL, WORK_EFFORT.SEQUENCE_NUM, WORK_EFFORT.SERVICE_LOADER_NAME, WORK_EFFORT.SHOW_AS_ENUM_ID, WORK_EFFORT.SNAP_SHOT_DATE, WORK_EFFORT.SNAP_SHOT_DESCRIPTION, WORK_EFFORT.SOURCE_REFERENCE_ID, WORK_EFFORT.SPECIAL_TERMS, WORK_EFFORT.TEMP_EXPR_ID, WORK_EFFORT.TIME_TRANSPARENCY, WORK_EFFORT.TOTAL_ENUM_ID_ASSOC, WORK_EFFORT.TOTAL_ENUM_ID_KPI, WORK_EFFORT.TOTAL_ENUM_ID_SONS, WORK_EFFORT.TOTAL_MILLI_SECONDS_ALLOWED, WORK_EFFORT.TOTAL_MONEY_ALLOWED, WORK_EFFORT.UNIVERSAL_ID, WORK_EFFORT.UOM_RANGE_SCORE_ID, WORK_EFFORT.WEIGHT_ASSOC_WORK_EFFORT, WORK_EFFORT.WEIGHT_KPI, WORK_EFFORT.WEIGHT_REVIEW, WORK_EFFORT.WEIGHT_SONS, WORK_EFFORT.WORK_EFFORT_ASSOC_TYPE_ID, WORK_EFFORT.WORK_EFFORT_ID, WORK_EFFORT.WORK_EFFORT_NAME, WORK_EFFORT.WORK_EFFORT_NAME_LANG, WORK_EFFORT.WORK_EFFORT_PARENT_ID, WORK_EFFORT.WORK_EFFORT_PURPOSE_TYPE_ID, WORK_EFFORT.WORK_EFFORT_REVISION_ID, WORK_EFFORT.WORK_EFFORT_SNAPSHOT_ID, WORK_EFFORT.WORK_EFFORT_TYPE_ID, WORK_EFFORT.WORK_EFFORT_TYPE_PERIOD_ID\n" +
                    "from WORK_EFFORT WORK_EFFORT\n" +
                    "inner join WORK_EFFORT_TYPE WORK_EFFORT_TYPE\n" +
                    "on WORK_EFFORT_TYPE.WORK_EFFORT_TYPE_ID = WORK_EFFORT.WORK_EFFORT_TYPE_ID\n" +
                    "where WORK_EFFORT.WORK_EFFORT_REVISION_ID is null and WORK_EFFORT_TYPE.IS_ROOT = 'Y' and WORK_EFFORT_TYPE.IS_TEMPLATE = 'Y' "
    })
    @ResultMap("workEffort")
    List<WorkEffort> selectWorkEffortsIsRootIsTemplate();


    @Update({
            "update work_effort",
            "set work_effort_type_id = #{workEffortTypeId,jdbcType=VARCHAR},",
            "current_status_id = #{currentStatusId,jdbcType=VARCHAR},",
            "last_status_update = #{lastStatusUpdate,jdbcType=TIMESTAMP},",
            "work_effort_purpose_type_id = #{workEffortPurposeTypeId,jdbcType=VARCHAR},",
            "work_effort_parent_id = #{workEffortParentId,jdbcType=VARCHAR},",
            "scope_enum_id = #{scopeEnumId,jdbcType=VARCHAR},",
            "priority = #{priority,jdbcType=NUMERIC},",
            "percent_complete = #{percentComplete,jdbcType=NUMERIC},",
            "work_effort_name = #{workEffortName,jdbcType=VARCHAR},",
            "show_as_enum_id = #{showAsEnumId,jdbcType=VARCHAR},",
            "send_notification_email = #{sendNotificationEmail,jdbcType=CHAR},",
            "description = #{description,jdbcType=VARCHAR},",
            "location_desc = #{locationDesc,jdbcType=VARCHAR},",
            "estimated_start_date = #{estimatedStartDate,jdbcType=TIMESTAMP},",
            "estimated_completion_date = #{estimatedCompletionDate,jdbcType=TIMESTAMP},",
            "actual_start_date = #{actualStartDate,jdbcType=TIMESTAMP},",
            "actual_completion_date = #{actualCompletionDate,jdbcType=TIMESTAMP},",
            "estimated_milli_seconds = #{estimatedMilliSeconds,jdbcType=DOUBLE},",
            "estimated_setup_millis = #{estimatedSetupMillis,jdbcType=DOUBLE},",
            "estimate_calc_method = #{estimateCalcMethod,jdbcType=VARCHAR},",
            "actual_milli_seconds = #{actualMilliSeconds,jdbcType=DOUBLE},",
            "actual_setup_millis = #{actualSetupMillis,jdbcType=DOUBLE},",
            "total_milli_seconds_allowed = #{totalMilliSecondsAllowed,jdbcType=DOUBLE},",
            "total_money_allowed = #{totalMoneyAllowed,jdbcType=NUMERIC},",
            "money_uom_id = #{moneyUomId,jdbcType=VARCHAR},",
            "special_terms = #{specialTerms,jdbcType=VARCHAR},",
            "time_transparency = #{timeTransparency,jdbcType=NUMERIC},",
            "universal_id = #{universalId,jdbcType=VARCHAR},",
            "source_reference_id = #{sourceReferenceId,jdbcType=VARCHAR},",
            "fixed_asset_id = #{fixedAssetId,jdbcType=VARCHAR},",
            "facility_id = #{facilityId,jdbcType=VARCHAR},",
            "info_url = #{infoUrl,jdbcType=VARCHAR},",
            "recurrence_info_id = #{recurrenceInfoId,jdbcType=VARCHAR},",
            "temp_expr_id = #{tempExprId,jdbcType=VARCHAR},",
            "runtime_data_id = #{runtimeDataId,jdbcType=VARCHAR},",
            "note_id = #{noteId,jdbcType=VARCHAR},",
            "service_loader_name = #{serviceLoaderName,jdbcType=VARCHAR},",
            "quantity_to_produce = #{quantityToProduce,jdbcType=DOUBLE},",
            "quantity_produced = #{quantityProduced,jdbcType=DOUBLE},",
            "quantity_rejected = #{quantityRejected,jdbcType=DOUBLE},",
            "reserv_persons = #{reservPersons,jdbcType=DOUBLE},",
            "reserv2nd_p_p_perc = #{reserv2ndPPPerc,jdbcType=DOUBLE},",
            "reserv_nth_p_p_perc = #{reservNthPPPerc,jdbcType=DOUBLE},",
            "accommodation_map_id = #{accommodationMapId,jdbcType=VARCHAR},",
            "accommodation_spot_id = #{accommodationSpotId,jdbcType=VARCHAR},",
            "revision_number = #{revisionNumber,jdbcType=NUMERIC},",
            "last_modified_date = #{lastModifiedDate,jdbcType=TIMESTAMP},",
            "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
            "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
            "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
            "sequence_num = #{sequenceNum,jdbcType=NUMERIC},",
            "organization_id = #{organizationId,jdbcType=VARCHAR},",
            "effort_uom_id = #{effortUomId,jdbcType=VARCHAR},",
            "product_id = #{productId,jdbcType=VARCHAR},",
            "empl_position_type_id = #{emplPositionTypeId,jdbcType=VARCHAR},",
            "local_name_content_id = #{localNameContentId,jdbcType=VARCHAR},",
            "estimated_total_effort = #{estimatedTotalEffort,jdbcType=DOUBLE},",
            "weight_kpi = #{weightKpi,jdbcType=DOUBLE},",
            "weight_review = #{weightReview,jdbcType=DOUBLE},",
            "weight_sons = #{weightSons,jdbcType=DOUBLE},",
            "org_unit_role_type_id = #{orgUnitRoleTypeId,jdbcType=VARCHAR},",
            "org_unit_id = #{orgUnitId,jdbcType=VARCHAR},",
            "weight_assoc_work_effort = #{weightAssocWorkEffort,jdbcType=DOUBLE},",
            "work_effort_assoc_type_id = #{workEffortAssocTypeId,jdbcType=VARCHAR},",
            "last_correct_score_date = #{lastCorrectScoreDate,jdbcType=TIMESTAMP},",
            "total_enum_id_kpi = #{totalEnumIdKpi,jdbcType=VARCHAR},",
            "total_enum_id_sons = #{totalEnumIdSons,jdbcType=VARCHAR},",
            "total_enum_id_assoc = #{totalEnumIdAssoc,jdbcType=VARCHAR},",
            "process_id = #{processId,jdbcType=VARCHAR},",
            "is_posted = #{isPosted,jdbcType=CHAR},",
            "etch = #{etch,jdbcType=VARCHAR},",
            "work_effort_type_period_id = #{workEffortTypePeriodId,jdbcType=VARCHAR},",
            "snap_shot_date = #{snapShotDate,jdbcType=TIMESTAMP},",
            "snap_shot_description = #{snapShotDescription,jdbcType=VARCHAR},",
            "work_effort_snapshot_id = #{workEffortSnapshotId,jdbcType=VARCHAR},",
            "uom_range_score_id = #{uomRangeScoreId,jdbcType=VARCHAR},",
            "work_effort_revision_id = #{workEffortRevisionId,jdbcType=VARCHAR},",
            "scheduled_start_date = #{scheduledStartDate,jdbcType=TIMESTAMP},",
            "scheduled_completion_date = #{scheduledCompletionDate,jdbcType=TIMESTAMP},",
            "work_effort_name_lang = #{workEffortNameLang,jdbcType=VARCHAR},",
            "description_lang = #{descriptionLang,jdbcType=VARCHAR},",
            "data_soll = #{dataSoll,jdbcType=TIMESTAMP}",
            "where work_effort_id = #{workEffortId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(WorkEffort row);

    List<WorkEffort> getWorkEfforts(@Param("userLoginId") String userLoginId, @Param("parentTypeId") String parentTypeId,  @Param("filter") Map<String, Object> filter , @Param("useFilter") boolean useFilter, @Param("workEffortTypeIds") String[] workEffortTypeIds, @Param("userPreferenceOrganizationUnitId") String userPreferenceOrganizationUnitId);

    List<WorkEffort> getWorkEffortParents(@Param("workEffortParentId") String workEffortParentId);

    List<WorkEffort> getParentsbyWorkEffort(@Param("workEffortId") String workEffortId);

    WorkEffort getWorkEffort(@Param("workEffortId") String workEffortId);

    WorkEffortEx getWorkEffortEx(@Param("workEffortId") String workEffortId);

    List<WorkEffort> getWorkEffortsIsRootIsTemplate();

    List<WorkEffortEx> getWorkEffortExList(@Param("userPreferenceOrganizationUnitId") String userPreferenceOrganizationUnitId);

    List<WorkEffortEx> getWorkEffortExListPagination(@Param("LIMIT") Integer limit, @Param("OFFSET") Integer offset, @Param("filters") Filter[] filters, @Param("filterGenericLabel") Filter[] filterGenericLabel, @Param("SORTORDER") Integer sortOrder, @Param("SORTFIELD") String sortField, @Param("ORGANIZATIONID") String organizationId, @Param("matchModeSearch") String matchModeSearch, @Param("SECONDARYLANGUAGE") Boolean secondaryLanguage);

    List<WorkEffort> getWorkEffortByOrgId(@Param("userPreferenceOrganizationUnitId") String userPreferenceOrganizationUnitId);


    @Select({
            "select we.WORK_EFFORT_ID , we.WORK_EFFORT_NAME , we.WORK_EFFORT_NAME_LANG , we.ETCH ,",
            "p.party_name as party_name, p.party_name_lang as party_name_lang,",
            "wet.description as wet_description, wet.description_lang as wet_description_lang",
            "from work_effort we",
            "join party p on p.party_id = we.org_unit_id",
            "join work_effort_type wet on wet.work_effort_type_id = we.work_effort_type_id",
            "WHERE WE.WORK_EFFORT_REVISION_ID IS NULL and we.organization_id = #{organizationId,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffortExDropdown", value = {
            @Result(column="work_effort_id", property="workEffortId", jdbcType=JdbcType.VARCHAR),
            @Result(column="work_effort_name", property="workEffortName", jdbcType=JdbcType.VARCHAR),
            @Result(column="work_effort_name_lang", property="workEffortNameLang", jdbcType=JdbcType.VARCHAR),
            @Result(column="etch", property="etch", jdbcType=JdbcType.VARCHAR),
            @Result(column="party_name", property="party.partyName", jdbcType=JdbcType.VARCHAR),
            @Result(column="party_name_lang", property="party.partyNameLang", jdbcType=JdbcType.VARCHAR),
            @Result(column="wet_description", property="workEffortType.description", jdbcType=JdbcType.VARCHAR),
            @Result(column="wet_description_lang", property="workEffortType.descriptionLang", jdbcType=JdbcType.VARCHAR)
    })
    List<WorkEffortEx> getWorkEffortDropdown(@Param("organizationId") String organizationId);
}