import { Injectable } from '@angular/core';


import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { Party } from '../model/party';
import { UserPreferenceService } from './user-preference.service';
import { PartyEx } from '../model/partyEx';
import {GenericService} from "./commons/generic.service";

@Injectable({
  providedIn: 'root'
})
export class PartyService extends GenericService {
  constructor(protected client: ApiClientService,
              private userPreferenceService: UserPreferenceService) {
    super(client);
  }

  partys(parentTypeId: string): Observable<Party[]> {
    return this.client
      .get(`party/${parentTypeId}`).pipe(
        map(json => json.results as Party[])
      );
  }

  orgUnits(parentTypeId: string, options: string, workEffortTypeId: string, company: string): Observable<Party[]> {
    let optionsURL = '';
    if (options) {
      optionsURL = '?roleTypeId=' + options;
    }
    if (workEffortTypeId) {
      optionsURL += optionsURL === '' ? `?workEffortTypeId=${workEffortTypeId}` : `&workEffortTypeId=${workEffortTypeId}`;
    }
    if (company) {
      optionsURL += optionsURL === '' ? `?company=${company}` : `&company=${company}`;
    }

    return this.client
      .get(`orgUnits/${parentTypeId}` + optionsURL).pipe(
        map(json => json.results as Party[])
      );
  }

  orgUOGestore(context: string): Observable<PartyEx[]> {
    return this.userPreferenceService.getOrganizationId().pipe(
      switchMap((organizationId) => this.client
        .get(`uo-gestore/${context}/${organizationId}`).pipe(
          map(json => json.results as PartyEx[])
        ))
    );

  }

  getPartyByRoleTypeId(roleTypeId: string): Observable<Party[]> {
    return this.client
      .get(`party/role-type/${roleTypeId}`).pipe(
        map(json => json.results as Party[])
      );

  }

  getByRoleTypeIdAndNotInGlAccount(roleTypeId: string, glAccountId: string): Observable<Party[]> {
    return this.client
      .get(`party/role-type/${roleTypeId}/${glAccountId}`).pipe(
        map(json => json.results as Party[])
      );

  }

  getUOGestoreByRespCenterRoleTypeId(context: string, respCenterRoleTypeId: string): Observable<PartyEx[]> {
    return this.client
      .get(`uo-gestore/${context}/resp-center-role-type/${respCenterRoleTypeId}`).pipe(
        map(json => json.results as PartyEx[])
      );

  }

  getPartyByOrgId(): Observable<Party[]> {
    return this.client
      .get(`party/partyByOrgId`).pipe(
        map(json => json.results as Party[])
      );
  }

  roleTypePartys(roleTypeId: string, options: string, workEffortTypeId: string): Observable<Party[]> {
    let optionsURL = '';
    if (options) {
      optionsURL = '?roleTypeIdFrom=' + options;
    }
    if (workEffortTypeId) {
      optionsURL += optionsURL === '' ? `?workEffortTypeId=${workEffortTypeId}` : `&workEffortTypeId=${workEffortTypeId}`;
    }
    return this.client
      .get(`party/roleType/${roleTypeId}` + optionsURL).pipe(
        map(json => json.results as Party[])
      );
  }

  roleTypePartysBetween(roleTypeId: string): Observable<Party[]> {
    return this.client
      .get(`party/roleType/between/${roleTypeId}`).pipe(
        map(json => json.results as Party[])
      );
  }

  roleTypePartysIn(roleTypeId: string): Observable<Party[]> {
    return this.client
      .get(`party/roleType/in/${roleTypeId}`).pipe(
        map(json => json.results as Party[])
      );
  }

  roleTypePartysNotIn(roleTypeId: string): Observable<Party[]> {
    return this.client
      .get(`party/roleType/notin/${roleTypeId}`).pipe(
        map(json => json.results as Party[])
      );
  }

  getManagers(): Observable<Party[]> {
    return this.client
      .get(`party/managers`).pipe(
        map(json => json.results as Party[])
      );
  }

  getPartiesByRoleTypeIdAndCurrentOrganizationId(roleTypeId: string): Observable<Party[]> {
    return this.client
      .get(`party/role-type/${roleTypeId}/current-organization`).pipe(
        map(json => json.results as Party[])
      );
  }


  getHeaderPortalPage(): Observable<String[]> {
    return this.client
      .get(`party/headerPortalPage`).pipe(
        map(json => json as String[])
      );
  }
}
