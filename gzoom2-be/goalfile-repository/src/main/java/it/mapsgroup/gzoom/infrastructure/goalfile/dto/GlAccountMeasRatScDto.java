package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GlAccountMeasRatScDto {
    private String glAccountId;
    private String uomId;
    private Double uomRatingValue;
    private String uomCode;
    private String uomCodeLang;
    private String uomDescr;
    private String uomDescrLang;
 }