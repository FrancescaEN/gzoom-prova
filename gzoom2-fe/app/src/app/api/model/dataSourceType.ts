import { VariableGridArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';

/**
 * Model of a DataSourceType.
 */
export class DataSourceType {
  constructor(
    public dataSourceTypeId?: string,
    public description?: string
  ) { }


  public variableGridArray?: VariableGridArray;


}
