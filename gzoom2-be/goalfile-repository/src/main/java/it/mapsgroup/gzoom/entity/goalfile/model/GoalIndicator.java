package it.mapsgroup.gzoom.entity.goalfile.model;

import it.mapsgroup.gzoom.entity.uom.Uom;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoalIndicator {
    private Goal goal;
    private Indicator indicator;
    private List<GoalValue> goalValues;

    private String id;

    private String accountName;
    private String accountNameLang;
    private String accountCode;
    private String description;
    private String descriptionLang;
    private String comments;
    private String commentsLang;
    private String comments2;
    private String comments2Lang;
    private String source;
    private String sourceLang;

    private String currentStatusId;

    private String orgUnitId;
    private String orgUnitRoleTypeId;

    private String dataSourceId;


    private Double kpiScoreWeight;
    private Double kpiOtherWeight;
    private Uom uom;
    private String uomRatingValue;
    private String valueCode;
    private String valueCodeLang;
    private String valueDescr;
    private String valueDescrLang;

    private Boolean posted; // TODO D

    private BigDecimal sequenceId;

    private List<RatingScale> ratingScales;
    private List<GoalValue> value;

    public boolean isRatingScale() {
        return getIndicator() != null && getIndicator().getUom() != null
               && getIndicator().getUom().getType() != null && "RATING_SCALE".equals(getIndicator().getUom().getType().getId());
    }

    public boolean isPosted() {
        return posted != null && posted;
    }
}
