package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.*;

import java.time.Instant;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalRevision {

    private String id;
    private String description;
    private String descriptionLang;
    private Instant refDate;
}
