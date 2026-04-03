package it.mapsgroup.gzoom.entity.goalfile.model;

import it.mapsgroup.gzoom.entity.uom.Uom;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoalAssoc {

    private Goal fromGoal;
    private Goal toGoal;
    private String assocTypeId;
    private OffsetDateTime fromDate;
    private OffsetDateTime thruDate;
    private BigDecimal sequenceNum;
    private Double assocWeight;
    private String comments;
    private String commentsLang;

    public String getGoalIdFrom() {
        return fromGoal != null ? fromGoal.getId() : null;
    }

    public String getGoalIdTo() {
        return toGoal != null ? toGoal.getId() : null;
    }
}