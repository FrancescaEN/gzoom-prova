import { Component, OnInit } from '@angular/core';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith, switchMap, tap } from 'rxjs';
import { MenuItem } from 'primeng/api';
import { ActionInput, ActionOutput, HeadArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { HeadFilter } from 'app/layout/tables/table/table-configuration';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { PartyRelationshipTypeService } from 'app/api/service/party-relationship-type.service';
import { PartyRelationshipType } from 'app/api/model/partyRelationshipType';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-types-relationships-between-subjects',
  templateUrl: './types-relationships-between-subjects.component.html',
  styleUrls: ['./types-relationships-between-subjects.component.css']
})
export class TypesRelationshipsBetweenSubjectsComponent implements OnInit {
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
  dropdownPRT: MenuItem[] = [];

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly router: Router,
    private readonly dataStorageService: DataStorageService,
    private readonly partyRelationshipTypeService: PartyRelationshipTypeService,
    private readonly tbService: TableEditingCellService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    const reload = this._reload.pipe(mergeMap(() => this.partyRelationshipTypeService.getPartyRelationshipType()));
    const w$ = this.route.data.pipe(
      map((data: { obss: PartyRelationshipType[] }) => data.obss),
      mergeWith(reload)
    );
    this.setHeadArray();

    w$.subscribe(async y => {
      this.dropdownPRT = [];
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach(x => { this.dropdownPRT.push({ label: x.partyRelationshipName, id: x.partyRelationshipTypeId }) });
      this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.item = this.dropdownPRT);

      y.forEach((e, index) => {
        this.gridArray.push({
          partyRelationshipTypeId: e.partyRelationshipTypeId,
          partyRelationshipName: e.partyRelationshipName,
          parentTypeId: e.parentTypeId,
          parentTypeDesc: this.dropdownPRT.find(x => x.id == e.parentTypeId)?.label,
          description: e.description,

          variableGridArray: {
            id: e.partyRelationshipTypeId,
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

      { head: 'Code', fieldName: 'partyRelationshipTypeId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, width: '15vw', textLength: 20 },
      { head: 'Name', fieldName: 'partyRelationshipName', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, width: '20vw', textLength: 100, },
      {
        head: 'Classification', fieldName: 'parentTypeDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, width: '15vw',
        dropdown: {
          item: this.dropdownPRT,
          clear: true,
          key: 'parentTypeId',
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.loading = true);
            await this.setPRTDropdown();
            this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.item = this.dropdownPRT);
            this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.loading = false);
          }
        }
      },
      { head: 'Description', fieldName: 'description', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, width: '30vw', textLength: 255, },
      { head: '', fieldName: 'null', actionInput: ActionInput.null, actionOutput: ActionOutput.actionDetails, width: '5vw', filter: HeadFilter.null },
    );

  }

  async setPRTDropdown() {
    this.dropdownPRT = [];
    await lastValueFrom(this.partyRelationshipTypeService.getPartyRelationshipType()).then(x => {
      x.sort((a, b) => this.tbService.sortDataDW(a, b, 'partyRelationshipName'));

      x.forEach(y => {
        this.dropdownPRT.push({ label: y.partyRelationshipName, id: y.partyRelationshipTypeId })
      })
    }).catch((error) => console.log(error));
  }


  toRatingScale(itemClick) {
    let etch = itemClick.partyRelationshipTypeId ?? "";
    let label = itemClick.description ?? "";
    let encodedURI = encodeURIComponent(itemClick.partyRelationshipTypeId);

    this.dataStorageService.setData(this.router.url + `/${itemClick.partyRelationshipTypeId}`, 'title', etch + " - " + label);
    this.router.navigate([`${encodedURI}`], { relativeTo: this.route });
  }


  openNew() {
    this.elementToAdd = {
      partyRelationshipTypeId: null,
      partyRelationshipName: null,
      parentTypeId: null,
      parentTypeDesc: null,
      description: null,

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
      let obj = new PartyRelationshipType(
        e.partyRelationshipTypeId,
        e.partyRelationshipName,
        e.parentTypeId,
        e.description,
        "Y"
      );

      await this.partyRelationshipTypeService.createPartyRelationshipType(obj)
        .then(() => {

          this.msgService.successCreateWithId(obj.partyRelationshipTypeId);
          e.variableGridArray.id = obj.partyRelationshipTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.partyRelationshipTypeId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new PartyRelationshipType(
        e.partyRelationshipTypeId,
        e.partyRelationshipName,
        e.parentTypeId,
        e.description,
        "Y"
      );

      await this.partyRelationshipTypeService.updatePartyRelationshipType(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.partyRelationshipTypeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.partyRelationshipTypeId);
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


      this.partyRelationshipTypeService.deletePartyRelationshipType(listGridElement.map(x => x.partyRelationshipTypeId))
        .then(() => {
          this.msgService.successDelete()
          let tmpGrid = this.gridArray;
          listGridElement.forEach(w => { tmpGrid = tmpGrid.filter(r => r.partyRelationshipTypeId != w.partyRelationshipTypeId) })
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

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  
  resetAllElement(){
    this._reload.next();
  }

}

