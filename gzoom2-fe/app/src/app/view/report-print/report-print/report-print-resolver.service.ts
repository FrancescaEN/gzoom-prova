import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from '@angular/router'
import { lastValueFrom } from 'rxjs';

import { LockoutService } from '../../../commons/service/lockout.service';
import { Report } from '../report';
import { ReportService } from 'app/api/service/report.service';


/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */
export const reportPrintResolver: ResolveFn<void | Report[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | Report[]> => {
  const reportService = inject(ReportService);
  const lockoutService = inject(LockoutService);
  var parentTypeId = route.parent.data.context;
  console.log('resolve reports');

  const reportService$ = reportService.reports(parentTypeId);
  return lastValueFrom(reportService$).then(reports => { return reports; })
    .catch(err => {
      console.error('Cannot retrieve report', err);
      lockoutService.lockout();
    });
}
