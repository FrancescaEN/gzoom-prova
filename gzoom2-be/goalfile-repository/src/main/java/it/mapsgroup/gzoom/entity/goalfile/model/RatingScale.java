package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingScale {
    private String id;
    private Double ratingValue;
    private String valueCode;
    private String valueCodeLang;
    private String valueDescription;
    private String valueDescriptionLang;
    private String code;
    private String codeLang;
    private String description;
    private String descriptionLang;
    private String indicatorId;
    private String goalIndicatorId;
}
