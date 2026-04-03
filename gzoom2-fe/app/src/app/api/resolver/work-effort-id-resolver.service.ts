import { inject, } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { Observable, lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortService } from '../service/work-effort.service';

import { LoaderService } from 'app/shared/loader/loader.service';
import { WorkEffort } from '../model/work-effort';

export const workEffortIdResolverService: ResolveFn<void | WorkEffort> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | WorkEffort | Observable<void | WorkEffort> | Promise<void | WorkEffort> => {
  const workEffortService = inject(WorkEffortService);
  const lockoutService = inject(LockoutService);
  const loaderService = inject(LoaderService);

  console.log('resolve workEffort');

  loaderService.show();
  const workEffortService$ = workEffortService.getWorkEffort(route.queryParamMap.get('id'));
  return lastValueFrom(workEffortService$).then(workEfforts => { return workEfforts; })
    .catch(err => {
      console.error('Cannot retrieve workEffort', err);
      lockoutService.lockout();
    });
}
