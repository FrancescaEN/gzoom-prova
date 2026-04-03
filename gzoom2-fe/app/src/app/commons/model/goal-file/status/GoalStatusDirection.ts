import { VarUtils } from '../../../utils/var-utils';

export enum GoalStatusChangeDirection {
  FORWARD = 'FORWARD',
  BACKWARD = 'BACKWARD'
}

export namespace GoalStatusChangeDirection {
  export function fromString(value: string): GoalStatusChangeDirection | null {
    const status = (Object.values(GoalStatusChangeDirection) as GoalStatusChangeDirection[]).find(v => v === value);
    return VarUtils.isDefined(status) ? status : null;
  }
}

