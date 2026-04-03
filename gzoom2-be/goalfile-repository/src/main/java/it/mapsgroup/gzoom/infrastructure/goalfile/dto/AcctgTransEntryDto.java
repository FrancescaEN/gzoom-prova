package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AcctgTransEntryDto {
        private GlFiscalTypeDto glFiscalType;
        private GlAccountDto glAccount;
        private WorkEffortMeasureDto workEffortMeasure;
        private WorkEffortMeasRatScDto workEffortMeasRatSc;
        private WorkEffortMeasRatScDto workEffortMeasRatScDto;

        private String acctgTransId;

        private String acctgTransEntrySeqId;

        private Double amount;
        private Double origAmount;
        private Boolean amountLocked;

        private String currencyUomId;

        private String description;
        private String descriptionLang;

        private String reference;
        private String referenceLang;

        private Double perfAmountCalc;
        private Double perfAmountTarget;
        private Double perfAmountActual;
        private Double perfAmountMin;
        private Double perfAmountMax;

        private Boolean scorekpi;

        private String workEffortRevisionId;
        private String organizationPartyId;

        private String createdByUserLogin;
        private Instant createdStamp;
        private Instant createdTxStamp;
        private String lastModifiedByUserLogin;
        private Instant lastUpdatedStamp;
        private Instant lastUpdatedTxStamp;

        public boolean isScorekpi() {
                return scorekpi != null && scorekpi;
        }
}
