import { Component, OnInit } from '@angular/core';
import { Subject, map, mergeMap, mergeWith } from 'rxjs';
import { MenuItem } from "primeng/api";
import { Table } from 'primeng/table';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';
import { JobLogEx } from 'app/api/model/jobLogEx';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { I18NService } from 'app/i18n/i18n.service';
import { LanguageService } from 'app/api/service/language.service';
import { JobLogService } from 'app/api/service/job-log.service';
import { LoaderService } from 'app/shared/loader/loader.service';
import { InfoPage } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';
import { ToolbarService } from 'app/commons/service/toolbar.service';

@Component({
  selector: 'app-consulting-log',
  templateUrl: './consulting-log.component.html',
  styleUrls: ['./consulting-log.component.css']
})
export class ConsultingLogComponent implements OnInit {


  _reload: Subject<void>;
  reload: boolean = false;
  dataTable: Table;
  loading: boolean = true;
  er: boolean = false;

  headArray: HeadArray[] = [
    {
      head: "",
      fieldName: "id",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      display: "none",
    },
  ];

  gridArray: JobLogEx[] = [];
  newRow: any;
  itemsButtonSlideMenu: MenuItem[];

  selectJobLogId: string;
  infoCurrentPage: InfoPage = { offset: 0, limit: 50, filter: [] };
  totalRecords: Number;
  filterStartArray = [];

  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();

  constructor(
    private route: ActivatedRoute,
    private readonly router: Router,
    private readonly dataStorageService: DataStorageService,
    private readonly usrPreferenceService: UserPreferenceService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly jobLogService: JobLogService,
    private readonly loaderService: LoaderService,
    private readonly toolbarService: ToolbarService

  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit(): Promise<void> {
    this.loaderService.hide();
    this.toolbarService.setDisabledNew(true);

    const reload = this._reload.pipe(
      mergeMap(() =>
        this.jobLogService.getJobLogEx(this.infoCurrentPage)
      )
    )

    this.setHeadArray();

    const jobLogEx = this.route.data.pipe(
      map((data: { obss: JobLogEx[] }) => data.obss),
      mergeWith(reload)
    );


    jobLogEx.subscribe((data) => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);

      this.setGridArray(data);


    });

    this.itemsButtonSlideMenu = [
      {
        label: this.i18nService.translate("Exec Params"),
        icon: "pi pi-search",
        command: () => this.toDetailsExecParams(),
      },
      {
        label: this.i18nService.translate("Details Log"),
        icon: "pi pi-search",
        command: () => this.toDetailsDetailsLog(),
      },
    ];

  }

  setGridArray(data) {
    data.forEach((e) => {


      this.totalRecords = e["totalRow"];

      let ID = e.jobLogId;

      this.gridArray.push({
        jobLogId: e.jobLogId,
        serviceTypeId: e.serviceTypeId,
        serviceTypeIdDesc: e.jobLogServiceType.description,
        serviceName: e.serviceName,
        logDate: e.logDate ? new Date(e.logDate) : null,
        logEndDate: e.logEndDate ? new Date(e.logEndDate) : null,
        recordElaborated: e.recordElaborated,
        blockingErrors: e.blockingErrors,
        warningMessages: e.warningMessages,
        elabRef1: e.elabRef1,
        userLoginId: e.userLoginId,
        sessionId: e.sessionId,

        variableGridArray: {
          id: ID,
          updated: false,
          buttonDetails: false,
          inputLabeldata: true,
          inputLabelNumber: true,
          inputNotes: false,
          outputData: true,
          inputNew: false,
          dropdownData: true,
          buttonMultipleDetails: true,
          // dateFormat: this.i18nService.getFormat("date-time-short")
          dateFormat: 'medium'

        },
      });
    });
    this.loading = false;
  }

  async setHeadArray() {

    this.headArray.push(
      {
        head: "IdLog",
        fieldName: "jobLogId",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        width: '5vw'
      },
      {
        head: "Tipology Service",
        fieldName: "serviceTypeIdDesc",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
        width: '10vw'
      },
      {
        head: "Service Name",
        fieldName: "serviceName",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
        width: '12vw'
      },
      {
        head: "Start Timestamp",
        fieldName: "logDate",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.textFilter,
        textLength: 20,
        width: '10vw'
      },
      {
        head: "End Timestamp",
        fieldName: "logEndDate",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.textFilter,
        textLength: 20,
        width: '10vw'
      },
      {
        head: "Number Records",
        fieldName: "recordElaborated",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        content: "center",
        width: '5vw'
      },
      {
        head: "Number Errors",
        fieldName: "blockingErrors",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        content: "center",
        width: '5vw'
      },
      {
        head: "Number Warnings",
        fieldName: "warningMessages",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        content: "center",
        width: '5vw'
      },
      {
        head: "Processing Reference",
        fieldName: "elabRef1",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        width: '8vw'
      },
      {
        head: "User Login",
        fieldName: "userLoginId",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        width: '8vw'
      },
      {
        head: "Session Id",
        fieldName: "sessionId",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        width: '8vw'
      },
      {
        head: "",
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.actionDetails,
        filter: HeadFilter.null,
      },
    );
  }

  shareItemEvent(data: JobLogEx) {
    this.selectJobLogId = data.jobLogId;
  }

  toDetailsExecParams() {
    let encodedURI = encodeURIComponent(this.selectJobLogId);

    this.dataStorageService.setData(this.router.url + `/${this.selectJobLogId}`, 'title', this.selectJobLogId + ".");
    this.router.navigate([`${encodedURI}/jobLogJobExecParams`], { relativeTo: this.route });
  }

  toDetailsDetailsLog() {
    let encodedURI = encodeURIComponent(this.selectJobLogId);

    this.dataStorageService.setData(this.router.url + `/${this.selectJobLogId}`, 'title', this.selectJobLogId + ".");
    this.router.navigate([`${encodedURI}/jobLogLog`], { relativeTo: this.route });
  }

  shareInfoPagination(data) {
    const queryParams: Params = { limit: data.rows, offset: data.first };
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: "merge",
      state: { isSecondaryLang: this.secondaryLang, infoPage: this.infoCurrentPage }
    });
  }

  async loadData(data?) {

    if (data) {
      this.infoCurrentPage.filter = this.filterStartArray;

      data.filter.forEach((x) => {
        if (x.value != null) {
          this.infoCurrentPage.filter = [x, ...this.infoCurrentPage.filter];
        }
      });

      this.infoCurrentPage.limit = data.limit;
      this.infoCurrentPage.offset = data.offset;
      this.infoCurrentPage.sortField = data.sortField;
      this.infoCurrentPage.sortOrder = data.sortOrder;
      this.infoCurrentPage.secondaryLang = this.secondaryLang;

    }

    this._reload.next();

  }

}
