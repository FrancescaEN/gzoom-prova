import { VariableGridArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { DataSource } from './dataSource';
import { Enumeration } from './enumeration';
import { DataSourceType } from './dataSourceType';

/**
 * Model of a DataSourceEx.
 */
export class DataSourceEx extends DataSource {
  public enumeration?: Enumeration;
  public dataSourceType?: DataSourceType;

  public dataSourceTypeIdDesc?: string;
  public valModIdDesc?: string;
}
