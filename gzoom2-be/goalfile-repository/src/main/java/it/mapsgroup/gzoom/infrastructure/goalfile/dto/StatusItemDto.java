package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusItemDto {
    private String statusId;
    private String description;
    private String descriptionLang;
    private StatusTypeDto statusType;
    private String sequenceId;
}