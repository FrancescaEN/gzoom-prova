import { VarUtils } from '../../utils/var-utils';

export class GoalPeriod {
  periodName: string;
  fromDate: Date;
  thruDate: Date;

  static fromJson(src: any): GoalPeriod {
    const tgt = new GoalPeriod();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.periodName = src.periodName;
    tgt.fromDate = VarUtils.isDefined(src.fromDate) ? new Date(src.fromDate) : null;
    tgt.thruDate = VarUtils.isDefined(src.thruDate) ? new Date(src.thruDate) : null;

    return tgt;
  }
}
