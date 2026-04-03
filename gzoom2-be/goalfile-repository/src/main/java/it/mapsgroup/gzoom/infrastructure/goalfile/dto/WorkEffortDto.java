package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import it.mapsgroup.gzoom.infrastructure.orgchart.dto.PartyDto;
import it.mapsgroup.gzoom.infrastructure.orgchart.dto.RoleTypeDto;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class WorkEffortDto {
    private String workEffortId;
    private OffsetDateTime lastStatusUpdate;
    private String workEffortName;
    private String workEffortNameLang;
    private Double objWeight;
    private String sourceReferenceId;
    private String etch;
    private String description;
    private String descriptionLang;
    private OffsetDateTime estimatedStartDate;
    private OffsetDateTime estimatedCompletionDate;
    private OffsetDateTime scheduledStartDate;
    private OffsetDateTime scheduledCompletionDate;
    private OffsetDateTime actualStartDate;
    private OffsetDateTime actualCompletionDate;
    private String organizationId;
    private OffsetDateTime refDate;

    private WorkEffortTypeDto workEffortType;
    private StatusItemDto statusItem;
    private StatusTypeDto statusType;
    private RoleTypeDto orgUnitRoleType;
    private PartyDto orgUnitParty;
    private RoleTypeDto supUnitRoleType;
    private PartyDto supUnitParty;
    private RoleTypeDto topUnitRoleType;
    private PartyDto topUnitParty;
    private WorkEffortTypePeriodDto workEffortTypePeriod;
    private WorkEffortRevisionDto workEffortRevision;

    private WorkEffortDto workEffortParent;

    private OffsetDateTime createdDate;
    private String createdByUserLogin;
    private OffsetDateTime lastModifiedDate;
    private String lastModifiedByUserLogin;
    private OffsetDateTime lastUpdatedStamp;
    private OffsetDateTime lastUpdatedTxStamp;
    private OffsetDateTime createdStamp;
    private OffsetDateTime createdTxStamp;
    private Boolean posted;

    private List<WorkEffortMeasureDto> workEffortMeasures;
    private List<WorkEffortNoteDto> workEffortNotes;

    private WorkEffortPurposeTypeDto workEffortPurposeType;

    public WorkEffortDto setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public WorkEffortDto setLastModifiedByUserLogin(String lastModifiedByUserLogin) {
        this.lastModifiedByUserLogin = lastModifiedByUserLogin;
        return this;
    }

    public WorkEffortDto setLastUpdatedStamp(OffsetDateTime lastUpdatedStamp) {
        this.lastUpdatedStamp = lastUpdatedStamp;
        return this;
    }

    public WorkEffortDto setLastUpdatedTxStamp(OffsetDateTime lastUpdatedTxStamp) {
        this.lastUpdatedTxStamp = lastUpdatedTxStamp;
        return this;
    }

    public boolean isPosted() {
        return posted != null && posted;
    }
}