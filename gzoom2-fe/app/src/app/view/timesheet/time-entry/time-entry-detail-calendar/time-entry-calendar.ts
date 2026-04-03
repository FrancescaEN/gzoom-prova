import { TimeEntry } from "app/api/model/time_entry";
import { VariableGridArray } from "app/layout/tables/table-calendar-timesheet/table-calendar-configuration";

/**
 * Model of a TimeEntryCalendar.
 */
export class TimeEntryRowCalendar {
  constructor(
    public workEffortName?: string,
    public TimeEntryCalendarId?: string,
    public workEffortId?: string,
    public partyId?: string,
    public rateTypeId?: string,
    public rateTypeIdDescription?: string,
    public timesheetId?: string,
    public effortUomId?: string,
    public workEffort?: string,
    public timeEntriesCell?: TimeEntryCellCalendar[],
    public variableGridArray?: VariableGridArray,
    public totalHoursRow?: number,
    public labeltotalHoursRow?: string
  ) { }
}

export class TimeEntryCellCalendar {
  constructor(
    public fromDate?: Date,
    public thruDate?: Date,
    public timeEntryCellId?: number,
    public timeEntry?: TimeEntry,
    public hours?: number,
    public labelHours?: string,
    public updated?: boolean,
    public comments?: string,
    public orderId?: string,
    public jobId?: string,
    public underline?: boolean
  ) { }
}
