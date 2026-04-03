import { inject, } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { Observable, lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortService } from '../service/work-effort.service';
import { WorkEffortEx } from '../model/workEffortEx';
import { LoaderService } from 'app/shared/loader/loader.service';

export const workEffortExResolverService: ResolveFn<void | WorkEffortEx[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | WorkEffortEx[] | Observable<void | WorkEffortEx[]> | Promise<void | WorkEffortEx[]> => {
  const workEffortService = inject(WorkEffortService);
  const lockoutService = inject(LockoutService);
  const loaderService = inject(LoaderService);

  console.log('resolve workEffortEx');

  loaderService.show();
  const workEffortService$ = workEffortService.workEffortEx();
  return lastValueFrom(workEffortService$).then(workEfforts => { return workEfforts; })
    .catch(err => {
      console.error('Cannot retrieve workEffort', err);
      lockoutService.lockout();
    });
}
