import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';

import { lastValueFrom } from 'rxjs';


import { LockoutService } from '../../../commons/service/lockout.service';

import { Report } from '../report';
import { ReportService } from 'app/api/service/report.service';
/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */
export const reportResolver: ResolveFn<void | Report> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | Report> => {
  const reportService = inject(ReportService);
  const lockoutService = inject(LockoutService);

  let parentTypeId = route.parent.data.context;
  let reportContentId = route.params.reportContentId;
  let analysis = route.params.analysis;
  let resourceName = route.params.resourceName;
  let workEffortTypeId = route.params.workEffortTypeId;
  console.log('resolve report for ' + parentTypeId + 'reportContentId=' + reportContentId + ' resourceName=' + resourceName + ' workEffortTypeId=' + workEffortTypeId + ' analysis=' + analysis);
  //TODO come faccio?


  const reportService$ = reportService.report(parentTypeId, reportContentId, resourceName, workEffortTypeId);
  return lastValueFrom(reportService$).then(report => { return report; })
    .catch(err => {
      console.error('Cannot retrieve report', err);
      lockoutService.lockout();
    });
}
