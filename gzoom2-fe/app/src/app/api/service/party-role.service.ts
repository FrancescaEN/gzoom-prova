import { Injectable } from "@angular/core";
import { ApiClientService } from "app/commons/service/client.service";
import { Observable, map } from "rxjs";
import { PartyRole } from "../model/partyRole";
import { PartyRoleEx } from "../model/partyRoleEx";
import { HttpParams } from "@angular/common/http";

/**
 *  @author Alex Tivoli
 */
@Injectable({
  providedIn: 'root'
})
export class PartyRoleService {
  constructor(private client: ApiClientService) { }

  getPartyRoleOrgId(): Observable<PartyRoleEx[]> {
    return this.client
      .get(`party-role/`)
      .pipe(map((json) => json.results as PartyRoleEx[]));
  }

  getPartyRoleExRoleType(accountTypeEnumId?: string): Observable<PartyRoleEx[]> {
    const params = accountTypeEnumId ? new HttpParams().set('accountTypeEnumId', accountTypeEnumId) : new HttpParams(); 
    return this.client
      .get(`party-role/ex-role-type`, params ) 
      .pipe(map((json) => json.results as PartyRoleEx[]));
  }

  getPartyRole(params: any): Observable<PartyRole[]> {
    return this.client
      .get(`party-role/all`, params)
      .pipe(map((json) => json.results as PartyRole[]));
  }

}
