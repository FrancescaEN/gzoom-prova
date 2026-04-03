package it.mapsgroup.gzoom.infrastructure.orgchart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleTypeDto {
    private String roleTypeId;
    private String parentTypeId;
    private String hasTable;
    private String description;
    private String shortLabel;
    private String descriptionLang;
    private String shortLabelLang;
}