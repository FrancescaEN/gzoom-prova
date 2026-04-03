import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { PeriodTypeService } from 'app/api/service/period-type.service';
import { PeriodType } from '../model/period-type';
/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */

export const periodTypeResolver: ResolveFn<void | PeriodType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | PeriodType[] | Observable<void | PeriodType[]> | Promise<void | PeriodType[]> => {

  console.log('resolve PeriodType');

  const periodTypeService = inject(PeriodTypeService);

  const lockoutService = inject(LockoutService);

  const obs$ = periodTypeService.periodTypes();

  return lastValueFrom(obs$).then(periodTypes => { return periodTypes; })

    .catch(err => {

      console.error('Cannot retrieve PeriodType', err);

      lockoutService.lockout();

    });

}
