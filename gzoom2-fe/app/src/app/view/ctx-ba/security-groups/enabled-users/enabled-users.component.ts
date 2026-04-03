import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, Subscription, lastValueFrom, map, mergeMap, mergeWith, tap } from 'rxjs';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { MenuItem } from 'primeng/api';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { UserLoginSecurityGroupService } from 'app/api/service/user-login-security-group.service';
import { UserLoginService } from 'app/api/service/user-login.service';
import { UserLoginSecurityGroup } from 'app/api/model/userLoginSecurityGroup';
import { MsgService } from 'app/commons/service/message.service';
import { SecurityGroupService } from 'app/api/service/security-group.service';
import { SecurityGroup } from 'app/api/model/securityGroup';

@Component({
  selector: 'app-enabled-users',
  templateUrl: './enabled-users.component.html',
  styleUrls: ['./enabled-users.component.css']
})
export class EnabledUsersComponent implements OnInit, OnDestroy {
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
  dropdownUsers: MenuItem[] = [];
  obs$: Subscription;
  selectSecurityGroup: SecurityGroup;

  constructor(
    private route: ActivatedRoute,
    private readonly languageService: LanguageService,
    private readonly userLoginSecurityGroupService: UserLoginSecurityGroupService,
    private readonly userLoginService: UserLoginService,
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

    const reload = this._reload.pipe(mergeMap(() => this.userLoginSecurityGroupService.getUserLoginSecurityGroupByGroupId(this.groupId)));
    const data$ = this.route.data.pipe(
      map((data: { obss: UserLoginSecurityGroup[] }) => data.obss),
      mergeWith(reload)
    );
    await this.setUsersDropdown();

    this.setHeadArray();

    this.obs$ = data$.pipe(
      tap(() => {
        this.gridArray = [];
        if (this.newRow) this.gridArray.push(this.newRow);
      }),
      map((array: UserLoginSecurityGroup[]) => {
        array.map((x) => {

          this.gridArray.push({
            groupId: x.groupId,
            userLoginId: x.userLoginId,
            fromDate: x.fromDate? new Date(x.fromDate): null,
            thruDate: x.thruDate? new Date(x.thruDate): null,

            variableGridArray: {
              id: x.userLoginId,
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
        head: 'User', fieldName: 'userLoginId', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter,  required: true, unique: true,
        dropdown: {
          item: this.dropdownUsers,
          clear: false,
          key: 'userLoginId',
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'userLoginId').forEach(y => y.dropdown.loading = true);
            await this.setUsersDropdown();
            this.headArray.filter(x => x.fieldName == 'userLoginId').forEach(y => y.dropdown.item = this.dropdownUsers);
            this.headArray.filter(x => x.fieldName == 'userLoginId').forEach(y => y.dropdown.loading = false);

          }
        },width:'30vw'
      },
      { head: 'Start Date', fieldName: 'fromDate', actionInput: ActionInput.inputDate, actionOutput: ActionOutput.outputDate, filter: HeadFilter.dateFilter, required: true, unique: true  },
      { head: 'End Date', fieldName: 'thruDate', clearCalendar: true, actionInput: ActionInput.inputDate, actionOutput: ActionOutput.outputDate, filter: HeadFilter.dateFilter },
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

  async setUsersDropdown() {
    this.dropdownUsers = [];
    await lastValueFrom(this.userLoginService.findAllOrderByPrimaryKey()).then(data => {

      data.forEach(x => {
        let lab = x.userLoginId;

        this.dropdownUsers.push({ label: lab, id: x.userLoginId });
      })
    }
    ).catch((error) => console.log(error));
  }

  openNew() {
    const now = new Date();
    now.setHours(0, 0, 0, 0);
    this.elementToAdd = {
      groupId: this.groupId,
      userLoginId: null,
      fromDate: now,
      thruDate: null,

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
      let obj = new UserLoginSecurityGroup(
        e.groupId,
        e.userLoginId,
        e.fromDate,
        e.thruDate
      );

      await this.userLoginSecurityGroupService.createUserLoginSecurityGroup(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.userLoginId);
          e.variableGridArray.id = obj.userLoginId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.userLoginId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new UserLoginSecurityGroup(
        e.groupId,
        e.userLoginId,
        e.fromDate,
        e.thruDate
      );
      await this.userLoginSecurityGroupService.updateUserLoginSecurityGroup(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.userLoginId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.userLoginId);
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
        this.userLoginSecurityGroupService.deleteUserLoginSecurityGroup(e.userLoginId, e.groupId, e.fromDate)
          .then(() => {
            this.msgService.successDeleteWithId(e.userLoginId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.userLoginId);
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
