import { EnumerationType } from './enumerationType';
import { JobLogLog } from './jobLogLog';

/**
 * Model of a JobLogEx.
 */
export class JobLogLogEx extends JobLogLog {

  public enumerationType?: EnumerationType;

  public logTypeEnumIdDesc?: string;

}
