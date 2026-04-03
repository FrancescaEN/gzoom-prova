import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortAnalysisService } from 'app/api/service/work-effort-analysis.service';

/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */
export const workeEffortAnalysisTargetResolver: ResolveFn<void | any[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | any[]> => {

  const workEffortAnalysisService = inject(WorkEffortAnalysisService);
  const lockoutService = inject(LockoutService);

  console.log('resolve Target');
  const context = route.parent.data.context;
  const analysisId = route.paramMap.get('analysisId');
  console.log('resolver param context = ' + context + ' analysisId = ' + analysisId);

  const WorkEffortService$ = workEffortAnalysisService.getWorkEffortAnalysisTargetSummary(context, analysisId)
  return lastValueFrom(WorkEffortService$).then(Analyses => { return Analyses; })
    .catch(err => { // TODO serve il lockout?
      console.error('Cannot retrieve Analyses', err);
      lockoutService.lockout();
    });
}
