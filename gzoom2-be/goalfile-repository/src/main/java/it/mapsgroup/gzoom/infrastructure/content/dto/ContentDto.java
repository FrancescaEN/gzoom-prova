package it.mapsgroup.gzoom.infrastructure.content.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {

    private String contentId;
    private String contentTypeId;
    private String dataResourceId;
    private String statusId;
    private String serviceName;
    private String contentName;
    private String description;
    private String descriptionLang;
    private String mimeTypeId;

    private OffsetDateTime lastUpdatedStamp;
    private OffsetDateTime lastUpdatedTxStamp;
    private OffsetDateTime createdStamp;
    private OffsetDateTime createdTxStamp;
    private String lastModifiedByUserLogin;
    private String createdByUserLogin;
}