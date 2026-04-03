import { Component, OnInit } from '@angular/core';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith, switchMap } from 'rxjs';
import { ActionInput, ActionOutput, HeadArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { HeadFilter } from 'app/layout/tables/table/table-configuration';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { CustomMethodService } from 'app/api/service/custom-method.service';
import { CustomMethod } from 'app/api/model/customMethod';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-calculation-formulas',
  templateUrl: './calculation-formulas.component.html',
  styleUrls: ['./calculation-formulas.component.css']
})
export class CalculationFormulasComponent implements OnInit {
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

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly router: Router,
    private readonly dataStorageService: DataStorageService,
    private readonly customMethodService: CustomMethodService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    const reload = this._reload.pipe(mergeMap(() => this.customMethodService.getCustomMethodList()));
    const w$ = this.route.data.pipe(
      map((data: { obss: CustomMethod[] }) => data.obss),
      mergeWith(reload)
    );
    this.setHeadArray();

    w$.subscribe(y => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);

      y.forEach((e, index) => {

        this.gridArray.push({
          customMethodId: e.customMethodId,
          description: e.description,
          customMethodTypeId: e.customMethodTypeId,
          customMethodName: e.customMethodName,

          variableGridArray: {
            id: e.customMethodId,
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

      { head: 'Code', fieldName: 'customMethodId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, width: '10vw', textLength: 20 },
      { head: 'Description', fieldName: 'description', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, width: '30vw', textLength: 255, },
      { head: 'Formula', fieldName: 'customMethodName', actionInput: ActionInput.inputNotes, actionOutput: ActionOutput.outputNotes, filter: HeadFilter.textFilter, required: true, width: '30vw', textLength: 2000, },
      { head: '', fieldName: 'null', actionInput: ActionInput.null, actionOutput: ActionOutput.actionDetails, width: '5vw', filter: HeadFilter.null },

    );

  }

  toRatingScale(itemClick) {
    let etch = itemClick.customMethodId ?? "";
    let label = itemClick.description ?? "";
    let encodedURI = encodeURIComponent(itemClick.customMethodId);

    this.dataStorageService.setData(this.router.url + `/${itemClick.customMethodId}`, 'title', etch + ". " + label);
    this.router.navigate([`${encodedURI}`], { relativeTo: this.route });
  }


  openNew() {
    this.elementToAdd = {
      customMethodId: null,
      description: null,
      customMethodTypeId: "GL_ACC",
      customMethodName: null,

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
      let obj = new CustomMethod(
        e.customMethodId,
        e.description,
        e.customMethodTypeId,
        e.customMethodName
      );

      await this.customMethodService.createCustomMethod(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.customMethodId);
          e.variableGridArray.id = obj.customMethodId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.customMethodId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new CustomMethod(
        e.customMethodId,
        e.description,
        e.customMethodTypeId,
        e.customMethodName
      );

      await this.customMethodService.updateCustomMethod(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.customMethodId)
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.customMethodId);
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

        this.customMethodService.deleteCustomMethod(e.customMethodId)
          .then(() => {
            this.msgService.successDeleteWithId(e.customMethodId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.customMethodId)
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
