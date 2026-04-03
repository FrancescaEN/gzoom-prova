import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { HttpErrorResponse } from '@angular/common/http';
import { PartyRelationshipType } from '../model/partyRelationshipType'




@Injectable()
export class PartyRelationshipTypeService {

    constructor(private client: ApiClientService) { }

    /**
     * Gets the list of party_relationship_type.
     * 
     * @returns Observable<PartyRelationshipType[]>
     */
    getPartyRelationshipType(): Observable<PartyRelationshipType[]> {
        return this.client
            .get(`party-relationship-type/`).pipe(
                map(json => json.results as PartyRelationshipType[])
            );
    }

    /**
     * Create a party_relationship_type.
     * 
     * @param partyRelationshipType - PartyRelationshipType
     * @returns Promise<PartyRelationshipType>
     */
    async createPartyRelationshipType(partyRelationshipType: PartyRelationshipType): Promise<PartyRelationshipType> {
        const client$ = this.client.post(`party-relationship-type/`, JSON.stringify(partyRelationshipType));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a party_relationship_type.
     * 
     * @param partyRelationshipType - PartyRelationshipType
     * @returns Promise<PartyRelationshipType>
     */
    updatePartyRelationshipType(partyRelationshipType: PartyRelationshipType): Promise<PartyRelationshipType> {
        const client$ = this.client.put('party-relationship-type/', JSON.stringify(partyRelationshipType));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the party_relationship_type ids passed to it.
     * 
     * @param partyRelationTypeId - string[]
     * @returns Promise
     */
    deletePartyRelationshipType(partyRelationTypeId: string[]): Promise<any> {
        const client$ = this.client.delete(`party-relationship-type/${partyRelationTypeId}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }

}
