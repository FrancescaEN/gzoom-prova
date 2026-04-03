import { lastValueFrom, Subject } from 'rxjs';
import { Injectable } from '@angular/core';
import { UserLogin } from '../user-login';
import { ApiClientService } from '../../commons/service/client.service';
import { AuthModeService } from 'app/commons/service/auth-mode.service';
import { KeycloakAuthService } from 'app/commons/service/keycloak-auth.service';

@Injectable()
export class ChangePasswordService {
  private popupSubject = new Subject<any>();
  popupObservable = this.popupSubject.asObservable();
  
  constructor(private client: ApiClientService,
     private authMode: AuthModeService,
     private keycloakAuth: KeycloakAuthService
  ) { }

  openPopup(user: UserLogin) {
    if (this.authMode.isKeycloakEnabled()) {
      return this.keycloakAuth.changePassword();
    }
    
    this.popupSubject.next(user);
  }

  changePassword(username: String, password: String, newPassword: String):  Promise<UserLogin> {
    const body = JSON.stringify({ username: username, password: password, newPassword: newPassword });

    const client$ = this.client.post('change-password', body);
    return lastValueFrom(client$).then(response => response)
    .catch(async (error) => {
      return Promise.reject(await error.error.message);
    });

    // return this.client
    //   .post('change-password', body)
    //   .toPromise()
    //   .then(response => response)
    //   .catch(async (error) => {
    //     return Promise.reject(await error.error.message);
    //   });
    }
}
