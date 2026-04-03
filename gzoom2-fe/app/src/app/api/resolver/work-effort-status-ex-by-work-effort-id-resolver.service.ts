import { inject, } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { Observable, lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortStatusService } from '../service/work-effort-status.service';
import { WorkEffortStatusEx } from '../model/workEffortStatusEx';

export const workEffortStatusExByWorkEffortIdResolverService: ResolveFn<void | WorkEffortStatusEx[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | WorkEffortStatusEx[] | Observable<void | WorkEffortStatusEx[]> | Promise<void | WorkEffortStatusEx[]> => {
  const workEffortStatusService = inject(WorkEffortStatusService);
  const lockoutService = inject(LockoutService);

  var workEffortId = route.parent.paramMap.get('id');  
  console.log('resolve workEffortStatusEx workEffortId= ' + workEffortId);

  const workEffortStatusService$ = workEffortStatusService.getWorkEffortStatusEx(workEffortId);
  return lastValueFrom(workEffortStatusService$).then(workEfforts => { return workEfforts; })
    .catch(err => {
      console.error('Cannot retrieve workEffort', err);
      lockoutService.lockout();
    });
}
