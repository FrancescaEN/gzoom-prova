import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { MenuItem } from 'primeng/api';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith } from 'rxjs';
import { HeadArray, ActionInput, ActionOutput, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { LanguageService } from 'app/api/service/language.service';
import { CustomTimePeriod } from 'app/api/model/customTimePeriod';
import { CustomTimePeriodService } from 'app/api/service/custom-time-period.service';
import { PeriodTypeService } from 'app/api/service/period-type.service';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-periods',
  templateUrl: './periods.component.html',
  styleUrls: ['./periods.component.css']
})
export class PeriodsComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;
  flag: boolean = false;
  loading: boolean = true;
  elementToAdd: any;

  headArray: HeadArray[] = [
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: any[] = [];
  newRow: any;
  er: boolean = false;
  secondaryLang: boolean;
  languages: [] = [];

  dropdownIsClosed: MenuItem[] = [{ label: this.i18nService.translate('N'), id: 'N' },
  { label: this.i18nService.translate('Y'), id: 'Y' }];
  dropdownPeriodType: MenuItem[] = [];
  dropdownCustomTypePeriod: MenuItem[] = [];
  tmp: any[];

  constructor(
    private route: ActivatedRoute,
    private readonly customTimePeriodService: CustomTimePeriodService,
    private readonly periodTypeService: PeriodTypeService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    const reload = this._reload.pipe(mergeMap(() => this.customTimePeriodService.getCustomTimePeriod()));
    const w$ = this.route.data.pipe(
      map((data: { obss: CustomTimePeriod[] }) => data.obss),
      mergeWith(reload)
    );


    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();
      this.headArray[2].pathIconFlag = lang[0];
      this.headArray[3].pathIconFlag = lang[1];
      this.headArray[6].pathIconFlag = lang[0];
      this.headArray[7].pathIconFlag = lang[1];


    } else { this.setHeadArray(); }

    await lastValueFrom(this.periodTypeService.periodTypes())
      .then(data =>
        data.forEach(x => {
          this.dropdownPeriodType.push({ label: x.description, id: x.periodTypeId });
        })).catch((error) => console.log(error));


    w$.subscribe(y => {
      this.dropdownCustomTypePeriod = []
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach(x => { this.dropdownCustomTypePeriod.push({ label: (!this.secondaryLang) ? x.periodName : x.periodNameLang, id: x.customTimePeriodId }) });
      this.headArray.filter(x => x.fieldName == 'parentPeriodDesc').forEach(y => y.dropdown.item = this.dropdownCustomTypePeriod);

      y.forEach((e, index) => {
        this.gridArray.push({
          customTimePeriodId: e.customTimePeriodId,
          customTimePeriodCode: e.customTimePeriodCode,
          customTimePeriodCodeLang: e.customTimePeriodCodeLang,
          periodTypeId: e.periodTypeId,
          periodTypeDesc: this.dropdownPeriodType.filter(x => x.id == e.periodTypeId).map(y => y.label)[0],
          periodNum: e.periodNum,
          periodName: e.periodName,
          periodNameLang: e.periodNameLang,
          isClosed: e.isClosed,
          isClosedLabel: this.i18nService.translate(e.isClosed),
          fromDate: e.fromDate? new Date(e.fromDate): null,
          thruDate: e.thruDate? new Date(e.thruDate): null,
          parentPeriodId: e.parentPeriodId,
          parentPeriodDesc: this.dropdownCustomTypePeriod.filter(x => x.id == e.parentPeriodId).map(y => y.label)[0],

          variableGridArray: {
            id: e.customTimePeriodId,
            updated: false,
            buttonDetails: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: false,
            outputData: true,
            inputNew: false,
            dropdownData: true,
          }

        })
        this.loading = false;
      });

    })
  }

  setHeadArray() {
    this.headArray.push({ head: 'ID', fieldName: 'customTimePeriodId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, required: true, unique: true, textLength: 20 },
      { head: 'Code', fieldName: 'customTimePeriodCode', actionInput: ActionInput.inputLabeldata, flag: this.flag, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, required: true, textLength: 60 })
    if (this.i18nService.getLanguageType() == "BILING") this.headArray.push({ head: 'Code', fieldName: 'customTimePeriodCodeLang', flag: true, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, required: true, textLength: 60, width: '5vw' });
    this.headArray.push({
      head: 'Periodicity', fieldName: 'periodTypeDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData,filter: HeadFilter.dropdownFilter,
      dropdown: {
        item: this.dropdownPeriodType,
        clear: false,
        key: "periodTypeId",
        command: async () => {
          this.dropdownPeriodType = []
          await lastValueFrom(this.periodTypeService.periodTypes())
            .then(data =>
              data.forEach(x => {
                this.dropdownPeriodType.push({ label: x.description, id: x.periodTypeId });
              })).catch((error) => console.log(error));

          this.headArray.filter(x => x.fieldName == 'periodTypeDesc').forEach(y => y.dropdown.item = this.dropdownPeriodType);

        }
      }, required: true
    },
      { head: 'Number', fieldName: 'periodNum', content: 'center', actionInput: ActionInput.inputLabelNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.popUpFilter, required: true },
      { head: 'Description', fieldName: 'periodName', flag: this.flag, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 100 });
    if (this.i18nService.getLanguageType() == "BILING") this.headArray.push({ head: 'Description', fieldName: 'periodNameLang', flag: true, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 100, width: '8vw' });
    this.headArray.push({ head: 'Closed', fieldName: 'isClosedLabel', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, dropdown: { item: this.dropdownIsClosed, clear: false, key: 'isClosed' }, sortIcon: false, required: true, content: "center" },
      { head: 'Start Date', fieldName: 'fromDate', actionInput: ActionInput.inputDate, actionOutput: ActionOutput.outputDate, filter: HeadFilter.dateFilter, required: true },
      { head: 'End Date', fieldName: 'thruDate', actionInput: ActionInput.inputDate, actionOutput: ActionOutput.outputDate, filter: HeadFilter.dateFilter, required: true },
      {
        head: 'Classification', fieldName: 'parentPeriodDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter,
        dropdown: {
          item: this.dropdownCustomTypePeriod,
          clear: true,
          key: 'parentPeriodId',
          command: async () => {
            this.dropdownCustomTypePeriod = [];
            await lastValueFrom(this.customTimePeriodService.getCustomTimePeriod())
              .then(y => {
                y.forEach(x => { this.dropdownCustomTypePeriod.push({ label: (!this.secondaryLang) ? x.periodName : x.periodNameLang, id: x.customTimePeriodId }) });

              }).catch((error) => console.log(error));
            this.headArray.filter(x => x.fieldName == 'parentPeriodDesc').forEach(y => y.dropdown.item = this.dropdownCustomTypePeriod);
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

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  openNew() {

    this.elementToAdd = {

      customTimePeriodId: null,
      customTimePeriodCode: null,
      customTimePeriodCodeLang: null,
      periodTypeId: null,
      periodTypeDesc: null,
      periodNum: null,
      periodName: null,
      periodNameLang: null,
      isClosed: "N",
      isClosedLabel: "N",
      fromDate: null,
      thruDate: null,
      parentPeriodId: null,
      parentPeriodDesc: null,


      variableGridArray: {
        id: "new" + Math.random(),
        buttonDetails: false,
        dropdownData: true,
        inputLabeldata: true,
        inputLabelNumber: true,
        inputNotes: false,
        outputData: false,
        inputNew: true,
        updated: true,
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
      let obj = new CustomTimePeriod(
        e.customTimePeriodId,
        e.customTimePeriodCode,
        e.customTimePeriodCodeLang,
        e.periodNum,
        e.periodTypeId,
        e.periodName,
        e.periodNameLang,
        e.isClosed,
        e.parentPeriodId,
        e.fromDate,
        e.thruDate
      );

      await this.customTimePeriodService.createCustomTimePeriod(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.customTimePeriodId);
          e.variableGridArray.id = obj.customTimePeriodId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.customTimePeriodId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new CustomTimePeriod(
        e.customTimePeriodId,
        e.customTimePeriodCode,
        e.customTimePeriodCodeLang,
        e.periodNum,
        e.periodTypeId,
        e.periodName,
        e.periodNameLang,
        e.isClosed,
        e.parentPeriodId,
        e.fromDate,
        e.thruDate
      );

      await this.customTimePeriodService.updateCustomTimePeriod(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.customTimePeriodId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.customTimePeriodId);
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

        this.customTimePeriodService.deleteCustomTimePeriod(e.customTimePeriodId)
          .then(() => {
            this.msgService.successDeleteWithId(e.customTimePeriodId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.customTimePeriodId);
            this._reload.next();
          });

      });

    }
    else {
      this.msgService.successDelete();
    }

  }

  resetAllElement() {
    this._reload.next();
  }
}
