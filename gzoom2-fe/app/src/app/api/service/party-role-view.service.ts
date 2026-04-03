import { Injectable } from "@angular/core";
import { ApiClientService } from "app/commons/service/client.service";
import { PartyRoleView } from "../model/partyRoleView";
import { Observable, map } from "rxjs";

/**
 *  @author Leonardo Minaudo
 */
@Injectable({
  providedIn: 'root'
})
export class PartyRoleViewService {
  constructor(private client: ApiClientService) { }

  getPartyRoleView(
    statusId: string,
    organizationId: string
  ): Observable<PartyRoleView[]> {
    return this.client
      .get(`party-role-view/${statusId}/${organizationId}`)
      .pipe(map((json) => json.results as PartyRoleView[]));
  }

  getPartyRoleViewRoleTypeId(
    statusId: string,
    organizationId: string,
    roleTypeId: string
  ): Observable<PartyRoleView[]> {
    return this.client
      .get(`party-role-view/${statusId}/${organizationId}/${roleTypeId}`)
      .pipe(map((json) => json.results as PartyRoleView[]));
  }

  getPartyRoleViewByRoleTypeId(
    roleTypeId: string
  ): Observable<PartyRoleView[]> {
    return this.client
      .get(`party-role-view/${roleTypeId}`)
      .pipe(map((json) => json.results as PartyRoleView[]));
  }
}
