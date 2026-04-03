package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalStatusType {
    private String id;
    private GoalStatusType parentType;
    private String description;
    private String descriptionLang;
    private PhaseEnumCode phaseEnumCode;
    private String phaseEnumId;
}
