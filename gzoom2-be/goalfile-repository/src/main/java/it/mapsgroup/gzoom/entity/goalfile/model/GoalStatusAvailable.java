package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalStatusAvailable {
    private GoalStatus goalStatus;
    private GoalStatusChangeDirection direction;
}
