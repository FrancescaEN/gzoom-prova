package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomTimePeriodDto {
    private String periodName;
    private OffsetDateTime fromDate;
    private OffsetDateTime thruDate;
}