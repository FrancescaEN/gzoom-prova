import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";

export class WorkEffortStatus {
  public workEffortId?: string;
  public statusId?: string;
  public statusDatetime?: Date;
  public setByUserLogin?: string;
  public reason?: string;

  constructor(
    workEffortId?: string,
    statusId?: string,
    statusDatetime?: Date,
    setByUserLogin?: string,
    reason?: string
  ) {
    this.workEffortId = workEffortId;
    this.statusId = statusId;
    this.statusDatetime = statusDatetime;
    this.setByUserLogin = setByUserLogin;
    this.reason = reason;
  }


}
