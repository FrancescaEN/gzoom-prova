import { VarUtils } from '../../../utils/var-utils';

export class GoalType {
  id: string;
  description: string;
  weightKpi: number;
  weightSons: number;
  weightKpiControlSum: number;
  weightControlSum: number;
  totalEnumIdKpi: string;
  totalEnumIdSons: string;

  static fromJson(src: any): GoalType {
    const tgt = new GoalType();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.id = src.id;
    tgt.description = src.description;
    tgt.weightKpi = src.weightKpi;
    tgt.weightSons = src.weightSons;
    tgt.weightKpiControlSum = src.weightKpiControlSum;
    tgt.weightControlSum = src.weightControlSum;
    tgt.totalEnumIdKpi = src.totalEnumIdKpi;
    tgt.totalEnumIdSons = src.totalEnumIdSons;

    return tgt;
  }
}
