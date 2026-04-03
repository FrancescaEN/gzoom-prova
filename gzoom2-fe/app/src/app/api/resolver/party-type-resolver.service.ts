import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';

import { lastValueFrom, Observable } from 'rxjs';


import { LockoutService } from '../../commons/service/lockout.service';
import { PartyTypeService } from '../service/party-type.service';
import { PartyType } from '../model/party-type';


export const partyTypeResolverService: ResolveFn<void | PartyType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | PartyType[] | Observable<void | PartyType[]> | Promise<void | PartyType[]> => {

    console.log('resolve Party Type');

    const partyTypeService = inject(PartyTypeService);

    const lockoutService = inject(LockoutService);

    const obs$ = partyTypeService.partyTypes();

    return lastValueFrom(obs$).then(partyTypes => { return partyTypes; })

        .catch(err => {

            console.error('Cannot retrieve Party Type', err);

            lockoutService.lockout();

        });

}
