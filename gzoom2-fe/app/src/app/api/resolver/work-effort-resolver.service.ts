import { inject, } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';

import { lastValueFrom } from 'rxjs';


import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortService } from '../service/work-effort.service';
import { WorkEffort } from '../model/work-effort';

export const workEffortResolverService: ResolveFn<void | WorkEffort[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffort[]> => {
  const workEffortService = inject(WorkEffortService);
  const lockoutService = inject(LockoutService);

  var parentTypeId = route.parent.params.parentTypeId;
  console.log('resolve workEffort parentTypeId=' + parentTypeId);

  const workEffortService$ = workEffortService.workEfforts(parentTypeId, '_NA_', true);
  return lastValueFrom(workEffortService$).then(workEfforts => { return workEfforts; })
    .catch(err => {
      console.error('Cannot retrieve workEffort', err);
      lockoutService.lockout();
    });
}
