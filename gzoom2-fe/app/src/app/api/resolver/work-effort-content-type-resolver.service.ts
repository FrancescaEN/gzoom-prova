import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortContentTypeService } from 'app/api/service/work-effort-content-type.service';
import { WorkEffortContentType } from 'app/api/model/workEffortContentType';

export const workEffortContentTypeResolver: ResolveFn<void | WorkEffortContentType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffortContentType[]> => {
    const workEffortContentTypeService = inject(WorkEffortContentTypeService);
    const lockoutService = inject(LockoutService);

    console.log('resolve workEffortContentType');

    const obs$ = workEffortContentTypeService.getWorkEffortContentType();
    return lastValueFrom(obs$).then(obs => { return obs; })
        .catch(err => {
            console.error('Cannot retrieve workEffortContentType', err);
            lockoutService.lockout();
        });
}