import { Injectable, inject } from '@angular/core';
import { ApiClientService } from 'app/commons/service/client.service';
import { Observable, map } from 'rxjs';
import { AcctgTransEntry } from '../model/acctgTransEntry';
import { TableLazyLoad } from 'app/commons/model/tableLazyLoad';

@Injectable({
  providedIn: 'root'
})
export class AcctgTransEntryService {


  private ROOT_REQ = "acctg-trans-entry";
  private client = inject(ApiClientService);

  getIndicatorMovements(accountTypeEnumId: string, inputEnumId: string, detectOrgUnitIdFlag: string, event: TableLazyLoad, isReservedAccount?: string, params?: any): Observable<AcctgTransEntry[]> {
    let url = this.ROOT_REQ + `/indicator-movements/${accountTypeEnumId}/${inputEnumId}/${detectOrgUnitIdFlag}/`;
    if (isReservedAccount) url += isReservedAccount;
    return this.client
      .post(url, event, params).pipe(
        map(json => json.results as AcctgTransEntry[])
      );
  }

  countIndicatorMovements(accountTypeEnumId: string, inputEnumId: string, detectOrgUnitIdFlag: string, event: TableLazyLoad, isReservedAccount?: string, params?: any): Observable<number> {
    let url = this.ROOT_REQ + `/count-indicator-movements/${accountTypeEnumId}/${inputEnumId}/${detectOrgUnitIdFlag}/`;
    if (isReservedAccount) url += isReservedAccount;
    return this.client
      .post(url, event, params).pipe(
        map(json => json as number)
      );
  }

  getMovementByPrimaryKey(acctgTransId: string, acctgTransEntrySeqId: string): Observable<AcctgTransEntry> {
    return this.client.get(`${this.ROOT_REQ}/${acctgTransId}/${acctgTransEntrySeqId}`)
      .pipe(
        map(json => json as AcctgTransEntry)
      );
  }

  updateAcctgTransEntry(acctgTransEntries: AcctgTransEntry[]): Observable<boolean> {
    return this.client.put(this.ROOT_REQ, acctgTransEntries)
      .pipe(map(json => json as boolean));
  }

  updateDetailMovement(acctgTransEntry: AcctgTransEntry): Observable<boolean> {

    return this.client.put(this.ROOT_REQ + '/detail', acctgTransEntry)
      .pipe(map(json => json as boolean));
  }

  createAcctgTransEntryEx(customTimePeriodId: string, acctgTransEntry: AcctgTransEntry): Observable<boolean> {
    return this.client.post(this.ROOT_REQ + `/create/${customTimePeriodId}`, acctgTransEntry)
      .pipe(map(json => json as boolean));
  }

  deleteAcctgTransEntries(acctgTransEntries: AcctgTransEntry[]): Observable<boolean> {
    return this.client.post(this.ROOT_REQ + `/delete`, acctgTransEntries)
      .pipe(map(json => json as boolean));
  }
}
