package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteDataDto {

    private String noteId;

    private String noteName;

    private String noteInfo;

    private OffsetDateTime noteDateTime;

    private OffsetDateTime lastUpdatedStamp;

    private OffsetDateTime lastUpdatedTxStamp;

    private OffsetDateTime createdStamp;

    private OffsetDateTime createdTxStamp;

    private String noteParty;

    private Boolean publicNote;

    private String noteNameLang;

    private String noteInfoLang;

    private String lastModifiedByUserLogin;

    private String createdByUserLogin;
}