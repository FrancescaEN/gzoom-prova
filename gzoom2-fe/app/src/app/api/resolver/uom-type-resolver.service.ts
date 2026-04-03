import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';

import { lastValueFrom } from 'rxjs';


import { LockoutService } from '../../commons/service/lockout.service';
import { UomService } from '../service/uom.service';
import { UomType } from 'app/view/ctx-ac/uom-type/uom_type';

/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */
export const uomTypeResolver: ResolveFn<void | UomType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | UomType[]> => {
  console.log('resolve UomType');
  const uomService = inject(UomService);
  const lockoutService = inject(LockoutService);

  const uomService$ = uomService.uomTypes();
  return lastValueFrom(uomService$).then(uomTypes => { return uomTypes; })
    .catch(err => { // TODO devo fare il lockout?
      console.error('Cannot retrieve uomType', err);
      lockoutService.lockout(); // TODO cos'e?
    });
}