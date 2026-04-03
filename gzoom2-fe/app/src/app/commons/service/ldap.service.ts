import { Injectable } from '@angular/core';
import { ApiClientService } from './client.service';
import { Observable, map } from 'rxjs';

@Injectable()
export class LdapService {
 
  constructor(private client: ApiClientService) {}

   ldapAuthorization(): Observable<boolean> {
     return this.client.get('/api/getEnableChangePassword').pipe(
       map(allowChangePassword => allowChangePassword as boolean)
     );
  }

}
