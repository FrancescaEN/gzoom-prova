import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';
import { PartyType } from '../model/party-type';


@Injectable()
export class PartyTypeService {

  constructor(private client: ApiClientService) { }

  partyTypes(): Observable<PartyType[]> {
    return this.client
      .get(`party-types/`).pipe(
        map(json => json.results as PartyType[])
      );
  }

}
