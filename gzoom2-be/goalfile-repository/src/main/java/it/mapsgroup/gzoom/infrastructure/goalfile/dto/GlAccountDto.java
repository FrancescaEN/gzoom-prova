package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import it.mapsgroup.gzoom.infrastructure.uom.dto.PeriodTypeDto;
import it.mapsgroup.gzoom.infrastructure.uom.dto.UomDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GlAccountDto {

        private String glAccountId;
        private String glAccountTypeId;
        private String glAccountClassId;
        private String glResourceTypeId;
        private String parentGlAccountId;
        private String referencedAccountId;
        private String accountTypeEnumId;

        private String accountCode;
        private String accountName;
        private String accountNameLang;
        private String description;
        private String descriptionLang;
        private String source;
        private String sourceLang;

        private String debitCreditDefault;
        private UomDto defaultUom;
        private Trend trendEnumId;

        private String currentStatusId;
        private PeriodTypeDto periodType;
        private Instant fromDate;
        private Instant thruDate;

        private String detectOrgUnitIdFlag;
        private String respCenterId;
        private String respCenterRoleTypeId;
        private String roleTypeId;

        private String inputEnumId;

        private String weOtherGoalEnumId;
        private String otherWorkEffortId;

        private CustomMethodDto customMethod;
        private BigDecimal prioCalc;
        private String periodicalAbsoluteEnumId;
        private String weMeasureTypeEnumId;
        private String weScoreRangeEnumId;
        private String weScoreConvEnumId;
        private String weAlertRuleEnumId;
        private String weWithoutPerf;
        private String weWithoutTarget;
        private String targetPeriodEnumId;
        private String uomRangeId;

        private String createdByUserLogin;
        private Instant createdStamp;
        private Instant createdTxStamp;
        private String lastModifiedByUserLogin;
        private Instant lastUpdatedStamp;
        private Instant lastUpdatedTxStamp;

        private List<GlAccountMeasRatScDto> glAccountMeasRatScs;

        public enum Trend {
                TREND_CONST, TREND_INC
        }
}
