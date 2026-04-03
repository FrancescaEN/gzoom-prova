package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import it.mapsgroup.gzoom.entity.goalfile.model.PhaseEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder // Use SuperBuilder here for inheritance
@AllArgsConstructor
@NoArgsConstructor
public class StatusTypeDto {
    private String statusTypeId;
    private String parentTypeId;
    private String description;
    private String portalTypeId;
    private String phaseStEnumId;
    private PhaseEnumCode phaseStEnumCode;
 }