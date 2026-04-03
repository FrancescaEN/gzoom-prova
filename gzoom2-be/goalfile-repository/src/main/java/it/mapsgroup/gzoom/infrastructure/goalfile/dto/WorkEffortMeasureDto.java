package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

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
public class WorkEffortMeasureDto {

        private WorkEffortDto workEffort;
        private GlAccountDto glAccount;

        private String workEffortMeasureId;
        private WorkEffortMeasRatScDto workEffortMeasRatSc;
        private List<AcctgTransEntryDto> acctgTransEntry;
        private String uomDescr;
        private String uomDescrLang;
        private String comments;
        private String commentsLang;
        private String comments2;
        private String comments2Lang;

        private String currentStatusId;
        private Instant fromDate;
        private Instant thruDate;

        private String orgUnitId;
        private String orgUnitRoleTypeId;

        private String dataSourceId;

        private String weOtherGoalEnumId;
        private String otherWorkEffortId;

        private GlAccountDto account;
        private UomDto uom;

        private Double kpiScoreWeight;
        private Double kpiOtherWeight;

        private Boolean posted; // TODO D

        private BigDecimal sequenceId;

        private String createdByUserLogin;
        private Instant createdStamp;
        private Instant createdTxStamp;
        private String lastModifiedByUserLogin;
        private Instant lastUpdatedStamp;
        private Instant lastUpdatedTxStamp;

        /*public boolean isPosted() {
                return posted != null && posted;
        }*/
}
