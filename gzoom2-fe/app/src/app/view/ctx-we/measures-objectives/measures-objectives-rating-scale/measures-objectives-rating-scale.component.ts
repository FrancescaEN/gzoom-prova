import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith, switchMap, tap } from 'rxjs';
import { ActionInput, ActionOutput, Dropdown, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { WorkEffortMeasRatScService } from 'app/api/service/work-effort-meas-rat-sc.service';
import { WorkEffortMeasRatScExUomRatingScale } from 'app/api/model/workEffortMeasRatScExUomRatingScale';
import { MenuItem } from 'primeng/api';
import { UomRatingScaleService } from 'app/api/service/uom-rating-scale.service';
import { WorkEffortMeasure } from 'app/api/model/workEffortMeasure';
import { WorkEffortMeasRatSc } from 'app/api/model/workEffortMeasRatSc';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { MsgService } from 'app/commons/service/message.service';


@Component({
  selector: 'app-measures-objectives-rating-scale',
  templateUrl: './measures-objectives-rating-scale.component.html',
  styleUrls: ['./measures-objectives-rating-scale.component.css']
})
export class MeasuresObjectivesRatingScaleComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  flag: boolean = false;
  loading: boolean = true;
  er: boolean = false;
  backLink = '../../'

  headArray: HeadArray[] = [
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: any[] = [];
  newRow: any;
  secondaryLang: boolean;
  languages: [] = [];

  dropdown: Dropdown[] = [];
  dropdownURV: MenuItem[] = [];

  workEffortMeasureId: string;
  workEffortMeasure: WorkEffortMeasure;
  uomId: string;
  itemDWSelected: any;

  title: string;

  constructor(
    private route: ActivatedRoute,
    private readonly workEffortMeasRatScService: WorkEffortMeasRatScService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly uomRatingScaleService: UomRatingScaleService,
    private readonly dataStorageService: DataStorageService,
    private readonly router: Router,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    this.route.paramMap.subscribe(paramMap => {
      this.workEffortMeasureId = paramMap.get('workEffortMeasureId');
    });

    let we = (!this.secondaryLang) ? this.dataStorageService.getData(this.router.url, 'WORK_EFFORT0') : this.dataStorageService.getData(this.router.url, 'WORK_EFFORT1');
    let gl = (!this.secondaryLang) ? this.dataStorageService.getData(this.router.url, 'GL_ACCOUNT0') : this.dataStorageService.getData(this.router.url, 'GL_ACCOUNT1');

    if (!!we || !!gl)
      this.title = this.i18nService.translate("Work Effort") + ": " + we + " - " + this.i18nService.translate("Unit Cont./Extr.") + ": " + gl;


    const reload = this._reload.pipe(mergeMap(() => this.workEffortMeasRatScService.getRatingScaleWEM(this.workEffortMeasureId)));
    const wemrs$ = this.route.data.pipe(
      map((data: { obss: WorkEffortMeasRatScExUomRatingScale[] }) => data.obss),
      mergeWith(reload)
    );


    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();

      this.headArray[2].pathIconFlag = lang[0];
      this.headArray[3].pathIconFlag = lang[1];
      this.headArray[4].pathIconFlag = lang[0];
      this.headArray[5].pathIconFlag = lang[1];


    } else { this.setHeadArray(); }

    wemrs$.subscribe(y => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach((e, index) => {
        this.uomId = e.uomRatingScale.uomId;
        let ID = "" + e.workEffortMeasureId + e.uomRatingScale.uomId + e.uomRatingScale.uomRatingValue;
        let descrURV = ((!this.secondaryLang) ? e.uomRatingScale.description : e.uomRatingScale.descriptionLang);

        this.dropdown = [];
        this.dropdown['uomRatingValueDesc'] = {
          id: ID,
          fieldName: 'uomRatingValueDesc',
          item: [{ label: descrURV, id: "" + e.uomRatingScale.uomRatingValue }],
          clear: false,
          loading: false,
          key: 'uomRatingValue',
          command: async () => {

            let tmp = this.gridArray;

            tmp.filter(x => x.variableGridArray.id == this.itemDWSelected.dropdown['uomRatingValueDesc'].id)[0].dropdown['uomRatingValueDesc'].loading = true;
            this.gridArray = tmp;

            await this.setURVDropdown(this.uomId);

            let dp = tmp.filter(x => x.variableGridArray.id == this.itemDWSelected.dropdown['uomRatingValueDesc'].id)[0].dropdown['uomRatingValueDesc'].item;

            let keyExist = dp.map(item => item['uomRatingValueDesc']);
            let ray = [];
            this.dropdownURV.forEach(y => {
              if (!keyExist.includes(y)) {
                ray.push(y);
              }
            })

            tmp.filter(x => x.variableGridArray.id == this.itemDWSelected.dropdown['uomRatingValueDesc'].id)[0].dropdown['uomRatingValueDesc'].item = ray;
            tmp.filter(x => x.variableGridArray.id == this.itemDWSelected.dropdown['uomRatingValueDesc'].id)[0].dropdown['uomRatingValueDesc'].loading = false;
            this.gridArray = tmp;
          }
        }

        this.gridArray.push({
          uomRatingValue: "" + e.uomRatingScale.uomRatingValue,
          uomRatingValueDesc: descrURV,
          uomCode: e.uomCode,
          uomCodeLang: e.uomCodeLang,
          uomDescr: e.uomDescr,
          uomDescrLang: e.uomDescrLang,
          uomId: e.uomId,
          workEffortMeasureId: this.workEffortMeasureId,

          dropdown: this.dropdown,

          variableGridArray: {
            id: ID,
            updated: false,
            buttonDetails: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            inputNew: false,
            dropdownData: true,
          }

        })
      });
      this.loading = false;
    })

  }

  shareDropdownItemRow(item) {
    this.itemDWSelected = item;
  }

  setHeadArray() {
    let languageType = this.i18nService.getLanguageType();
    this.headArray.push({ head: this.i18nService.translate('Rating Scale'), fieldName: 'uomRatingValueDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, readonly: true,  required: true });

    this.headArray.push({ head: this.i18nService.translate('Code'), fieldName: 'uomCode', flag: this.flag, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, textLength: 255, required: true, width:'5vw'  });
    if (languageType == "BILING") this.headArray.push({ head: this.i18nService.translate('Code'), fieldName: 'uomCodeLang', flag: true, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, required: true, textLength: 255, width:'5vw'   });

    this.headArray.push({ head: this.i18nService.translate('Description'), fieldName: 'uomDescr', flag: this.flag, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, textLength: 2000, required: true,  });
    if (languageType == "BILING") this.headArray.push({ head: this.i18nService.translate('Description'), fieldName: 'uomDescrLang', flag: true, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 2000 });

    this.headArray.push({ head: '', fieldName: 'workEffortMeasureId', unique: true, actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" })
    this.headArray.push({ head: '', fieldName: 'uomId', unique: true, actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" })
    this.headArray.push({ head: '', fieldName: 'uomRatingValue', unique: true, actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" })

  }

  async setURVDropdown(uomId) {
    this.dropdownURV = [];
    await lastValueFrom(this.uomRatingScaleService.getUomRatingScale(uomId)).then(data => {
      data.forEach(x => {
        this.dropdownURV.push({
          label: ((!this.secondaryLang) ? x.description : x.descriptionLang), id: "" + x.uomRatingValue
        });
      })
    }
    ).catch((error) => console.log(error));
  }

  openNew() {
    let ID = "new" + Math.random();
    let tmpElAdd = {
      uomRatingValue: null,
      uomRatingValueDesc: null,
      uomCode: null,
      uomCodeLang: null,
      uomDescr: null,
      uomDescrLang: null,
      uomId: this.uomId,
      workEffortMeasureId: this.workEffortMeasureId,
      dropdown: null,
      variableGridArray: {
        id: ID,
        updated: true,
        buttonDetails: false,
        inputLabeldata: true,
        inputLabelNumber: true,
        inputNotes: true,
        outputData: false,
        inputNew: true,
        dropdownData: true,
      }

    }

    this.dropdown = [];
    this.dropdown['uomRatingValueDesc'] = {
      id: ID,
      fieldName: 'uomRatingValueDesc',
      item: [],
      clear: false,
      loading: false,
      key: 'uomRatingValue',
      command: async () => {

        let tmp = this.gridArray;

        tmp.filter(x => x.variableGridArray.id == this.itemDWSelected.dropdown['uomRatingValueDesc'].id)[0].dropdown['uomRatingValueDesc'].loading = true;
        this.gridArray = tmp;

        await this.setURVDropdown(this.uomId);

        let dp = tmp.filter(x => x.variableGridArray.id == this.itemDWSelected.dropdown['uomRatingValueDesc'].id)[0].dropdown['uomRatingValueDesc'].item;

        let keyExist = dp.map(item => item['uomRatingValueDesc']);
        let ray = [];
        this.dropdownURV.forEach(y => {
          if (!keyExist.includes(y)) {
            ray.push(y);
          }
        })

        tmp.filter(x => x.variableGridArray.id == this.itemDWSelected.dropdown['uomRatingValueDesc'].id)[0].dropdown['uomRatingValueDesc'].item = ray;
        tmp.filter(x => x.variableGridArray.id == this.itemDWSelected.dropdown['uomRatingValueDesc'].id)[0].dropdown['uomRatingValueDesc'].loading = false;
        this.gridArray = tmp;
      }
    }
    tmpElAdd.dropdown = this.dropdown;
    this.elementToAdd = tmpElAdd;
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
      let obj = new WorkEffortMeasRatSc(
        e.workEffortMeasureId,
        e.uomId,
        e.uomRatingValue,
        e.uomCode,
        e.uomCodeLang,
        e.uomDescr,
        e.uomDescrLang
      );
      await this.workEffortMeasRatScService.createWorkEffortMeasRatSc(obj)
        .then(() => {
          this.msgService.successCreate();
          let ID = "" + obj.workEffortMeasureId + obj.uomId + obj.uomRatingValue;
          e.variableGridArray.id = ID;
          e.dropdown['uomRatingValueDesc'].id = ID;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.error(error);
          this.er = true;
        });

    });

    return !this.er;
  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new WorkEffortMeasRatSc(
        e.workEffortMeasureId,
        e.uomId,
        e.uomRatingValue,
        e.uomCode,
        e.uomCodeLang,
        e.uomDescr,
        e.uomDescrLang
      );

      await this.workEffortMeasRatScService.updateWorkEffortMeasRatSc(obj)
        .then(() => {
          this.msgService.successUpdate();
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.error(error);
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
        this.workEffortMeasRatScService.deleteWorkEffortMeasRatSc(e.workEffortMeasureId, e.uomId, e.uomRatingValue)
          .then(() => {
            this.msgService.successDelete();
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.error(error.message);
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
  
  resetAllElement(){
    this._reload.next();
  }

}
