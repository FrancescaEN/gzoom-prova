import { Party,  } from "./party";
import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { WorkEffortView } from "./workEffortView";
import { WorkEffortType } from "./workEffortType";

export class WorkEffortViewEx extends WorkEffortView {
    
    public party?: Party;

    public workEffortType?: WorkEffortType;

    public variableGridArray?: VariableGridArray;
}