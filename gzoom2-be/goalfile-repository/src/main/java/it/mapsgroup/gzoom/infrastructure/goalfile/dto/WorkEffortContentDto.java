package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkEffortContentDto {

    private String workEffortId;
    private String contentId;
    private String workEffortContentTypeId;
    private OffsetDateTime fromDate;
    private OffsetDateTime thruDate;
    private OffsetDateTime lastUpdatedStamp;
    private OffsetDateTime lastUpdatedTxStamp;
    private OffsetDateTime createdStamp;
    private OffsetDateTime createdTxStamp;
    private String lastModifiedByUserLogin;
    private String createdByUserLogin;
}