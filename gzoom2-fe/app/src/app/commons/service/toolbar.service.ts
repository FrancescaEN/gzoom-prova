import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';


@Injectable()
export class ToolbarService {

    private _disabledNew: BehaviorSubject<boolean> = new BehaviorSubject(null);
    private _disabledDelete: BehaviorSubject<boolean> = new BehaviorSubject(null);
    private _disabledSave: BehaviorSubject<boolean> = new BehaviorSubject(null);
    private _disabledReset: BehaviorSubject<boolean> = new BehaviorSubject(null);

    disabledNew$ = this._disabledNew.asObservable();
    disabledDelete$ = this._disabledDelete.asObservable();
    disabledSave$ = this._disabledSave.asObservable();
    disabledReset$ = this._disabledReset.asObservable();


    setDisabledNew(value: boolean) {
        this._disabledNew.next(value);
    }

    setDisabledDelete(value: boolean) {
        this._disabledDelete.next(value);
    }

    setDisabledSave(value: boolean) {
        this._disabledSave.next(value);
    }

    setDisabledReset(value: boolean) {
        this._disabledReset.next(value);
    }

    setPrimaryBoardComponentButton() {
        this._disabledNew.next(false);
        this._disabledDelete.next(true);
        this._disabledSave.next(true);
        this._disabledReset.next(true);
    }

    setDefaultFormComponentButton() {
        this._disabledNew.next(true);
        this._disabledDelete.next(false);
        this._disabledSave.next(true);
        this._disabledReset.next(true);
    }

    getIsDisabledNew$(): Observable<boolean> {
        return this.disabledNew$;
    }

    getIsDisabledDelete$(): Observable<boolean> {
        return this.disabledDelete$;
    }

    getIsDisabledSave$(): Observable<boolean> {
        return this.disabledSave$;
    }

    getIsDisabledReset$(): Observable<boolean> {
        return this.disabledReset$;
    }

}