package it.mapsgroup.gzoom.infrastructure.content.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataResourceDto {
    private String dataResourceId;
    private String dataResourceTypeId;
    private String dataTemplateTypeId;
    private String statusId;
    private String dataResourceName;
    private String mimeTypeId;
    private String objectInfo;
    private String isPublic;

    private OffsetDateTime lastUpdatedStamp;
    private OffsetDateTime lastUpdatedTxStamp;
    private OffsetDateTime createdStamp;
    private OffsetDateTime createdTxStamp;
    private String lastModifiedByUserLogin;
    private String createdByUserLogin;
}
