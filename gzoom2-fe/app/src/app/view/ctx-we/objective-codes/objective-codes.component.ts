import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WorkEffortSequenceService } from 'app/api/service/work-effort-sequence.service';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, map, mergeMap, mergeWith } from 'rxjs';
import { WorkEffortSequence } from 'app/api/model/workEffortSequence';
import { HeadArray, ActionInput, ActionOutput, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { MsgService } from 'app/commons/service/message.service';



@Component({
  selector: 'app-objective-codes',
  templateUrl: './objective-codes.component.html',
  styleUrls: ['./objective-codes.component.css']
})
export class ObjectiveCodesComponent implements OnInit {
  _reload: Subject<void>;
  elementToAdd: WorkEffortSequence;
  loading: boolean = true;
  reload: boolean = false;
  er: boolean = false;

  headArray: HeadArray[] = [
    { head: '', fieldName: 'idNumber', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" },
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: WorkEffortSequence[] = [];
  editingKeyId: string;
  newRow: any;

  constructor(
    private route: ActivatedRoute,
    private readonly workEffortSequenceService: WorkEffortSequenceService,
    private readonly i18nService: I18NService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  ngOnInit() {
    const reloadWES = this._reload.pipe(mergeMap(() => this.workEffortSequenceService.getWorkEffortSequence()));
    const w$ = this.route.data.pipe(
      map((data: { objCod: WorkEffortSequence[] }) => data.objCod),
      mergeWith(reloadWES)
    );

    this.headArray.push({ head: this.i18nService.translate('Name'), fieldName: 'seqName', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, unique: true, required: true, textLength: 60, width: "15vw" });
    this.headArray.push({ head: this.i18nService.translate('Code'), fieldName: 'seqId', actionInput: ActionInput.inputLabelNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.textFilter, required: true, width: "15vw" });

    this.headArray.push({
      head: "",
      fieldName: "null",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.null,
      filter: HeadFilter.null,
      width:'0.1vw'
    })

    w$.subscribe(y => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach((e, index) => {
        this.gridArray.push({
          seqId: e.seqId,
          seqName: e.seqName,

          variableGridArray: {
            id: e.seqName,
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

  openNew() {

    this.elementToAdd = {

      seqName: null,
      seqId: null,

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
      let obj = new WorkEffortSequence(
        e.seqId,
        e.seqName
      );

      await this.workEffortSequenceService.createWorkEffortSequence(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.seqName);
          e.variableGridArray.id = obj.seqName;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.seqName);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new WorkEffortSequence(
        e.seqId,
        e.seqName
      );

      await this.workEffortSequenceService.updateWorkEffortSequence(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.seqName);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.seqName);
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

        this.workEffortSequenceService.deleteWorkEffortSequence(e.seqName)
          .then(() => {
            this.msgService.successDeleteWithId(e.seqName);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.seqName);
            this._reload.next();
          });

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
