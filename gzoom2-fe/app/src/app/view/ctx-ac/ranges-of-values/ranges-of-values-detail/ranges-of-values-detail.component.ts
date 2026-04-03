import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith, switchMap, tap } from 'rxjs';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { MenuItem } from 'primeng/api';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { UomRangeValuesService } from 'app/api/service/uom-range-values.service';
import { UomRangeValues } from 'app/api/model/uomRangeValues';
import { ContentService } from 'app/api/service/content.service';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { MsgService } from 'app/commons/service/message.service';



@Component({
  selector: 'app-ranges-of-values-detail',
  templateUrl: './ranges-of-values-detail.component.html',
  styleUrls: ['./ranges-of-values-detail.component.css']
})
export class RangesOfValuesDetailComponent {
  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
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

  uomRangeId

  title: string;

  dropdownY_N: MenuItem[] = [
    { label: this.i18nService.translate('Y'), id: 'Y' },
    { label: this.i18nService.translate('N'), id: 'N' }];

  dropdownEnum: MenuItem[];

  images$: Observable<string[]>;
  images;

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly dataStorageService: DataStorageService,
    private readonly uomRangeValuesService: UomRangeValuesService,
    private readonly router: Router,
    private readonly contentService: ContentService,
    private readonly enumerationService: EnumerationService,
    private readonly tbService: TableEditingCellService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    this.route.paramMap.subscribe(paramMap => {
      this.uomRangeId = paramMap.get('uomRangeId');
    });

    if (this.dataStorageService.getData(this.router.url, 'title'))
      this.title = this.dataStorageService.getData(this.router.url, 'title');

    await this.setEnumDropdown();
    const reload = this._reload.pipe(mergeMap(() => this.uomRangeValuesService.getUomRangeValuesList(this.uomRangeId)));
    const urv$ = this.route.data.pipe(
      map((data: { obss: UomRangeValues[] }) => data.obss),
      mergeWith(reload)
    );


    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();

      this.headArray[2].pathIconFlag = lang[0];
      this.headArray[3].pathIconFlag = lang[1];


    } else { this.setHeadArray(); }

    urv$.subscribe(y => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach((e, index) => {
        this.gridArray.push({
          uomRangeId: e.uomRangeId,
          uomRangeValuesId: e.uomRangeValuesId,
          comments: e.comments,
          commentsLang: e.commentsLang,
          isPositive: e.isPositive,
          isPositiveDesc: this.i18nService.translate(e.isPositive ?? ''),
          fromValue: e.fromValue,
          thruValue: e.thruValue,
          rangeValuesFactorMin: e.rangeValuesFactorMin,
          rangeValuesFactor: e.rangeValuesFactor,
          alert: e.alert,
          alertDesc: this.i18nService.translate(e.alert ?? ''),
          iconContentId: e.iconContentId,
          colorEnumId: e.colorEnumId,
          colorEnumIdDesc: this.dropdownEnum.filter(x => x.id == e.colorEnumId).map(y => y.label)[0],
          prorateRange: e.prorateRange,
          prorateRangeDesc: this.i18nService.translate(e.prorateRange ?? ''),

          variableGridArray: {
            id: e.uomRangeValuesId,
            updated: false,
            buttonDetails: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            inputNew: false,
            dropdownData: true,
            selectFile: true,
            inputLabelDecimalNumber: true,
            minFractionDigits: 2,
            maxFractionDigits: 20
          }

        })
      });
      this.loading = false;
    });
  }

  setHeadArray() {
    let languageType = this.i18nService.getLanguageType();
    this.headArray.push({ head: 'Code', fieldName: 'uomRangeValuesId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, textLength: 20, required: true, unique: true });

    this.headArray.push({ head: 'Comments', fieldName: 'comments', flag: this.flag, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, textLength: 255, required: true });
    if (languageType == "BILING") this.headArray.push({ head: 'Comments', fieldName: 'commentsLang', flag: true, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255 });

    this.headArray.push(
      { head: 'Positive', fieldName: "isPositiveDesc", actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, required: true, dropdown: { item: this.dropdownY_N, clear: false, key: 'isPositive' }, content: "center", sortIcon: false },
      { head: 'From Value', fieldName: 'fromValue', actionInput: ActionInput.inputLabelDecimalNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.popUpFilter, content: 'center' },
      { head: 'Thru Value', fieldName: 'thruValue', actionInput: ActionInput.inputLabelDecimalNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.popUpFilter, content: 'center' },
      { head: 'Minimum Coefficient', fieldName: 'rangeValuesFactorMin', actionInput: ActionInput.inputLabelDecimalNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.popUpFilter, content: 'center' },
      { head: 'Coefficient', fieldName: 'rangeValuesFactor', actionInput: ActionInput.inputLabelDecimalNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.popUpFilter, content: 'center' },
      { head: 'Alert', fieldName: "alertDesc", actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, required: true, dropdown: { item: this.dropdownY_N, clear: false, key: 'alert' }, content: "center", sortIcon: false },
      { head: 'Band average', fieldName: "prorateRangeDesc", actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, required: true, dropdown: { item: this.dropdownY_N, clear: false, key: 'prorateRange' }, content: "center", sortIcon: false },
      {
        head: 'Color', fieldName: 'colorEnumIdDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter,
        dropdown: {
          item: this.dropdownEnum,
          clear: true,
          disableSort: true,
          loading: false,
          key: 'colorEnumId',
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'colorEnumIdDesc').forEach(y => y.dropdown.loading = true);
            await this.setEnumDropdown();
            this.headArray.filter(x => x.fieldName == 'colorEnumIdDesc').forEach(y => y.dropdown.item = this.dropdownEnum);
            this.headArray.filter(x => x.fieldName == 'colorEnumIdDesc').forEach(y => y.dropdown.loading = false);
          }
        },
      },
      { head: 'Attach Icon', fieldName: 'upload', actionInput: ActionInput.null, actionOutput: ActionOutput.selectFile, filter: HeadFilter.null, content: 'center', sortIcon: false },
      { head: 'Icon', fieldName: 'iconContentId', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter },
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

  async setEnumDropdown() {
    this.dropdownEnum = [];


    await lastValueFrom(this.enumerationService.enumerations('COLOR')).then(data => {

      data.sort((a, b) => this.tbService.sortDataDW(a, b, ((!this.secondaryLang) ? 'description' : 'descriptionLang')));

      data.forEach(x => {
        let lab = ((!this.secondaryLang) ? x.description : x.descriptionLang);

        this.dropdownEnum.push({ label: lab, id: x.enumId });
      })
    }
    ).catch((error) => console.log(error));
  }

  async selectFileLoad(event) {

    this.images = undefined;
    await lastValueFrom(this.contentService.getContentListByContentTypeId("ICON")
      .pipe(
        map(contentExDataResource => contentExDataResource.map(y => ({ src: 'data:' + y.dataResource.mimeTypeId + ';base64,' + y.imageBase64, alt: y.dataResource.dataResourceId })))
      ))
      .then(x => {
        if (x.length > 0) {
          this.images = x;
        }
        else {
          this.images = null;
        }
      });
  }

  selectedImageFun(e: { image: { src: string, alt: string }, item }) {
    e.item.iconContentId = e.image.alt;
  }

  openNew() {
    this.elementToAdd = {
      uomRangeId: this.uomRangeId,
      uomRangeValuesId: null,
      comments: null,
      commentsLang: null,
      isPositive: "Y",
      isPositiveDesc: this.i18nService.translate('Y'),
      fromValue: null,
      thruValue: null,
      rangeValuesFactorMin: null,
      rangeValuesFactor: null,
      alert: "N",
      alertDesc: "N",
      iconContentId: null,
      colorEnumId: null,
      prorateRange: "N",
      prorateRangeDesc: "N",


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
        buttonMultipleDetails: false,
        selectFile: true,
        inputLabelDecimalNumber: true,
        minFractionDigits: 2,
        maxFractionDigits: 20
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
      let obj = new UomRangeValues(
        e.uomRangeId,
        e.uomRangeValuesId,
        e.comments,
        e.isPositive,
        e.fromValue,
        e.thruValue,
        e.iconContentId,
        e.alert,
        e.rangeValuesFactor,
        e.rangeValuesFactorMin,
        e.colorEnumId,
        e.prorateRange,
        e.commentsLang
      );
      await this.uomRangeValuesService.createUomRangeValues(obj)
        .then((x) => {
          this.msgService.successCreateWithId(obj.uomRangeValuesId);
          e.variableGridArray.id = obj.uomRangeValuesId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonMultipleDetails = true;
          e.variableGridArray.selectFile = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.uomRangeValuesId);
          this.er = true;
        });

    });

    return !this.er;
  }

  update(gridElement, iconContentId?) {
    gridElement.forEach(async e => {
      let obj = new UomRangeValues(
        e.uomRangeId,
        e.uomRangeValuesId,
        e.comments,
        e.isPositive,
        e.fromValue,
        e.thruValue,
        iconContentId ?? e.iconContentId,
        e.alert,
        e.rangeValuesFactor,
        e.rangeValuesFactorMin,
        e.colorEnumId,
        e.prorateRange,
        e.commentsLang
      );
      await this.uomRangeValuesService.updateUomRangeValues(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.uomRangeValuesId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.uomRangeValuesId);
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
        this.uomRangeValuesService.deleteUomRangeValues(e.uomRangeValuesId)
          .then(() => {
            this.msgService.successDeleteWithId(e.uomRangeValuesId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.uomRangeValuesId);
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
