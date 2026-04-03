package it.mapsgroup.gzoom.entity.goalfile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.mapsgroup.gzoom.common.TreeNode;
import it.mapsgroup.gzoom.entity.orgchart.model.OrganizationUnit;
import it.mapsgroup.gzoom.entity.orgchart.model.OrganizationUnitType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Goal implements TreeNode<Goal> {
    private String id;
    private String code;
    private String etch;
    private String name;
    private String nameLang;
    private String description;
    private String descriptionLang;
    private OffsetDateTime fromDate;
    private OffsetDateTime thruDate;
    private OffsetDateTime scheduledStartDate;
    private OffsetDateTime scheduledCompletionDate;
    private OffsetDateTime actualStartDate;
    private OffsetDateTime actualCompletionDate;
    private OffsetDateTime referenceDate;
    private String lastModifiedByUserLogin;
    private OffsetDateTime lastUpdatedStamp;
    private Boolean posted;
    private Double objWeight;

    private String userLoginId;
    private String organizationId;

    private GoalType goalType;
    private OrganizationUnitType organizationUnitType;
    private OrganizationUnit organizationUnit;
    private OrganizationUnit supervisorOrganizationUnit;
    private OrganizationUnit topOrganizationUnit;
    private GoalStatus goalStatus;
    private OffsetDateTime lastStatusUpdate;
    private GoalRevision goalRevision;
    private GoalPeriod goalPeriod;

    /** Legame ROOT e workEffortParentId */
    private GoalFile goalFileParent;
    /** Legame gerarchico (assoc con parentTypeId = HIE) */
    private Goal goalHierarchy;
    /** Legame Padre */
    private Goal goalFather;


    private GoalPurposeType goalPurposeType;

    private List<Goal> children;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<GoalIndicator> indicators;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<GoalNote> notes;

    @JsonIgnore
    public String getGoalTypeId() {
        return goalType != null ? goalType.getId() : null;
    }

    @JsonIgnore
    public String getGoalStatusTypeIdFromPeriod() {
        return goalPeriod != null ? goalPeriod.getStatusTypeId() : null;
    }

    public void addChild(Goal child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    public boolean isPosted() {
        return posted != null && posted;
    }
}
