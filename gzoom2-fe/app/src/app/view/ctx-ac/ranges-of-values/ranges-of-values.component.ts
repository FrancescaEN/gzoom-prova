import { Component, OnInit } from '@angular/core';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith, switchMap } from 'rxjs';
import { MenuItem } from 'primeng/api';
import { ActionInput, ActionOutput, HeadArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { LanguageService } from 'app/api/service/language.service';
import { HeadFilter } from 'app/layout/tables/table/table-configuration';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { UomRangeService } from 'app/api/service/uom-range.service';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { UomRange } from 'app/api/model/uomRange';
import { UomService } from 'app/api/service/uom.service';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-ranges-of-values',
  templateUrl: './ranges-of-values.component.html',
  styleUrls: ['./ranges-of-values.component.css']
})
export class RangesOfValuesComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  loading: boolean = true;
  er: boolean = false;

  headArray: HeadArray[] = [
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: any[] = [];
  newRow: any;
  secondaryLang: boolean;
  dropdownUom: MenuItem[] = [];

  constructor(
    private route: ActivatedRoute,
    private readonly tbService: TableEditingCellService,
    private readonly uomRangeService: UomRangeService,
    private readonly i18nService: I18NService,
    private readonly router: Router,
    private readonly dataStorageService: DataStorageService,
    private readonly uomService: UomService,
    private readonly languageService: LanguageService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    const reload = this._reload.pipe(mergeMap(() => this.uomRangeService.getUomRangeList()));
    const w$ = this.route.data.pipe(
      map((data: { obss: UomRange[] }) => data.obss),
      mergeWith(reload)
    );
    this.setHeadArray();
    await this.setUomDropdown();

    w$.subscribe(y => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);

      y.forEach((e, index) => {

        this.gridArray.push({
          uomRangeId: e.uomRangeId,
          description: e.description,
          uomId: e.uomId,
          uomIdDesc: this.dropdownUom.filter(x => x.id == e.uomId).map(y => y.label)[0],

          variableGridArray: {
            id: e.uomRangeId,
            updated: false,
            buttonDetails: true,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: true,
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

      { head: 'ID', fieldName: 'uomRangeId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, width: '10vw', textLength: 20 },
      { head: 'Description', fieldName: 'description', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, width: '60vw', textLength: 255, },
      {
        head: 'Unit of Measure', fieldName: 'uomIdDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, required: true,
        dropdown: {
          item: this.dropdownUom,
          clear: false,
          disableSort: false,
          loading: false,
          key: 'uomId',
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'uomIdDesc').forEach(y => y.dropdown.loading = true);
            await this.setUomDropdown();
            this.headArray.filter(x => x.fieldName == 'uomIdDesc').forEach(y => y.dropdown.item = this.dropdownUom);
            this.headArray.filter(x => x.fieldName == 'uomIdDesc').forEach(y => y.dropdown.loading = false);
          }

        }
      },
      { head: '', fieldName: 'null', actionInput: ActionInput.null, actionOutput: ActionOutput.actionDetails, width: '5vw', filter: HeadFilter.null },

    );

  }

  toRatingScale(itemClick) {
    let etch = itemClick.uomRangeId ?? "";
    let label = itemClick.description ?? "";

    this.dataStorageService.setData(this.router.url + `/${itemClick.uomRangeId}`, 'title', etch + " - " + label);
    this.router.navigate([`${itemClick.uomRangeId}`], { relativeTo: this.route });
  }


  async setUomDropdown() {
    this.dropdownUom = [];
    const obs$ = this.uomService.uoms();

    await lastValueFrom(obs$).then(data => {
      data.forEach(x => {
        let lab = ((!this.secondaryLang) ? x.description : x.descriptionLang);

        this.dropdownUom.push({ label: lab, id: x.uomId });
      })
    }
    ).catch((error) => console.log(error));

  }


  openNew() {
    this.elementToAdd = {
      uomRangeId: null,
      description: null,
      uomId: null,
      uomIdDesc: null,

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
      let obj = new UomRange(
        e.uomRangeId,
        e.description,
        e.uomId
      );

      await this.uomRangeService.createUomRange(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.uomRangeId);
          e.variableGridArray.id = obj.uomRangeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.uomRangeId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new UomRange(
        e.uomRangeId,
        e.description,
        e.uomId
      );

      await this.uomRangeService.updateUomRange(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.uomRangeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.uomRangeId);
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

        this.uomRangeService.deleteUomRange(e.uomRangeId)
          .then(() => {
            this.msgService.successDeleteWithId(e.uomRangeId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.uomRangeId);
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

