import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiClientService } from '../../commons/service/client.service';
import { ContactMech } from '../model/contactMech';


/**
 * @author Alex Tivoli
 */
@Injectable()
export class ContactMechService {

    constructor(private client: ApiClientService) { }


    /**
     * Gets a infoString from userloginId
     * 
     * @returns Observable<ContactMech[]>
     */
    getContactMechInfoString(): Observable<ContactMech> {
        return this.client
            .get(`contactMech/infoString`).pipe(
                map(json => json as ContactMech)
            );
    }
}