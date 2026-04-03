import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { TimesheetService } from '../service/timesheet.service';
import { TimeEntry } from '../model/time_entry';
/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */

export const timeEntryResolver: ResolveFn<void | TimeEntry[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | TimeEntry[] | Observable<void | TimeEntry[]> | Promise<void | TimeEntry[]> => {

  var id = route.paramMap.get('id');
  console.log('resolve timeEntries for timesheetId = ' + id);


  const timesheetService = inject(TimesheetService);

  const lockoutService = inject(LockoutService);


  const timesheetService$ = timesheetService.timeEntries(id)

  return lastValueFrom(timesheetService$).then(timesheets => { return timesheets; })

    .catch(err => {

      console.error('Cannot retrieve timesheets', err);

      lockoutService.lockout();

    });

}


