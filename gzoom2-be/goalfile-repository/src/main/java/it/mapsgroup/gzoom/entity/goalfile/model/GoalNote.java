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
public class GoalNote {
    private String id;
    private String goalId;
    private String goalTypeId;
    private String name;
    private String nameLang;
    private String info;
    private String infoLang;
    private String party;
    private OffsetDateTime dateTime;
    private Boolean attribute;
    private Boolean note;
    private Boolean defaultNote;
    private BigDecimal sequenceId;
    private String statusEnumId;
    private String organizationId;
    private Boolean main;
    private Boolean html;
    private String automatic;
    private Boolean posted;
    private Boolean internalNote;
    private Boolean mandatory;
    private BigDecimal maxLength;
    private BigDecimal periodNum;
    private String value;

    public GoalNote setId(String id) {
        this.id = id;
        return this;
    }

    public GoalNote setInfo(String info) {
        this.info = info;
        return this;
    }
}
