package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkEffortTypeAttrDto {

    private String workEffortTypeId;
    private String attrName;
    private String attrNameLang;
    private Boolean attribute;
    private Boolean note;
    private Boolean defaultNote;
    private BigDecimal sequenceId;
    private String statusEnumId;
    private String organizationId;
    private Boolean main;
    private Boolean html;
    private String isAutomatic;
    private String noteId;
    private Boolean internalNote;
    private Boolean mandatory;
    private BigDecimal maxLenght;
    private BigDecimal periodNum;

}