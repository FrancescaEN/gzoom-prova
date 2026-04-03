import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffort } from '../model/work-effort';
import { WorkEffortTypeService } from '../service/work-effort-type.service';

export const workEffortTypeIsRootResolverService: ResolveFn<void | WorkEffort[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffort[]> => {
    const workEffortTypeService = inject(WorkEffortTypeService);
    const lockoutService = inject(LockoutService);

    console.log('resolve workEffortTypes where IsRoot is True');

    const workEffortTypeService$ = workEffortTypeService.workEffortTypesIsRoot('Y');
    return lastValueFrom(workEffortTypeService$).then(workEffortTypes => { return workEffortTypes; })
        .catch(err => {
            console.error('Cannot retrieve workEffortTypes', err);
            lockoutService.lockout();
        });
}