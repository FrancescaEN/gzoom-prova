import { inject, } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { Observable, lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortService } from '../service/work-effort.service';
import { WorkEffortEx } from '../model/workEffortEx';
import { LoaderService } from 'app/shared/loader/loader.service';
import { WorkEffort } from '../model/work-effort';

export const workEffortByIdResolverService: ResolveFn<void | WorkEffort> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | WorkEffort | Observable<void | WorkEffort> | Promise<void | WorkEffort> => {
  const workEffortService = inject(WorkEffortService);
  const lockoutService = inject(LockoutService);
  const loaderService = inject(LoaderService);

  var id = route.parent.paramMap.get('id');
  console.log('resolve workEffort' + id);

  // loaderService.show();
  const workEffortService$ = workEffortService.getWorkEffort(id);
  return lastValueFrom(workEffortService$).then(workEfforts => { return workEfforts; })
    .catch(err => {
      console.error('Cannot retrieve workEffort', err);
      lockoutService.lockout();
    });
}
