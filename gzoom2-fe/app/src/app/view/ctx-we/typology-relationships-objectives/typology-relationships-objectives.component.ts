import { Component, OnInit } from '@angular/core';
import { WorkEffortAssocType } from 'app/api/model/workEffortAssocType';
import { WorkEffortAssocTypeService } from 'app/api/service/work-effort-assoc-type.service';
import { I18NService } from 'app/i18n/i18n.service';
import { map, mergeMap, mergeWith } from 'rxjs/operators';
import { Observable, Subject, lastValueFrom } from 'rxjs';
import { HeadArray, ActionInput, ActionOutput, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { ActivatedRoute } from '@angular/router';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { CanComponentDeactivate } from 'app/shared/can-deactivate.guard';
import { MsgService } from 'app/commons/service/message.service';


@Component({
  selector: 'app-typology-relationships-objectives',
  templateUrl: './typology-relationships-objectives.component.html',
  styleUrls: ['./typology-relationships-objectives.component.css']
})
export class TypologyRelationshipsObjectivesComponent implements OnInit, CanComponentDeactivate {
  _reload: Subject<void>;
  reload: boolean = false;

  elementToAdd: any;
  loading: boolean = true;
  dropdownItemWEAT: MenuItem[] = [];
  lastChoiceDropdown: MenuItem;
  newRow: any;

  headArray: HeadArray[] = [
    { head: '', fieldName: 'idNumber', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" },
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: any[] = [];
  tmpGridArray: WorkEffortAssocType[] = [];

  editingKeyId: string;
  selectionWEAT: WorkEffortAssocType[] = [];
  er: boolean = false;


  constructor(
    private route: ActivatedRoute,
    private readonly workEffortAssocTypeService: WorkEffortAssocTypeService,
    private readonly i18nService: I18NService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  ngOnInit() {

    const reloadWEAT = this._reload.pipe(mergeMap(() => this.workEffortAssocTypeService.getWorkEffortAssocType()));
    const w$ = this.route.data.pipe(
      map((data: { weats: WorkEffortAssocType[] }) => data.weats),
      mergeWith(reloadWEAT)
    )

    w$
      .subscribe(y => {
        this.gridArray = [];
        if (this.newRow) this.gridArray.push(this.newRow);
        y.forEach(e => this.dropdownItemWEAT.push({ label: e.description, id: e.workEffortAssocTypeId }));
        this.setHeadArray();

        y.forEach((e, index) => {
          this.gridArray.push({
            workEffortAssocTypeId: e.workEffortAssocTypeId,
            description: e.description,
            parentTypeId: e.parentTypeId,
            parentDescription: this.dropdownItemWEAT.find(x => x.id == e.parentTypeId)?.label,
            variableGridArray: {
              id: e.workEffortAssocTypeId,
              updated: false,
              buttonDetails: false,
              dropdownData: true,
              inputLabeldata: true,
              inputLabelNumber: false,
              inputNotes: false,
              outputData: true,
              inputNew: false
            }

          });

        });
        this.loading = false;

      })
  }

  setHeadArray() {
    this.headArray = [];
    this.headArray.push({ head: this.i18nService.translate('Code'), fieldName: 'workEffortAssocTypeId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, width: '5vw', required: true, unique: true, textLength: 20 });
    this.headArray.push({ head: this.i18nService.translate('Description'), fieldName: 'description', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, width: '30vw', required: true, textLength: 255 });
    this.headArray.push({
      head: this.i18nService.translate('Classification'), fieldName: 'parentDescription', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, width: '15vw',
      dropdown: {
        item: this.dropdownItemWEAT,
        clear: true,
        key: 'parentTypeId',
        command: async () => {
          this.headArray.filter(x => x.fieldName == 'parentDescription').forEach(y => y.dropdown.loading = true);
          this.dropdownItemWEAT = [];
          await lastValueFrom(this.workEffortAssocTypeService.getWorkEffortAssocType())
            .then(y =>
              y.forEach(e => this.dropdownItemWEAT.push({ label: e.description, id: e.workEffortAssocTypeId }))

            ).catch((error) => console.log(error));
          this.headArray.filter(x => x.fieldName == 'parentDescription').forEach(y => y.dropdown.item = this.dropdownItemWEAT);
          this.headArray.filter(x => x.fieldName == 'parentDescription').forEach(y => y.dropdown.loading = false);
        }
      }
    });

  }

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }


  saveNewAndOpen(gridElement) {
    if (this.create(gridElement)) {
      this.openNew();
    }
    else this.er = false;
  }

  openNew() {

    this.elementToAdd = {
      workEffortAssocTypeId: null,
      description: null,
      parentTypeId: null,
      parentDescription: null,
      variableGridArray: {
        id: "new" + Math.random(),
        buttonDetails: false,
        dropdownData: true,
        inputLabeldata: true,
        inputLabelNumber: false,
        inputNotes: false,
        outputData: false,
        inputNew: true,
        updated: true,
      }
    }

    this.gridArray = [this.elementToAdd, ...this.gridArray];
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
      let obj = new WorkEffortAssocType(
        e.workEffortAssocTypeId,
        e.description,
        e.parentTypeId
      );

      await this.workEffortAssocTypeService.createWorkEffortAssocType(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.workEffortAssocTypeId);
          e.variableGridArray.id = obj.workEffortAssocTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.workEffortAssocTypeId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new WorkEffortAssocType(
        e.workEffortAssocTypeId,
        e.description,
        e.parentTypeId
      );

      await this.workEffortAssocTypeService.updateWorkEffortAssocType(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.workEffortAssocTypeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.workEffortAssocTypeId);
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

        this.workEffortAssocTypeService.deleteWorkEffortAssocType(e.workEffortAssocTypeId)
          .then(() => {
            this.msgService.successDeleteWithId(e.workEffortAssocTypeId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.workEffortAssocTypeId);
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
