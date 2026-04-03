package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoalPurposeType {
    private String id;
    private String description;
    private String descriptionLang;
    private String code;
}
