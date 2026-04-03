package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkEffortStatusDto {
    private String workEffortId;
    private String statusId;
    private OffsetDateTime statusDateTime;
    private String reason;
    private String byUserLogin;
    private StatusItemDto statusItem;
    private PersonDto person;
}
