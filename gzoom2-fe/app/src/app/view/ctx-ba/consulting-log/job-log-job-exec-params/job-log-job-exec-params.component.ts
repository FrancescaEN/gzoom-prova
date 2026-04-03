import { Component, OnInit } from '@angular/core';
import { Subject, lastValueFrom, map, mergeMap, mergeWith } from 'rxjs';
import { Table } from 'primeng/table';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { JobLogJobExecParams } from 'app/api/model/jobLogJobExecParams';
import { ActivatedRoute, Router } from '@angular/router';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { I18NService } from 'app/i18n/i18n.service';
import { LanguageService } from 'app/api/service/language.service';
import { JobLogJobExecParamsService } from 'app/api/service/job-log-job-exec-params.service';
import { LoaderService } from 'app/shared/loader/loader.service';
import { JobLogService } from 'app/api/service/job-log.service';
import { JobLog } from 'app/api/model/jobLog';

@Component({
  selector: 'app-job-log-job-exec-params',
  templateUrl: './job-log-job-exec-params.component.html',
  styleUrls: ['./job-log-job-exec-params.component.css']
})
export class JobLogJobExecParamsComponent implements OnInit {

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

  gridArray: JobLogJobExecParams[] = [];
  newRow: any;
  secondaryLang: boolean;
  jobLogId: string;

  selectJobLog: JobLog;
  title: String;

  constructor(
    private route: ActivatedRoute,
    private readonly jobLogService: JobLogService,
    private readonly languageService: LanguageService,
    private readonly jobLogJobExecParamsService: JobLogJobExecParamsService,
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
        this.jobLogJobExecParamsService.getJobLogJobExecParams(this.jobLogId)
      )
    )

    this.setHeadArray();

    const jobLogJobExecParams = this.route.data.pipe(
      map((data: { obss: JobLogJobExecParams[] }) => data.obss),
      mergeWith(reload)
    );


    jobLogJobExecParams.subscribe((data) => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);

      data.forEach((e, index) => {

        let ID = e.jobLogId;

        this.gridArray.push({
          jobLogId: e.jobLogId,
          parameterName: e.parameterName,
          parameterValue: e.parameterValue,

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
        head: "IdLog",
        fieldName: "jobLogId",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
        display: 'none',
      },
      {
        head: "Detail Id",
        fieldName: "parameterName",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
      },
      {
        head: "Value",
        fieldName: "parameterValue",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
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
