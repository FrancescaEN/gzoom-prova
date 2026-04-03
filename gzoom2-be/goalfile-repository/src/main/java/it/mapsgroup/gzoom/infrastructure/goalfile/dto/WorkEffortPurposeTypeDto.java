package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkEffortPurposeTypeDto {

    private String workEffortPurposeTypeId;

    private String parentTypeId;

    private String description;

    private String descriptionLang;

    private Instant lastUpdatedStamp;

    private Instant lastUpdatedTxStamp;

    private Instant createdStamp;

    private Instant createdTxStamp;

    private String workEffortPurposeTypeCode;

    private String purposeTypeEnumId;

    private String lastModifiedByUserLogin;

    private String createdByUserLogin;
}