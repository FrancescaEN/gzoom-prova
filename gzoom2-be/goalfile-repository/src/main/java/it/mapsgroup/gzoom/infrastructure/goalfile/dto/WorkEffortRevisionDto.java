package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkEffortRevisionDto {

    private String workEffortRevisionId;
    private String description;
    private String descriptionLang;
    private Instant refDate;
}