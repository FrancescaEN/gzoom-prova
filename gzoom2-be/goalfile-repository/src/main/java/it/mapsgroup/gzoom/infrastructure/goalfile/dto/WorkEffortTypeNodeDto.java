package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class WorkEffortTypeNodeDto {
    private WorkEffortTypeDto father;
    private WorkEffortTypeDto child;
    private BigDecimal sequenceNum;
}