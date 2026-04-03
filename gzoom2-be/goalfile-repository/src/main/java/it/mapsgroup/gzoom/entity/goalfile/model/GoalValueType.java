package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoalValueType {
    private String id;
    private String description;
    private String descriptionLang;
    private GoalValueTypeEnum GoalValueTypeEnum;

    public enum GoalValueTypeEnum {
        ACTUAL,
        TARGET
    }
}
