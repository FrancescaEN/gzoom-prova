package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import it.mapsgroup.gzoom.infrastructure.content.dto.DataResourceDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkEffortTypeDto {
    private String workEffortTypeId;
    private String parentTypeId;
    private String description;
    private String etch;
    private String frameEnumId;
    private String codePrefix;
    private String seqOnlyId;
    private String descriptionLang;
    private BigDecimal seqEsp;
    private Double weightKpi;
    private Double weightSons;
    private Double weightAssocWorkEffort;
    private Double weightControlSum;
    private Double weightKpiControlSum;
    private String totalEnumIdKpi;
    private String totalEnumIdSons;
    private String totalEnumIdAssoc;
    private String orgUnitRoleTypeId;
    private String orgUnitRoleTypeId2;
    private String orgUnitRoleTypeId3;
    private String purposeTypeId;
    /** Relazione Collegati, utilizzata nel calcolo */
    private String workEffortAssocTypeId;

    private DataResourceDto icon;
    private String hierarchyAssocTypeId;
    private Boolean indicAuto;

    private Boolean root;

    /** Relazione Father */
    private String fatherAssocTypeId;
    /** Tipologia Father */
    private String fatherTypeId;

    public boolean isIndicAuto() {
        return indicAuto != null && indicAuto;
    }
}