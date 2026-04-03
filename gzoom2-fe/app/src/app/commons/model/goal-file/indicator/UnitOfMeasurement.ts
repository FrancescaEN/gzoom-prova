import { VarUtils } from '../../../utils/var-utils';

export class UnitOfMeasurement {
  abbreviation: string;

  static fromJson(src: any): UnitOfMeasurement {
    const tgt = new UnitOfMeasurement();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.abbreviation = src.abbreviation;

    return tgt;
  }
}
