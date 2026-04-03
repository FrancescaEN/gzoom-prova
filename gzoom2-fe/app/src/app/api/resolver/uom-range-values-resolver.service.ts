import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { LockoutService } from '../../commons/service/lockout.service';
import { UomRangeValues } from 'app/view/ctx-ac/uom/range-values/uomRangeValues';
import { Observable, lastValueFrom } from 'rxjs';
import { UomRangeValuesService } from '../service/uom-range-values.service';

export const uomRangeValuesResolver: ResolveFn<void | UomRangeValues[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | UomRangeValues[] | Observable<void | UomRangeValues[]> | Promise<void | UomRangeValues[]> => {

  console.log('resolve uomRangeValues');

  const uomRangeValuesService = inject(UomRangeValuesService);
  const lockoutService = inject(LockoutService);

  let id = route.paramMap.get('uomRangeId');
  const uomRangeValuesService$ = uomRangeValuesService.getUomRangeValuesList(id)

  return lastValueFrom(uomRangeValuesService$).then(uomRangeValues => { return uomRangeValues; })

    .catch(err => {

      console.error('Cannot retrieve uomRangeValues', err);

      lockoutService.lockout();

    });

}
