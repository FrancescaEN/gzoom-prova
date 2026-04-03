import { VariableGridArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';

/**
 * Model of a JobLogServiceType.
 */
export class JobLogServiceType {
  constructor(
    public serviceTypeId?: string,
    public description?: string,
    public logInfo?: string

  ) { }

  public variableGridArray?: VariableGridArray;
}
