import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { UomRange } from '../model/uomRange';
import { UomRangeService } from '../service/uom-range.service';


export const uomRangeResolver: ResolveFn<void | UomRange[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | UomRange[]> => {

    const uomRangeService = inject(UomRangeService);
    const lockoutService = inject(LockoutService);

    const uomRangeService$ = uomRangeService.getUomRangeList();
    return lastValueFrom(uomRangeService$).then(uomRange => { return uomRange; })
        .catch(err => {
            console.error('Cannot retrieve uom range', err);
            lockoutService.lockout();
        });
}
