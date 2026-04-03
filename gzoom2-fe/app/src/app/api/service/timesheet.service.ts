import { Injectable } from "@angular/core";
import { lastValueFrom, Observable } from "rxjs";
import { map } from "rxjs/operators";
import { ApiClientService } from "../../commons/service/client.service";
import { Timesheet } from "../model/timesheet";
import { TimeEntry } from "../model/time_entry";
import { WorkEffort } from "../model/work-effort";
import * as _ from "lodash";
import { NoteData } from "app/view/node/node";
import { Filter, InfoPage } from "app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration";
import { CustomTimePeriod } from "../model/customTimePeriod";
import { StatusItem } from "../model/statusItem";

@Injectable()
export class TimesheetService {
  constructor(private client: ApiClientService) { }

  timesheets(): Observable<Timesheet[]> {
    return this.client
      .get(`timesheet/`)
      .pipe(map((json) => json.results as Timesheet[]));
  }

  timesheetsPagination(InfoPage: InfoPage): Observable<any> {
    return this.client
      .post(`timesheet/pagination`, InfoPage)
      .pipe(map((json) => json as any));
  }

  timesheetsCustomTimePeriodDropdownFilter(): Observable<CustomTimePeriod[]> {
    return this.client
      .get(`customtimeperiods/custom-time-period-dropdown-filter`)
      .pipe(map((json) => json.results as CustomTimePeriod[]));
  }

  timesheetsStatusDropdownFilter(): Observable<StatusItem[]> {
    return this.client
      .get(`status-item/status-dropdown-filter`)
      .pipe(map((json) => json.results as StatusItem[]));
  }

  params(id: string): Observable<NoteData[]> {
    return this.client
      .get(`timesheet/params/${id}`)
      .pipe(map((json) => json.results as NoteData[]));
  }

  isAdmin(context: string): Observable<boolean> {
    return this.client
      .get(`permission/permission/role/${context}`)
      .pipe(map((json) => json as boolean));
  }

  deleteTimesheet(timesheets: string[]): Promise<any> {
    const client$ = this.client.post(
      "timesheet/delete",
      timesheets
    );
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((error: any) => {
        console.error(`Error while deleting in: ${error.error.message}`);
        return Promise.reject(error.error);
      });
  }

  createTimesheet(timesheet: Timesheet): Promise<Timesheet> {
    console.log("create Timesheet");

    const client$ = this.client.post(
      "timesheet/timesheet",
      this.saveTimesheetBodifier(timesheet)
    );
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((response) => {
        console.error(`Error while creating in: ${response}`);
        return Promise.reject(response.json() || response);
      });
  }

  updateTimesheet(
    timesheetId: string,
    timesheet: Timesheet
  ): Promise<Timesheet> {
    console.log("update Timesheet");

    const client$ = this.client.put(
      `timesheet/${timesheetId}`,
      this.saveTimesheetBodifier(timesheet)
    );
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((response: any) => {
        console.error(`Error while updating in: ${response}`);
        return Promise.reject(response.json() || response);
      });
  }

  validStatusIdTimesheet(timesheetId: string): Promise<Timesheet> {
    console.log("valid StatusId Timesheet");

    const client$ = this.client.put(`timesheet/validStatus/${timesheetId}`);
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((response: any) => {
        console.error(`Error while updating in: ${response}`);
        return Promise.reject(response.json() || response);
      });
  }

  reopenStatusIdTimesheet(timesheetId: string): Promise<Timesheet> {
    console.log("valid StatusId Timesheet");

    const client$ = this.client.put(`timesheet/reopenStatus/${timesheetId}`);
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((response: any) => {
        console.error(`Error while updating in: ${response}`);
        return Promise.reject(response.json() || response);
      });
  }

  timeEntries(timesheetId: string): Observable<TimeEntry[]> {
    console.log("search timeEntries for timesheetId: " + timesheetId);
    return this.client
      .get(`time-entry/time-entry/${timesheetId}`)
      .pipe(map((json) => json.results as TimeEntry[]));
  }

  timesheetTimeEntry(timesheetId: string): Observable<Timesheet> {
    return this.client
      .get(`timesheet/timesheetbyid/${timesheetId}`)
      .pipe(map((json) => json as Timesheet));
  }

  workEfforts(timesheetId: string, secondaryLang: boolean): Observable<WorkEffort[]> {
    return this.client
      .get(`time-entry/time-entry-work-efforts/${timesheetId}/${secondaryLang}`)
      .pipe(map((json) => json.results as WorkEffort[]));
  }

  updateTimeEntry(timeEntries: TimeEntry): Promise<TimeEntry> {
    console.log("update timeEntry");
    const client$ = this.client.put(
      "time-entry/",
      this.saveTimeEntriesBodifier(timeEntries)
    );
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((response) => {
        console.error(`Error while updating in: ${response}`);
        return Promise.reject(response.json() || response);
      });
  }

  createTimeEntry(timeEntries: TimeEntry): Promise<string> {
    console.log("create timeEntry");
    const client$ = this.client.post(
      "time-entry/",
      this.saveTimeEntriesBodifier(timeEntries)
    );
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((response) => {
        console.error(`Error while creating in: ${response}`);
        return Promise.reject(response);
      });
  }

  deleteTimeEntry(timeEntrys: string[]): Promise<TimeEntry> {
    const client$ = this.client.delete(`time-entry/${timeEntrys}`);
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((error: any) => {
        console.error(`Error while deleting in: ${error.error.message}`);
        return Promise.reject(error.error);
      });
  }

  //Bodifier methods
  saveTimesheetBodifier(timesheet) {
    return {
      fromDate: new Date(timesheet.fromDate),
      thruDate: new Date(timesheet.thruDate),
      actualHours: timesheet ? timesheet.actualHours : null,
      contractHours: timesheet ? timesheet.contractHours : null,
      partyId: timesheet ? timesheet.partyId : null,
      timesheetId: timesheet ? timesheet.timesheetId : null,
    };
  }

  saveTimeEntriesBodifier(timeEntries: TimeEntry) {
    return (timeEntries = {
      workEffortId: timeEntries.workEffortId,
      timesheetId: timeEntries.timesheetId,
      percentage: timeEntries.percentage,
      timeEntryId: timeEntries.variableGridArray.id,
      fromDate: new Date(timeEntries.fromDate),
      thruDate: new Date(timeEntries.thruDate),
      description: timeEntries.description,
      rateTypeId: timeEntries.rateTypeId,
      hours: timeEntries.hours,
      planHours: timeEntries.planHours,
      comments: timeEntries.comments,
      partyId: timeEntries.partyId,
      orderId: timeEntries.orderId,
      jobId: timeEntries.jobId,
      effortUomId: timeEntries.effortUomId,
    });
  }
}
