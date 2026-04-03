import { VarUtils } from '../../../utils/var-utils';

export enum GoalStatusId {
  ToBeAssigned = 'VALIND_EXEC120',
  Assigned = 'VALIND_EXEC130',
  InsertingEvidence = 'VALIND_EXEC140',
  InvestigativePhase = 'VALIND_EXEC150',
  EvaluationPhase = 'VALIND_EXEC160',
  EvaluationCompleted = 'VALIND_EXEC170',

  Archived = 'VALIND_EXEC180',
  SignedByUSR = 'VALIND_EXEC190',
  SignedByDS = 'VALIND_EXEC200',
  SignedByDSArchived = 'VALIND_EXEC201',
  ApprovedBySilentAssent = 'VALIND_EXEC210',
  ApprovedBySilentAssentArchived = 'VALIND_EXEC211',
  ContradictoryRequestFromDS = 'VALIND_EXEC220',
  ContradictoryProcess = 'VALIND_EXEC230',
  ContradictoryOutcomeSignedByUSR = 'VALIND_EXEC240',
  ContradictoryOutcomeSignedByDS = 'VALIND_EXEC250',
  ContradictoryOutcomeSignedByDSArchived = 'VALIND_EXEC251',
  ContradictoryRequestFromUSR = 'VALIND_EXEC260',
  ContradictoryRequestFromUSRArchived = 'VALIND_EXEC261',
  Conciliation = 'VALIND_EXEC300',
  ConciliationConcluded = 'VALIND_EXEC310'
}

export namespace GoalStatusId {
  export function fromString(value: string): GoalStatusId | null {
    const status = (Object.values(GoalStatusId) as GoalStatusId[]).find(v => v === value);
    return VarUtils.isDefined(status) ? status : null;
  }
}
