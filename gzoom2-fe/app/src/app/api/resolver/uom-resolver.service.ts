import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { UomService } from '../service/uom.service';
import { Uom } from 'app/api/model/uom';

/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */

export const uomResolver: ResolveFn<void | Uom[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | Uom[] | Observable<void | Uom[]> | Promise<void | Uom[]> => {

  console.log('resolve uom');

  const uomService = inject(UomService);

  const lockoutService = inject(LockoutService);

  const uomService$ = uomService.uoms();

  return lastValueFrom(uomService$).then(uoms => { return uoms; })

    .catch(err => {

      console.error('Cannot retrieve uom', err);

      lockoutService.lockout();

    });

}
