import { GoalStatus as GoalStatusDTO } from '../../../../api/service/goal-file/dto/status/goal-status';
import { GoalStatusAvailable as GoalStatusAvailableDTO } from '../../../../api/service/goal-file/dto/status/goal-status-available';
import { VarUtils } from '../../../utils/var-utils';
import { GoalStatusChangeDirection } from './GoalStatusDirection';

export class GoalStatus {
  id?: string;
  description?: string;
  descriptionLang?: string;
  direction?: GoalStatusChangeDirection;
  sequenceId?: string;
  reason?: string;

  setId(id?: string): GoalStatus {
    this.id = id;
    return this;
  }

  setDescription(description?: string): GoalStatus {
    this.description = description;
    return this;
  }

  setReason(reason?: string): GoalStatus {
    this.reason = reason;
    return this;
  }

  static fromGoalStatusAvailable(src: GoalStatusAvailableDTO): GoalStatus {
    const tgt = new GoalStatus();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.id = src.goalStatus?.id;
    tgt.description = src.goalStatus?.description;
    tgt.descriptionLang = src.goalStatus?.descriptionLang;
    tgt.sequenceId = src.goalStatus?.sequenceId;
    tgt.direction = GoalStatusChangeDirection.fromString(src.direction);

    return tgt;
  }

  static fromGoalStatus(src: GoalStatusDTO): GoalStatus {
    const tgt = new GoalStatus();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.id = src.id;
    tgt.description = src.description;
    tgt.descriptionLang = src.descriptionLang;

    return tgt;
  }
}
