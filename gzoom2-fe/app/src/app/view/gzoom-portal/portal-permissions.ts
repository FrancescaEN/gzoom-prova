import { GoalStatusId } from '../../commons/model/goal-file/status/goal-status-id';
import { StringUtils } from '../../commons/utils/string-utils';

export enum PortalPermission {
  AttachmentsRead = 'AttachmentsRead',
  AttachmentsWrite = 'AttachmentsWrite',
  ConciliationRead = 'ConciliationRead',
  ContextDataRead = 'ContextDataRead',
  ContradictoryRead = 'ContradictoryRead',
  ContradictoryWrite = 'ContradictoryWrite',
  DetailsRead = 'DetailsRead',
  IntegrationNoteWrite = 'IntegrationNoteWrite',
  NotesRead = 'NotesRead',
  NotesWrite = 'NotesWrite',
  // ReportsRead = 'ReportsRead',
  StatusRead = 'StatusRead',
  StatusWrite = 'StatusWrite',
  TotalResultRead = 'TotalResultRead',
}

export namespace PortalPermission {
  const PORTAL_PERMISSIONS: { [key in PortalPermission]: GoalStatusId[] } = {
    [PortalPermission.AttachmentsRead]: [
      GoalStatusId.InsertingEvidence,
      GoalStatusId.SignedByUSR,
      GoalStatusId.SignedByDS,
      GoalStatusId.ApprovedBySilentAssent,
      GoalStatusId.ContradictoryRequestFromDS,
      GoalStatusId.ContradictoryOutcomeSignedByDS,
      GoalStatusId.ContradictoryRequestFromUSR,
      GoalStatusId.ContradictoryOutcomeSignedByUSR,
      GoalStatusId.ConciliationConcluded
    ],
    [PortalPermission.AttachmentsWrite]: [
      GoalStatusId.InsertingEvidence
    ],

    [PortalPermission.ConciliationRead]: [
      GoalStatusId.ConciliationConcluded
    ],

    [PortalPermission.ContextDataRead]: [
      GoalStatusId.InsertingEvidence,
      GoalStatusId.SignedByUSR,
      GoalStatusId.SignedByDS,
      GoalStatusId.ApprovedBySilentAssent,
      GoalStatusId.ContradictoryRequestFromDS,
      GoalStatusId.ContradictoryOutcomeSignedByDS,
      GoalStatusId.ContradictoryRequestFromUSR,
      GoalStatusId.ContradictoryOutcomeSignedByUSR,
      GoalStatusId.ConciliationConcluded
    ],

    [PortalPermission.ContradictoryRead]: [
      GoalStatusId.ContradictoryRequestFromDS,
      GoalStatusId.ContradictoryOutcomeSignedByDS,
      GoalStatusId.ContradictoryRequestFromUSR,
      GoalStatusId.ContradictoryOutcomeSignedByUSR,
      GoalStatusId.ConciliationConcluded
    ],
    [PortalPermission.ContradictoryWrite]: [
      GoalStatusId.SignedByUSR
    ],

    [PortalPermission.IntegrationNoteWrite]: [
      GoalStatusId.InsertingEvidence
    ],

    [PortalPermission.DetailsRead]: Object.values(GoalStatusId)
      .filter((value): value is GoalStatusId =>
        typeof value === 'string'
        && value !== GoalStatusId.ToBeAssigned
        && value !== GoalStatusId.InvestigativePhase
        && value !== GoalStatusId.EvaluationPhase
        && value !== GoalStatusId.EvaluationCompleted
        && value !== GoalStatusId.ContradictoryProcess
        && value !== GoalStatusId.Conciliation),

    [PortalPermission.NotesRead]: [
      GoalStatusId.InsertingEvidence,
      GoalStatusId.SignedByUSR,
      GoalStatusId.SignedByDS,
      GoalStatusId.ApprovedBySilentAssent,
      GoalStatusId.ContradictoryRequestFromDS,
      GoalStatusId.ContradictoryOutcomeSignedByDS,
      GoalStatusId.ContradictoryRequestFromUSR,
      GoalStatusId.ContradictoryOutcomeSignedByUSR,
      GoalStatusId.ConciliationConcluded
    ],
    [PortalPermission.NotesWrite]: [
      GoalStatusId.InsertingEvidence
    ],

    // [PortalPermission.ReportsRead]: [
    //   GoalStatusId.InsertingEvidence,
    //   GoalStatusId.SignedByUSR,
    //   GoalStatusId.SignedByDS,
    //   GoalStatusId.ApprovedBySilentAssent,
    //   GoalStatusId.ContradictoryRequestFromDS,
    //   GoalStatusId.ContradictoryOutcomeSignedByDS,
    //   GoalStatusId.ContradictoryRequestFromUSR,
    //   GoalStatusId.ContradictoryOutcomeSignedByUSR
    // ],

    [PortalPermission.StatusRead]: [
      GoalStatusId.Assigned,
      GoalStatusId.InsertingEvidence,
      GoalStatusId.SignedByUSR,
      GoalStatusId.SignedByDS,
      GoalStatusId.ApprovedBySilentAssent,
      GoalStatusId.ContradictoryRequestFromDS,
      GoalStatusId.ContradictoryOutcomeSignedByDS,
      GoalStatusId.ContradictoryRequestFromUSR,
      GoalStatusId.ContradictoryOutcomeSignedByUSR
    ],
    [PortalPermission.StatusWrite]: [
      GoalStatusId.SignedByUSR,
      GoalStatusId.ContradictoryOutcomeSignedByUSR
    ],

    [PortalPermission.TotalResultRead]: Object.values(GoalStatusId)
      .filter((value): value is GoalStatusId =>
        typeof value === 'string'
        && StringUtils.startsWith(value, 'VALIND_EXEC')
        && +StringUtils.subString(value, 'VALIND_EXEC'.length) >= 190)
  }

  export function generatePortalPermissions(statusId: string | null): { [key in PortalPermission]?: boolean } {
    return Object.fromEntries(
      Object.entries(PORTAL_PERMISSIONS).map(([permission, allowedStatuses]) => [
        permission,
        allowedStatuses.includes(GoalStatusId.fromString(statusId))
      ])
    );
  }
}
