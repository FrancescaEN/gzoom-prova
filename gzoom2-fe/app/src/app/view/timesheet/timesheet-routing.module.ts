import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { TimesheetResolver } from './timesheet/timesheet-resolver.service';
import { TimesheetTableComponent } from '../timesheet/timesheet/timesheet-table/timesheet-table.component';
import { timeEntryResolver } from '../../api/resolver/time-entry-resolver.service';
import { TimeEntryDetailComponent } from './time-entry/time-entry-detail-table/time-entry-detail.component';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { TimeEntryDetailCalendarComponent } from './time-entry/time-entry-detail-calendar/time-entry-detail-calendar.component';
import { canActivateCheckPermissionItemGuard } from 'app/commons/service/guard.service';

export const routeTimesheet: Route = {
  path: 'timesheet',
  loadChildren: () =>
    import('./timesheet.module'),
  data: { breadcrumb: 'TimeSheet' },
  canActivate: [canActivateCheckPermissionItemGuard],

};

const routes: Routes = [
  {
    path: 'M/:id', component: TimeEntryDetailComponent, canDeactivate: [canDeactivateUnsavedGuard], resolve: {
      timeEntries: timeEntryResolver,
    }
  },
  {
    path: 'C/:id', component: TimeEntryDetailCalendarComponent, canDeactivate: [canDeactivateUnsavedGuard], resolve: {
      timeEntries: timeEntryResolver,
    }
  },
  { path: '', component: TimesheetTableComponent, resolve: { timesheets: TimesheetResolver } }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TimesheetRoutingModule { }
