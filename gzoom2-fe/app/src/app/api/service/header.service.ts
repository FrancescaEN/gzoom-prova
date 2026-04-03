import { Injectable } from '@angular/core';
import { catchError, map } from 'rxjs/operators';
import { ApiClientService } from '../../commons/service/client.service';
import {  Observable, of } from 'rxjs';

@Injectable()
export class HeaderService {

  constructor(private client: ApiClientService) { }

  isSwitchTitleLogo(): Observable<boolean> {
    return this.client.get("/profile/switchTitleLogo").pipe(
      map(json => json as boolean),
      catchError( () => of(false))
    );
  }


}
