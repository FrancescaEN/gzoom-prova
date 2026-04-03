import { WorkEffort } from "app/api/model/work-effort";
import { WorkEffortType } from "app/view/report-print/report";
import { StatusType } from "./statusType";
import { Party, PartyParentRole } from "./party";
import { RoleType } from "./role-type";
import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { StatusItem } from "./statusItem";

export class WorkEffortEx extends WorkEffort {
    public workEffortType?: WorkEffortType;
    public statusItem?: StatusItem;
    public statusType?: StatusType;
    public party?: Party;
    public roleType?: RoleType;
    public partyParentRole?: PartyParentRole;

    public workEffortTypeIdDesc?: string;
    public statusItemDesc?: string;
    public statusTypeDesc?: string;
    public partyParentRoleCode?: string;
    public partyName?: string;
    public currentStatusDesc?: string;
    public orgDesc?: string;

    public variableGridArray?: VariableGridArray;
}