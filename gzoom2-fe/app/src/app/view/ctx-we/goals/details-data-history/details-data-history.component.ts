import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { WorkEffortStatus } from 'app/api/model/workEffortStatus';
import { WorkEffortStatusEx } from 'app/api/model/workEffortStatusEx';
import { LanguageService } from 'app/api/service/language.service';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { WorkEffortStatusService } from 'app/api/service/work-effort-status.service';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { MsgService } from 'app/commons/service/message.service';
import { ToolbarService } from 'app/commons/service/toolbar.service';
import { I18NService } from 'app/i18n/i18n.service';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { LoaderService } from 'app/shared/loader/loader.service';
import { MenuItem } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subject, Subscription, map, mergeMap, mergeWith } from 'rxjs';

@Component({
  selector: 'app-details-data-history',
  templateUrl: './details-data-history.component.html',
  styleUrls: ['./details-data-history.component.css']
})
export class DetailsDataHistoryComponent implements OnInit, OnDestroy {

  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  dataTable: Table;
  flag: boolean = false;
  loading: boolean = true;
  er: boolean = false;
  backLink = '../../'

  headArray: HeadArray[] = [
    {
      head: "",
      fieldName: "id",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      display: "none",
    },
  ];

  gridArray: WorkEffortStatusEx[] = [];
  newRow: any;
  secondaryLang: boolean;

  dropdownStatus: MenuItem[] = [];

  itemDWSelected: any;
  dataSourceTypeService: any;

  workEffortStatus$: Subscription;

  workEffortId: string;


  constructor(
    private route: ActivatedRoute,
    private readonly router: Router,
    private readonly dataStorageService: DataStorageService,
    private readonly usrPreferenceService: UserPreferenceService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly loaderService: LoaderService,
    private msgService: MsgService,
    private toolbarService: ToolbarService,
    private workEffortStatusService: WorkEffortStatusService

  ) {
    this.toolbarService.setPrimaryBoardComponentButton();
    this.toolbarService.setDisabledNew(true);
    this._reload = new Subject<void>();
  }

  ngOnDestroy(): void {
    this.workEffortStatus$?.unsubscribe();
  }

  async ngOnInit(): Promise<void> {
    this.loaderService.hide();
    this.secondaryLang = await this.languageService.secondaryLang();
    this.workEffortId = this.route.snapshot.params.id;

    const reload = this._reload.pipe(
      mergeMap(() =>
        this.workEffortStatusService.getWorkEffortStatusEx(this.workEffortId)
      )
    )

    this.setHeadArray();

    const workEffortStatus = this.route.data.pipe(
      map((data: { obss: WorkEffortStatusEx[] }) => data.obss),
      mergeWith(reload)
    );

    // await this.setStatusDescDropdown();

    this.workEffortStatus$ = workEffortStatus.subscribe((data) => {
      this.gridArray = [];
      // if (this.newRow) this.gridArray.push(this.newRow);      

      data.forEach((e) => {

        let ID = e.workEffortId + e.statusId + e.statusDatetime;

        this.gridArray.push({
          workEffortId: e.workEffortId,
          statusId: e.statusId,
          statusDesc: this.secondaryLang ? e.statusItem.descriptionLang : e.statusItem.description,
          statusDatetime: e.statusDatetime? new Date(e.statusDatetime):null,
          setByUserLogin: e.setByUserLogin,
          reason: e.reason,

          variableGridArray: {
            id: ID,
            updated: false,
            inputLabeldata: false,
            inputNotes: true,
            outputData: true,
            inputNew: false,
            dropdownData: false,
          },
        });
      });
      this.loading = false;
    });
  }


  async setHeadArray() {

    this.headArray.push(
      {
        head: "Id",
        fieldName: "workEffortId",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
        unique: true,
        width: '10vw',
      },
      {
        head: "Status",
        fieldName: "statusDesc",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
        width: '15vw'
      },
      {
        head: "Date / Time",
        fieldName: "statusDatetime",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.textFilter,
        textLength: 20,
        width: '15vw'
      },
      {
        head: "User",
        fieldName: "setByUserLogin",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 255,
        width: '20vw'
      },
      {
        head: "Reason",
        fieldName: "reason",
        actionInput: ActionInput.inputNotes,
        actionOutput: ActionOutput.outputNotes,
        filter: HeadFilter.textFilter,
        required: true,
        width: '20vw'
      }
    );
  }

  saveAllElement(elementUpdated) {
    // let newElement = elementUpdated.filter(x => x.variableGridArray.id.includes("new"));
    // if (newElement.length > 0) {
    //   this.reload = true;
    //   this.create(newElement);
    //   this.reload = false;
    // }
    elementUpdated = elementUpdated.filter(x => !x.variableGridArray.id.includes("new"));
    if (elementUpdated.length > 0) {

      this.update(elementUpdated);
    }

  }

  update(gridElement) {

    gridElement.forEach(async e => {
      let obj = new WorkEffortStatus();

      obj.workEffortId = e.workEffortId;
      obj.statusId = e.statusId;
      obj.reason = e.reason;
      obj.setByUserLogin = e.setByUserLogin;
      obj.statusDatetime = e.statusDatetime;

      await this.workEffortStatusService.updateWorkEffortStatus(obj)
        .then(() => {
          this.msgService.successUpdate();
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.workEffortId);
          this.er = true;
        });
    });
    return !this.er;
  }

  delete(listGridElement: WorkEffortStatusEx[]) {
    if (listGridElement.filter(x => x.variableGridArray.id.includes("new")).length > 0) {
      this.newRow = listGridElement.filter(x => x.variableGridArray.id.includes("new"));
      listGridElement = listGridElement.filter(x => !this.newRow.includes(x));
      this.gridArray = this.gridArray.filter(x => !x.variableGridArray.id.includes("new"));
      this.newRow = null;
    }
    if (listGridElement.length > 0) {
      listGridElement.forEach(e => {

        this.workEffortStatusService.deleteWorkEffortStatus(e)
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
