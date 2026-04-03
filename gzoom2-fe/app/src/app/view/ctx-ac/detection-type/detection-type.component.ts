import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { MenuItem, } from 'primeng/api';
import { BehaviorSubject, Observable, Subject, lastValueFrom, map, mergeMap, mergeWith, switchMap } from 'rxjs';
import { HeadArray, ActionInput, ActionOutput, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { GlFiscalTypeService } from 'app/api/service/gl-fiscal-type.service';
import { LanguageService } from 'app/api/service/language.service';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { GlFiscalType } from 'app/api/model/glFiscalType';
import { MsgService } from 'app/commons/service/message.service';



@Component({
  selector: 'app-detection-type',
  templateUrl: './detection-type.component.html',
  styleUrls: ['./detection-type.component.css']
})
export class DetectionTypeComponent implements OnInit {
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  reload: boolean = false;
  elementToAdd: any;

  loading: boolean = true;

  headArray: HeadArray[] = [
    { head: '', fieldName: 'idNumber', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" },
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: any[] = [];
  editingKeyId: string;
  selectionList: any[] = [];
  newRow: any;
  flag: boolean = false;
  er: boolean = false;

  secondaryLang: boolean;
  dropdownPhase: MenuItem[] = [];
  dropdownY_N: MenuItem[] = [
    { label: this.i18nService.translate('Y'), id: 'Y' },
    { label: this.i18nService.translate('N'), id: 'N' }];

  constructor(
    private route: ActivatedRoute,
    private readonly glFiscalTypeService: GlFiscalTypeService,
    private readonly enumerationService: EnumerationService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private msgService: MsgService
  ) {

  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    /*const reload = this._reload.pipe(mergeMap(() => this.glFiscalTypeService.getGlFiscalType()));
    const w$ = this.route.data.pipe(
      map((data: { obss: GlFiscalType[] }) => data.obss),
      mergeWith(reload)
    );*/

    const w$ = this.reload$.pipe(
      switchMap(() => this.glFiscalTypeService.getGlFiscalType())
    );

    await this.setEnumerationDropdown();
    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();
      this.headArray[3].pathIconFlag = lang[0];
      this.headArray[4].pathIconFlag = lang[1];


    } else { this.setHeadArray(); }

    w$
      .subscribe(y => {
        this.gridArray = [];
        if (this.newRow) this.gridArray.push(this.newRow);
        y.forEach((e, index) => {

          this.gridArray.push({
            glFiscalTypeId: e.glFiscalTypeId,
            description: e.description,
            descriptionLang: e.descriptionLang,
            glFiscalTypeEnumId: e.glFiscalTypeEnumId,
            phaseDesc: this.dropdownPhase.find(x => x.id == e.glFiscalTypeEnumId)?.label,
            isFinancialUsed: e.isFinancialUsed,
            isAccountUsed: e.isAccountUsed,
            isIndicatorUsed: e.isIndicatorUsed,
            isFinancialUsedDesc: ((e.isFinancialUsed) ? this.i18nService.translate(e.isFinancialUsed) : null),
            isAccountUsedDesc: ((e.isAccountUsed) ? this.i18nService.translate(e.isAccountUsed) : null),
            isIndicatorUsedDesc: ((e.isIndicatorUsed) ? this.i18nService.translate(e.isIndicatorUsed) : null),

            variableGridArray: {
              id: e.glFiscalTypeId,
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
        })
        this.loading = false;
      });
  }

  setHeadArray() {
    this.headArray.push({ head: 'Code', fieldName: 'glFiscalTypeId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, width: "5vw", textLength: 20 });
    this.headArray.push({ head: 'Description', fieldName: "description", flag: this.flag, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, });
    if (this.i18nService.getLanguageType() == "BILING") this.headArray.push({ head: 'Description', fieldName: 'descriptionLang', flag: true, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, });
    this.headArray.push({
      head: 'Phase', fieldName: "phaseDesc", actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter,
      dropdown: {
        item: this.dropdownPhase,
        clear: false,
        key: 'glFiscalTypeEnumId',
        command: async () => {
          this.headArray.filter(x => x.fieldName == 'phaseDesc').forEach(y => y.dropdown.loading = true);
          await this.setEnumerationDropdown();
          this.headArray.filter(x => x.fieldName == 'phaseDesc').forEach(y => y.dropdown.item = this.dropdownPhase);
          this.headArray.filter(x => x.fieldName == 'phaseDesc').forEach(y => y.dropdown.loading = false);

        }
      }, required: true, sortIcon: false
    });
    this.headArray.push({ head: 'Financial movements', fieldName: "isFinancialUsedDesc", actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, dropdown: { item: this.dropdownY_N, clear: false, key: 'isFinancialUsed' }, sortIcon: false });
    this.headArray.push({ head: 'isAccountUsed', fieldName: "isAccountUsedDesc", actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, dropdown: { item: this.dropdownY_N, clear: false, key: 'isAccountUsed' }, sortIcon: false });
    this.headArray.push({ head: 'Indicator movements', fieldName: "isIndicatorUsedDesc", actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, dropdown: { item: this.dropdownY_N, clear: false, key: 'isIndicatorUsed' }, sortIcon: false });
    this.headArray.push({
      head: "",
      fieldName: "null",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.null,
      filter: HeadFilter.null,
      width: '0.5vw'
    })
  }

  async setEnumerationDropdown() {
    this.dropdownPhase = [];
    await lastValueFrom(this.enumerationService.enumerations('GL_FISCAL_TYPE'))
      .then(e =>
        e.forEach(item => this.dropdownPhase.push({ label: (!this.secondaryLang) ? item.description : item.descriptionLang, id: item.enumId }))
      ).catch((error) => console.log(error));
  }

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  openNew() {
    this.elementToAdd = {

      glFiscalTypeId: null,
      description: null,
      descriptionLang: null,
      glFiscalTypeEnumId: null,
      phaseDesc: null,
      isFinancialUsed: 'N',
      isAccountUsed: 'N',
      isIndicatorUsed: 'N',
      isFinancialUsedDesc: this.i18nService.translate('N'),
      isAccountUsedDesc: this.i18nService.translate('N'),
      isIndicatorUsedDesc: this.i18nService.translate('N'),

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
      let obj = new GlFiscalType(
        e.glFiscalTypeId,
        e.description,
        e.descriptionLang,
        e.glFiscalTypeEnumId,
        e.isFinancialUsed,
        e.isAccountUsed,
        e.isIndicatorUsed
      );

      await this.glFiscalTypeService.createGlFiscalType(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.glFiscalTypeId)
          e.variableGridArray.id = obj.glFiscalTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.glFiscalTypeId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new GlFiscalType(
        e.glFiscalTypeId,
        e.description,
        e.descriptionLang,
        e.glFiscalTypeEnumId,
        e.isFinancialUsed,
        e.isAccountUsed,
        e.isIndicatorUsed
      );

      await this.glFiscalTypeService.updateGlFiscalType(obj)
        .then(() => {
          this.msgService.successUpdate(obj.glFiscalTypeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.glFiscalTypeId);
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

        this.glFiscalTypeService.deleteGlFiscalType(e.glFiscalTypeId)
          .then(() => {
            this.msgService.successDeleteWithId(e.glFiscalTypeId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.glFiscalTypeId);
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
