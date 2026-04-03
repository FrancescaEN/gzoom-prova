import { VariableGridArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';

/**
 * Model of a WorkEffortSequence.
 */
export class WorkEffortSequence {
  constructor(
    public seqId?: number,
    public seqName?: string,
    public createdByUserLogin?: string,
    public createdStamp?: string,
    public createdTxStamp?: string,
    public lastModifiedByUserLogin?: string,
    public lastUpdatedStamp?: string,
    public lastUpdatedTxStamp?: string,


  ) { }

  public variableGridArray?: VariableGridArray;


}
