import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortAnalysis } from '../model/workEffortAnalysis';
import { WorkEffortAnalysisService } from 'app/api/service/work-effort-analysis.service';
import { WorkEffortAnalysisEx } from '../model/workEffortAnalysisEx';

/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */
export const workeEffortAnalysisByContextResolver: ResolveFn<void | WorkEffortAnalysis[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffortAnalysis[]> => {
  console.log('resolve Analysis');
  const context = route.parent.data.context;
  console.log('resolver param context = ' + context);
  const workEffortAnalysisService = inject(WorkEffortAnalysisService);
  const lockoutService = inject(LockoutService);

  const WorkEffortAnalysisService$ = workEffortAnalysisService.getWorkEffortAnalysisWithContext(context)
  return lastValueFrom(WorkEffortAnalysisService$).then(Analyses => { return Analyses; })
    .catch(err => { // TODO serve il lockout?
      console.error('Cannot retrieve Analyses', err);
      lockoutService.lockout();
    });
}

export const workeEffortAnalysisExResolver: ResolveFn<void | WorkEffortAnalysisEx[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffortAnalysisEx[]> => {
  const workEffortAnalysisService = inject(WorkEffortAnalysisService);
  const lockoutService = inject(LockoutService);
  const context = route.parent.data.context;

  const WorkEffortAnalysisService$ = workEffortAnalysisService.getWorkEffortAnalysisExList(context);
  return lastValueFrom(WorkEffortAnalysisService$).then(Analyses => { return Analyses; })
    .catch(err => { // TODO serve il lockout?
      console.error('Cannot retrieve Analyses', err);
      lockoutService.lockout();
    });
}

export const workeEffortAnalysisExByIdResolver: ResolveFn<void | WorkEffortAnalysisEx> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffortAnalysisEx> => {
  const workEffortAnalysisService = inject(WorkEffortAnalysisService);
  const lockoutService = inject(LockoutService);
  const workEffortAnalysisId = route.paramMap.get('workEffortAnalysisId');
  const WorkEffortAnalysisService$ = workEffortAnalysisService.getWorkEffortAnalysisExById(workEffortAnalysisId);
  return lastValueFrom(WorkEffortAnalysisService$).then(Analyses => { return Analyses; })
    .catch(err => { // TODO serve il lockout?
      console.error('Cannot retrieve Analyses', err);
      lockoutService.lockout();
    });
}