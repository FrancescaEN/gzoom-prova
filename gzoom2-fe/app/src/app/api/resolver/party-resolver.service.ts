import { inject, Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';

import { lastValueFrom, Observable } from 'rxjs';


import { LockoutService } from '../../commons/service/lockout.service';
import { PartyService } from '../service/party.service';
import { Party } from '../model/party';

/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */
export const partyResolver: ResolveFn<void | Party[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | Party[]> => {
  const partyService = inject(PartyService);
  const lockoutService = inject(LockoutService);
  console.log('resolve partys');
  var parentTypeId = 'CTX_PR';
  console.log('resolve orgUnits parentTypeId=' + parentTypeId);

  const partyService$ = partyService.partys(parentTypeId);
  return lastValueFrom(partyService$).then(partys => { return partys; })
    .catch(err => {
      console.error('Cannot retrieve party', err);
      lockoutService.lockout();
    });
}
