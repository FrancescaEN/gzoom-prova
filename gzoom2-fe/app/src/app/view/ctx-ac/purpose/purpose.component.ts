import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { MenuItem } from 'primeng/api';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith } from 'rxjs';
import { Table } from 'primeng/table';
import { HeadArray, ActionInput, ActionOutput, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { LanguageService } from 'app/api/service/language.service';
import { WorkEffortPurposeType } from 'app/api/model/workEffortPurposeType';
import { WorkEffortPurposeTypeService } from 'app/api/service/work-effort-purpose-type.service';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-purpose',
  templateUrl: './purpose.component.html',
  styleUrls: ['./purpose.component.css']
})
export class PurposeComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  dataTable: Table;
  flag: boolean = false;
  loading: boolean = true;
  er: boolean = false;

  headArray: HeadArray[] = [
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: any[] = [];
  editingKeyId: string;
  selectionList: any[] = [];
  newRow: any;

  secondaryLang: boolean;
  languages: [] = [];
  dropdownWEPT: MenuItem[] = [];
  dropdownPurposeType: MenuItem[] = [];

  constructor(
    private route: ActivatedRoute,
    private readonly workEffortPurposeTypeService: WorkEffortPurposeTypeService,
    private readonly enumeration: EnumerationService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    const reload = this._reload.pipe(mergeMap(() => this.workEffortPurposeTypeService.getWorkEffortPurposeType()));
    const w$ = this.route.data.pipe(
      map((data: { obss: WorkEffortPurposeType[] }) => data.obss),
      mergeWith(reload)
    );

    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();
      this.headArray[3].pathIconFlag = lang[0];
      this.headArray[4].pathIconFlag = lang[1];


    } else { this.setHeadArray(); }

    await lastValueFrom(this.enumeration.enumerations('PURPOSE_TYPE')).then(data =>
      data.forEach(x => {
        this.dropdownPurposeType.push({ label: (!this.secondaryLang) ? x.description : x.descriptionLang, id: x.enumId });
      })
    ).catch((error) => console.log(error));


    w$.subscribe(y => {
      this.dropdownWEPT = []
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach(x => { this.dropdownWEPT.push({ label: (!this.secondaryLang) ? x.description : x.descriptionLang, id: x.workEffortPurposeTypeId }) });
      this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.item = this.dropdownWEPT);

      y.forEach((e, index) => {
        this.gridArray.push({
          workEffortPurposeTypeId: e.workEffortPurposeTypeId,
          workEffortPurposeTypeCode: e.workEffortPurposeTypeCode,
          purposeTypeEnumId: e.purposeTypeEnumId,
          purposeTypeDesc: this.dropdownPurposeType.filter(x => x.id == e.purposeTypeEnumId).map(y => y.label)[0],
          parentTypeId: e.parentTypeId,
          parentTypeDesc: this.dropdownWEPT.filter(x => x.id == e.parentTypeId).map(y => y.label)[0],
          descriptionLang: e.descriptionLang,
          description: e.description,
          variableGridArray: {
            id: e.workEffortPurposeTypeId,
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
    this.headArray.push(
      { head: 'ID', fieldName: 'workEffortPurposeTypeId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, textLength: 20, width: '5vw' },
      { head: 'Code', fieldName: 'workEffortPurposeTypeCode', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 20, width: '5vw' },
      { head: 'Description', fieldName: 'description', flag: this.flag, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, width: '5vw' });
    if (this.i18nService.getLanguageType() == "BILING") this.headArray.push({ head: 'Description', fieldName: 'descriptionLang', flag: true, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, width: '5vw' });
    this.headArray.push(
      {
        head: 'Classification', fieldName: 'parentTypeDesc', actionInput: ActionInput.dropdownData, filter: HeadFilter.dropdownFilter, actionOutput: ActionOutput.outputLabelData,
        dropdown: {
          item: this.dropdownWEPT,
          clear: true,
          key: 'parentTypeId',
          command: async () => {
            this.dropdownWEPT = [];
            await lastValueFrom(this.workEffortPurposeTypeService.getWorkEffortPurposeType())
              .then(x => {
                x.forEach(y => { this.dropdownWEPT.push({ label: (!this.secondaryLang) ? y.description : y.descriptionLang, id: y.workEffortPurposeTypeId }) });
              }).catch((error) => console.log(error));
            this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.item = this.dropdownWEPT);


          }
        }, width: '5vw'
      },
      {
        head: 'Purpose Type', fieldName: 'purposeTypeDesc', actionInput: ActionInput.dropdownData, filter: HeadFilter.dropdownFilter, actionOutput: ActionOutput.outputLabelData,
        dropdown: {
          item: this.dropdownPurposeType,
          clear: false,
          key: 'purposeTypeEnumId',
          command: async () => {
            this.dropdownPurposeType = [];
            await lastValueFrom(this.enumeration.enumerations('PURPOSE_TYPE')).then(data =>
              data.forEach(x => {
                this.dropdownPurposeType.push({ label: (!this.secondaryLang) ? x.description : x.descriptionLang, id: x.enumId });
              })
            ).catch((error) => console.log(error));
            this.headArray.filter(x => x.fieldName == 'purposeTypeDesc').forEach(y => y.dropdown.item = this.dropdownPurposeType);


          }
        }, required: true, width: '5vw'
      });

      this.headArray.push({
        head: "",
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.null,
        filter: HeadFilter.null,
        width:'0.5vw'
      })

  }

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  openNew() {
    this.elementToAdd = {
      workEffortPurposeTypeId: null,
      workEffortPurposeTypeCode: null,
      purposeTypeEnumId: null,
      purposeTypeDesc: null,
      parentTypeId: null,
      parentTypeDesc: null,
      descriptionLang: null,
      description: null,
      variableGridArray: {
        id: "new" + Math.random(),
        updated: true,
        buttonDetails: false,
        inputLabeldata: true,
        inputLabelNumber: true,
        inputNotes: false,
        outputData: false,
        inputNew: true,
        dropdownData: true,
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
      let obj = new WorkEffortPurposeType(
        e.workEffortPurposeTypeId,
        e.workEffortPurposeTypeCode,
        e.purposeTypeEnumId,
        e.parentTypeId,
        e.descriptionLang,
        e.description);

      await this.workEffortPurposeTypeService.createWorkEffortPurposeType(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.workEffortPurposeTypeId);
          e.variableGridArray.id = e.workEffortPurposeTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.workEffortPurposeTypeId);
          this.er = true;
        });
    })

    return !this.er;

  }

  update(gridElement) {

    gridElement.forEach(async e => {
      let obj = new WorkEffortPurposeType(
        e.workEffortPurposeTypeId,
        e.workEffortPurposeTypeCode,
        e.purposeTypeEnumId,
        e.parentTypeId,
        e.descriptionLang,
        e.description);

      await this.workEffortPurposeTypeService.updateWorkEffortPurposeType(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.workEffortPurposeTypeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.workEffortPurposeTypeId);
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
      this.workEffortPurposeTypeService.deleteWorkEffortPurposeType(listGridElement.map(x => x.workEffortPurposeTypeId))
        .then(() => {
          this.msgService.successDelete();
          let tmpGrid = this.gridArray;
          listGridElement.forEach(w => { tmpGrid = tmpGrid.filter(r => r.workEffortPurposeTypeId != w.workEffortPurposeTypeId) })
          this.gridArray = tmpGrid;
        })
        .catch((error) => {
          this.msgService.error(error.message);
          this._reload.next();
        });
    }
    else {
      this.msgService.successDelete();
    }
  }

  
  resetAllElement(){
    this._reload.next();
  }


}
