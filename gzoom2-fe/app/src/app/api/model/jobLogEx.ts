import { JobLogServiceType } from './JobLogServiceType';
import { Enumeration } from './enumeration';
import { JobLog } from './jobLog';
import { StandardImportFieldConfig } from './standardImportFiledConfig';

/**
 * Model of a JobLogEx.
 */
export class JobLogEx extends JobLog {

  public jobLogServiceType?: JobLogServiceType;

  public serviceTypeIdDesc?: string;

}
