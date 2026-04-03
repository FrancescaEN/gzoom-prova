package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GoalType {
    private String id;
    private String parentTypeId;
    private String description;
    private String descriptionLang;
    private String etch;
    private BigDecimal seqEsp;

    private String frameEnumId;
    private String codePrefix;
    private String seqOnlyId;

    private Double weightKpi;
    private Double weightSons;
    private Double weightControlSum;
    private Double weightKpiControlSum;
    private Double weightAssocWorkEffort;
    private String totalEnumIdKpi;
    private String totalEnumIdSons;
    private String totalEnumIdAssoc;
    private String hierarchyAssocTypeId;
    private String orgUnitRoleTypeId;
    private String orgUnitRoleTypeId2;
    private String orgUnitRoleTypeId3;
    private String purposeTypeId;
    /** Relazione Collegati, utilizzata nel calcolo */
    private String goalAssocTypeId;

    private Boolean automaticIndicator;

    /** Relazione Father */
    private String fatherAssocTypeId;
    /** Relazione Father */
    private String fatherTypeId;

    public GoalType setId(String id) {
        this.id = id;
        return this;
    }

    public GoalType setDescription(String description) {
        this.description = description;
        return this;
    }

    public GoalType setDescriptionLang(String descriptionLang) {
        this.descriptionLang = descriptionLang;
        return this;
    }

    public boolean hasAutomaticIndicator() {
        return automaticIndicator != null && automaticIndicator;
    }
}
