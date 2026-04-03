import { Injectable } from '@angular/core';
import { TableState } from 'primeng/api';
import { Table } from 'primeng/table';

@Injectable({
  providedIn: 'root'
})
export class PrimengTableService {

  constructor() { }


  /**
   * This function given a table and a state taken from storage
   * sets its values in the given table.
   * 
   * @param table @type Table primeng/table
   * @param state @type string
   */
  setTableState(table: Table, state: string) {
    table.clearFilterValues();

    if (state) {
      const storagedState: TableState = JSON.parse(state, this.reviver);
      table.filters = storagedState.filters;
      table.first = storagedState.first;
      table.rows = storagedState.rows;
      table.sortField = storagedState.sortField;
      table.sortOrder = storagedState.sortOrder;
    }
  }

  private reviver(key: any, value: any) {
    const dateFormat = /\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d{3}Z/;
    if (typeof value === 'string' && dateFormat.test(value)) {
      return new Date(value);
    }

    return value;
  };
}
