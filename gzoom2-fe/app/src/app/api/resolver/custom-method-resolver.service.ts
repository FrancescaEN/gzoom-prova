import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { CustomMethod } from '../model/customMethod';
import { CustomMethodService } from '../service/custom-method.service';


export const customMethodResolver: ResolveFn<void | CustomMethod[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | CustomMethod[]> => {

    const customMethodService = inject(CustomMethodService);
    const lockoutService = inject(LockoutService);

    const customMethodService$ = customMethodService.getCustomMethodList();
    return lastValueFrom(customMethodService$).then(customMethod => { return customMethod; })
        .catch(err => {
            console.error('Cannot retrieve custom method', err);
            lockoutService.lockout();
        });
}
