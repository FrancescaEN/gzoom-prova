import {Injectable, OnDestroy} from '@angular/core';
import {FieldType} from "./model/enum/FieldType";
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DynamicFormHelperService implements OnDestroy {

  // todo remove unused
  fieldTypes = FieldType;
  // ordering set
  newGoalFields = new Set(['goalType', 'code', 'name', 'fromDate', 'thruDate', 'supervisorOrganizationUnit']);
  newGoalFieldsOrder = ['goalType', 'code', 'name', 'fromDate', 'thruDate', 'supervisorOrganizationUnit'];

  resetFormSubject$: Subject<null> = new Subject<null>();

  refreshListOnApiCallSubject$: Subject<any> = new Subject<any>();

  selectValueChangedSubject$: Subject<any> = new Subject<any>();

  refreshFieldStructureSubject$: Subject<any> = new Subject<any>();


  constructor() { }

  ngOnDestroy(): void {

    console.log("DynamicFormHelperService destroyed");
    this.resetFormSubject$.unsubscribe();
    this.refreshListOnApiCallSubject$.unsubscribe();
    this.selectValueChangedSubject$.unsubscribe();
    this.refreshFieldStructureSubject$.unsubscribe();
  }


}
