import { Component, OnInit } from '@angular/core';
import { Subject, lastValueFrom, map, mergeMap, mergeWith } from 'rxjs';
import { Table } from 'primeng/table';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { JobLogLogEx } from 'app/api/model/jobLogLogEx';
import { ActivatedRoute, Router } from '@angular/router';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { I18NService } from 'app/i18n/i18n.service';
import { LanguageService } from 'app/api/service/language.service';
import { JobLogLogService } from 'app/api/service/job-log-log.service';
import { LoaderService } from 'app/shared/loader/loader.service';
import { JobLogService } from 'app/api/service/job-log.service';
import { JobLog } from 'app/api/model/jobLog';

@Component({
  selector: 'app-job-log-log',
  templateUrl: './job-log-log.component.html',
  styleUrls: ['./job-log-log.component.css']
})
export class JobLogLogComponent implements OnInit {

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

  gridArray: JobLogLogEx[] = [];
  newRow: any;
  secondaryLang: boolean;
  jobLogId: string;

  selectJobLog: JobLog;
  title: String;

  constructor(
    private route: ActivatedRoute,
    private readonly languageService: LanguageService,
    private readonly jobLogLogService: JobLogLogService,
    private readonly jobLogService: JobLogService,
    private readonly loaderService: LoaderService

  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit(): Promise<void> {
    this.loaderService.hide();
    this.secondaryLang = await this.languageService.secondaryLang();

    this.route.parent.paramMap.subscribe(paramMap => {
      this.jobLogId = paramMap.get('jobLogId');
    });   

    this.selectJobLog = await lastValueFrom(this.jobLogService.getJobLogById(this.jobLogId))
    this.title =  this.jobLogId + " - " + this.selectJobLog.serviceName;

    const reload = this._reload.pipe(
      mergeMap(() =>
        this.jobLogLogService.getJobLogLogEx(this.jobLogId)
      )
    )


    this.setHeadArray();

    const jobLogJobExecParams = this.route.data.pipe(
      map((data: { obss: JobLogLogEx[] }) => data.obss),
      mergeWith(reload)
    );


    jobLogJobExecParams.subscribe((data) => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);

      data.forEach((e) => {

        let ID = e.jobLogId;

        this.gridArray.push({
          jobLogId: e.jobLogId,
          jobLogLogId: e.jobLogLogId,
          logTypeEnumId: e.logTypeEnumId,
          logTypeEnumIdDesc: e.enumerationType.description,
          logCode: e.logCode,
          logMessage: e.logMessage,
          valueRef1: e.valueRef1,
          valueRef2: e.valueRef2,
          valueRef3: e.valueRef3,

          variableGridArray: {
            id: ID,
            updated: false,
            buttonDetails: true,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: false,
            outputData: true,
            inputNew: false,
            dropdownData: true,
          },
        });
      });
      this.loading = false;
    });

  }

  async setHeadArray() {

    this.headArray.push(
      {
        head: "jobLogId",
        fieldName: "jobLogId",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        display: 'none',
      },
      {
        head: "Detail Id",
        fieldName: "jobLogLogId",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        width:'5vw'
      },
      {
        head: "Tipology Message",
        fieldName: "logTypeEnumIdDesc",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        width:'8vw'
      },
      {
        head: "Code",
        fieldName: "logCode",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
        width:'8vw'
      },
      {
        head: "Message",
        fieldName: "logMessage",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputDialogNote,
        filter: HeadFilter.textFilter,
        textLength: 20,
        width:'15vw'
      },
      {
        head: "Reference 1",
        fieldName: "valueRef1",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputDialogNote,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        width:'15vw'
      },
      {
        head: "Reference 2",
        fieldName: "valueRef2",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputDialogNote,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        width:'15vw'
      },
      {
        head: "Reference 3",
        fieldName: "valueRef3",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputDialogNote,
        filter: HeadFilter.popUpFilter,
        textLength: 20,
        width:'15vw',
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

}
