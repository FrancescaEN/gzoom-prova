import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { UomRatingScale } from '../model/uomRatingScale';
import { UomRatingScaleService } from '../service/uom-rating-scale.service';

/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */

export const uomRatingScaleResolver: ResolveFn<void | UomRatingScale[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | UomRatingScale[] | Observable<void | UomRatingScale[]> | Promise<void | UomRatingScale[]> => {

  var id = route.paramMap.get('uomId');
  console.log('resolve uomRatingScale ' + id);

  const uomRatingScaleService = inject(UomRatingScaleService);

  const lockoutService = inject(LockoutService);

  const uomService$ = uomRatingScaleService.uomRatingScales(id)

  return lastValueFrom(uomService$).then(uomRatingScales => { return uomRatingScales; })

    .catch(err => {

      console.error('Cannot retrieve uomRatingScales', err);

      lockoutService.lockout();

    });

}
