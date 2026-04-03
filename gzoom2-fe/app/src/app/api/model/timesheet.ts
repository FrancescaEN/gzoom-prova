import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { NoteData } from "./noteData";
import { tsByUserLogin } from "./tsByUserLogin";
import { CustomTimePeriod } from "./customTimePeriod";
import { Party } from "./party";

export class Timesheet {
  constructor(
    public idNumber?: number,
    public party?: Party,
    public partyName?: string,
    public partyStructure?: string,
    public timesheetId?: string,
    public fromDate?: Date,
    public thruDate?: Date,
    public variableGridArray?: VariableGridArray,
    public contractHours?: number,
    public actualHours?: number,
    public workEffort?: string,
    public workEffortId?: string,
    public workEffortTypePeriod?: string,
    public statusItem?: string,
    public statusId?: string,
    public effortUomId?: string,
    public uom?: Object,
    public abbreviation?: string,
    public description?: string,
    public partyHistoryView?: Object,
    public updatable?: any,
    public noteData?: NoteData,
    public partyParentRole?: string,
    public partyParentRoleUser?: string,
    public partyParentRoleStructure?: string,
    public partyParentRoleStructureLang?: string,
    public workEffortTypeContent?: string,
    public tsByUserLogin?: tsByUserLogin[],
    public customTimePeriod?: CustomTimePeriod
  ) { }
}
