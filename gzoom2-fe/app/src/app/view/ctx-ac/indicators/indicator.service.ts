import { Injectable } from '@angular/core';
import { GlAccount } from 'app/api/model/glAccount';
import { SelectItem } from 'primeng/api';
import { Observable, ReplaySubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class IndicatorService {
  private subject = new ReplaySubject<GlAccount>(1);
  private title = new Subject<string>();
  private code = new Subject<string>();
  private currentStatus = new Subject<string>();

  private statusItems = new ReplaySubject<SelectItem[]>(1);

  private enableValueList = new Subject<boolean>();


  setGlAccount(glAccount: GlAccount) {
    this.subject.next(glAccount);
  }

  clearGlAccount() {
    this.subject.next(null);
  }

  getGlAccount(): Observable<GlAccount> {
    return this.subject.asObservable();
  }

  setTitle(title: string) {
    this.title.next(title);
  }

  getTitle(): Observable<string> {
    return this.title.asObservable();
  }

  setCode(code: string) {
    this.code.next(code);
  }

  getCode(): Observable<string> {
    return this.code.asObservable();
  }

  setCurrentStatus(currentStatusId: string) {
    this.currentStatus.next(currentStatusId);
  }

  getCurrentStatus(): Observable<string> {
    return this.currentStatus.asObservable();
  }

  setStatusItems(statusItems: SelectItem[]) {
    this.statusItems.next(statusItems);
  }

  getStatusItems(): Observable<SelectItem[]> {
    return this.statusItems.asObservable();
  }

  setEnableValueList(value: boolean) {
    this.enableValueList.next(value);
  }

  getEnableValueList(): Observable<boolean> {
    return this.enableValueList.asObservable();
  }
}
