import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { UserPreference } from '../../shared/user-preference';
import { AuthService } from '../../commons/service/auth.service';
import { UserLoginValidPartyRole } from '../model/userLoginValidPartyRole';


@Injectable()
export class UserPreferenceService {


  constructor(private client: ApiClientService,
    private readonly authService: AuthService) { }


  updateUserPreference(userPreference: UserPreference): Observable<boolean> {
    return this.client.put(`user-preference/`, userPreference)
      .pipe(map(json => json as boolean));
  }

  getUserPreferenceNA(userPrefTypeId: String): Observable<UserPreference> {
    return this.client
      .get(`user-preference-na/` + userPrefTypeId).pipe(
        map(json => json as UserPreference)
      );
  }

  getUserPreference(userPrefTypeId: String): Observable<UserPreference> {
    return this.client
      .get(`user-preference/` + userPrefTypeId).pipe(
        map(json => json as UserPreference)
      );
  }

  getOrganizationId(): Observable<string> {
    return this.getUserPreference('ORGANIZATION_PARTY').pipe(map(userPreference => userPreference.userPrefValue))
  }
  getDefaultPortalPage() {
    return this.client.get(`/user-preference/default-portal-page`).pipe(
      map(ret => ret as string)
    );
  }

  getOrganizationMultiType() {
    return this.client.get('/profile/organization-multi-type').pipe(
      map(json => json as string)
    );
  }

  getOrganizations() {
    const user = this.authService.userProfile();
    return this.client.get('/user-login-party-role/' + user.username).pipe(
      map(json => json as UserLoginValidPartyRole[])
    );
  }

}
