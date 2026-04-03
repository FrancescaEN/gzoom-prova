package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkEffortTypePeriodDto {
    private String workEffortTypePeriodId;
    private String workEffortTypeId;
    private String customTimePeriodId;
    private CustomTimePeriodDto customTimePeriod;
    private String glFiscalTypeEnumId;
    private String statusTypeId;
    private OffsetDateTime perLavFrom;
    private OffsetDateTime perLavThru;
    private OffsetDateTime refDate;
    private String desProc;
    private String statusEnumId;
    private String organizationId;
}