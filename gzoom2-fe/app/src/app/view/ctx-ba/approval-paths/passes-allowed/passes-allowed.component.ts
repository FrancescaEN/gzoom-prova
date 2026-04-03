import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, Subscription, lastValueFrom, map, mergeMap, mergeWith, tap } from 'rxjs';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { MenuItem } from 'primeng/api';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { StatusValidChange } from 'app/api/model/statusValidChange';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { StatusValidChangeService } from 'app/api/service/status-valid-change.service';
import { StatusItemService } from 'app/api/service/status-item.service';
import { MsgService } from 'app/commons/service/message.service';
import { StatusType } from 'app/api/model/statusType';
import { StatusTypeService } from 'app/api/service/status-type.service';

@Component({
  selector: 'app-passes-allowed',
  templateUrl: './passes-allowed.component.html',
  styleUrls: ['./passes-allowed.component.css']
})
export class PassesAllowedComponent implements OnInit, OnDestroy {
  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  flag: boolean = false;
  loading: boolean = true;
  er: boolean = false;

  headArray: HeadArray[] = [];

  gridArray: any[] = [];
  newRow: any;
  secondaryLang: boolean;
  languages: [] = [];

  statusTypeId: string;

  title: string;

  dropdownState: MenuItem[] = [];
  dropdownStateTo: MenuItem[] = [];

  obs$: Subscription;

  selectedStatusType: StatusType;

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly dataStorageService: DataStorageService,
    private readonly router: Router,
    private readonly statusTypeService: StatusTypeService,
    private readonly statusValidChangeService: StatusValidChangeService,
    private readonly statusItemService: StatusItemService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  ngOnDestroy(): void {
    this.obs$.unsubscribe();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    const routeParams = this.route.parent.snapshot.paramMap;
    this.statusTypeId = routeParams.get('statusTypeId');

    this.selectedStatusType = await lastValueFrom(this.statusTypeService.getStatusTypeById(this.statusTypeId))
    this.title =  this.selectedStatusType.statusTypeId + " - " + this.selectedStatusType.description;

    const reload = this._reload.pipe(mergeMap(() => this.statusValidChangeService.getByStatusTypeId(this.statusTypeId)));
    const data$ = this.route.data.pipe(
      map((data: { obss: StatusValidChange[] }) => data.obss),
      mergeWith(reload)
    );
    await this.setStateDropdown();
    await this.setToStateDropdown();

    this.setHeadArray();

    this.obs$ = data$.pipe(
      tap(() => {
        this.gridArray = [];
        if (this.newRow) this.gridArray.push(this.newRow);
      }),
      map((array: StatusValidChange[]) => {
        array.map((x) => {

          this.gridArray.push({
            statusId: x.statusId,
            statusIdDesc: this.dropdownState.find(y => y.id == x.statusId)?.label,
            statusIdTo: x.statusIdTo,
            statusIdToDesc: this.dropdownStateTo.find(y => y.id == x.statusIdTo)?.label,
            transitionName: x.transitionName,
            conditionExpression: x.conditionExpression,

            variableGridArray: {
              id: x.statusId + x.statusIdTo,
              updated: false,
              inputLabeldata: true,
              inputLabelNumber: true,
              inputNotes: true,
              outputData: true,
              inputNew: false,
              dropdownData: true,
            }
          })
        })
      })
    ).subscribe(() => {
      this.loading = false;
    })
  }

  setHeadArray() {
    this.headArray.push(

      {
        head: 'Stato di partenza', fieldName: 'statusIdDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, required: true, unique: true,
        dropdown: {
          item: this.dropdownState,
          clear: false,
          key: 'statusId',
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'statusIdDesc').forEach(y => y.dropdown.loading = true);
            await this.setStateDropdown();
            this.headArray.filter(x => x.fieldName == 'statusIdDesc').forEach(y => y.dropdown.item = this.dropdownState);
            this.headArray.filter(x => x.fieldName == 'statusIdDesc').forEach(y => y.dropdown.loading = false);

          }
        }
      },
      {
        head: 'Stato di arrivo', fieldName: 'statusIdToDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, required: true, unique: true,
        dropdown: {
          item: this.dropdownStateTo,
          clear: false,
          key: 'statusIdTo',
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'statusIdToDesc').forEach(y => y.dropdown.loading = true);
            await this.setToStateDropdown();
            this.headArray.filter(x => x.fieldName == 'statusIdToDesc').forEach(y => y.dropdown.item = this.dropdownStateTo);
            this.headArray.filter(x => x.fieldName == 'statusIdToDesc').forEach(y => y.dropdown.loading = false);

          }
        }
      },
      { head: 'Description', fieldName: 'transitionName', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, },
      { head: 'Condition', fieldName: 'conditionExpression', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, textLength: 255, },
      {
        head: "",
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.null,
        filter: HeadFilter.null,
        width: '0.5vw'
      }
    );
  }

  async setStateDropdown() {
    this.dropdownState = [];
    await lastValueFrom(this.statusItemService.getStatusItemStateFrom(this.statusTypeId)).then(data => {

      data.forEach(x => {
        let lab = x.statusType.description + " - " + ((!this.secondaryLang) ? x.description : x.descriptionLang);

        this.dropdownState.push({ label: lab, id: x.statusId });
      })
    }
    ).catch((error) => console.log(error));
  }

  async setToStateDropdown() {
    this.dropdownStateTo = [];
    await lastValueFrom(this.statusItemService.getStatusItemStateTo()).then(data => {

      data.forEach(x => {
        let lab = x.statusType.description + " - " + ((!this.secondaryLang) ? x.description : x.descriptionLang);

        this.dropdownStateTo.push({ label: lab, id: x.statusId });
      })
    }
    ).catch((error) => console.log(error));
  }


  openNew() {
    this.elementToAdd = {
      statusId: null,
      statusIdDesc: null,
      statusIdTo: null,
      statusIdToDesc: null,
      transitionName: null,
      conditionExpression: null,


      variableGridArray: {
        id: "new" + Math.random(),
        updated: true,
        buttonDetails: false,
        inputLabeldata: true,
        inputLabelNumber: true,
        outputData: false,
        inputNew: true,
        dropdownData: true,
        inputNotes: true,
      }

    }

    this.gridArray = [this.elementToAdd, ...this.gridArray];
  }

  saveNewAndOpen(gridElement) {
    if (this.create(gridElement)) {
      this.openNew();
    }
    else this.er = false;
  }

  saveAllElement(elementUpdated) {
    let newElement = elementUpdated.filter(x => x.variableGridArray.id.includes("new"));
    if (newElement.length > 0) {
      this.reload = true;
      this.create(newElement);
      this.reload = false;
    }
    elementUpdated = elementUpdated.filter(x => !x.variableGridArray.id.includes("new"));
    if (elementUpdated.length > 0) {

      this.update(elementUpdated);
    }

  }


  create(gridElement): boolean {
    gridElement.forEach(async e => {
      let obj = new StatusValidChange(
        e.statusId,
        e.statusIdTo,
        e.transitionName,
        e.conditionExpression
      );

      await this.statusValidChangeService.createStatusValidChange(obj, this.statusTypeId)
        .then(() => {
          this.msgService.successCreateWithId(obj.statusId);
          e.variableGridArray.id = obj.statusId + obj.statusIdTo;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.statusId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new StatusValidChange(
        e.statusId,
        e.statusIdTo,
        e.transitionName,
        e.conditionExpression
      );
      await this.statusValidChangeService.updateStatusValidChange(obj, this.statusTypeId)
        .then(() => {
          this.msgService.successUpdateWithId(obj.statusId)
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.statusId);
          this.er = true;
        });
    });

    return !this.er;
  }

  delete(listGridElement) {
    if (listGridElement.filter(x => x.variableGridArray.id.includes("new")).length > 0) {
      this.newRow = listGridElement.filter(x => x.variableGridArray.id.includes("new"));
      listGridElement = listGridElement.filter(x => !this.newRow.includes(x));
      this.gridArray = this.gridArray.filter(x => !x.variableGridArray.id.includes("new"));
      this.newRow = null;
    }
    if (listGridElement.length > 0) {
      listGridElement.forEach(e => {
        this.statusValidChangeService.deleteStatusValidChange(e.statusId, e.statusIdTo)
          .then(() => {
            this.msgService.successDeleteWithId(e.statusId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.statusId);
            this._reload.next();
          });

      });

    }
    else {
      this.msgService.successDelete();
    }

  }

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  resetAllElement() {
    this._reload.next();
  }
}
