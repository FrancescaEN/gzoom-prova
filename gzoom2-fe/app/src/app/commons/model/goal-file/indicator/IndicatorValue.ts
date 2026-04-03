import { VarUtils } from '../../../utils/var-utils';
import { IndicatorValueGoalIndicator } from './IndicatorValueGoalIndicator';

export class IndicatorValue {
  valueUomId: string;
  amount: number;
  origAmount: number;
  scoreKpi: boolean;
  goalIndicator?: IndicatorValueGoalIndicator;

  static fromJson(src: any): IndicatorValue {
    const tgt = new IndicatorValue();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.valueUomId = src.valueUomId;
    tgt.amount = src.amount;
    tgt.origAmount = src.origAmount;
    tgt.scoreKpi = src.scorekpi;
    tgt.goalIndicator = IndicatorValueGoalIndicator.fromJson(src.goalIndicator);

    return tgt;
  }
}
