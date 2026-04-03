package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalStatus {
    private String id;
    private String description;
    private String descriptionLang;
    private String orderStatus;
    private GoalStatusType goalFileStatusType;
    private String sequenceId;
    private OffsetDateTime statusDateTime;
    private String reason;
    private String byUserLogin;
    private String firstName;
    private String lastName;
}
