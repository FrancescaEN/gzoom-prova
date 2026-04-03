package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoalContent {

    private String contentId;
    private String dataResourceId;
    private String goalId;
    private String dataResourceTypeId;
    private String dataTemplateTypeId;

    private String contentTypeId;
    private String goalContentTypeId;
    private String contentName;
    private String description;
    private String descriptionLang;
    private String objectInfo;

    private String mimeTypeId;
    private String contentStatusId;
    private String dataResourceStatusId;
    private String serviceName;
    private String isPublic;

    private OffsetDateTime fromDate;
    private OffsetDateTime thruDate;

}
