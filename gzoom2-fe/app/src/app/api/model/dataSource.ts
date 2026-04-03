import { VariableGridArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';

/**
 * Model of a DataSource.
 */
export class DataSource {
  constructor(
    public dataSourceId?: string,
    public dataSourceTypeId?: string,
    public description?: string,
    public lastUpdatedStamp?: string,
    public lastUpdatedTxStamp?: string,
    public createdStamp?: string,
    public createdTxStamp?: string,
    public valModId?: string,
    public lastModifiedByUserLogin?: string,
    public createdByUserLogin?: string
  ) { }

  public variableGridArray?: VariableGridArray;
}
