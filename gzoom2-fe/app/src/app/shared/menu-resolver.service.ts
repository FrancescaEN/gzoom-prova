import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';

import { lastValueFrom, Observable } from 'rxjs';


import { LockoutService } from '../commons/service/lockout.service';
import { MenuService } from '../commons/service/menu.service';
import { RootMenu } from '../commons/model/dto';

/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */
export const menuResolver: ResolveFn<void | RootMenu> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | RootMenu> => {
  const menuService = inject(MenuService);
  const lockoutService = inject(LockoutService);

  const menuService$ = menuService.menu();
  return lastValueFrom(menuService$).then(root => { return root; })
    .catch(err => {
      console.error('Cannot retrieve menu', err);
      lockoutService.lockout();
    });

}
