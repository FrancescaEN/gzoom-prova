import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { MenuItem } from 'primeng/api';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith } from 'rxjs';
import { Table } from 'primeng/table';
import { HeadArray, ActionInput, ActionOutput, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { LanguageService } from 'app/api/service/language.service';
import { WorkEffortContentTypeService } from 'app/api/service/work-effort-content-type.service';
import { ContentTypeService } from 'app/api/service/content-type.service';
import { WorkEffortContentType } from 'app/api/model/workEffortContentType';
import { MsgService } from 'app/commons/service/message.service';


@Component({
  selector: 'app-types-attachments-objectives',
  templateUrl: './types-attachments-objectives.component.html',
  styleUrls: ['./types-attachments-objectives.component.css']
})
export class TypesAttachmentsObjectivesComponent implements OnInit {
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
  newRow: any;
  secondaryLang: boolean;
  languages: [] = [];
  dropdownContentType: MenuItem[] = [];
  dropdownWECT: MenuItem[] = [];

  constructor(
    private route: ActivatedRoute,
    private readonly wectService: WorkEffortContentTypeService,
    private readonly contentTypeService: ContentTypeService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    const reload = this._reload.pipe(mergeMap(() => this.wectService.getWorkEffortContentType()));
    const w$ = this.route.data.pipe(
      map((data: { obss: WorkEffortContentType[] }) => data.obss),
      mergeWith(reload)
    );

    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();
      this.headArray[2].pathIconFlag = lang[0];
      this.headArray[3].pathIconFlag = lang[1];


    } else { this.setHeadArray(); }


    await lastValueFrom(this.contentTypeService.getContentTypeWithParentTypeId('PARENT_ENCLOSE')).then(data =>
      data.forEach(x => {
        this.dropdownContentType.push({ label: x.description, id: x.contentTypeId });
      })

    ).catch((error) => console.log(error))

    w$.subscribe(y => {
      this.dropdownWECT = []
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach(x => { this.dropdownWECT.push({ label: (!this.secondaryLang) ? x.description : x.descriptionLang, id: x.workEffortContentTypeId }) });
      this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.item = this.dropdownWECT);

      y.forEach((e, index) => {
        this.gridArray.push({
          workEffortContentTypeId: e.workEffortContentTypeId,
          parentTypeId: e.parentTypeId,
          parentTypeDesc: this.dropdownWECT.filter(x => x.id == e.parentTypeId).map(y => y.label)[0],
          contentTypeDesc: this.dropdownContentType.filter(x => x.id == e.contentTypeId).map(y => y.label)[0],
          contentTypeId: e.contentTypeId,
          descriptionLang: e.descriptionLang,
          description: e.description,
          variableGridArray: {
            id: e.workEffortContentTypeId,
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
      { head: this.i18nService.translate('Code'), fieldName: 'workEffortContentTypeId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, textLength: 20, width: '5vw' },
      { head: this.i18nService.translate('Description'), fieldName: 'description', flag: this.flag, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, width: '10vw' });
    if (this.i18nService.getLanguageType() == "BILING") this.headArray.push({ head: this.i18nService.translate('Description'), fieldName: 'descriptionLang', flag: true, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, width: '10vw' });
    this.headArray.push(
      {
        head: this.i18nService.translate('Classification'), fieldName: 'parentTypeDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter,
        dropdown: {
          item: this.dropdownWECT,
          clear: true,
          key: 'parentTypeId',
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.loading = true);

            this.dropdownWECT = [];
            await lastValueFrom(this.wectService.getWorkEffortContentType()).then(x => {
              x.forEach(y => {
                this.dropdownWECT.push({ label: (!this.secondaryLang) ? y.description : y.descriptionLang, id: y.workEffortContentTypeId })
              })
            }).catch((error) => console.log(error));
            this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.item = this.dropdownWECT);
            this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.loading = false);

          }
        }, width: '10vw'
      },
      {
        head: this.i18nService.translate('Type Content Annex'), fieldName: 'contentTypeDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, required: true,
        dropdown: {
          item: this.dropdownContentType,
          clear: false,
          key: 'contentTypeId',
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'contentTypeDesc').forEach(y => y.dropdown.loading = true);
            this.dropdownContentType = [];
            await lastValueFrom(this.contentTypeService.getContentTypeWithParentTypeId('PARENT_ENCLOSE')).then(x => {
              x.forEach(y => {
                this.dropdownContentType.push({ label: y.description, id: y.contentTypeId });
              })
            }).catch((error) => console.log(error));
            this.headArray.filter(x => x.fieldName == 'contentTypeDesc').forEach(y => y.dropdown.item = this.dropdownContentType);
            this.headArray.filter(x => x.fieldName == 'contentTypeDesc').forEach(y => y.dropdown.loading = false);
          },
        }, width: '10vw'
      },{
        head: "",
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.null,
        filter: HeadFilter.null,
        width:'0.5vw'
      });

  }

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  openNew() {
    this.elementToAdd = {
      workEffortContentTypeId: null,
      parentTypeId: null,
      parentTypeDesc: null,
      contentTypeDesc: null,
      contentTypeId: null,
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
      let obj = new WorkEffortContentType(
        e.workEffortContentTypeId,
        e.parentTypeId,
        e.description,
        e.descriptionLang,
        e.contentTypeId
      );

      await this.wectService.createWorkEffortContentType(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.workEffortContentTypeId);
          e.variableGridArray.id = e.workEffortContentTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.workEffortContentTypeId);
          this.er = true;
        });
    })

    return !this.er;

  }

  update(gridElement) {

    gridElement.forEach(async e => {
      let obj = new WorkEffortContentType(
        e.workEffortContentTypeId,
        e.parentTypeId,
        e.description,
        e.descriptionLang,
        e.contentTypeId
      );

      await this.wectService.updateWorkEffortContentType(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.workEffortContentTypeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.workEffortContentTypeId);
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
      this.wectService.deleteWorkEffortContentType(listGridElement.map(x => x.workEffortContentTypeId))
        .then(() => {
          this.msgService.successDelete();
          let tmpGrid = this.gridArray;
          listGridElement.forEach(w => { tmpGrid = tmpGrid.filter(r => r.workEffortContentTypeId != w.workEffortContentTypeId) })
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
