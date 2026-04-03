package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkEffortContentDetailDto {
    private String workEffortContentTypeId;
    private OffsetDateTime fromDate;
    private OffsetDateTime thruDate;
    private String description;
    private String descriptionLang;
    private String contentId;
    private String contentTypeId;
    private String contentName;
    private String dataResourceId;
    private String objectInfo;
}