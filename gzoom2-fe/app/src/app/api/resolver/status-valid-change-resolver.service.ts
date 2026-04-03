import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { StatusValidChangeService } from '../service/status-valid-change.service';
import { StatusValidChange } from '../model/statusValidChange';


export const statusValidChangeResolverService: ResolveFn<void | StatusValidChange[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | StatusValidChange[] | Observable<void | StatusValidChange[]> | Promise<void | StatusValidChange[]> => {
    const statusTypeId = route.parent.paramMap.get('statusTypeId');
    const statusValidChangeService = inject(StatusValidChangeService);
    const lockoutService = inject(LockoutService);

    const statusValidChangeService$ = statusValidChangeService.getByStatusTypeId(statusTypeId);
    return lastValueFrom(statusValidChangeService$).then(statusValidChanges => { return statusValidChanges; })
        .catch(err => {
            console.error('Cannot retrieve Status Valid Change', err);
            lockoutService.lockout();
        });
}