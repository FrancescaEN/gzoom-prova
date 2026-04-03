package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkEffortAssocDto {
    private WorkEffortDto workEffortFrom;
    private WorkEffortDto workEffortTo;
    private String workEffortAssocTypeId;
    private OffsetDateTime fromDate;
    private OffsetDateTime thruDate;
    private BigDecimal sequenceNum;
    private Double assocWeight;
    private String comments;
    private String commentsLang;
    private OffsetDateTime lastUpdatedStamp;
    private OffsetDateTime lastUpdatedTxStamp;
    private OffsetDateTime createdStamp;
    private OffsetDateTime createdTxStamp;
    private String lastModifiedByUserLogin;
    private String createdByUserLogin;
}