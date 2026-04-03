import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subject, Subscription, lastValueFrom, map, mergeMap, mergeWith, tap } from 'rxjs';
import { MenuItem } from 'primeng/api';

import { ActionInput, ActionOutput, HeadArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { LanguageService } from 'app/api/service/language.service';
import { HeadFilter } from 'app/layout/tables/table/table-configuration';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { SecurityGroupService } from 'app/api/service/security-group.service';
import { PortalPageService } from 'app/api/service/portal-page.service';
import { SecurityGroup } from 'app/api/model/securityGroup';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-security-groups',
  templateUrl: './security-groups.component.html',
  styleUrls: ['./security-groups.component.css']
})
export class SecurityGroupsComponent implements OnInit, OnDestroy {
  _reload: Subject<void>;
  reload: boolean = false;

  elementToAdd: any;
  loading: boolean = true;
  er: boolean = false;
  secondaryLang: boolean;
  headArray: HeadArray[] = [];

  gridArray: any[] = [];
  newRow: any;
  obs$: Subscription;

  itemsButtonSlideMenu = [
    {
      label: this.i18nService.translate("Permission"),
      icon: "pi pi-angle-right",
      command: () => this.toDetail("permission"),
    },
    {
      label: this.i18nService.translate("SecurityGroupPermissionExcept"),
      icon: "pi pi-angle-right",
      command: () => this.toDetail("functions-excluded"),
    },
    {
      label: this.i18nService.translate("Enabled users"),
      icon: "pi pi-angle-right",
      command: () => this.toDetail("enabled-users"),
    },
  ];
  selectedIndex;

  dropdownPortal: MenuItem[] = [];


  constructor(
    private readonly route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly router: Router,
    private readonly dataStorageService: DataStorageService,
    private readonly securityGroupService: SecurityGroupService,
    private readonly portalPageService: PortalPageService,
    private readonly languageService: LanguageService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  ngOnDestroy(): void {
    this.obs$.unsubscribe();
  }

  async ngOnInit() {
    this.languageService.secondaryLang().then(result => this.secondaryLang = result);
    await this.setPortalDropdown();
    this.setHeadArray();

    const reload = this._reload.pipe(mergeMap(() => this.securityGroupService.getSecurityGroupList()));
    const w$ = this.route.data.pipe(
      map((data: { obss: SecurityGroup[] }) => data.obss),
      mergeWith(reload)
    );

    this.obs$ = w$.pipe(
      tap(() => {
        this.gridArray = [];
        if (this.newRow) this.gridArray.push(this.newRow);
      }),
      map((array: SecurityGroup[]) => {
        array.map((x) => {

          this.gridArray.push({
            groupId: x.groupId,
            description: x.description,
            defaultPortalPageId: x.defaultPortalPageId,
            defaultPortalPageDesc: this.dropdownPortal.find((y) => y.id == x.defaultPortalPageId)?.label,

            variableGridArray: {
              id: x.groupId,
              updated: false,
              inputLabeldata: true,
              inputLabelNumber: true,
              inputNotes: true,
              outputData: true,
              inputNew: false,
              dropdownData: true,
              buttonMultipleDetails: true
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
      { head: 'Code', fieldName: 'groupId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, width: '12vw', textLength: 20 },
      { head: 'Description', fieldName: 'description', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, width: '30vw', textLength: 255, },
      {
        head: 'Default Portal', fieldName: 'defaultPortalPageDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter,
        dropdown: {
          item: this.dropdownPortal,
          clear: true,
          key: 'defaultPortalPageId',
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'defaultPortalPageDesc').forEach(y => y.dropdown.loading = true);
            await this.setPortalDropdown();
            this.headArray.filter(x => x.fieldName == 'defaultPortalPageDesc').forEach(y => y.dropdown.item = this.dropdownPortal);
            this.headArray.filter(x => x.fieldName == 'defaultPortalPageDesc').forEach(y => y.dropdown.loading = false);

          }
        }, width: '15vw'
      },
      {
        head: "",
        width: '5vw',
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.actionDetails,
        filter: HeadFilter.null,
      }
    );

  }

  async setPortalDropdown() {
    this.dropdownPortal = [];

    await lastValueFrom(this.portalPageService.findByParentPortalPageId("GP_WE_PORTAL")).then(data => {

      data.forEach(x => {
        let lab = (x.portalPageName);

        this.dropdownPortal.push({ label: lab, id: x.portalPageId });
      })
    }
    ).catch((error) => console.log(error));
  }

  shareItemEvent(data) {
    this.selectedIndex = data;
  }

  toDetail(component: string) {
    this.router.navigate([`${this.selectedIndex.groupId}/${component}`], { relativeTo: this.route });
  }

  openNew() {
    this.elementToAdd = {
      groupId: null,
      description: null,
      defaultPortalPageId: null,
      defaultPortalPageDesc: null,

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
      let obj = new SecurityGroup(
        e.groupId,
        e.description,
        e.defaultPortalPageId
      );

      await this.securityGroupService.createSecurityGroup(obj)
        .then(() => {

          this.msgService.successCreateWithId(obj.groupId);
          e.variableGridArray.id = obj.groupId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonMultipleDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.groupId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new SecurityGroup(
        e.groupId,
        e.description,
        e.defaultPortalPageId
      );

      await this.securityGroupService.updateSecurityGroup(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.groupId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.groupId);
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

        this.securityGroupService.deleteSecurityGroup(e.groupId)
          .then(() => {
            this.msgService.successDeleteWithId(e.groupId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.groupId);
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