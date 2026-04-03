import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith } from 'rxjs';
import { HeadArray, ActionInput, ActionOutput, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { LanguageService } from 'app/api/service/language.service';
import { GlResourceTypeService } from 'app/api/service/gl-resource-type.service';
import { GlResourceType } from 'app/api/model/glResourceType';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-nature-unit-cont-extr',
  templateUrl: './nature-unit-cont-extr.component.html',
  styleUrls: ['./nature-unit-cont-extr.component.css']
})
export class NatureUnitContExtrComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: GlResourceType;
  flag: boolean = false;
  loading: boolean = true;

  headArray: HeadArray[] = [
    { head: '', fieldName: 'idNumber', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" },
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: GlResourceType[] = [];
  newRow: any;
  er: boolean = false;

  secondaryLang: boolean;
  languages: [] = [];

  constructor(
    private route: ActivatedRoute,
    private readonly glResourceTypeService: GlResourceTypeService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    const reload = this._reload.pipe(mergeMap(() => this.glResourceTypeService.getGlResourceType()));
    const w$ = this.route.data.pipe(
      map((data: { obss: GlResourceType[] }) => data.obss),
      mergeWith(reload)
    );

    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();
      this.headArray[3].pathIconFlag = lang[0];
      this.headArray[4].pathIconFlag = lang[1];

    } else { this.setHeadArray(); }


    w$.subscribe(y => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach((e, index) => {
        this.gridArray.push({
          glResourceTypeId: e.glResourceTypeId,
          description: e.description,
          descriptionLang: e.descriptionLang,

          variableGridArray: {
            id: e.glResourceTypeId,
            updated: false,
            buttonDetails: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: false,
            outputData: true,
            inputNew: false
          }

        })
      })
      this.loading = false;
    })
  }

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  setHeadArray() {
    this.headArray.push({ head: 'Code', fieldName: 'glResourceTypeId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, width: "5vw", unique: true, textLength: 20 });
    this.headArray.push({ head: 'Description', fieldName: 'description', flag: this.flag, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, width: "10vw" });
    if (this.i18nService.getLanguageType() == "BILING") this.headArray.push({ head: 'Description', fieldName: 'descriptionLang', flag: true, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, width: "10vw" });
    this.headArray.push({
      head: "",
      fieldName: "null",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.null,
      filter: HeadFilter.null,
      width: '0.1vw'
    })
  }

  openNew() {

    this.elementToAdd = {

      glResourceTypeId: null,
      description: null,
      descriptionLang: null,

      variableGridArray: {
        id: "new" + Math.random(),
        buttonDetails: false,
        dropdownData: true,
        inputLabeldata: true,
        inputLabelNumber: true,
        inputNotes: false,
        outputData: false,
        inputNew: true,
        updated: true
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
      let obj = new GlResourceType(
        e.glResourceTypeId,
        e.description,
        e.descriptionLang
      );

      await this.glResourceTypeService.createGlResourceType(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.glResourceTypeId);
          e.variableGridArray.id = obj.glResourceTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.glResourceTypeId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new GlResourceType(
        e.glResourceTypeId,
        e.description,
        e.descriptionLang
      );

      await this.glResourceTypeService.updateGlResourceType(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.glResourceTypeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.glResourceTypeId);
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

        this.glResourceTypeService.deleteGlResourceType(e.glResourceTypeId)
          .then(() => {
            this.msgService.successDeleteWithId(e.glResourceTypeId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.glResourceTypeId);
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
