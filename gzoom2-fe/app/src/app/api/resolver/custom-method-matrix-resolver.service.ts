import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { CustomMethodMatrixService } from '../service/custom-method-matrix.service';
import { CustomMethodMatrix } from '../model/customMethodMatrix';


export const customMethodMatrixResolver: ResolveFn<void | CustomMethodMatrix[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | CustomMethodMatrix[]> => {

    const customMethodMatrixService = inject(CustomMethodMatrixService);
    const lockoutService = inject(LockoutService);

    const customMethodMatrixService$ = customMethodMatrixService.getCustomMethodMatrixList(route.paramMap.get('customMethodId'));
    return lastValueFrom(customMethodMatrixService$).then(customMethodMatrix => { return customMethodMatrix; })
        .catch(err => {
            console.error('Cannot retrieve custom method matrix', err);
            lockoutService.lockout();
        });
}
