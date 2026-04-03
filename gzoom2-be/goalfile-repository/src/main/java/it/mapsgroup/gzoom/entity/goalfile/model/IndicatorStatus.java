package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndicatorStatus {
    private String id;
    private String description;
    private String descriptionLang;
}
