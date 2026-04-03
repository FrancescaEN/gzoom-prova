package it.mapsgroup.gzoom.infrastructure.uom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PeriodTypeDto {
    private String periodTypeId;
    private String description;
 }