import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { HttpErrorResponse } from '@angular/common/http';
import { PartyRelationshipRole } from '../model/partyRelationshipRole'




@Injectable()
export class PartyRelationshipRoleService {

    constructor(private client: ApiClientService) { }

    /**
     * Gets the list of party_relationship_role by partyRelationshipTypeId.
     * 
     * @returns Observable<PartyRelationshipRole[]>
     */
    getPartyRelationshipRole(partyRelationshipTypeId: string): Observable<PartyRelationshipRole[]> {
        return this.client
            .get(`party-relationship-role/${partyRelationshipTypeId}`).pipe(
                map(json => json.results as PartyRelationshipRole[])
            );
    }

    /**
     * Create a party_relationship_role.
     * 
     * @param partyRelationshipRole - PartyRelationshipRole
     * @returns Promise<PartyRelationshipRole>
     */
    async createPartyRelationshipRole(partyRelationshipRole: PartyRelationshipRole): Promise<PartyRelationshipRole> {
        const client$ = this.client.post(`party-relationship-role/`, JSON.stringify(partyRelationshipRole));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a party_relationship_role.
     * 
     * @param partyRelationshipRole - PartyRelationshipRole
     * @returns Promise<PartyRelationshipRole>
     */
    updatePartyRelationshipRole(partyRelationshipRole: PartyRelationshipRole): Promise<PartyRelationshipRole> {
        const client$ = this.client.put('party-relationship-role/', JSON.stringify(partyRelationshipRole));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the party_relationship_role ids passed to it.
     * 
     * @param partyRelationTypeId - string
     * @returns Promise
     */
    deletePartyRelationshipRole(partyRelationTypeId: string, roleTypeValidFrom: string, roleTypeValidTo: string): Promise<any> {
        const client$ = this.client.delete(`party-relationship-role/${partyRelationTypeId}/${roleTypeValidFrom}/${roleTypeValidTo}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }

}
