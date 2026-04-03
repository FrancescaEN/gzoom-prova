package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoalValue {
    private Goal goal;
    private GoalIndicator goalIndicator;
    private RatingScale ratingScale;
    private GoalValueType goalValueType;

    /** Identificativo Testata Movimento */
    private String id;
    /** Identificativo Dettaglio Movimento */
    private String entryId;

    private String typeId;

    private String comment;
    private String commentLang;

    private String valueUomId;
    private String comments;
    private String commentsLang;
    private String reference;
    private String referenceLang;

    private String orgUnitId;
    private String orgUnitRoleTypeId;
    private String organizationId;

    private Double amount;
    private Double origAmount;
    private String amountLocked;

    private Boolean posted;

    private BigDecimal sequenceId;

    private OffsetDateTime refDate;

    /**
     * Distingue i movimenti SCOREKPI dagli altri
     */
    private Boolean scorekpi;

    public boolean isPosted() {
        return posted != null && posted;
    }

    public boolean isScorekpi() {
        return scorekpi != null && scorekpi;
    }
}
