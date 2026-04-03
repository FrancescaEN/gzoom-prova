import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';

import { lastValueFrom } from 'rxjs';

import { UserPreferenceService } from '../api/service/user-preference.service';

import { UserPreference } from './user-preference';

/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */
export const visualThemeResolver: ResolveFn<void | UserPreference> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | UserPreference> => {
  const userPreferenceService = inject(UserPreferenceService);
  const userPreferenceService$ = userPreferenceService.getUserPreference('VISUAL_THEME')
  return lastValueFrom(userPreferenceService$).then(userPreference => { return userPreference; })
    .catch(err => {
      console.error('Cannot retrieve userPreference', err);
      //this.lockoutService.lockout(); cancella tutta la sessione e ti rimanda all'homepage
    });
}
