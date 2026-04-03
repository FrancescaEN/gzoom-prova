import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, Subscription, lastValueFrom, map, mergeMap, mergeWith, tap } from 'rxjs';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { MenuItem } from 'primeng/api';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { StatusItemService } from 'app/api/service/status-item.service';
import { StatusItem } from 'app/api/model/statusItem';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { MsgService } from 'app/commons/service/message.service';
import { StatusTypeService } from 'app/api/service/status-type.service';
import { StatusType } from 'app/api/model/statusType';

@Component({
  selector: 'app-predicted-states',
  templateUrl: './predicted-states.component.html',
  styleUrls: ['./predicted-states.component.css']
})
export class PredictedStatesComponent implements OnInit, OnDestroy {
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

  obs$: Subscription;

  selectedStatusType: StatusType;

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly dataStorageService: DataStorageService,
    private readonly router: Router,
    private readonly statusTypeService: StatusTypeService,
    private readonly statusItemService: StatusItemService,
    private readonly enumService: EnumerationService,
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

    const reload = this._reload.pipe(mergeMap(() => this.statusItemService.getStatusItemList(this.statusTypeId)));
    const data$ = this.route.data.pipe(
      map((data: { obss: StatusItem[] }) => data.obss),
      mergeWith(reload)
    );
    await this.setStateDropdown();

    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();

      this.headArray[1].pathIconFlag = lang[0];
      this.headArray[2].pathIconFlag = lang[1];


    } else { this.setHeadArray(); }


    this.obs$ = data$.pipe(
      tap(() => {
        this.gridArray = [];
        if (this.newRow) this.gridArray.push(this.newRow);
      }),
      map((array: StatusItem[]) => {
        array.map((x) => {

          this.gridArray.push({
            statusId: x.statusId,
            statusTypeId: x.statusTypeId,
            description: x.description,
            descriptionLang: x.descriptionLang,
            statusCode: x.statusCode,
            sequenceId: x.sequenceId,
            actStEnumId: x.actStEnumId,
            actStEnumDesc: this.dropdownState.find((y) => y.id == x.actStEnumId)?.label,

            variableGridArray: {
              id: x.statusId,
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

    let languageType = this.i18nService.getLanguageType();
    this.headArray.push(

      { head: 'Code', fieldName: 'statusId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, textLength: 20 },
      { head: 'Description', fieldName: 'description', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, flag: this.flag, filter: HeadFilter.textFilter, required: true, textLength: 255, },
    );
    if (languageType == "BILING") this.headArray.push({ head: 'Description', fieldName: 'descriptionLang', flag: true, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, });
    this.headArray.push(
      { head: 'Sigla', fieldName: 'statusCode', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 60, },
      { head: 'Sequence', fieldName: 'sequenceId', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, required: true, textLength: 20, },
      {
        head: 'State', fieldName: 'actStEnumDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter,  required: true,
        dropdown: {
          item: this.dropdownState,
          clear: true,
          key: 'actStEnumId',
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'actStEnumDesc').forEach(y => y.dropdown.loading = true);
            await this.setStateDropdown();
            this.headArray.filter(x => x.fieldName == 'actStEnumDesc').forEach(y => y.dropdown.item = this.dropdownState);
            this.headArray.filter(x => x.fieldName == 'actStEnumDesc').forEach(y => y.dropdown.loading = false);

          }
        }
      }, {
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
    await lastValueFrom(this.enumService.enumerations("ACT_STATUS")).then(data => {

      data.forEach(x => {
        let lab = ((!this.secondaryLang) ? x.description : x.descriptionLang);

        this.dropdownState.push({ label: lab, id: x.enumId });
      })
    }
    ).catch((error) => console.log(error));
  }

  openNew() {
    this.elementToAdd = {
      statusId: null,
      statusTypeId: this.statusTypeId,
      description: null,
      descriptionLang: null,
      statusCode: null,
      sequenceId: null,
      actStEnumId: null,
      actStEnumDesc: null,


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
      let obj = new StatusItem(
        e.statusId,
        this.statusTypeId,
        e.description,
        e.descriptionLang,
        e.statusCode,
        e.sequenceId,
        e.actStEnumId
      );

      await this.statusItemService.createStatusItem(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.statusId);
          e.variableGridArray.id = obj.statusId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.statusId)
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new StatusItem(
        e.statusId,
        this.statusTypeId,
        e.description,
        e.descriptionLang,
        e.statusCode,
        e.sequenceId,
        e.actStEnumId
      );
      await this.statusItemService.updateStatusItem(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.statusId);
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
        this.statusItemService.deleteStatusItem(e.statusId)
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
