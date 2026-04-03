package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.Score;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysis;

import java.time.Instant;
import java.util.List;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysisEx;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysisTypeTypeExt;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WorkEffortAnalysisMapper {

    @Delete({
            "delete from work_effort_analysis",
            "where work_effort_analysis_id = #{workEffortAnalysisId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String workEffortAnalysisId);

    @Update({
            "update work_effort_analysis",
            "set description = #{description,jdbcType=VARCHAR},",
            "work_effort_type_id = #{workEffortTypeId,jdbcType=VARCHAR},",
            "work_effort_id = #{workEffortId,jdbcType=VARCHAR},",
            "data_visibility = #{dataVisibility,jdbcType=VARCHAR},",
            "reference_date = #{referenceDate,jdbcType=TIMESTAMP},",
            "is_monitor = #{isMonitor,jdbcType=CHAR},",
            "year_prev = #{yearPrev,jdbcType=TIMESTAMP},",
            "year_m1_prev = #{yearM1Prev,jdbcType=TIMESTAMP},",
            "year_m2_prev = #{yearM2Prev,jdbcType=TIMESTAMP},",
            "year_m3_prev = #{yearM3Prev,jdbcType=TIMESTAMP},",
            "year_m4_prev = #{yearM4Prev,jdbcType=TIMESTAMP},",
            "year_p1_prev = #{yearP1Prev,jdbcType=TIMESTAMP},",
            "year_p2_prev = #{yearP2Prev,jdbcType=TIMESTAMP},",
            "year_p3_prev = #{yearP3Prev,jdbcType=TIMESTAMP},",
            "year_p4_prev = #{yearP4Prev,jdbcType=TIMESTAMP},",
            "year_real = #{yearReal,jdbcType=TIMESTAMP},",
            "year_m1_real = #{yearM1Real,jdbcType=TIMESTAMP},",
            "year_m2_real = #{yearM2Real,jdbcType=TIMESTAMP},",
            "year_m3_real = #{yearM3Real,jdbcType=TIMESTAMP},",
            "year_m4_real = #{yearM4Real,jdbcType=TIMESTAMP},",
            "year_p1_real = #{yearP1Real,jdbcType=TIMESTAMP},",
            "year_p2_real = #{yearP2Real,jdbcType=TIMESTAMP},",
            "year_p3_real = #{yearP3Real,jdbcType=TIMESTAMP},",
            "year_p4_real = #{yearP4Real,jdbcType=TIMESTAMP},",
            "label_prev = #{labelPrev,jdbcType=VARCHAR},",
            "label_m1_prev = #{labelM1Prev,jdbcType=VARCHAR},",
            "label_m2_prev = #{labelM2Prev,jdbcType=VARCHAR},",
            "label_m3_prev = #{labelM3Prev,jdbcType=VARCHAR},",
            "label_m4_prev = #{labelM4Prev,jdbcType=VARCHAR},",
            "label_p1_prev = #{labelP1Prev,jdbcType=VARCHAR},",
            "label_p2_prev = #{labelP2Prev,jdbcType=VARCHAR},",
            "label_p3_prev = #{labelP3Prev,jdbcType=VARCHAR},",
            "label_p4_prev = #{labelP4Prev,jdbcType=VARCHAR},",
            "label_real = #{labelReal,jdbcType=VARCHAR},",
            "label_m1_real = #{labelM1Real,jdbcType=VARCHAR},",
            "label_m2_real = #{labelM2Real,jdbcType=VARCHAR},",
            "label_m3_real = #{labelM3Real,jdbcType=VARCHAR},",
            "label_m4_real = #{labelM4Real,jdbcType=VARCHAR},",
            "label_p1_real = #{labelP1Real,jdbcType=VARCHAR},",
            "label_p2_real = #{labelP2Real,jdbcType=VARCHAR},",
            "label_p3_real = #{labelP3Real,jdbcType=VARCHAR},",
            "label_p4_real = #{labelP4Real,jdbcType=VARCHAR},",
            "availability_id = #{availabilityId,jdbcType=VARCHAR},",
            "report_id = #{reportId,jdbcType=VARCHAR},",
            "work_effort_purpose_type_id = #{workEffortPurposeTypeId,jdbcType=VARCHAR},",
            "work_effort_type_id_sez1 = #{workEffortTypeIdSez1,jdbcType=VARCHAR},",
            "work_effort_type_id_sez2 = #{workEffortTypeIdSez2,jdbcType=VARCHAR},",
            "work_effort_type_id_sez3 = #{workEffortTypeIdSez3,jdbcType=VARCHAR},",
            "work_effort_type_id_sez4 = #{workEffortTypeIdSez4,jdbcType=VARCHAR},",
            "work_effort_type_id_sez5 = #{workEffortTypeIdSez5,jdbcType=VARCHAR},",
            "work_effort_type_id_sez6 = #{workEffortTypeIdSez6,jdbcType=VARCHAR},",
            "type_balance_score_con_id = #{typeBalanceScoreConId,jdbcType=VARCHAR},",
            "type_balance_score_tar_id = #{typeBalanceScoreTarId,jdbcType=VARCHAR},",
            "type_balance_tar_ind_id = #{typeBalanceTarIndId,jdbcType=VARCHAR},",
            "type_balance_cons_ind_id = #{typeBalanceConsIndId,jdbcType=VARCHAR},",
            "description1 = #{description1,jdbcType=VARCHAR},",
            "description2 = #{description2,jdbcType=VARCHAR},",
            "description3 = #{description3,jdbcType=VARCHAR},",
            "description4 = #{description4,jdbcType=VARCHAR},",
            "description5 = #{description5,jdbcType=VARCHAR},",
            "exclude_validity = #{excludeValidity,jdbcType=CHAR},",
            "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
            "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
            "description_lang = #{descriptionLang,jdbcType=VARCHAR}",
            "where work_effort_analysis_id = #{workEffortAnalysisId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(WorkEffortAnalysis row);

    @Insert({
            "insert into work_effort_analysis (work_effort_analysis_id, description, ",
            "work_effort_type_id, work_effort_id, ",
            "data_visibility, reference_date, ",
            "is_monitor, year_prev, ",
            "year_m1_prev, year_m2_prev, ",
            "year_m3_prev, year_m4_prev, ",
            "year_p1_prev, year_p2_prev, ",
            "year_p3_prev, year_p4_prev, ",
            "year_real, year_m1_real, ",
            "year_m2_real, year_m3_real, ",
            "year_m4_real, year_p1_real, ",
            "year_p2_real, year_p3_real, ",
            "year_p4_real, label_prev, ",
            "label_m1_prev, label_m2_prev, ",
            "label_m3_prev, label_m4_prev, ",
            "label_p1_prev, label_p2_prev, ",
            "label_p3_prev, label_p4_prev, ",
            "label_real, label_m1_real, ",
            "label_m2_real, label_m3_real, ",
            "label_m4_real, label_p1_real, ",
            "label_p2_real, label_p3_real, ",
            "label_p4_real, availability_id, ",
            "report_id, work_effort_purpose_type_id, ",
            "work_effort_type_id_sez1, work_effort_type_id_sez2, ",
            "work_effort_type_id_sez3, work_effort_type_id_sez4, ",
            "work_effort_type_id_sez5, work_effort_type_id_sez6, ",
            "type_balance_score_con_id, type_balance_score_tar_id, ",
            "type_balance_tar_ind_id, type_balance_cons_ind_id, ",
            "description1, description2, ",
            "description3, description4, ",
            "description5, exclude_validity, ",
            "last_updated_stamp, last_updated_tx_stamp, ",
            "created_stamp, created_tx_stamp, ",
            "description_lang)",
            "values (#{workEffortAnalysisId,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
            "#{workEffortTypeId,jdbcType=VARCHAR}, #{workEffortId,jdbcType=VARCHAR}, ",
            "#{dataVisibility,jdbcType=VARCHAR}, #{referenceDate,jdbcType=TIMESTAMP}, ",
            "#{isMonitor,jdbcType=CHAR}, #{yearPrev,jdbcType=TIMESTAMP}, ",
            "#{yearM1Prev,jdbcType=TIMESTAMP}, #{yearM2Prev,jdbcType=TIMESTAMP}, ",
            "#{yearM3Prev,jdbcType=TIMESTAMP}, #{yearM4Prev,jdbcType=TIMESTAMP}, ",
            "#{yearP1Prev,jdbcType=TIMESTAMP}, #{yearP2Prev,jdbcType=TIMESTAMP}, ",
            "#{yearP3Prev,jdbcType=TIMESTAMP}, #{yearP4Prev,jdbcType=TIMESTAMP}, ",
            "#{yearReal,jdbcType=TIMESTAMP}, #{yearM1Real,jdbcType=TIMESTAMP}, ",
            "#{yearM2Real,jdbcType=TIMESTAMP}, #{yearM3Real,jdbcType=TIMESTAMP}, ",
            "#{yearM4Real,jdbcType=TIMESTAMP}, #{yearP1Real,jdbcType=TIMESTAMP}, ",
            "#{yearP2Real,jdbcType=TIMESTAMP}, #{yearP3Real,jdbcType=TIMESTAMP}, ",
            "#{yearP4Real,jdbcType=TIMESTAMP}, #{labelPrev,jdbcType=VARCHAR}, ",
            "#{labelM1Prev,jdbcType=VARCHAR}, #{labelM2Prev,jdbcType=VARCHAR}, ",
            "#{labelM3Prev,jdbcType=VARCHAR}, #{labelM4Prev,jdbcType=VARCHAR}, ",
            "#{labelP1Prev,jdbcType=VARCHAR}, #{labelP2Prev,jdbcType=VARCHAR}, ",
            "#{labelP3Prev,jdbcType=VARCHAR}, #{labelP4Prev,jdbcType=VARCHAR}, ",
            "#{labelReal,jdbcType=VARCHAR}, #{labelM1Real,jdbcType=VARCHAR}, ",
            "#{labelM2Real,jdbcType=VARCHAR}, #{labelM3Real,jdbcType=VARCHAR}, ",
            "#{labelM4Real,jdbcType=VARCHAR}, #{labelP1Real,jdbcType=VARCHAR}, ",
            "#{labelP2Real,jdbcType=VARCHAR}, #{labelP3Real,jdbcType=VARCHAR}, ",
            "#{labelP4Real,jdbcType=VARCHAR}, #{availabilityId,jdbcType=VARCHAR}, ",
            "#{reportId,jdbcType=VARCHAR}, #{workEffortPurposeTypeId,jdbcType=VARCHAR}, ",
            "#{workEffortTypeIdSez1,jdbcType=VARCHAR}, #{workEffortTypeIdSez2,jdbcType=VARCHAR}, ",
            "#{workEffortTypeIdSez3,jdbcType=VARCHAR}, #{workEffortTypeIdSez4,jdbcType=VARCHAR}, ",
            "#{workEffortTypeIdSez5,jdbcType=VARCHAR}, #{workEffortTypeIdSez6,jdbcType=VARCHAR}, ",
            "#{typeBalanceScoreConId,jdbcType=VARCHAR}, #{typeBalanceScoreTarId,jdbcType=VARCHAR}, ",
            "#{typeBalanceTarIndId,jdbcType=VARCHAR}, #{typeBalanceConsIndId,jdbcType=VARCHAR}, ",
            "#{description1,jdbcType=VARCHAR}, #{description2,jdbcType=VARCHAR}, ",
            "#{description3,jdbcType=VARCHAR}, #{description4,jdbcType=VARCHAR}, ",
            "#{description5,jdbcType=VARCHAR}, #{excludeValidity,jdbcType=CHAR}, ",
            "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
            "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
            "#{descriptionLang,jdbcType=VARCHAR})"
    })
    int insert(WorkEffortAnalysis row);

    @Select({
        "select",
        "work_effort_analysis_id, description, work_effort_type_id, work_effort_id, data_visibility, ",
        "reference_date, is_monitor, year_prev, year_m1_prev, year_m2_prev, year_m3_prev, ",
        "year_m4_prev, year_p1_prev, year_p2_prev, year_p3_prev, year_p4_prev, year_real, ",
        "year_m1_real, year_m2_real, year_m3_real, year_m4_real, year_p1_real, year_p2_real, ",
        "year_p3_real, year_p4_real, label_prev, label_m1_prev, label_m2_prev, label_m3_prev, ",
        "label_m4_prev, label_p1_prev, label_p2_prev, label_p3_prev, label_p4_prev, label_real, ",
        "label_m1_real, label_m2_real, label_m3_real, label_m4_real, label_p1_real, label_p2_real, ",
        "label_p3_real, label_p4_real, availability_id, report_id, work_effort_purpose_type_id, ",
        "work_effort_type_id_sez1, work_effort_type_id_sez2, work_effort_type_id_sez3, ",
        "work_effort_type_id_sez4, work_effort_type_id_sez5, work_effort_type_id_sez6, ",
        "type_balance_score_con_id, type_balance_score_tar_id, type_balance_tar_ind_id, ",
        "type_balance_cons_ind_id, description1, description2, description3, description4, ",
        "description5, exclude_validity, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, description_lang",
        "from work_effort_analysis",
        "where work_effort_analysis_id = #{workEffortAnalysisId,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffortAnalysis", value = {
        @Result(column="work_effort_analysis_id", property="workEffortAnalysisId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_type_id", property="workEffortTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_id", property="workEffortId", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_visibility", property="dataVisibility", jdbcType=JdbcType.VARCHAR),
        @Result(column="reference_date", property="referenceDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="is_monitor", property="isMonitor", jdbcType=JdbcType.CHAR),
        @Result(column="year_prev", property="yearPrev", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_m1_prev", property="yearM1Prev", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_m2_prev", property="yearM2Prev", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_m3_prev", property="yearM3Prev", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_m4_prev", property="yearM4Prev", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_p1_prev", property="yearP1Prev", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_p2_prev", property="yearP2Prev", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_p3_prev", property="yearP3Prev", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_p4_prev", property="yearP4Prev", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_real", property="yearReal", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_m1_real", property="yearM1Real", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_m2_real", property="yearM2Real", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_m3_real", property="yearM3Real", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_m4_real", property="yearM4Real", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_p1_real", property="yearP1Real", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_p2_real", property="yearP2Real", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_p3_real", property="yearP3Real", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="year_p4_real", property="yearP4Real", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="label_prev", property="labelPrev", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_m1_prev", property="labelM1Prev", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_m2_prev", property="labelM2Prev", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_m3_prev", property="labelM3Prev", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_m4_prev", property="labelM4Prev", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_p1_prev", property="labelP1Prev", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_p2_prev", property="labelP2Prev", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_p3_prev", property="labelP3Prev", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_p4_prev", property="labelP4Prev", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_real", property="labelReal", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_m1_real", property="labelM1Real", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_m2_real", property="labelM2Real", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_m3_real", property="labelM3Real", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_m4_real", property="labelM4Real", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_p1_real", property="labelP1Real", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_p2_real", property="labelP2Real", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_p3_real", property="labelP3Real", jdbcType=JdbcType.VARCHAR),
        @Result(column="label_p4_real", property="labelP4Real", jdbcType=JdbcType.VARCHAR),
        @Result(column="availability_id", property="availabilityId", jdbcType=JdbcType.VARCHAR),
        @Result(column="report_id", property="reportId", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_purpose_type_id", property="workEffortPurposeTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_type_id_sez1", property="workEffortTypeIdSez1", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_type_id_sez2", property="workEffortTypeIdSez2", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_type_id_sez3", property="workEffortTypeIdSez3", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_type_id_sez4", property="workEffortTypeIdSez4", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_type_id_sez5", property="workEffortTypeIdSez5", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_effort_type_id_sez6", property="workEffortTypeIdSez6", jdbcType=JdbcType.VARCHAR),
        @Result(column="type_balance_score_con_id", property="typeBalanceScoreConId", jdbcType=JdbcType.VARCHAR),
        @Result(column="type_balance_score_tar_id", property="typeBalanceScoreTarId", jdbcType=JdbcType.VARCHAR),
        @Result(column="type_balance_tar_ind_id", property="typeBalanceTarIndId", jdbcType=JdbcType.VARCHAR),
        @Result(column="type_balance_cons_ind_id", property="typeBalanceConsIndId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description1", property="description1", jdbcType=JdbcType.VARCHAR),
        @Result(column="description2", property="description2", jdbcType=JdbcType.VARCHAR),
        @Result(column="description3", property="description3", jdbcType=JdbcType.VARCHAR),
        @Result(column="description4", property="description4", jdbcType=JdbcType.VARCHAR),
        @Result(column="description5", property="description5", jdbcType=JdbcType.VARCHAR),
        @Result(column="exclude_validity", property="excludeValidity", jdbcType=JdbcType.CHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR)
    })
    WorkEffortAnalysis selectByPrimaryKey(String workEffortAnalysisId);

    @Select({
            "select * ",
            "from work_effort_analysis"
    })
    @ResultType(WorkEffortAnalysis.class)
    List<WorkEffortAnalysis> selectAll();

    List<WorkEffortAnalysis> getWorkEffortAnalysesWithContext(@Param("context") String context, @Param("userLoginId") String userLoginId, @Param("permission") String permission);

    List<WorkEffortAnalysisTypeTypeExt> getWorkEffortAnalysisTargetHeader(@Param("analysisId") String analysisId, @Param("workEffortId") String workEffortId);

    List<WorkEffortAnalysisTypeTypeExt> getWorkEffortAnalysisTargetSummary(@Param("permission") String permission, @Param("analysisId") String analysisId, @Param("userLoginId") String userLoginId, @Param("organizationId") String organizationId);

    @Select({
            "select * ",
            "from work_effort_analysis",
            "where reference_date = #{refDate,jdbcType=TIMESTAMP} and work_effort_type_id = #{workEffortTypeId,jdbcType=VARCHAR}"
    })
    @ResultMap("workEffortAnalysis")
    List<WorkEffortAnalysis> getWorkEffortAnalysis(@Param("refDate") Instant refDate, @Param("workEffortTypeId") String workEffortTypeId);

    List<Score> getPdoScore(@Param("workEffortId") String workEffortId);

    List<Score> getKPIscore(@Param("workEffortId") String workEffortId, @Param("analysisRefDate") Instant analysisRefDate);

    List<Score> getDetailPdoScore(@Param("workEffortId") String workEffortId, @Param("glFiscalTypeId") String glFiscalTypeId);

    List<Score> getPdoAccount(@Param("glAccountId") String glAccountId, @Param("orgUnitRoleTypeId") String orgUnitRoleTypeId, @Param("orgUnitId") String orgUnitId, @Param("workEffortMeasureId") String workEffortMeasureId);

    List<Score> getPdoScorekpi(@Param("workEffortMeasureId") String workEffortMeasureId);

    List<WorkEffortAnalysisEx> getWorkEffortAnalysisEx(String context);

    WorkEffortAnalysisEx getWorkEffortAnalysisExById(@Param("workEffortAnalysisId") String workEffortAnalysisId);
}