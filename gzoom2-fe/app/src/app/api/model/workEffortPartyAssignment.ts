import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { RoleType } from "./role-type";
import { WorkEffortView } from "./workEffortView";
import { PartyRoleView } from "./partyRoleView";

/**
 * Model of WorkEffortPartyAssignment
 */
export class WorkEffortPartyAssignment {
  constructor(
    public workEffortName?: string,
    public workEffortId?: string,
    public partyRoleViewDescription?: string,
    public partyId?: string,
    public roleTypeId?: string,
    public roleTypeDescription?: string,
    public roleTypeWeight?: string,
    public comments?: string,
    public commentsLang?: string,
    public fromDate?: Date,
    public thruDate?: Date,

    public roleType?: RoleType,
    public workEffortView?: WorkEffortView,
    public partyRoleView?: PartyRoleView,

    public variableGridArray?: VariableGridArray,
    public dropdown?: any
  ) { }
}
