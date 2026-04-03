import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith } from 'rxjs';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { MenuItem } from 'primeng/api';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { PartyRelationshipRoleService } from 'app/api/service/party-relationship-role.service';
import { PartyRelationshipRole } from 'app/api/model/partyRelationshipRole';
import { PartyRelationshipTypeService } from 'app/api/service/party-relationship-type.service';
import { RoleTypeService } from 'app/api/service/role-type.service';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-types-relationships-between-subjects-details',
  templateUrl: './types-relationships-between-subjects-details.component.html',
  styleUrls: ['./types-relationships-between-subjects-details.component.css']
})
export class TypesRelationshipsBetweenSubjectsDetailsComponent {
  _reload: Subject<void>;
  reload: boolean = false;

  elementToAdd: any;
  secondaryLang: boolean;
  loading: boolean = true;
  er: boolean = false;

  headArray: HeadArray[] = [
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: any[] = [];
  newRow: any;

  partyRelationshipTypeId;

  title: string;

  dropdownRT: MenuItem[] = [];

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly dataStorageService: DataStorageService,
    private readonly partyRelationshipRoleService: PartyRelationshipRoleService,
    private readonly router: Router,
    private readonly tbService: TableEditingCellService,
    private readonly roleTypeService: RoleTypeService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    const routeParams = this.route.snapshot.paramMap;
    this.partyRelationshipTypeId = decodeURIComponent(routeParams.get('partyRelationshipTypeId'));

    if (this.dataStorageService.getData(this.router.url, 'title'))
      this.title = this.dataStorageService.getData(this.router.url, 'title');

    const reload = this._reload.pipe(mergeMap(() => this.partyRelationshipRoleService.getPartyRelationshipRole(this.partyRelationshipTypeId)));
    const urv$ = this.route.data.pipe(
      map((data: { obss: PartyRelationshipRole[] }) => data.obss),
      mergeWith(reload)
    );
    await this.setRTDropdown();
    this.setHeadArray();

    urv$.subscribe(y => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach((e, index) => {
        let ID = e.partyRelationshipTypeId + e.roleTypeValidFrom + e.roleTypeValidTo;
        this.gridArray.push({
          partyRelationshipTypeId: e.partyRelationshipTypeId,
          roleTypeValidFrom: e.roleTypeValidFrom,
          roleTypeValidTo: e.roleTypeValidTo,
          roleTypeValidFromDesc: this.dropdownRT.find(x => x.id == e.roleTypeValidFrom)?.label,
          roleTypeValidToDesc: this.dropdownRT.find(x => x.id == e.roleTypeValidTo)?.label,
          informativeSequence: e.informativeSequence,

          variableGridArray: {
            id: ID,
            updated: false,
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
      {
        head: 'From Role', fieldName: 'roleTypeValidFromDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, required: true, unique: true,
        dropdown: {
          item: this.dropdownRT,
          clear: false,
          key: 'roleTypeValidFrom',
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'roleTypeValidFromDesc').forEach(y => y.dropdown.loading = true);
            await this.setRTDropdown();
            this.headArray.filter(x => x.fieldName == 'roleTypeValidFromDesc').forEach(y => y.dropdown.item = this.dropdownRT);
            this.headArray.filter(x => x.fieldName == 'roleTypeValidFromDesc').forEach(y => y.dropdown.loading = false);
          }
        }
      },
      {
        head: 'To Role', fieldName: 'roleTypeValidToDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter,  required: true, unique: true,
        dropdown: {
          item: this.dropdownRT,
          clear: false,
          key: 'roleTypeValidTo',
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'roleTypeValidToDesc').forEach(y => y.dropdown.loading = true);
            await this.setRTDropdown();
            this.headArray.filter(x => x.fieldName == 'roleTypeValidToDesc').forEach(y => y.dropdown.item = this.dropdownRT);
            this.headArray.filter(x => x.fieldName == 'roleTypeValidToDesc').forEach(y => y.dropdown.loading = false);
          }
        }
      },
      { head: 'Informative Sequence', fieldName: 'informativeSequence', actionInput: ActionInput.inputLabelNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.textFilter,  content: 'center' },
      {
        head: "",
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.null,
        filter: HeadFilter.null,
        width:'0.5vw'
      }

    );
  }

  async setRTDropdown() {
    this.dropdownRT = [];
    await lastValueFrom(this.roleTypeService.roleTypes()).then(x => {
      x.sort((a, b) => this.tbService.sortDataDW(a, b, ((!this.secondaryLang) ? 'description' : 'descriptionLang')));

      x.forEach(y => {
        this.dropdownRT.push({ label: ((!this.secondaryLang) ? y.description : y.descriptionLang), id: y.roleTypeId })
      })
    }).catch((error) => console.log(error));
  }


  openNew() {
    this.elementToAdd = {
      partyRelationshipTypeId: this.partyRelationshipTypeId,
      roleTypeValidFrom: null,
      roleTypeValidTo: null,
      roleTypeValidFromDesc: null,
      roleTypeValidToDesc: null,
      informativeSequence: null,

      variableGridArray: {
        id: "new" + Math.random(),
        updated: true,
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
      let obj = new PartyRelationshipRole(
        this.partyRelationshipTypeId,
        e.roleTypeValidFrom,
        e.roleTypeValidTo,
        e.informativeSequence
      );
      await this.partyRelationshipRoleService.createPartyRelationshipRole(obj)
        .then((x) => {

          this.msgService.successCreateWithId(obj.roleTypeValidFrom + ', ' + obj.roleTypeValidTo);
          e.variableGridArray.id = obj.partyRelationshipTypeId + obj.roleTypeValidFrom + obj.roleTypeValidTo;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;

          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.roleTypeValidFrom + ', ' + obj.roleTypeValidTo);
          this.er = true;
        });

    });

    return !this.er;
  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new PartyRelationshipRole(
        this.partyRelationshipTypeId,
        e.roleTypeValidFrom,
        e.roleTypeValidTo,
        e.informativeSequence
      );
      await this.partyRelationshipRoleService.updatePartyRelationshipRole(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.roleTypeValidFrom + ', ' + obj.roleTypeValidTo);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.roleTypeValidFrom + ', ' + obj.roleTypeValidTo);
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
        this.partyRelationshipRoleService.deletePartyRelationshipRole(e.partyRelationshipTypeId, e.roleTypeValidFrom, e.roleTypeValidTo)
          .then(() => {
            this.msgService.successDeleteWithId(e.roleTypeValidFrom + ', ' + e.roleTypeValidTo);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.roleTypeValidFrom + ', ' + e.roleTypeValidTo);
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

}
