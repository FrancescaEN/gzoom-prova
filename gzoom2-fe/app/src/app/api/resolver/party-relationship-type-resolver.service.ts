import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { PartyRelationshipType } from '../model/partyRelationshipType';
import { PartyRelationshipTypeService } from '../service/party-relationship-type.service';


export const partyRelationshipTypeResolver: ResolveFn<void | PartyRelationshipType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | PartyRelationshipType[]> => {

    const partyRelationshipTypeService = inject(PartyRelationshipTypeService);
    const lockoutService = inject(LockoutService);

    const obs$ = partyRelationshipTypeService.getPartyRelationshipType();
    return lastValueFrom(obs$).then(obs => { return obs; })
        .catch(err => {
            console.error('Cannot retrieve PartyRelationshipType', err);
            lockoutService.lockout();
        });
}