package it.mapsgroup.gzoom.infrastructure.orgchart.dto;

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
public class PartyDto {
    private String partyId;
    private String externalId;
    private String partyName;
    private String partyNameLang;
    private String parentRoleCode;
    private PartyDto responsible;
    private RoleTypeDto roleType;
    private String partyTypeId;
    private String statusId;
}