import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, Subscription, lastValueFrom, map, mergeMap, mergeWith, tap } from 'rxjs';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { MenuItem } from 'primeng/api';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { SecurityGroupPermissionService } from 'app/api/service/security-group-permission.service';
import { SecurityGroupPermission } from 'app/api/model/securityGroupPermission';
import { SecurityPermissionService } from 'app/api/service/security-permission.service';
import { MsgService } from 'app/commons/service/message.service';
import { SecurityGroupService } from 'app/api/service/security-group.service';
import { SecurityGroup } from 'app/api/model/securityGroup';

@Component({
  selector: 'app-permission',
  templateUrl: './permission.component.html',
  styleUrls: ['./permission.component.css']
})
export class PermissionComponent implements OnInit, OnDestroy {
  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  flag: boolean = false;
  loading: boolean = true;
  er: boolean = false;
  backLink = '../../'

  headArray: HeadArray[] = [];

  gridArray: any[] = [];
  newRow: any;
  secondaryLang: boolean;
  languages: [] = [];

  groupId: string;

  title: string;
  dropdownPermission: MenuItem[] = [];
  obs$: Subscription;

  selectSecurityGroup: SecurityGroup;

  constructor(
    private route: ActivatedRoute,
    private readonly languageService: LanguageService,
    private readonly securityGroupPermissionService: SecurityGroupPermissionService,
    private readonly securityPermissionService: SecurityPermissionService,
    private readonly securityGroupService: SecurityGroupService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  ngOnDestroy(): void {
    this.obs$.unsubscribe();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    const routeParams = this.route.snapshot.parent.paramMap;
    this.groupId = routeParams.get('groupId');

    this.selectSecurityGroup = await lastValueFrom(this.securityGroupService.getSecurityGroupById(this.groupId))
    this.title =  this.groupId + " - " + this.selectSecurityGroup.description;  

    const reload = this._reload.pipe(mergeMap(() => this.securityGroupPermissionService.getSecurityGroupPermissionByGroupId(this.groupId)));
    const data$ = this.route.data.pipe(
      map((data: { obss: SecurityGroupPermission[] }) => data.obss),
      mergeWith(reload)
    );
    await this.setPermissionDropdown();

    this.setHeadArray();

    this.obs$ = data$.pipe(
      tap(() => {
        this.gridArray = [];
        if (this.newRow) this.gridArray.push(this.newRow);
      }),
      map((array: SecurityGroupPermission[]) => {
        array.map((x) => {

          this.gridArray.push({
            groupId: x.groupId,
            permissionId: x.permissionId,
            permissionDesc: this.dropdownPermission.find(y => y.id == x.permissionId)?.label,

            variableGridArray: {
              id: x.permissionId,
              updated: false,
              inputLabeldata: true,
              inputLabelNumber: true,
              inputNotes: true,
              outputData: true,
              inputNew: false,
              dropdownData: true,
            }
          })
        })
      })
    ).subscribe(() => {
      this.loading = false;
    })
  }

  setHeadArray() {
    this.headArray.push(

      {
        head: 'Permission', fieldName: 'permissionDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, required: true, unique: true,
        dropdown: {
          item: this.dropdownPermission,
          clear: false,
          key: 'permissionId',
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'permissionDesc').forEach(y => y.dropdown.loading = true);
            await this.setPermissionDropdown();
            this.headArray.filter(x => x.fieldName == 'permissionDesc').forEach(y => y.dropdown.item = this.dropdownPermission);
            this.headArray.filter(x => x.fieldName == 'permissionDesc').forEach(y => y.dropdown.loading = false);

          }
        }
      },
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

  async setPermissionDropdown() {
    this.dropdownPermission = [];
    await lastValueFrom(this.securityPermissionService.findByEnabledOrderByPrimaryKey('Y')).then(data => {

      data.forEach(x => {
        let lab = x.permissionId + " - " + x.description;

        this.dropdownPermission.push({ label: lab, id: x.permissionId });
      })
    }
    ).catch((error) => console.log(error));
  }

  openNew() {
    this.elementToAdd = {
      groupId: this.groupId,
      permissionId: null,
      permissionDesc: null,

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
      let obj = new SecurityGroupPermission(
        e.groupId,
        e.permissionId
      );

      await this.securityGroupPermissionService.createSecurityGroupPermission(obj)
        .then(() => {

          this.msgService.successCreateWithId(obj.permissionId);
          e.variableGridArray.id = obj.permissionId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.permissionId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new SecurityGroupPermission(
        e.groupId,
        e.permissionId
      );
      await this.securityGroupPermissionService.updateSecurityGroupPermission(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.permissionId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.permissionId);
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
        this.securityGroupPermissionService.deleteSecurityGroupPermission(e.groupId, e.permissionId)
          .then(() => {
            this.msgService.successDeleteWithId(e.permissionId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.permissionId);
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
