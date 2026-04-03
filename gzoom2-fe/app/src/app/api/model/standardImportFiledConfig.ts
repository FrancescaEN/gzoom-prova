import { VariableGridArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';

/**
 * Model of a StandardImportFieldConfig.
 */
export class StandardImportFieldConfig {
  constructor(
    public dataSourceId?: string,
    public standardInterface?: string,
    public internalFieldName?: string,
    public interfaceSeq?: number,
    public externalFieldName?: string,
    public defaultValue?: string,
    public lastModifiedByUserLogin?: string,
    public createdByUserLogin?: string,
    public lastUpdatedStamp?: string,
    public lastUpdatedTxStamp?: string,
    public createdStamp?: string,
    public createdTxStamp?: string
  ) { }

  public variableGridArray?: VariableGridArray;
}
