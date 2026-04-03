package it.mapsgroup.gzoom.infrastructure.uom.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UomTypeDto {
    private String uomTypeId;
    private String description;
}