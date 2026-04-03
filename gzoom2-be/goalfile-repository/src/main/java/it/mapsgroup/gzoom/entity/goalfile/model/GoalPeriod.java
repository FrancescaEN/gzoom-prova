package it.mapsgroup.gzoom.entity.goalfile.model;

import it.mapsgroup.gzoom.entity.uom.Period;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalPeriod {
    private String id;
    private String statusTypeId;
    private Period period;
    private OffsetDateTime fromDate;
    private OffsetDateTime thruDate;
    private OffsetDateTime refDate;
    private String periodName;
}
