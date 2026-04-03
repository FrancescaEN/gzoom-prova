import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, Params, NavigationEnd } from '@angular/router';
import { Validators, UntypedFormControl, UntypedFormGroup, UntypedFormBuilder, UntypedFormArray } from '@angular/forms';
import { lastValueFrom, Observable, Subject } from 'rxjs';
import { first, map, mergeMap, mergeWith, switchMap } from 'rxjs/operators';
import { ConfirmationService, MessageService, Message as MessageError } from 'primeng/api';
import { I18NService } from '../../../../i18n/i18n.service';
import { Message } from '../../../../commons/model/message';
import { Uom } from '../../../../api/model/uom';
import { UomRatingScale } from '../../../../api/model/uomRatingScale';
import { UomService } from '../../../../api/service/uom.service';
import { LanguageService } from 'app/api/service/language.service';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { UomRatingScaleService } from 'app/api/service/uom-rating-scale.service';
import { MsgService } from 'app/commons/service/message.service';
import { CardDetail } from 'app/layout/gzoom-card-detail/gzoom-card-detail.component';



const RATING_SCALE = 'RATING_SCALE';

@Component({
  selector: 'app-uom-rating-scale-component',
  templateUrl: './uom-rating-scale.component.html',
  styleUrls: ['./uom-rating-scale.component.css'],
})

export class UomRatingScaleComponent implements OnInit {


  gridArray: any[] = [];
  selectedUom: Uom;

  /** Error message from be*/
  error = '';

  uom: Uom;
  isRatingScale: boolean;
  uomRatingScales: UomRatingScale[];
  selectedUomId: string;
  selecteduomRatingValue: string;
  displayDialog: boolean;
  selectedUomRatingScale: UomRatingScale;
  newUomRatingScale: boolean = false;
  newUomRatingScaleDetailToAdd: UomRatingScale;
  displayRangeScale: boolean;
  _reload: Subject<void>;
  form: { [name: string]: UntypedFormGroup | UntypedFormControl | UntypedFormArray };
  isEdit: boolean;
  riRowTable: number;
  tempGridArray: any[] = [];
  uomToDelete: UomRatingScale;
  dataTable: any;
  editingKeyId: string;
  isDeleted: boolean = false;
  elementToAdd: any;
  selectedIndex: number;

  langType: string;
  languages: string[] = [];
  flag: boolean = false;


  loading: boolean = true;
  reload: boolean = false;
  er: boolean = false;
  newRow: any;
  secondaryLang: boolean;

  title: string = ""


  /* ####### config for table component ########## */

  headArray: HeadArray[] = [];

  constructor(
    private readonly uomService: UomService,
    private readonly uomRatingScaleService: UomRatingScaleService,
    private readonly route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private languageService: LanguageService,
    private readonly msgService: MsgService) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {

    this.secondaryLang = await this.languageService.secondaryLang();

    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();
      this.headArray[2].pathIconFlag = lang[0];
      this.headArray[3].pathIconFlag = lang[1];

    } else { this.setHeadArray(); }

    this.route.paramMap.subscribe(paramMap => {
      this.selectedUomId = paramMap.get('uomId');

    });

    this.selectedUom = await lastValueFrom(this.uomService.getUomById(this.selectedUomId))

    this.title = this.selectedUom.uomId + " - " + (this.secondaryLang ? this.selectedUom.descriptionLang : this.selectedUom.description) + " - " + (this.secondaryLang ? this.selectedUom.abbreviationLang : this.selectedUom.abbreviation)

    const reloadedUoms = this._reload.pipe(mergeMap(() => this.uomRatingScaleService.uomRatingScales(this.selectedUomId)));
    const uomsRatingValueObs = this.route.data.pipe(
      map((data: { uomRatingScales: UomRatingScale[] }) => data.uomRatingScales),
      mergeWith(reloadedUoms)
    );
    uomsRatingValueObs.subscribe((data) => {
      this.gridArray = []
      data.forEach((element) => {
        this.selectedUomId = element.uomId;
        this.gridArray.push({
          id: element.uomId + element.uomRatingValue,
          uomId: element.uomId,
          descriptionLang: element.descriptionLang,
          description: element.description,
          uomRatingValue: element.uomRatingValue,
          variableGridArray: {
            id: element.uomId + element.uomRatingValue,
            updated: false,
            buttonDetails: false,
            inputLabeldata: true,
            inputLabelDecimalNumber: false,
            inputNotes: false,
            outputData: false,
            inputNew: false,
            dropdownData: false,
            minFractionDigits: this.selectedUom.decimalScale,
            maxFractionDigits: this.selectedUom.decimalScale
          },
        })
      })
      this.loading = false;
    });
  }

  setHeadArray() {

    this.headArray.push({ head: this.i18nService.translate('Uom Id'), fieldName: 'uomId', actionInput: ActionInput.null, actionOutput: ActionOutput.null, display: "none", filter: HeadFilter.null },
      { head: this.i18nService.translate('Uom Rating Value'), required: true, fieldName: 'uomRatingValue', actionInput: ActionInput.inputLabelDecimalNumber, actionOutput: ActionOutput.outputLabelNumber, display: "table-cell", filter: HeadFilter.textFilter, width: '10vw', sortIcon: true, unique: true },
      { head: this.i18nService.translate('Description'), required: true, fieldName: 'description', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, display: "table-cell", filter: HeadFilter.textFilter, flag: true, sortIcon: true, width: '15vw' })

    if (this.flag) {
      this.headArray.push({ head: this.i18nService.translate('Description'), required: true, fieldName: 'descriptionLang', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, display: "table-cell", filter: HeadFilter.textFilter, flag: true, sortIcon: true, width: '15vw' })
    }

    this.headArray.push({
      head: "",
      fieldName: "null",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.null,
      filter: HeadFilter.null,
      width: '0.1vw'
    })

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

  openNew() {
    let ID = "new" + Math.random();
    let tmpElAdd: UomRatingScale = {
      uomId: null,
      descriptionLang: null,
      description: null,
      uomRatingValue: null,
      variableGridArray: {
        id: ID,
        updated: true,
        buttonDetails: false,
        inputLabeldata: true,
        inputLabelDecimalNumber: true,
        inputNotes: false,
        outputData: false,
        inputNew: true,
        dropdownData: false,
        minFractionDigits: this.selectedUom.decimalScale,
        maxFractionDigits: this.selectedUom.decimalScale
      }
    }

    this.elementToAdd = tmpElAdd;
    this.gridArray = [this.elementToAdd, ...this.gridArray];
  }


  create(gridElement): boolean {

    gridElement.forEach(async e => {
      let obj = new UomRatingScale();
      obj.uomId = this.selectedUomId;
      obj.description = e.description;
      if (this.secondaryLang) { obj.descriptionLang = e.descriptionLang };
      obj.uomRatingValue = e.uomRatingValue;

      if ((obj.uomId != null || obj.uomId != undefined) && (obj.uomRatingValue != null || obj.uomRatingValue != undefined)) {
        await this.uomRatingScaleService.createUomRatingScale(obj)
          .then((x) => {
            this.msgService.successCreate();
            let ID = x.uomId + x.uomRatingValue;
            e.variableGridArray.id = ID;
            e.uomId = x.uomId;
            e.variableGridArray.updated = false;
            e.variableGridArray.inputNew = false;
            e.variableGridArray.outputData = true;
            e.variableGridArray.inputLabelDecimalNumber = false;

            if (this.reload) this._reload.next()
          })
          .catch((error) => {
            this.msgService.error(error);
            this.er = true;
          });
      }
      else {
        this.msgService.errorFieldsRequired();
        this.er = true;
      }
    })
    return !this.er;
  }

  update(gridElement) {

    gridElement.forEach(async e => {
      let obj = new UomRatingScale();

      obj.uomId = e.uomId
      obj.description = e.description;
      if (this.secondaryLang) { obj.descriptionLang = e.descriptionLang };
      obj.uomRatingValue = e.uomRatingValue;

      if ((obj.uomId != null || obj.uomId != undefined) && (obj.uomRatingValue != null || obj.uomRatingValue != undefined)) {
        await this.uomRatingScaleService.updateUomRatingScale(obj)
          .then(() => {
            this.msgService.successUpdate();
            this._reload.next()
          })
          .catch((error) => {
            this.msgService.error(error);
            this.er = true;
          });
      }
      else {
        this.msgService.errorFieldsRequired();
        this.er = true;
      }
    });
    return !this.er;
  }

  delete(listGridElement: UomRatingScale[]) {
    if (listGridElement.filter(x => x.variableGridArray.id.includes("new")).length > 0) {
      this.newRow = listGridElement.filter(x => x.variableGridArray.id.includes("new"));
      listGridElement = listGridElement.filter(x => !this.newRow.includes(x));
      this.gridArray = this.gridArray.filter(x => !x.variableGridArray.id.includes("new"));
      this.newRow = null;
    }
    if (listGridElement.length > 0) {
      listGridElement.forEach(e => {

        this.uomRatingScaleService.deleteUomRatingScale(e.uomId, e.uomRatingValue)
          .then(() => {
            this.msgService.successDelete();
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.error(error.error.message);
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
