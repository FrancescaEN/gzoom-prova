import { VarUtils } from '../../../../../commons/utils/var-utils';
import { GoalStatus as GoalStatusModel } from '../../../../../commons/model/goal-file/status/goal-status';

export class GoalStatus {
  id?: string;
  description?: string;
  descriptionLang?: string;
  sequenceId?: string;
  reason?: string;

  static fromGoalStatus(src: GoalStatusModel): GoalStatus {
    const tgt = new GoalStatus();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.id = src.id;
    tgt.description = src.description;
    tgt.descriptionLang = src.descriptionLang;
    tgt.reason = src.reason;

    return tgt;
  }
}
