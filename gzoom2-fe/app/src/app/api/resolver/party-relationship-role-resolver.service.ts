import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { PartyRelationshipRoleService } from '../service/party-relationship-role.service';
import { PartyRelationshipRole } from '../model/partyRelationshipRole';


export const partyRelationshipRoleResolver: ResolveFn<void | PartyRelationshipRole[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | PartyRelationshipRole[]> => {

    const partyRelationshipRoleService = inject(PartyRelationshipRoleService);
    const lockoutService = inject(LockoutService);

    const partyRelationshipRoleService$ = partyRelationshipRoleService.getPartyRelationshipRole(route.paramMap.get('partyRelationshipTypeId'));
    return lastValueFrom(partyRelationshipRoleService$).then(partyRelationshipRole => { return partyRelationshipRole; })
        .catch(err => {
            console.error('Cannot retrieve partRelationshipRole', err);
            lockoutService.lockout();
        });
}
