import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { RateType } from "./rateType";
import { WorkEffort } from "./work-effort";

/**
 * Model of a TimeEntry.
 */
export class TimeEntry {
  constructor(
    public idNumber?: number,
    public workEffortName?: string,
    public timeEntryId?: string,
    public workEffortId?: string,
    public partyId?: string,
    public rateTypeId?: string,
    public rateTypeIdDescription?: string,
    public timesheetId?: string,
    public description?: string,
    public percentage?: number,
    public effortUomId?: string,
    public hours?: number,
    public fromDate?: Date,
    public thruDate?: Date,
    public planHours?: number,
    public comments?: string,
    public workEffort?: WorkEffort,
    public orderId?: string,
    public jobId?: string,
    public rateType?: RateType,
    public variableGridArray?: VariableGridArray
  ) { }
}
