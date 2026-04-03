import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortContentService } from '../service/work-effort-content.service';
import { WorkEffortContentEx } from '../model/workEffortContentEx';
import { LoaderService } from 'app/shared/loader/loader.service';

export const workEffortContentExResolver: ResolveFn<void | WorkEffortContentEx[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffortContentEx[]> => {

    const workEffortContentService = inject(WorkEffortContentService);
    const lockoutService = inject(LockoutService);
    const loaderService = inject(LoaderService);

    loaderService.show();
    const obs$ = workEffortContentService.getWorkEffortContentExList();
    return lastValueFrom(obs$).then(obs => { return obs; })
        .catch(err => {
            console.error('Cannot retrieve WorkEffortContentEx', err);
            lockoutService.lockout();
        });
}