package it.mapsgroup.gzoom.entity.orgchart.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationUnitType {
    private String id;
    private String description;
    private String descriptionLang;
}
