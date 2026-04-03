import { VariableGridArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';

/**
 * Model of a JobLogLog.
 */
export class JobLogLog {
  constructor(
    public jobLogLogId?: string,
    public jobLogId?: string,
    public logTypeEnumId?: string,
    public logCode?: number,
    public logMessage?: string,
    public valueRef1?: string,
    public valueRef2?: string,
    public valueRef3?: string,
    public logMessageLong?: number,
    public valuePk1?: number
  ) { }

  public variableGridArray?: VariableGridArray;
}
