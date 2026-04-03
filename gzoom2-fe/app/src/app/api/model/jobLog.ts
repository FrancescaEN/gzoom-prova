import { VariableGridArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';

/**
 * Model of a JobLog.
 */
export class JobLog {
  constructor(
    public jobLogId?: string,
    public jobId?: string,
    public userLoginId?: string,
    public serviceTypeId?: number,
    public serviceName?: string,
    public description?: string,
    public logDate?: Date,
    public logEndDate?: Date,
    public recordElaborated?: number,
    public warningMessages?: number,
    public blockingErrors?: number,
    public elabRef1?: string,
    public sessionId?: string,
  ) { }

  public variableGridArray?: VariableGridArray;
}
