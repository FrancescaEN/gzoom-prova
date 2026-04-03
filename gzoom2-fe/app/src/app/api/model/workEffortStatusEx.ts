import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { WorkEffortStatus } from "./workEffortStatus";
import { StatusItem } from "./statusItem";

export class WorkEffortStatusEx extends WorkEffortStatus {
  public statusItem?: StatusItem;

  public statusDesc?: string;
  public variableGridArray?: VariableGridArray

}
