import { VariableGridArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';

/**
 * Model of a JobLogJobExecParams.
 */
export class JobLogJobExecParams {
  constructor(
    public jobLogId?: string,
    public parameterName?: string,
    public parameterDescription?: string,
    public parameterType?: number,
    public parameterValue?: string
  ) { }

  public variableGridArray?: VariableGridArray;
}
