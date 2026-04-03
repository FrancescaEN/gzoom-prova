package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusItemChangeDto {
    private StatusItemDto statusItemFrom;
    private StatusItemDto statusItemTo;
    private String directionCode;
}