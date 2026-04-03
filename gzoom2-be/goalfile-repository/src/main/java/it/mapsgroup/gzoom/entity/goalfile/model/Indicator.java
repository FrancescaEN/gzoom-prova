package it.mapsgroup.gzoom.entity.goalfile.model;

import it.mapsgroup.gzoom.entity.uom.PeriodType;
import it.mapsgroup.gzoom.entity.uom.Uom;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Indicator {
    private String id;
    private String type;
    private IndicatorType typeEnumId;
    private IndicatorGroup indicatorGroup;

    private String code;
    private String name;
    private String nameLang;
    private String description;
    private String descriptionLang;
    private String source;
    private String sourceLang;

    private DebitCredit debitCredit;
    private Uom uom;
    private Trend trendEnumId;

    private IndicatorStatus status;
    private PeriodType periodType;
    private Instant fromDate;
    private Instant thruDate;

    private Boolean detectOrgUnitIdFlag;
    private String respCenterId;
    private String respCenterRoleTypeId;
    private String roleTypeId;

    private InputType inputEnumId;

    private String weOtherGoalEnumId;
    private String otherWorkEffortId;

    private String calcCustomMethodId;
    private BigDecimal priorityCalculation;
    private PeriodicalAbsolute periodicalAbsoluteEnumId;

    private GoalIndicatorType weMeasureTypeEnumId;
    private ScoreRange weScoreRangeEnumId;
    private ScoreConv weScoreConvEnumId;
    private AlertRule weAlertRuleEnumId;
    private WithoutPerformance weWithoutPerf;
    private WithoutTarget weWithoutTarget;
    private TargetPeriod targetPeriodEnumId;
    private String uomRangeId;
    private List<RatingScale> ratingScales;

    public enum DebitCredit {
        C, D
    }

    public enum IndicatorType {
        INDICATOR, FINANCIAL, ACCOUNT
    }

    public enum Trend {
        TREND_CONST, TREND_INC
    }

    public enum InputType {
        ACCINP_OBJ, ACCINP_UO
    }

    public enum ScoreRange {
        WESCORE_ISVALUE
        , WESCORE_DIRECTRANGE
        , WESCORE_PRORATERANGE
        , WESCORE_MAXRANGE
        , WESCORE_NOCALC
    }

    public enum ScoreConv {
        WECONVER_NOCONVERSIO
        , WECONVER_ABSOLUTEGAP
        , WECONVER_PERCENTGAP
        , WECONVER_PROGRESWRK
        , WECONVER_PERCENTWRK
        , WECONVER_PERCENTPY
        , WECONVER_PERCLIMITS
        , WECONVER_4PERCLIMITS
        , WECONVER_WRKCAP
        , WECONVER_GAPCAP
    }

    public enum GoalIndicatorType {
        WEMT_ALERT
        , WEMT_ECONOMIC
        , WEMT_FINANCIAL
        , WEMT_HUMAN
        , WEMT_MEANS
        , WEMT_OUTCOME
        , WEMT_OUTPUT
        , WEMT_PERF
        , WEMT_QUALITY
        , WEMT_SCORE
    }

    public enum AlertRule {
        WEALERT_TARGETUP
        , WEALERT_TARGETDOWN
        , WEALERT_DIRECTRANGE
        , WEALERT_TARGETRANGE
        , WEALERT_PERCENTRANGE
    }

    public enum WithoutPerformance {
        WEWITHPERF_PERF_0
        , WEWITHPERF_SCORE_0
        , WEWITHPERF_ERROR
        , WEWITHPERF_NO_CALC
    }

    public enum WithoutTarget {
        WEWITHTARG_PRV_SCORE
        , WEWITHTARG_NO_CALC
    }

    public enum TargetPeriod {
        TARGET_EXEC_PERIOD
        , TARGET_PARENT_PERIOD
        , TARGET_PARENT_EXEC
    }

    public enum PeriodicalAbsolute {
        PRDABS_PERIODICAL
        , PRDABS_ABSOLUTE
        , PRDABS_ALL
    }
}
