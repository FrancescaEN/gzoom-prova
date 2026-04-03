import { Injectable } from '@angular/core';
import { Observable, ReplaySubject, Subject, distinctUntilChanged, startWith } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ButtonService {

  private disableSave = new Subject<boolean>();
  private showSave = new Subject<boolean>();
  private emitSave = new Subject<boolean>();
  private emitReset = new Subject<boolean>();
  private emitReload = new Subject<boolean>();

  constructor() {
    this.disableSaveButton(true);
  }

  disableSaveButton(value: boolean) {
    this.disableSave.next(value);
  }

  showSaveButton(value: boolean) {
    this.showSave.next(value);
  }

  getDisableSaveButton(): Observable<boolean> {
    return this.disableSave.asObservable()
      .pipe(
        startWith(true));
  }

  getShowSaveButton(): Observable<boolean> {
    return this.showSave.asObservable();
  }

  save() {
    this.emitSave.next(true);
  }

  clickSave(): Observable<boolean> {
    return this.emitSave.asObservable();
  }

  reset() {
    this.emitReset.next(true);
  }

  clickReset(): Observable<boolean> {
    return this.emitReset.asObservable();
  }

  reload() {
    this.emitReset.next(true);
  }

  clickReload(): Observable<boolean> {
    return this.emitReset.asObservable();
  }
}
