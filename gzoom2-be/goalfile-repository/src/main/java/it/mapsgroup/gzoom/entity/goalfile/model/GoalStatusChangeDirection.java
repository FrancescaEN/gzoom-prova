package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum GoalStatusChangeDirection {
    FORWARD("F"),
    BACKWARD("B");

    private final String code;

    GoalStatusChangeDirection(String code) {
        this.code = code;
    }

    public static GoalStatusChangeDirection getGoalStatusChangeDirection(String code) {
        return Arrays.stream(GoalStatusChangeDirection.values())
                .filter(s -> Objects.equals(s.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }
}
