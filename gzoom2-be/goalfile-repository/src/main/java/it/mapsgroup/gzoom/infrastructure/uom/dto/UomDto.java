package it.mapsgroup.gzoom.infrastructure.uom.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UomDto {

    private String uomId;
    private UomTypeDto uomType;
    private String abbreviation;
    private String abbreviationLang;
    private String description;
    private String descriptionLang;
    private Integer decimalScale;
    private Double minValue;
    private Double maxValue;
    private Instant lastUpdatedStamp;
    private Instant lastUpdatedTxStamp;
    private Instant createdStamp;
    private Instant createdTxStamp;
    private String lastModifiedByUserLogin;
    private String createdByUserLogin;
}