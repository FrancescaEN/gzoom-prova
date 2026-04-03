package it.mapsgroup.gzoom.entity.orgchart.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationUnit {
    private String id;
    private String mainCode;
    private String otherCode;
    private String name;
    private String nameLang;

    private OrganizationUnitType organizationUnitType;

    private Person responsible;
}
