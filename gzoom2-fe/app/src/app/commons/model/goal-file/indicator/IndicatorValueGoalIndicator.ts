import { VarUtils } from '../../../utils/var-utils';

export class IndicatorValueGoalIndicator {
  valueCode: string;
  valueDescription: number;
  ratingScale: boolean;

  static fromJson(src: any): IndicatorValueGoalIndicator {
    const tgt = new IndicatorValueGoalIndicator();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.valueCode = src.valueCode;
    tgt.valueDescription = src.valueDescr;
    tgt.ratingScale = src.ratingScale;

    return tgt;
  }
}
