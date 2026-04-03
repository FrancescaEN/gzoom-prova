import { inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';

import { lastValueFrom, Observable } from 'rxjs';


import { LockoutService } from '../../commons/service/lockout.service';
import { PartyService } from '../service/party.service';
import { Party } from '../model/party';

/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */
export const orgUnitResolver: ResolveFn<void | Party[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | Party[]> => {
  const partyService = inject(PartyService);
  const lockoutService = inject(LockoutService);
  const parentTypeId = route.parent.data.context;
  console.log('resolve orgUnits parentTypeId=' + parentTypeId);

  const partyService$ = partyService.orgUnits(parentTypeId, null, null, 'Company');
  return lastValueFrom(partyService$).then(orgUnits => { return orgUnits; })
    .catch(err => {
      console.error('Cannot retrieve party', err);
      lockoutService.lockout();
    });
}
