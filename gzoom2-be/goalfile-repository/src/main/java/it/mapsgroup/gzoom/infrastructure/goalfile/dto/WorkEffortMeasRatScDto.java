package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkEffortMeasRatScDto {
    private String workEffortMeasureId;
    private String uomId;
    private Double uomRatingValue;
    private String uomCode;
    private String uomCodeLang;
    private String uomDescr;
    private String uomDescrLang;
 }