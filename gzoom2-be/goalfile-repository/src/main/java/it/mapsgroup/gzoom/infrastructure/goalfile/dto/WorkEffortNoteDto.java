package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkEffortNoteDto {
    private String workEffortId;
    private String noteId;
    private String noteName;
    private String noteNameLang;
    private String noteInfo;
    private String noteInfoLang;
    private OffsetDateTime noteDateTime;
    private String noteParty;
    private Boolean internalNote;
    private Boolean main;
    private Boolean html;
    private BigDecimal sequenceId;
    private Boolean posted;
    private OffsetDateTime lastUpdatedStamp;
    private OffsetDateTime lastUpdatedTxStamp;
    private OffsetDateTime createdStamp;
    private OffsetDateTime createdTxStamp;
    private String lastModifiedByUserLogin;
    private String createdByUserLogin;
}