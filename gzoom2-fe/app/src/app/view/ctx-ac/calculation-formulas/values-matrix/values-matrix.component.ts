import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, map, mergeMap, mergeWith } from 'rxjs';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { CustomMethodMatrixService } from 'app/api/service/custom-method-matrix.service';
import { CustomMethodMatrix } from 'app/api/model/customMethodMatrix';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-values-matrix',
  templateUrl: './values-matrix.component.html',
  styleUrls: ['./values-matrix.component.css']
})
export class ValuesMatrixComponent {
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

  customMethodId;

  title: string;

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly dataStorageService: DataStorageService,
    private readonly router: Router,
    private readonly customMethodMatrixService: CustomMethodMatrixService,
    private msgService: MsgService

  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {

    const routeParams = this.route.snapshot.paramMap;
    this.customMethodId = decodeURIComponent(routeParams.get('customMethodId'));

    this.title = this.dataStorageService.getData(this.router.url.substring(0, this.router.url.lastIndexOf("/") + 1) + this.customMethodId, 'title');


    const reload = this._reload.pipe(mergeMap(() => this.customMethodMatrixService.getCustomMethodMatrixList(this.customMethodId)));
    const data$ = this.route.data.pipe(
      map((data: { obss: CustomMethodMatrix[] }) => data.obss),
      mergeWith(reload)
    );

    this.setHeadArray();

    data$.subscribe(y => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach((e, index) => {
        this.gridArray.push({
          customMethodId: e.customMethodId,
          customMethodMatrixId: e.customMethodMatrixId,
          rowInputValue: e.rowInputValue,
          columnInputValue: e.columnInputValue,
          outputValue: e.outputValue,

          variableGridArray: {
            id: e.customMethodMatrixId,
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
    });
  }

  setHeadArray() {

    this.headArray.push(
      { head: 'Row', fieldName: 'rowInputValue', actionInput: ActionInput.inputLabelNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.popUpFilter, required: true, content: 'center' },
      { head: 'Column', fieldName: 'columnInputValue', actionInput: ActionInput.inputLabelNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.popUpFilter, required: true, content: 'center' },
      { head: 'Value', fieldName: 'outputValue', actionInput: ActionInput.inputLabelNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.popUpFilter, required: true, content: 'center' },
    );
  }

  openNew() {
    this.elementToAdd = {
      customMethodId: this.customMethodId,
      customMethodMatrixId: null,
      rowInputValue: null,
      columnInputValue: null,
      outputValue: null,

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
      let obj = new CustomMethodMatrix(
        e.customMethodMatrixId,
        e.customMethodId,
        e.rowInputValue,
        e.columnInputValue,
        e.outputValue
      );
      await this.customMethodMatrixService.createCustomMethodMatrix(obj)
        .then((x) => {
          this.msgService.successCreateWithId(x);
          e.customMethodMatrixId = x;
          e.variableGridArray.id = x;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.error(error);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new CustomMethodMatrix(
        e.customMethodMatrixId,
        e.customMethodId,
        e.rowInputValue,
        e.columnInputValue,
        e.outputValue
      );
      await this.customMethodMatrixService.updateCustomMethodMatrix(obj)
        .then(() => {
          this.msgService.successUpdate()
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
        this.customMethodMatrixService.deleteCustomMethodMatrix(e.customMethodMatrixId)
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
