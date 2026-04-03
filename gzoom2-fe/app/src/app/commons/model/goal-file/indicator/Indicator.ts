import { ArrayUtils } from '../../../utils/array-utils';
import { VarUtils } from '../../../utils/var-utils';
import { UnitOfMeasurement } from './UnitOfMeasurement';
import { IndicatorValue } from './IndicatorValue';

export class Indicator {
  id: string;
  accountName: string;
  accountCode: string;
  description: string;
  uom: UnitOfMeasurement;
  value: IndicatorValue[];
  ratingScale: boolean;
  dataSource: string;
  dataSourceLang: string;

  kpiScoreWeight: number;
  comments: string;
  score?: number; // extracted from indicator value
  scoreCalculated?: number; // calculated based on goal weight, etc.

  static fromJson(src: any): Indicator {
    const tgt = new Indicator();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.id = src.id;
    tgt.accountName = src.accountName;
    tgt.accountCode = src.accountCode;
    tgt.description = src.description;
    tgt.uom = UnitOfMeasurement.fromJson(src.uom);
    tgt.value = ArrayUtils.notEmpty(src.value) ? src.value.map((v: any) => IndicatorValue.fromJson(v)) : [];
    tgt.ratingScale = src.ratingScale;

    tgt.kpiScoreWeight = src.kpiScoreWeight;
    tgt.comments = src.comments;
    tgt.dataSource = src.source;
    tgt.dataSourceLang = src.sourceLang;

    return tgt;
  }
}
