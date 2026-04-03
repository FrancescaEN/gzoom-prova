import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { CustomTimePeriodService } from 'app/api/service/custom-time-period.service';
import { CustomTimePeriod } from 'app/api/model/customTimePeriod';

export const customTimePeriodResolver: ResolveFn<void | CustomTimePeriod[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | CustomTimePeriod[] | Observable<void | CustomTimePeriod[]> | Promise<void | CustomTimePeriod[]> => {

    console.log('resolve Periods');

    const customTimePeriodService = inject(CustomTimePeriodService);

    const lockoutService = inject(LockoutService);


    const obs$ = customTimePeriodService.getCustomTimePeriod();

    return lastValueFrom(obs$).then(obs => { return obs; })

        .catch(err => {

            console.error('Cannot retrieve Periods', err);

            lockoutService.lockout();

        });

}