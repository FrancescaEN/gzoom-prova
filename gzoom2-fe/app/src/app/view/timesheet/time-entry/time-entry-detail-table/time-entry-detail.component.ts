import {
  Component,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder } from "@angular/forms";
import { Observable, Subject, Subscription, from, lastValueFrom } from "rxjs";
import { map, mergeWith, skip } from "rxjs/operators";
import { switchMap } from "rxjs/operators";
import { ConfirmationService, MenuItem, MessageService } from "primeng/api";
import { TimeEntry } from "../../../../api/model/time_entry";
import { Timesheet } from "../../../../api/model/timesheet";
import { Message } from "../../../../commons/model/message";
import { I18NService } from "../../../../i18n/i18n.service";
import { TimesheetService } from "../../../../api/service/timesheet.service";
import {
  HeadArray,
  HeadFilter,
  ActionInput,
  ActionOutput,
} from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { bodyBarArray, UnitType } from "app/layout/information-bar/bodyBarArray";
import { CanComponentDeactivate } from "../../../../shared/can-deactivate.guard";
import { WorkEffort } from "app/api/model/work-effort";
import { Message as MessageError } from "primeng/api";
import { RateTypeService } from "app/api/service/rate-type.service";
import { Location } from "@angular/common";
import { LanguageService } from "app/api/service/language.service";
import { DataForButton } from "app/layout/toolbar-data-table/toolbar-data-table-configuration";
import { ToolbarService } from "app/commons/service/toolbar.service";

@Component({
  selector: "app-time-entry-detail",
  templateUrl: "./time-entry-detail.component.html",
  styleUrls: ["./time-entry-detail.component.scss"],
})
export class TimeEntryDetailComponent implements OnInit, CanComponentDeactivate, OnDestroy {
  timeEntries: TimeEntry[];
  _reload: Subject<void>;
  error = "";
  msgs: Message[] = [];
  paramsTemp: any;
  context: string;
  isAdmin: boolean;
  selectedTimesheetId: string;
  selectedPartyId: string;
  selectedEffortUomId: string;
  tempWorkEffortName: any[] = [];
  staticWorkEffortName: any[] = [];
  selectedTimesheet: Timesheet = new Timesheet();
  period: string;
  employmentAmount: string;
  subject: string;
  totalPlanHours: number = 0;
  totalFinalHours: number = 0;
  timePercentage: number = 0;
  status: string;
  params: any = {
    managePlan: "",
    showReference: "",
    timeentryMapFormat: "",
    hasRateTypeList: "",
    hoursPercentage: "",
    showComments: "N",
    showOrderId: "N",
    showJobId: "N",
  };
  gridArray: TimeEntry[] = [];
  tempGridArray: any[] = [];
  buttonSave: boolean = false;
  buttonValidation: DataForButton = {
    booleanShow: false,
    titleLabel: this.i18nService.translate("Check Completed"),
    icon: "pi pi-check-circle",
  };

  buttonValidationReopen: DataForButton = {
    booleanShow: false,
    titleLabel: this.i18nService.translate("Timesheet reopen"),
    icon: "pi pi-check-circle",
  };

  timesheetServiceAdminSubscription: Subscription;
  timesheetServiceSubscription: Subscription;
  timesheetServiceTimeEntrySubscription: Subscription;
  timesheetServiceWorkEffortSubscription: Subscription;
  rateTypeSubscription: Subscription;

  dataTable: any;
  editingKeyId: string;
  newTimeEntry: boolean = false;
  elementToAdd: TimeEntry;
  selectionTimeEntrys: TimeEntry[] = [];
  workEfforts: MenuItem[] = [];
  updatable: boolean;
  buttonDelete: boolean;
  buttonNew: boolean = true;
  selectedOn: boolean;
  control: boolean;
  lastChoiceDropdown: string;
  messagesError: MessageError[] = [];
  rateTypeArray: MenuItem[] = [];
  defaultRate: any = {
    description: null,
    id: null,
  };
  loading: boolean = false;
  secondaryLang: boolean;

  backLink = '../../'

  bodyBarArray: bodyBarArray[] = [];

  headArray: HeadArray[] = [
    {
      head: "",
      fieldName: "id",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      display: "none",
    },
  ];

  constructor(
    private readonly timesheetService: TimesheetService,
    private readonly rateTypeService: RateTypeService,
    private readonly confirmationService: ConfirmationService,
    private readonly route: ActivatedRoute,
    public readonly i18nService: I18NService,
    private messageService: MessageService,
    private languageService: LanguageService,
    private readonly router: Router,
    private _location: Location,
    private toolbarService: ToolbarService,
  ) {
    this._reload = new Subject<void>();
  }

  ngOnDestroy(): void {
    this.timesheetServiceAdminSubscription?.unsubscribe();
    this.timesheetServiceSubscription?.unsubscribe();
    this.timesheetServiceWorkEffortSubscription?.unsubscribe();
    this.timesheetServiceTimeEntrySubscription?.unsubscribe();
    this.rateTypeSubscription?.unsubscribe();
  }

  async ngOnInit() {
    this.loading = true;
    this.collapseSidebar();
    this.toolbarService.setDisabledNew(false);
    this.toolbarService.setDisabledDelete(false);
    this.secondaryLang = await this.languageService.secondaryLang();
    this.context = this.route.parent.snapshot.data.context;

    this.timesheetServiceAdminSubscription = this.timesheetService.isAdmin(this.context).subscribe({
      next: (data) => {
        this.isAdmin = data;
      },
      error: (error) => {
        console.log("error", error);
        this.messagesError = [
          { severity: "error", summary: "Error", detail: error.message },
        ];
      },
    });

    const reloadedTimeEntries = this._reload.pipe(
      switchMap(() =>
        this.timesheetService.timeEntries(this.selectedTimesheetId)
      )
    );

    const timeEntryObs = this.route.data.pipe(
      map((data: { timeEntries: TimeEntry[] }) => data.timeEntries),
      mergeWith(reloadedTimeEntries)
    );

    // this.timesheetServiceTimeEntrySubscription = this.route.paramMap
    //   .pipe(
    //     switchMap((params) => {
    //       this.selectedTimesheetId = params.get("id");
    //       return this.timesheetService.timesheetTimeEntry(
    //         this.selectedTimesheetId
    //       );
    //     })
    //   )
    //   .subscribe({
    //     next: async (data) => {
    //       this.selectedTimesheet = data;
    //       this.selectedPartyId = this.selectedTimesheet.party["partyId"];
    //       this.selectedEffortUomId = this.selectedTimesheet.effortUomId;

    //       if (
    //         this.selectedTimesheet.statusId == "TIMESHEET_IN_PROCESS" &&
    //         this.selectedTimesheet.tsByUserLogin["updateable"] == "Y"
    //       ) {
    //         this.buttonValidation.booleanShow = true;
    //       }

    //       if (
    //         this.selectedTimesheet.statusId == "TIMESHEET_COMPLETED" &&
    //         this.isAdmin
    //       ) {
    //         this.buttonValidationReopen.booleanShow = true;
    //       }

    //       if (this.selectedTimesheet.tsByUserLogin["updateable"] == "N") {
    //         this.updatable = false;
    //         this.buttonDelete = false;
    //         this.buttonNew = false;
    //         this.selectedOn = false;
    //         this.headArray.push({
    //           head: this.i18nService.translate("workEffortName"),
    //           fieldName: "workEffortName",
    //           actionInput: ActionInput.dropdownData,
    //           actionOutput: ActionOutput.outputLabelData,
    //           dropdown: {
    //             item: this.workEfforts,
    //             clear: false,
    //             key: "workEffortId",
    //           },
    //           filter: HeadFilter.textFilter,
    //           width: "30vw",
    //           required: true,
    //         });
    //       } else {
    //         this.updatable = true;
    //         this.buttonDelete = false;
    //         this.buttonNew = true;
    //         this.selectedOn = true;
    //         this.headArray.push({
    //           head: this.i18nService.translate("workEffortName"),
    //           fieldName: "workEffortName",
    //           actionInput: ActionInput.dropdownData,
    //           actionOutput: ActionOutput.outputLabelData,
    //           dropdown: {
    //             item: this.workEfforts,
    //             clear: false,
    //             key: "workEffortId",
    //           },
    //           filter: HeadFilter.textFilter,
    //           width: "30vw",
    //           required: true,
    //         });
    //       }



    //       this.paramsTemp = this.timesheetService.params(
    //         this.selectedTimesheetId
    //       );

    //       await this.paramsTemp.forEach((element) => {
    //         element[0].noteInfo.split(";").forEach((item) => {
    //           if (item.split("=")[0] != "") {
    //             var itemLeft = item.split("=")[0].replace(/\s/g, "");

    //             if (!!item.split("=")[1] && item.split("=")[1].includes('"')) {
    //               switch (itemLeft) {
    //                 case "managePlan":
    //                   this.params.managePlan = item.split("=")[1].split('"')[1];
    //                   break;
    //                 case "showReference":
    //                   this.params.showReference = item
    //                     .split("=")[1]
    //                     .split('"')[1];
    //                   break;
    //                 case "timeentryMapFormat":
    //                   this.params.timeentryMapFormat = item
    //                     .split("=")[1]
    //                     .split('"')[1];
    //                   break;
    //                 case "hasRateTypeList":
    //                   this.params.hasRateTypeList = item
    //                     .split("=")[1]
    //                     .split('"')[1];
    //                   break;
    //                 case "rateTtypeList":
    //                   this.params.rateTtypeList = item
    //                     .split("=")[1]
    //                     .split('"')[1];
    //                   break;
    //                 case "hoursPercentage":
    //                   this.params.hoursPercentage = item
    //                     .split("=")[1]
    //                     .split('"')[1];
    //                   break;
    //                 case "showComments":
    //                   this.params.showComments = item
    //                     .split("=")[1]
    //                     .split('"')[1];
    //                   break;
    //                 case "showOrderId":
    //                   this.params.showOrderId = item
    //                     .split("=")[1]
    //                     .split('"')[1];
    //                   break;
    //                 case "showJobId":
    //                   this.params.showJobId = item.split("=")[1].split('"')[1];
    //                   break;
    //               }
    //             } else {
    //               switch (itemLeft) {
    //                 case "managePlan":
    //                   this.params.managePlan = item.split("=")[1].split("'")[1];
    //                   break;
    //                 case "showReference":
    //                   this.params.showReference = item
    //                     .split("=")[1]
    //                     .split("'")[1];
    //                   break;
    //                 case "timeentryMapFormat":
    //                   this.params.timeentryMapFormat = item
    //                     .split("=")[1]
    //                     .split("'")[1];
    //                   break;
    //                 case "hasRateTypeList":
    //                   this.params.hasRateTypeList = item
    //                     .split("=")[1]
    //                     .split("'")[1];
    //                   break;
    //                 case "rateTtypeList":
    //                   this.params.rateTtypeList = item
    //                     .split("=")[1]
    //                     .split("'")[1];
    //                   break;
    //                 case "hoursPercentage":
    //                   this.params.hoursPercentage = item
    //                     .split("=")[1]
    //                     .split("'")[1];
    //                   break;
    //                 case "showComments":
    //                   this.params.showComments = item
    //                     .split("=")[1]
    //                     .split("'")[1];
    //                   break;
    //                 case "showOrderId":
    //                   this.params.showOrderId = item
    //                     .split("=")[1]
    //                     .split("'")[1];
    //                   break;
    //                 case "showJobId":
    //                   this.params.showJobId = item.split("=")[1].split("'")[1];
    //                   break;
    //               }
    //             }
    //             this.params.glFiscalTypeEnumId =
    //               this.selectedTimesheet.workEffortTypePeriod[
    //               "glFiscalTypeEnumId"
    //               ];
    //           }
    //         });
    //       });

    //       await this.createBodyBar()

    //       if (
    //         this.params.timeentryMapFormat == "DDM" &&
    //         window.location.href.includes("/M/")
    //       ) {
    //         this._location.back();
    //       }

    //       if (this.params.hasRateTypeList == "Y") {
    //         this.headArray.push({
    //           head: this.i18nService.translate("rateTypeIdDescription"),
    //           fieldName: "rateTypeIdDescription",
    //           actionInput: ActionInput.dropdownData,
    //           actionOutput: ActionOutput.outputLabelData,
    //           dropdown: {
    //             item: this.rateTypeArray,
    //             clear: false,
    //             key: "rateTypeId"
    //           },
    //           filter: HeadFilter.textFilter,
    //           width: "10%",
    //           sortIcon: true,
    //           content: "left",
    //           required: true,
    //         });
    //       }

    //       if (this.params.managePlan == "Y") {
    //         this.bodyBarArray.push({ head: (this.params.hoursPercentage == 'P' ? "totalPlanHoursPercentage" : "totalPlanHours"), label: 0, unit: this.params.hoursPercentage == 'P' ? UnitType.PERCENTAGE : null });
    //         if (this.updatable) {
    //           this.headArray.push({
    //             head: this.params.hoursPercentage == 'P' ? this.i18nService.translate("planHoursPercent") : this.i18nService.translate("planHours"),
    //             fieldName: "planHours",
    //             actionInput: this.params.hoursPercentage == 'P' ? ActionInput.inputLabelPercentDecimalNumber : ActionInput.inputLabelDecimalNumber,
    //             actionOutput: this.params.hoursPercentage == 'P' ? ActionOutput.outputLabelPercentNumber : ActionOutput.outputLabelNumber,
    //             filter: HeadFilter.popUpFilter,
    //             width: "10%",
    //             sortIcon: true,
    //             content: "center",
    //           });
    //         } else {
    //           this.headArray.push({
    //             head: this.params.hoursPercentage == 'P' ? this.i18nService.translate("planHoursPercent") : this.i18nService.translate("planHours"),
    //             fieldName: "planHours",
    //             actionInput: ActionInput.outputData,
    //             actionOutput: this.params.hoursPercentage == 'P' ? ActionOutput.outputLabelPercentNumber : ActionOutput.outputLabelNumber,
    //             filter: HeadFilter.popUpFilter,
    //             width: "10%",
    //             sortIcon: true,
    //             content: "center",
    //           });
    //         }
    //       }

    //       if (this.updatable) {
    //         this.headArray.push({
    //           head: this.params.hoursPercentage == 'P' ? this.i18nService.translate("actualHoursPercent") : this.i18nService.translate("actualHours"),
    //           fieldName: "hours",
    //           actionInput: this.params.hoursPercentage == 'P' ? ActionInput.inputLabelPercentDecimalNumber : ActionInput.inputLabelDecimalNumber,
    //           actionOutput: this.params.hoursPercentage == 'P' ? ActionOutput.outputLabelPercentNumber : ActionOutput.outputLabelNumber,
    //           filter: HeadFilter.popUpFilter,
    //           width: "10%",
    //           sortIcon: true,
    //           content: "center",
    //         });
    //       } else {
    //         this.headArray.push({
    //           head: this.params.hoursPercentage == 'P' ? this.i18nService.translate("actualHoursPercent") : this.i18nService.translate("actualHours"),
    //           fieldName: "hours",
    //           actionInput: ActionInput.outputData,
    //           actionOutput: this.params.hoursPercentage == 'P' ? ActionOutput.outputLabelPercentNumber : ActionOutput.outputLabelNumber,
    //           filter: HeadFilter.popUpFilter,
    //           width: "10%",
    //           sortIcon: true,
    //           content: "center",
    //         });
    //       }

    //       if (this.updatable) {
    //         this.headArray.push({
    //           head: this.i18nService.translate("Comments"),
    //           fieldName: "comments",
    //           actionInput: ActionInput.inputNotes,
    //           actionOutput: ActionOutput.outputNotes,
    //           filter: HeadFilter.textFilter,
    //         });
    //       } else {
    //         this.headArray.push({
    //           head: this.i18nService.translate("Comments"),
    //           fieldName: "comments",
    //           actionInput: ActionInput.outputData,
    //           actionOutput: ActionOutput.outputNotes,
    //           filter: HeadFilter.textFilter,
    //         });
    //       }
    //     },
    //     error: (error) => {
    //       console.log("error", error);
    //       this.messagesError = [
    //         {
    //           severity: "error",
    //           summary: "Error",
    //           detail: error.message,
    //         },
    //       ];
    //     },
    //   });

    this.timesheetServiceTimeEntrySubscription = this.route.paramMap
      .pipe(
        switchMap((params) => {
          this.selectedTimesheetId = params.get("id");
          return this.timesheetService.timesheetTimeEntry(this.selectedTimesheetId);
        })
      )
      .subscribe({
        next: async (data) => {
          this.selectedTimesheet = data;
          this.selectedPartyId = this.selectedTimesheet.party["partyId"];
          this.selectedEffortUomId = this.selectedTimesheet.effortUomId;

          const statusId = this.selectedTimesheet.statusId;
          const updateable = this.selectedTimesheet.tsByUserLogin["updateable"];

          this.buttonValidation.booleanShow = (statusId === "TIMESHEET_IN_PROCESS" && updateable === "Y");
          this.buttonValidationReopen.booleanShow = (statusId === "TIMESHEET_COMPLETED" && this.isAdmin);

          const commonHead = {
            head: this.i18nService.translate("workEffortName"),
            fieldName: "workEffortName",
            actionInput: ActionInput.dropdownData,
            actionOutput: ActionOutput.outputLabelData,
            dropdown: {
              item: this.workEfforts,
              clear: false,
              key: "workEffortId",
            },
            filter: HeadFilter.textFilter,
            width: "30vw",
            required: true,
          };

          if (updateable === "N") {
            this.updatable = false;
            this.buttonDelete = false;
            this.buttonNew = false;
            this.selectedOn = false;
          } else {
            this.updatable = true;
            this.buttonDelete = false;
            this.buttonNew = true;
            this.selectedOn = true;
          }

          this.headArray.push(commonHead);

          this.paramsTemp = this.timesheetService.params(this.selectedTimesheetId);

          await this.paramsTemp.forEach((element) => {
            element[0].noteInfo.split(";").forEach((item) => {
              const [rawKey, rawValue] = item.split("=");
              if (!rawKey) return;

              const key = rawKey.replace(/\s/g, "");
              const value =
                rawValue?.includes('"')
                  ? rawValue.split('"')[1]
                  : rawValue?.split("'")[1];

              switch (key) {
                case "managePlan":
                  this.params.managePlan = value;
                  break;
                case "showReference":
                  this.params.showReference = value;
                  break;
                case "timeentryMapFormat":
                  this.params.timeentryMapFormat = value;
                  break;
                case "hasRateTypeList":
                  this.params.hasRateTypeList = value;
                  break;
                case "rateTtypeList":
                  this.params.rateTtypeList = value;
                  break;
                case "hoursPercentage":
                  this.params.hoursPercentage = value;
                  break;
                case "showComments":
                  this.params.showComments = value;
                  break;
                case "showOrderId":
                  this.params.showOrderId = value;
                  break;
                case "showJobId":
                  this.params.showJobId = value;
                  break;
              }

              this.params.glFiscalTypeEnumId = this.selectedTimesheet.workEffortTypePeriod["glFiscalTypeEnumId"];

            });
          });

          this.createBodyBar();

          if (
            this.params.timeentryMapFormat === "DDM" &&
            window.location.href.includes("/M/")
          ) {
            this._location.back();
          }

          if (this.params.hasRateTypeList === "Y") {
            this.headArray.push({
              head: this.i18nService.translate("rateTypeIdDescription"),
              fieldName: "rateTypeIdDescription",
              actionInput: ActionInput.dropdownData,
              actionOutput: ActionOutput.outputLabelData,
              dropdown: {
                item: this.rateTypeArray,
                clear: true,
                key: "rateTypeId",
              },
              filter: HeadFilter.textFilter,
              width: "10%",
              sortIcon: true,
              content: "left",
              required: true,
            });
          }

          const isPercentage = this.params.hoursPercentage === "P";

          if (this.params.managePlan === "Y") {
            this.bodyBarArray.push({
              head: isPercentage ? "totalPlanHoursPercentage" : "totalPlanHours",
              label: 0,
              unit: isPercentage ? UnitType.PERCENTAGE : null,
            });

            this.headArray.push({
              head: this.i18nService.translate(
                isPercentage ? "planHoursPercent" : "planHours"
              ),
              fieldName: "planHours",
              actionInput: this.params.glFiscalTypeEnumId == null
                ? (this.updatable
                  ? isPercentage
                    ? ActionInput.inputLabelPercentDecimalNumber
                    : ActionInput.inputLabelDecimalNumber
                  : ActionInput.outputData)
                : (this.params.glFiscalTypeEnumId === "GLFISCTYPE_TARGET"
                  ? (isPercentage
                    ? ActionInput.inputLabelPercentDecimalNumber
                    : ActionInput.inputLabelDecimalNumber)
                  : ActionInput.outputData),
              actionOutput: isPercentage
                ? ActionOutput.outputLabelPercentNumber
                : ActionOutput.outputLabelNumber,
              filter: HeadFilter.popUpFilter,
              width: "10%",
              sortIcon: true,
              content: "center",
            });
          }

          this.headArray.push({
            head: this.i18nService.translate(
              isPercentage ? "actualHoursPercent" : "actualHours"
            ),
            fieldName: "hours",
            actionInput: this.params.glFiscalTypeEnumId == null
              ? (this.updatable
                ? isPercentage
                  ? ActionInput.inputLabelPercentDecimalNumber
                  : ActionInput.inputLabelDecimalNumber
                : ActionInput.outputData)
              : (this.params.glFiscalTypeEnumId === "GLFISCTYPE_ACTUAL"
                ? (isPercentage
                  ? ActionInput.inputLabelPercentDecimalNumber
                  : ActionInput.inputLabelDecimalNumber)
                : ActionInput.outputData),
            actionOutput: isPercentage
              ? ActionOutput.outputLabelPercentNumber
              : ActionOutput.outputLabelNumber,
            filter: HeadFilter.popUpFilter,
            width: "10%",
            sortIcon: true,
            content: "center",
          });

          this.headArray.push({
            head: this.i18nService.translate("Comments"),
            fieldName: "comments",
            actionInput: this.updatable
              ? ActionInput.inputNotes
              : ActionInput.outputData,
            actionOutput: ActionOutput.outputNotes,
            filter: HeadFilter.textFilter,
          });
        },
        error: (error) => {
          console.log("error", error);
          this.messagesError = [
            {
              severity: "error",
              summary: "Error",
              detail: error.message,
            },
          ];
        },
      });


    this.timesheetServiceWorkEffortSubscription = this.route.paramMap
      .pipe(
        switchMap((params) => {
          this.selectedTimesheetId = params.get("id");
          return this.timesheetService.workEfforts(
            this.selectedTimesheetId,
            this.secondaryLang
          );
        })
      )
      .subscribe({
        next: (data) => {
          data.forEach((element, index) => {
            if (
              this.workEfforts.filter((x) => x.id === element.workEffortId)
                .length == 0
            ) {
              this.workEfforts.push({
                label: this.secondaryLang
                  ? element.workEffortNameLang
                  : element.workEffortName,
                id: element.workEffortId,
              });
            }
          });
        },
        error: (error) => {
          console.log("error", error);
          this.messagesError = [
            {
              severity: "error",
              summary: "Error",
              detail: error.message,
            },
          ];
        },
      });

    this.timesheetServiceSubscription = timeEntryObs.subscribe({
      next: (data) => {
        if (data) {
          this.gridArray = [];
          this.timeEntries = data;
          this.totalPlanHours = 0;
          this.totalFinalHours = 0;

          this.timeEntries.forEach((element, index) => {
            this.totalPlanHours += element.planHours;
            this.totalFinalHours += element.hours;
            this.gridArray.push({
              idNumber: index,
              timeEntryId: element.timeEntryId,
              workEffortId: element.workEffortId,
              workEffortName: this.secondaryLang
                ? element.workEffort["workEffortNameLang"]
                : element.workEffort["workEffortName"],
              hours: element.hours,
              planHours: element.planHours,
              comments: element.comments,
              timesheetId: element.timesheetId,
              rateTypeId: element.rateType.rateTypeId,
              rateTypeIdDescription: this.secondaryLang ? element.rateType.descriptionLang : element.rateType.description,
              variableGridArray: {
                id: element.timeEntryId,
                buttonDetails: false,
                dropdownData: false,
                inputLabeldata: false,
                inputLabelDecimalNumber: true,
                inputLabelPercentDecimalNumber: true,
                inputNotes: true,
                outputData: true,
                maxFractionDigits: 1,
                minFractionDigits: 1,
              },
            });
          });
        }
        this.loading = false;


      },
      error: (error) => {
        console.log("error", error);
        this.messagesError = [
          {
            severity: "error",
            summary: "Error",
            detail: error.message,
          },
        ];
      },
    });


    setTimeout(() => {
      this.reloadInfoBar();
    }, 500);

    this.tempGridArray = this.gridArray;
  }

  createBodyBar() {
    this.bodyBarArray = [
      {
        head: "Period",
        label: this.secondaryLang
          ? this.selectedTimesheet.customTimePeriod["periodNameLang"]
          : this.selectedTimesheet.customTimePeriod["periodName"]
      },
      {
        head: "Subject",
        label: this.selectedTimesheet.party["partyName"]
      },
      {
        head: "PartyStructure",
        label: this.secondaryLang
          ? this.selectedTimesheet.partyStructure["partyNameLang"]
          : this.selectedTimesheet.partyStructure["partyName"]
      },
      {
        head: "Status",
        label: this.secondaryLang
          ? this.selectedTimesheet.statusItem["descriptionLang"]
          : this.selectedTimesheet.statusItem["description"]
      },
      {
        head: "employmentAmount",
        label: this.selectedTimesheet.partyHistoryView["employmentAmount"],
        unit: UnitType.PERCENTAGE
      },
      {
        head: this.params.hoursPercentage == 'P' ? "totalFinalHoursPercentage" : "totalFinalHours",
        label: 0,
        unit: this.params.hoursPercentage == 'P' ? UnitType.PERCENTAGE : null,
      }
    ]
  }

  canDeactivate(): Observable<boolean> | boolean {
    return this.buttonSave;
  }

  notifyInputChanges(id) {
    this.gridArray.forEach((x) => {
      if (x.variableGridArray.id == id) {
        x.variableGridArray.updated = true;
      }
    });
    setTimeout(() => {
      this.buttonSave = true;
    }, 0);

    if (
      this.gridArray[0].variableGridArray.id.includes("new") &&
      !!this.gridArray[0].workEffortId
    ) {
      this.reloadRateType(
        this.gridArray[0].workEffortId,
        this.selectedTimesheet.workEffortId
      );
    }
  }

  shareDescriptorTable(data) {
    this.dataTable = data;
  }

  shareSelectionItem(data) {
    this.selectionTimeEntrys = data;
    if (this.selectionTimeEntrys.length == 0) {
      setTimeout(() => {
        this.buttonDelete = false;
      }, 0);
    } else {
      setTimeout(() => {
        this.buttonDelete = true;
      }, 0);
    }
  }

  selectedItemDropdown(item) {
    if (typeof item === "string" && this.control) {
      let tmp = this.gridArray;
      tmp.forEach((x) => {
        if (x.variableGridArray.id == item)
          x.workEffortName = this.lastChoiceDropdown;
      });
      this.gridArray = tmp;
    } else {
      this.lastChoiceDropdown = item;
      this.control = true;
    }
  }

  saveAllElement() {
    this.duplicateManage();

    if (this.controlTotalPercent(this.gridArray)) {

      if (this.validGridArray(this.gridArray)) {
        this.gridArray.forEach((element) => {
          if (element.hours == null) {
            element.hours = 0;
          }
          if (element.planHours == null) {
            element.planHours = 0;
          }

          element.variableGridArray.dropdownData = false;
        });
      }

      if (
        this.validGridArray(
          this.gridArray.filter((x) => x.variableGridArray.updated)
        )
      ) {
        if (
          this.gridArray.filter((x) => x.variableGridArray.id.includes("new"))
            .length > 0
        ) {
          this.gridArray
            .filter((x) => x.variableGridArray.id.includes("new"))
            .forEach(
              async (x) =>
                await this.timesheetService
                  .createTimeEntry(x)
                  .then(() => {
                    this.reloadInfoBar();
                  })
                  .catch((error) => {
                    console.log("error", error);
                    this.messagesError = [
                      {
                        severity: "error",
                        summary: "Error",
                        detail: error + " [" + x.timeEntryId + "]",
                      },
                    ];
                  })
            );
        }

        if (
          this.gridArray.filter(
            (x) =>
              x.variableGridArray.updated == true &&
              !x.variableGridArray.id.includes("new") &&
              x.hours != null
          ).length > 0
        ) {
          this.gridArray
            .filter(
              (x) =>
                x.variableGridArray.updated == true &&
                !x.variableGridArray.id.includes("new")
            )
            .forEach(
              async (x) =>
                await this.timesheetService
                  .updateTimeEntry(x)
                  .then(() => {
                    this.reloadInfoBar();
                  })
                  .catch((error) => {
                    console.log("error", error);
                    this.messagesError = [
                      {
                        severity: "error",
                        summary: "Error",
                        detail: error + " [" + x.timeEntryId + "]",
                      },
                    ];
                  })
            );
        }

        if (
          this.gridArray.filter(
            (x) =>
              x.variableGridArray.updated == true &&
              !x.variableGridArray.id.includes("new") &&
              x.hours == null
          ).length > 0
        ) {
          let idToRemove = this.gridArray.filter(
            (x) =>
              x.variableGridArray.updated == true &&
              !x.variableGridArray.id.includes("new") &&
              x.hours == null
          )[0].timeEntryId;

          let arrayIdTimesheed = [];

          arrayIdTimesheed.push(idToRemove);
          this.timesheetService
            .deleteTimeEntry(arrayIdTimesheed)
            .then((data) => {
              this._reload.next();
            })
            .catch((error) => {
              console.log("error", error.message);
              this.messagesError = [
                {
                  severity: "error",
                  summary: "Error",
                  detail: error.message,
                },
              ];
            });
        }

        setTimeout(() => {
          this.reload();
        }, 500);
      } else {
        this.messagesError = [
          {
            severity: "error",
            summary: "Error",
            detail: this.i18nService.translate(
              "All mandatory fields must be filled in"
            ),
          },
        ];
      }
    } else {
      this.messagesError = [
        {
          severity: "error",
          summary: "Error",
          detail: this.i18nService.translate(
            "The total number of hours exceeds 100%."
          ),
        },
      ];
    }


  }

  reload() {
    setTimeout(() => {
      this.buttonSave = false;
    }, 0);
    this._reload.next();
  }

  reloadInfoBar() {
    this.bodyBarArray.forEach((element) => {
      if (element.head == (this.params.hoursPercentage == 'P' ? "totalPlanHoursPercentage" : "totalPlanHours")) {
        element.label = 0;
        this.gridArray.forEach((e) => {
          element.label += e.planHours;
        });
      }
      if (element.head == (this.params.hoursPercentage == 'P' ? "totalFinalHoursPercentage" : "totalFinalHours")) {
        element.label = 0;
        this.gridArray.forEach((e) => {
          element.label += e.hours;
        });
      }
    });
  }

  async reloadRateType(workEffortId: string, workEffortIdTimesheet: string) {
    const rateTypeSubscription$ = this.rateTypeService.rateTypesWorkEffortId(workEffortId);

    await lastValueFrom(rateTypeSubscription$).then(data => {
      data.forEach((element) => {
        if (
          this.rateTypeArray.filter((x) => x.id === element.rateTypeId)
            .length == 0
        ) {
          this.rateTypeArray.push({
            label: this.secondaryLang
              ? element.rateType.descriptionLang
              : element.rateType.description,
            id: element.rateTypeId,
          });
          if (element.rate == 1) {
            this.defaultRate.description = this.secondaryLang
              ? element.rateType.descriptionLang
              : element.rateType.description;
            this.defaultRate.id = element.rateTypeId;
            this.gridArray[0].rateTypeId = element.rateTypeId;
            this.gridArray[0].rateTypeIdDescription = this.secondaryLang
              ? element.rateType.descriptionLang
              : element.rateType.description;
          }
        }
      });
    })

    if (this.rateTypeArray.length == 0) {

      const rateTypeSubscription$ = this.rateTypeService.rateTypesWorkEffortId(workEffortIdTimesheet);

      await lastValueFrom(rateTypeSubscription$).then(data => {
        data.forEach((element) => {
          if (
            this.rateTypeArray.filter((x) => x.id === element.rateTypeId)
              .length == 0
          ) {
            this.rateTypeArray.push({
              label: this.secondaryLang
                ? element.rateType.descriptionLang
                : element.rateType.description,
              id: element.rateTypeId,
            });
            if (element.rate == 1) {
              this.defaultRate.description = this.secondaryLang
                ? element.rateType.descriptionLang
                : element.rateType.description;
              this.defaultRate.id = element.rateTypeId;
              this.gridArray[0].rateTypeId = element.rateTypeId;
              this.gridArray[0].rateTypeIdDescription = this.secondaryLang
                ? element.rateType.descriptionLang
                : element.rateType.description;
            }
          }
        });
      })
    }
  }

  saveSelfElement(timeEntries: TimeEntry[]) {
    this.duplicateManage();

    if (
      this.validGridArray(
        this.gridArray.filter((x) => x.variableGridArray.updated)
      )
    ) {
      if (
        this.gridArray.filter((x) => x.variableGridArray.id.includes("new"))
          .length > 0
      ) {
        this.gridArray
          .filter((x) => x.variableGridArray.id.includes("new"))
          .forEach(async (x) => {
            if (x.hours == null) {
              x.hours = 0;
            }
            if (x.planHours == null) {
              x.planHours = 0;
            }
            await this.timesheetService
              .createTimeEntry(x)
              .then(() => {
                this.reloadInfoBar();
                this.msgs = [
                  {
                    severity: this.i18nService.translate("info"),
                    summary: this.i18nService.translate("Confirmed"),
                    detail:
                      this.i18nService.translate("New item added") +
                      " [" +
                      x.timeEntryId +
                      "]",
                  },
                ];
                this.messageService.add({
                  severity: "success",
                  summary: "Success",
                  detail: this.msgs[0].detail,
                });
              })
              .catch((error) => {
                console.log("error", error);
                this.messagesError = [
                  {
                    severity: "error",
                    summary: "Error",
                    detail: error + " [" + x.timeEntryId + "]",
                  },
                ];
              });
          });
      }

      if (
        this.gridArray.filter(
          (x) =>
            x.variableGridArray.updated == true &&
            !x.variableGridArray.id.includes("new")
        ).length > 0
      ) {
        this.gridArray
          .filter(
            (x) =>
              x.variableGridArray.updated == true &&
              !x.variableGridArray.id.includes("new")
          )
          .forEach(
            async (x) =>
              await this.timesheetService
                .updateTimeEntry(x)
                .then(() => {
                  this.reloadInfoBar();
                  this.msgs = [
                    {
                      severity: this.i18nService.translate("info"),
                      summary: this.i18nService.translate("Confirmed"),
                      detail:
                        this.i18nService.translate("Update Confirmation") +
                        " [" +
                        x.timeEntryId +
                        "]",
                    },
                  ];
                  this.messageService.add({
                    severity: "success",
                    summary: "Success",
                    detail: this.msgs[0].detail,
                  });
                })
                .catch((error) => {
                  console.log("error", error);
                  this.messagesError = [
                    {
                      severity: "error",
                      summary: "Error",
                      detail: error + " [" + x.timeEntryId + "]",
                    },
                  ];
                })
          );
      }
    } else {
      this.messagesError = [
        {
          severity: "error",
          summary: "Error",
          detail: this.i18nService.translate(
            "All mandatory fields must be filled in"
          ),
        },
      ];
    }
  }

  async openNew() {

    await this.reloadRateType(null, this.selectedTimesheet.workEffortId);

    if (!this.validGridArray(this.gridArray)) {
      this.messagesError = [
        {
          severity: "error",
          summary: "Error",
          detail: this.i18nService.translate(
            "All mandatory fields must be filled in"
          ),
        },
      ];
    } else {
      this.saveSelfElement(this.gridArray);
      this.gridArray.forEach((element) => {
        element.variableGridArray.dropdownData = false;
      });

      if (this.workEfforts.length > 0) {
        this.newTimeEntry = true;
        this.elementToAdd = {
          idNumber: 0,
          workEffortName: "",
          timesheetId: this.selectedTimesheetId,
          partyId: this.selectedTimesheet.party["partyId"],
          effortUomId: this.selectedEffortUomId,
          rateTypeId: this.defaultRate.id,
          rateTypeIdDescription: this.defaultRate.description,
          hours: null,
          planHours: null,
          fromDate: this.selectedTimesheet.fromDate,
          thruDate: this.selectedTimesheet.thruDate,
          comments: "",
          variableGridArray: {
            id: "new" + Math.random(),
            buttonDetails: false,
            dropdownData: true,
            inputLabeldata: false,
            inputLabelDecimalNumber: true,
            inputLabelPercentDecimalNumber: true,
            inputNotes: true,
            outputData: true,
            maxFractionDigits: 1,
            minFractionDigits: 1,
          },
        };

        this.gridArray = [this.elementToAdd, ...this.gridArray];
        this.editingKeyId = this.elementToAdd.variableGridArray.id;
        this.dataTable.editingRowKeys = {
          [this.editingKeyId]: true,
        };
      } else {
        this.messagesError = [
          {
            severity: "error",
            summary: "Error",
            detail: this.i18nService.translate("There are no goals to show"),
          },
        ];
      }
    }
  }

  deleteRowSelected() {
    if (this.selectionTimeEntrys.length > 0) {
      let itemToSave = [];
      this.gridArray.forEach((element) => {
        if (
          element.variableGridArray.id.substring(0, 3) == "new" &&
          !this.selectionTimeEntrys.includes(element)
        ) {
          itemToSave.push(element);
        }
      });

      if (itemToSave.length > 0) {
        this.saveSelfElement(itemToSave);
      }

      this.confirmationService.confirm({
        message: this.i18nService.translate(
          "Are you sure you want to proceed with the deletion?"
        ),
        header: this.i18nService.translate("Attention"),
        icon: "pi pi-question",
        accept: () => {
          let arrayIdTimesheed = [];
          this.selectionTimeEntrys.forEach((element) => {
            arrayIdTimesheed.push(element.variableGridArray.id);
            this.totalFinalHours = this.totalFinalHours - element.hours;
          });
          this.bodyBarArray.forEach((element) => {
            if (element.head == (this.params.hoursPercentage == 'P' ? "totalPlanHoursPercentage" : "totalPlanHours")) {
              element.label = this.totalPlanHours;
            }
            if (element.head == (this.params.hoursPercentage == 'P' ? "totalFinalHoursPercentage" : "totalFinalHours")) {
              element.label = this.totalFinalHours;
            }
          });
          this.selectionTimeEntrys = [];
          this.timesheetService
            .deleteTimeEntry(arrayIdTimesheed)
            .then((data) => {
              this.msgs = [
                {
                  severity: this.i18nService.translate("info"),
                  summary: this.i18nService.translate("Confirmed"),
                  detail: this.i18nService.translate("Delete confirmation"),
                },
              ];
              this.messageService.add({
                severity: "success",
                summary: "Success",
                detail: this.msgs[0].detail,
              });
              this._reload.next();
            })
            .catch((error) => {
              console.log("error", error.message);
              this.messagesError = [
                {
                  severity: "error",
                  summary: "Error",
                  detail: error.message,
                },
              ];
            });
        },
      });
    } else {
      this.messagesError = [
        {
          severity: "error",
          summary: "Error",
          detail: this.i18nService.translate("Please select a line"),
        },
      ];
    }
  }

  collapseSidebar() {
    const dom: any = document.querySelector("body");
    const menu: any = document.querySelector("#sidebar");
    dom.classList.add("push-right");
    menu.classList.add("collapse");
  }

  controlTotalPercent(array: TimeEntry[]): boolean {
    var totalFinalHours = 0;
    var totalPlanHours = 0;
    for (const element of array) {
      if (this.params.hoursPercentage == 'P') {
        if (element.hours > 100 || element.planHours > 100) {
          return false;
        }
        totalFinalHours += element.hours;
        totalPlanHours += element.planHours;
        if (totalFinalHours > 100 || totalPlanHours > 100) {
          return false;
        }
      }
    }
    return true;
  }

  validGridArray(array: TimeEntry[]): boolean {
    var control = 0;
    array.forEach((element) => {

      if (
        element.workEffortName == "" ||
        element.workEffortName == null ||
        element.rateTypeIdDescription == "" ||
        element.rateTypeIdDescription == null ||
        element.hours < 0 ||
        element.planHours < 0
      ) {
        control = 1;
      }
    });

    if (control == 0) {
      return true;
    } else {
      return false;
    }
  }

  duplicateManage() {
    let te: TimeEntry = this.gridArray.filter((x) =>
      x.variableGridArray.id.includes("new")
    )[0];
    if (te) {
      // somma delle ore
      let control = false;
      this.gridArray.forEach((x) => {
        if (
          x.workEffortId == te.workEffortId &&
          x.rateTypeId == te.rateTypeId &&
          !x.variableGridArray.id.includes("new")
        ) {
          x.variableGridArray.updated = false;
          control = true;
        }
      });
      if (control) {
        let dateMsg = new Date(te.fromDate);
        this.gridArray.shift();
        this.messagesError = [
          {
            severity: "warn",
            summary: "Warning",
            detail:
              this.i18nService.translate("Duplicate detection: ") +
              te.workEffortName +
              " " +
              te.rateTypeIdDescription +
              " " +
              dateMsg.getDate() +
              " / " +
              (dateMsg.getMonth() + 1) +
              " / " +
              dateMsg.getFullYear(),
          },
        ];
      }
    }
  }

  async validation() {
    this.confirmationService.confirm({
      message: this.i18nService.translate(
        "Are you sure you want to proceed with the Check Completed?"
      ),
      header: this.i18nService.translate("Attention"),
      icon: "pi pi-question",
      accept: async () => {
        await this.timesheetService
          .validStatusIdTimesheet(this.selectedTimesheetId)
          .then((data) => {
            this.buttonValidationReopen.booleanShow = true;
            this.msgs = [
              {
                severity: this.i18nService.translate("info"),
                summary: this.i18nService.translate("Confirmed"),
                detail: this.i18nService.translate("Update confirmation"),
              },
            ];
            this.messageService.add({
              severity: "success",
              summary: "Success",
              detail: this.msgs[0].detail,
            });
            this.selectionTimeEntrys = [];
            this.buttonValidation.booleanShow = false;
            this.reload();
          })
          .catch((error) => {
            console.log("error", error.message);
            this.messagesError = [
              {
                severity: "error",
                summary: "Error",
                detail: error.message,
              },
            ];
          });
        this.router.navigate(["/c/CTX_OR/management/timesheet"]);
      },
    });
  }

  reopen() {
    this.confirmationService.confirm({
      message: this.i18nService.translate(
        "Are you sure you want to reopen this timesheet?"
      ),
      header: this.i18nService.translate("Attention"),
      icon: "pi pi-question",
      accept: async () => {
        await this.timesheetService
          .reopenStatusIdTimesheet(this.selectedTimesheetId)
          .then((data) => {
            this.buttonValidation.booleanShow = true;
            this.msgs = [
              {
                severity: this.i18nService.translate("info"),
                summary: this.i18nService.translate("Confirmed"),
                detail: this.i18nService.translate("Update confirmation"),
              },
            ];
            this.messageService.add({
              severity: "success",
              summary: "Success",
              detail: this.msgs[0].detail,
            });
            this.selectionTimeEntrys = [];
            this.buttonValidationReopen.booleanShow = false;
            this.reload();
          })
          .catch((error) => {
            console.log("error", error.message);
            this.messagesError = [
              {
                severity: "error",
                summary: "Error",
                detail: error.message,
              },
            ];
          });
        this.router.navigate(["/c/CTX_OR/management/timesheet"]);
      },
    });
  }
}
