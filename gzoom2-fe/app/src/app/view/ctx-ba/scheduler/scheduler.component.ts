import { Component, DestroyRef, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { PlannerService } from 'app/api/service/scheduler/planner.service';
import { ActionInput, ActionOutput, HeadArray, HeadFilter, TagSeverity } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { Observable, Subject, Subscription, combineLatest, map, mergeMap, mergeWith, takeUntil, tap } from 'rxjs';
import { MenuItem, SelectItem } from 'primeng/api';

import { ActivatedRoute } from '@angular/router';
import { QrtzJobDetails } from 'app/api/model/qrtzJobDetails';
import { Frequency, JobData, ParameterJob, ServiceJobInfo } from 'app/api/model/scheduler';
import { I18NService } from 'app/i18n/i18n.service';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { MsgService } from 'app/commons/service/message.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';


enum JobState {
  PLANNED = "Planned",
  TO_PLAN = "To plan"
}

@Component({
  selector: 'app-scheduler',
  templateUrl: './scheduler.component.html',
  styleUrls: ['./scheduler.component.css'],
})
export class SchedulerComponent implements OnInit, OnDestroy {
  _reload: Subject<void>;
  _reloadTrigger: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  loading: boolean = true;
  er: boolean = false;
  error: string;
  headArray: HeadArray[] = [];
  gridArray: any[] = [];
  newRow: any;

  obs$: Subscription;
  jobData$: Subscription;
  servicesList: Observable<any>;
  dropdownServices: MenuItem[] = [];
  newValue: boolean = false;

  visibleDialog: boolean = false;
  headDialog: string;
  serviceJobInfo: ServiceJobInfo[];
  parameters: ParameterJob[];
  dateStart: Date[];
  dateEnd: Date[];
  repeat: boolean;
  frequencies: SelectItem[] = [
    { label: this.i18n.translate("Not repeat"), value: Frequency.NO_REPEAT },
    { label: this.i18n.translate("Every hour"), value: Frequency.HOUR },
    { label: this.i18n.translate("Every day"), value: Frequency.DAY },
    { label: this.i18n.translate("Every week"), value: Frequency.WEEK },
    { label: this.i18n.translate("Every month"), value: Frequency.MONTH },
    { label: this.i18n.translate("Every year"), value: Frequency.YEAR },
    { label: this.i18n.translate("Customized"), value: Frequency.CUSTOM },
  ];
  //selectedFreq: { name: string, code: string } = { name: this.i18n.translate("Not repeat"), code: Frequency.NO_REPEAT };
  cronValue: string;
  defaultStartDate: Date;
  defaultEndDate: Date;

  form: FormGroup = this.formBuilder.group({
    jobName: new FormControl({ value: null, disabled: true }, Validators.required),
    service: new FormControl({ value: null, disabled: true }, Validators.required),
    description: new FormControl(null),
    paramForm: new FormControl(),
    startDate: new FormControl(null, Validators.required),
    frequency: new FormControl(Frequency.NO_REPEAT, Validators.required),
    endDate: new FormControl({ value: null, disabled: true }),
    cronExpression: new FormControl({ value: null, disabled: true }, Validators.required)
  });

  get jobName() { return this.form.get('jobName') }
  get service() { return this.form.get('service') }
  get description() { return this.form.get('description') }
  get paramForm() { return this.form.get('paramForm') }
  get startDate() { return this.form.get('startDate') }
  get endDate() { return this.form.get('endDate') }
  get frequency() { return this.form.get('frequency') }
  get cronExpression() { return this.form.get('cronExpression') }


  constructor(
    private i18n: I18NService,
    private route: ActivatedRoute,
    private plannerService: PlannerService,
    private formBuilder: FormBuilder,
    private msgService: MsgService,
    private destroyRef: DestroyRef
  ) {
    this._reload = new Subject<void>();
    this._reloadTrigger = new Subject<void>();

  }

  ngOnInit(): void {
    const reload = this._reload.pipe(mergeMap(() => this.plannerService.getJobDetails()));
    const reloadTrigger = this._reloadTrigger.pipe(mergeMap(() => this.plannerService.getTriggers()))
    const w$ = this.route.data.pipe(
      map((data: { obss: QrtzJobDetails[] }) => data.obss),
      mergeWith(reload)
    );
    this.obs$ =
      combineLatest(
        {
          jobs: w$,
          services: this.plannerService.getServiceList().pipe(
            tap((s) => {
              this.serviceJobInfo = s.services
              this.serviceJobInfo?.map(x => { this.dropdownServices.push({ label: this.i18n.translate(x.name), id: x.className }) })
              this.setHeadArray();
              return this.serviceJobInfo
            })
          ),
          triggers: this.plannerService.getTriggers().pipe(mergeWith(reloadTrigger))
        }).pipe(
          tap(() => {
            this.gridArray = [];
            if (this.newRow) this.gridArray.push(this.newRow);
          }),
          map(({ jobs, services, triggers }) => {
            let serviceJobInfo: ServiceJobInfo[] = services.services;

            jobs.map((job) => {
              let planned: boolean = triggers.filter(x => x.jobName == job.jobName).length > 0

              this.gridArray.push({
                jobName: job.jobName,
                jobClassName: job.jobClassName,
                jobClassNameDesc: this.i18n.translate(serviceJobInfo?.find(x => x.className == job.jobClassName)?.name),
                jobData: job.jobData,
                serviceKey: serviceJobInfo?.find(x => x.className == job.jobClassName)?.key,
                description: job.description,

                variableGridArray: {
                  id: job.jobName,
                  updated: false,
                  buttonDetails: true,
                  inputLabeldata: true,
                  inputLabelNumber: true,
                  inputNotes: true,
                  outputData: true,
                  inputNew: false,
                  dropdownData: true,
                  stateSeverity: (planned) ? TagSeverity.SUCCESS : TagSeverity.WARNING,
                  stateValue: (planned) ? this.i18n.translate(JobState.PLANNED) : this.i18n.translate(JobState.TO_PLAN)
                }
              })
            });
            this.loading = false;
          })
        ).subscribe();

    this.frequency.valueChanges
      .pipe(
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe(frequencyValue => {
        if (frequencyValue !== Frequency.NO_REPEAT) {
          this.endDate.enable();
          if (frequencyValue === Frequency.CUSTOM) {
            this.cronExpression.enable();
          }
        }
        else {
          this.endDate.setValue(null);
          this.endDate.disable();
          this.cronExpression.disable();
        }
      });
  }

  setHeadArray() {
    this.headArray.push(

      { head: 'Name', fieldName: 'jobName', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, width: '20vw', readonly: true, textLength: 200 },
      { head: 'Description', fieldName: 'description', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, width: '30vw', textLength: 255 },
      {
        head: 'Service', fieldName: 'jobClassNameDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter,  required: true, width: '30vw', readonly: true,
        dropdown: {
          item: this.dropdownServices,
          clear: false,
          disableSort: false,
          loading: false,
          key: 'jobClassName'
        }
      },
      { head: 'State', fieldName: 'state', actionInput: ActionInput.null, actionOutput: ActionOutput.tag, width: '7vw', sortIcon: false },
      { head: 'Plan', fieldName: 'null', actionInput: ActionInput.null, actionOutput: ActionOutput.actionDetails, width: '5vw', content: 'center', sortIcon: false, filter: HeadFilter.null, iconDetail: 'pi pi-calendar' },

    );

  }



  delete(listGridElement) {

    if (listGridElement.length > 0) {
      listGridElement.forEach(e => {

        this.plannerService.deleteJob(e.jobName)
          .then(() => {
            this.msgService.successDeleteWithId(e.jobName);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.jobName);
            this._reload.next();
          });

      });

    }
  }

  saveAllElement(elementUpdated) {
    if (elementUpdated.length > 0) {
      elementUpdated.forEach(async e => {

        await this.plannerService.updateDescription(e.jobName, e.description)
          .then(() => {
            this.msgService.successUpdateWithId(e.jobName);
            this._reload.next()
          })
          .catch((error) => {
            this.msgService.errorWithId(error, e.jobName);
            this.er = true;
          });
      });

    }

  }

  openNew() {
    this.form.reset();
    this.parameters = null;
    this.frequency.setValue(Frequency.NO_REPEAT)
    this.newValue = true;
    this.jobName.enable();
    this.service.enable();
    this.description.enable();
    this.visibleDialog = true;
    //this.toPlan();
  }

  toPlan(item) {
    this.form.reset();

    this.jobName.disable();
    this.service.disable();
    this.description.disable();

    this.headDialog = null;
    this.parameters = null;
    this.newValue = false;
    this.error = null;
    this.visibleDialog = true;



    if (item) {
      this.headDialog = item.jobName;
      this.jobName.setValue(item.jobName);
      this.service.setValue(item.jobClassName);
      this.description.setValue(item.description);

      let params = this.serviceJobInfo.find(x => x.key == item.serviceKey).parameters;
      let paramForm = {}

      this.jobData$ = this.plannerService.getJobData(item.jobName).subscribe((x) => {
        params.forEach(par => {
          let defValue = null;
          if (x?.callbackObject != null) defValue = x.callbackObject[par.key];
          paramForm[par.key] = new FormControl(defValue, (par.required) ? Validators.required : Validators.nullValidator);

        });

        this.form.setControl("paramForm", this.formBuilder.group(paramForm));
        this.defaultStartDate = x?.startDate? new Date(x?.startDate): null;
        this.startDate.setValue(this.defaultStartDate);
        this.parameters = params;

        this.frequency.setValue(x?.frequency)
        this.cronValue = x?.cronExpression;
        this.cronExpression.setValue(this.cronValue);
        if (x?.endDate) {
          this.defaultEndDate = x?.endDate? new Date(x?.endDate) : null;
          this.endDate.setValue(this.defaultEndDate);
        }
      });
    }


  }

  serviceSelectedChanged(selectedServiceId: string) {
    let params = this.serviceJobInfo.find(x => x.className == selectedServiceId)?.parameters;
    let paramForm = {}

    params.forEach(par => {
      paramForm[par.key] = new FormControl(par.defaultValue ?? null, (par.required) ? Validators.required : Validators.nullValidator)
    })
    this.form.setControl("paramForm", this.formBuilder.group(paramForm))
    this.parameters = params;
  }

  onSubmit() {
    if (this.form.valid) {

      let cronExpression = this.getCronExpression(this.startDate.value, this.frequency.value);
      if (this.newValue) {
        let callbackObject: JobData = this.paramForm.value;

        let obj = new QrtzJobDetails(
          this.jobName.value as unknown as string,
          this.service.value as unknown as string,
          null,
          this.description.value as unknown as string);
        this.plannerService.createJob(obj, callbackObject, this.startDate.value as unknown as Date, cronExpression, this.endDate.value as unknown as Date, this.frequency.value)
          .then((result: boolean) => {
            if (result) {
              this.visibleDialog = false;
              this.msgService.successCreateWithId(obj.jobName);
              this._reload.next();
              this._reloadTrigger.next();
            }
            else {
              this.error = this.i18n.translate("Error");
            }
          }
          ).catch(error => this.error = error);

      }
      else {
        let callbackObject: JobData = this.paramForm.value;

        let obj = new QrtzJobDetails(
          this.jobName.value as unknown as string,
          this.service.value as unknown as string,
          null,
          this.description.value as unknown as string);
        this.plannerService.updateTrigger(obj, callbackObject, this.startDate.value as unknown as Date, cronExpression, this.endDate.value as unknown as Date, this.frequency.value)
          .then((result: boolean) => {
            if (result) {
              this.visibleDialog = false;
              this.msgService.successUpdateWithId(obj.jobName);
              this._reload.next();
              this._reloadTrigger.next();
            }
            else {
              this.error = this.i18n.translate("Error");
            }
          }
          ).catch(error => this.error = error);

      }
    }
  }

  getCronExpression(startDate: Date, frequency: Frequency): string {
    let cron: string = startDate.getSeconds() + " " + startDate.getMinutes();
    switch (frequency) {
      case Frequency.NO_REPEAT:
        cron += " " + startDate.getHours() + " " + startDate.getDate() + " " + (startDate.getMonth() + 1) + " " + "?" + " " + startDate.getFullYear();
        break;

      case Frequency.HOUR:
        cron += " * ? * *";
        break;

      case Frequency.DAY:
        cron += " " + startDate.getHours() + " * * ? *";
        break;

      case Frequency.WEEK:
        cron += " " + startDate.getHours() + " ? * " + (startDate.getDay() + 1) + " *";
        break;

      case Frequency.MONTH:
        cron += " " + startDate.getHours() + " " + startDate.getDate() + " * ? *";
        break;

      case Frequency.YEAR:
        cron += " " + startDate.getHours() + " " + startDate.getDate() + " " + (startDate.getMonth() + 1) + " ? *";
        break;

      case Frequency.CUSTOM:
        cron = this.form.get("cronExpression")?.value;
        break;

      default:
        cron = null;

    }
    console.log(cron);
    return cron;
  }

  ngOnDestroy(): void {
    this.obs$.unsubscribe();
    this.jobData$?.unsubscribe();
  }

  canDeactivate(): boolean {
    /*if (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0) {
      return confirm(this.i18n.translate('There are unsaved items, are you sure want to leave the page?'));
    } else {*/
    return true;
    //}
  }

}
