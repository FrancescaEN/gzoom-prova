import { Component, OnDestroy, OnInit, SimpleChanges } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import {
  Head,
  Subject,
  Subscription,
  debounceTime,
  fromEvent,
  lastValueFrom,
  map,
  mergeWith,
  switchMap,
} from "rxjs";
import { TimeEntry } from "app/api/model/time_entry";
import { Timesheet } from "app/api/model/timesheet";
import { PartyService } from "app/api/service/party.service";
import { TimesheetService } from "app/api/service/timesheet.service";
import { UomService } from "app/api/service/uom.service";
import { I18NService } from "app/i18n/i18n.service";
import { bodyBarArray } from "app/layout/information-bar/bodyBarArray";
import {
  ConfirmationService,
  MenuItem,
  Message,
  MessageService,
} from "primeng/api";
import {
  ActionInput,
  ActionOutput,
  FooterArray,
  HeadArray,
  HeadFilter,
} from "app/layout/tables/table-calendar-timesheet/table-calendar-configuration";
import { RateTypeService } from "app/api/service/rate-type.service";
import {
  TimeEntryCellCalendar,
  TimeEntryRowCalendar,
} from "./time-entry-calendar";
import { Message as MessageError } from "primeng/api";
import { LanguageService } from "app/api/service/language.service";
import { TsHolidaysDatesService } from "app/api/service/ts-holidays-dates.service";
import { TsHolidaysDates } from "app/api/model/tsHolidaysDate";
import { Location } from "@angular/common";
import { DataForButton } from "app/layout/toolbar-data-table/toolbar-data-table-configuration";
import { ToolbarService } from "app/commons/service/toolbar.service";

function getDaysInMonth(month, year) {
  var date = new Date(year, month, 1);
  var days = [];
  while (date.getMonth() === month) {
    days.push(new Date(date));
    date.setDate(date.getDate() + 1);
  }
  return days;
}

function getDayName(date = new Date(), locale: string[], secondaryLang) {
  let day: any;

  if (secondaryLang) {
    day = date.toLocaleDateString(locale[1], { weekday: "long" });
  } else {
    day = date.toLocaleDateString(locale[0], { weekday: "long" });
  }

  return day.slice(0, 3);
}

@Component({
  selector: "app-time-entry-detail-calendar",
  templateUrl: "./time-entry-detail-calendar.component.html",
  styleUrls: ["./time-entry-detail-calendar.component.css"],
})
export class TimeEntryDetailCalendarComponent implements OnInit, OnDestroy {
  gridArray: TimeEntryRowCalendar[] = [];
  footerArray: FooterArray[] = [];
  daysOfThisMonth: any[] = [];
  dataTable: any;
  context: string;
  isAdmin: boolean;
  updatable: boolean;
  selectionTimeEntrys: TimeEntryRowCalendar[] = [];
  selectedTimesheet: Timesheet;
  _reload: Subject<void>;
  selectedTimesheetId: string;
  selectedPartyId: string;
  selectedEffortUomId: string;
  buttonDelete: boolean;
  buttonNew: boolean = true;
  selectedOn: boolean = true;
  paramsTemp: any;
  workEfforts: MenuItem[] = [];
  params: any = {
    managePlan: "N",
    showReference: "N",
    timeentryMapFormat: "DMY",
    hasRateTypeList: "N",
    rateTtypeList: "STANDARD",
    hoursPercentage: "H",
    showComments: "N",
    showOrderId: "N",
    showJobId: "N",
  };
  rateTypeArray: MenuItem[] = [];
  rateTypeStartArray: MenuItem[] = [];
  messagesError: MessageError[] = [];
  elementToAdd: TimeEntryRowCalendar;
  defaultRate: any = {
    description: null,
    id: null,
  };
  editingKeyId: string;
  msgs: Message[] = [];
  rowCurrent: string;
  itemCurrent: TimeEntryRowCalendar;
  loading: boolean = false;
  totalFinalHours: number = 0;
  displayModal: boolean = false;
  timeEntryDisplayModal: TimeEntryCellCalendar = new TimeEntryCellCalendar();
  timeEntryRowDblClicked: TimeEntryRowCalendar = new TimeEntryRowCalendar();
  headerDateDialog: string;
  dayLang1: string = "sab";
  dayLang2: string = "dom";
  secondaryLang: boolean;
  languages: string[] = [];
  tsHolidaysDates: TsHolidaysDates[] = [];
  today: Date;
  buttonValidation: DataForButton = {
    booleanShow: false,
    titleLabel: this.i18nService.translate("Timesheet completed"),
    icon: "pi pi-check-circle",
  };
  buttonValidationReopen: DataForButton = {
    booleanShow: false,
    titleLabel: this.i18nService.translate("Timesheet reopen"),
    icon: "pi pi-check-circle",
  };

  resizeSubscription: Subscription;
  timesheetServiceAdminSubscription: Subscription;
  timesheetServiceSubscription: Subscription;
  timesheetServiceTimeEntrySubscription: Subscription;
  timesheetServiceWorkEffortSubscription: Subscription;
  rateTypeSubscription: Subscription;

  backLink= '../../'

  bodyBarArray: bodyBarArray[] = [
    {
      head: this.i18nService.translate("Period"),
      label: "",
    },
    {
      head: this.i18nService.translate("Subject"),
      label: "",
    },
    {
      head: this.i18nService.translate("PartyStructure"),
      label: "",
    },
    {
      head: this.i18nService.translate("Status"),
      label: "",
    },
    {
      head: this.i18nService.translate("employmentAmount"),
      label: 0,
    },
    // {
    //   head: this.i18nService.translate("Abbreviation"),
    //   label: 0,
    // },
    {
      head: "totalFinalHours",
      label: 0,
    },
  ];

  headRateTypeArray: HeadArray[] = [
    {
      head: this.i18nService.translate("rateTypeIdDescription"),
      fieldName: "rateTypeIdDescription",
      actionInput: ActionInput.dropdownData,
      actionOutput: ActionOutput.outputLabelData,
      dropdown: {
        item: this.rateTypeArray,
        clear: false,
        key: "rateTypeId",
      },
      filter: HeadFilter.null,
      width: "10vw",
      sortIcon: true,
      required: true,
      content: "left",
    },
  ];

  headWorkEffortArray: HeadArray[] = [
    {
      head: this.i18nService.translate("workEffortName"),
      fieldName: "workEffortName",
      actionInput: ActionInput.dropdownData,
      actionOutput: ActionOutput.outputLabelData,
      dropdown: {
        item: this.workEfforts,
        clear: false,
        key: "workEffortId",
      },
      filter: HeadFilter.null,
      width: "15vw",
      required: true,
    },
  ];

  numberArray: HeadArray[] = [];
  totalHoursRowArray: HeadArray[] = [
    {
      head: this.i18nService.translate("Totals"),
      fieldName: "labeltotalHoursRow",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelNumber,
      filter: HeadFilter.null,
      width: "5vw",
      content: "center",
    },
  ];

  constructor(
    private readonly timesheetService: TimesheetService,
    private readonly partyService: PartyService,
    private readonly uomService: UomService,
    private readonly rateTypeService: RateTypeService,
    private readonly confirmationService: ConfirmationService,
    private readonly route: ActivatedRoute,
    private messageService: MessageService,
    public readonly i18nService: I18NService,
    private languageService: LanguageService,
    private readonly router: Router,
    private tsHolidaysDatesService: TsHolidaysDatesService,
    private _location: Location,
    private toolbarService: ToolbarService,
  ) {
    this._reload = new Subject<void>();
  }

  ngOnDestroy(): void {
    this.resizeSubscription?.unsubscribe();
    this.timesheetServiceAdminSubscription?.unsubscribe();
    this.timesheetServiceSubscription?.unsubscribe();
    this.timesheetServiceWorkEffortSubscription?.unsubscribe();
    this.timesheetServiceTimeEntrySubscription?.unsubscribe();
    this.rateTypeSubscription?.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes && changes["totalFinalHours"]) {
      this.bodyBarArray.forEach((element) => {
        if (element.head == "totalFinalHours") {
          element.label = this.totalFinalHours.toString();
        }
      });
    }
  }

  async ngOnInit(): Promise<void> {
    this.loading = true;

    this.toolbarService.setDisabledNew(false);
    this.toolbarService.setDisabledDelete(false);

    this.buttonValidation.booleanShow = false;
    this.buttonValidationReopen.booleanShow = false;
    this.timeEntryDisplayModal.timeEntry = new TimeEntry();

    this.collapseSidebar();

    const resizeObservable = fromEvent(window, "resize").pipe(
      debounceTime(200) // Imposta un ritardo di 200 millisecondi per evitare chiamate troppo frequenti
    );

    this.resizeSubscription = resizeObservable.subscribe({
      next: () => {
        this.handleResize();
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

    this.secondaryLang = await this.languageService.secondaryLang();
    if (!this.secondaryLang) {
      this.dayLang1 = "Sam";
      this.dayLang2 = "Son";
    }

    await lastValueFrom(this.languageService.language())
      .then((data) => {
        this.languages[0] = data[0].replace("_", "-");
        if (this.secondaryLang) {
          this.languages[1] = data[1].replace("_", "-");
        }
      })
      .catch((error) => {
        console.log("error", error);
        this.messagesError = [
          {
            severity: "error",
            summary: "Error",
            detail: error,
          },
        ];
      });

    const timeEntryObs = this.route.data.pipe(
      map((data: { timeEntries: TimeEntry[] }) => data.timeEntries),
      mergeWith(reloadedTimeEntries)
    );

    this.timesheetServiceTimeEntrySubscription = this.route.paramMap
      .pipe(
        switchMap((params) => {
          this.selectedTimesheetId = params.get("id");
          return this.timesheetService.timesheetTimeEntry(
            this.selectedTimesheetId
          );
        })
      )
      .subscribe({
        next: async (data) => {
          this.selectedTimesheet = data;
          if (
            this.selectedTimesheet.statusId == "TIMESHEET_IN_PROCESS" &&
            this.selectedTimesheet.tsByUserLogin["updateable"] == "Y"
          ) {
            this.buttonValidation.booleanShow = true;
          }
          if (
            this.selectedTimesheet.statusId == "TIMESHEET_COMPLETED" &&
            this.isAdmin
          ) {
            this.buttonValidationReopen.booleanShow = true;
          }
          this.selectedPartyId = this.selectedTimesheet.party["partyId"];
          this.selectedEffortUomId = this.selectedTimesheet.effortUomId;

          if (this.selectedTimesheet.tsByUserLogin["updateable"] == "N") {
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

          this.bodyBarArray.forEach((element) => {
            if (element.head == this.i18nService.translate("Period")) {
              element.label = this.secondaryLang
                ? this.selectedTimesheet.customTimePeriod["periodNameLang"]
                : this.selectedTimesheet.customTimePeriod["periodName"];
            }
            if (element.head == this.i18nService.translate("Subject")) {
              element.label = this.selectedTimesheet.party["partyName"];
            }
            if (element.head == this.i18nService.translate("PartyStructure")) {
              element.label = this.secondaryLang
                ? this.selectedTimesheet.partyStructure["partyNameLang"]
                : this.selectedTimesheet.partyStructure["partyName"];
            }
            if (element.head == this.i18nService.translate("Status")) {
              element.label = this.secondaryLang
                ? this.selectedTimesheet.statusItem["descriptionLang"]
                : this.selectedTimesheet.statusItem["description"];
            }
            if (
              element.head == this.i18nService.translate("employmentAmount")
            ) {
              element.label =
                this.selectedTimesheet.partyHistoryView[
                  "employmentAmount"
                ].toString() + " %";
            }
            if (element.head == this.i18nService.translate("Abbreviation")) {
              element.label = this.secondaryLang
                ? this.selectedTimesheet.uom["descriptionLang"]
                : this.selectedTimesheet.uom["description"];
            }
          });

          this.paramsTemp = await this.timesheetService.params(
            this.selectedTimesheetId
          );

          await this.paramsTemp.forEach((element) => {
            element[0].noteInfo.split(";").forEach((item) => {
              if (item.split("=")[0] != "") {
                var itemLeft = item.split("=")[0].replace(/\s/g, "");

                if (!!item.split("=")[1] && item.split("=")[1].includes('"')) {
                  switch (itemLeft) {
                    case "managePlan":
                      this.params.managePlan = item.split("=")[1].split('"')[1];
                      break;
                    case "showReference":
                      this.params.showReference = item
                        .split("=")[1]
                        .split('"')[1];
                      break;
                    case "timeentryMapFormat":
                      this.params.timeentryMapFormat = item
                        .split("=")[1]
                        .split('"')[1];
                      break;
                    case "hasRateTypeList":
                      this.params.hasRateTypeList = item
                        .split("=")[1]
                        .split('"')[1];
                      break;
                    case "rateTtypeList":
                      this.params.rateTtypeList = item
                        .split("=")[1]
                        .split('"')[1];
                      break;
                    case "hoursPercentage":
                      this.params.hoursPercentage = item
                        .split("=")[1]
                        .split('"')[1];
                      break;
                    case "showComments":
                      this.params.showComments = item
                        .split("=")[1]
                        .split('"')[1];
                      break;
                    case "showOrderId":
                      this.params.showOrderId = item
                        .split("=")[1]
                        .split('"')[1];
                      break;
                    case "showJobId":
                      this.params.showJobId = item.split("=")[1].split('"')[1];
                      break;
                  }
                } else {
                  switch (itemLeft) {
                    case "managePlan":
                      this.params.managePlan = item.split("=")[1].split("'")[1];
                      break;
                    case "showReference":
                      this.params.showReference = item
                        .split("=")[1]
                        .split("'")[1];
                      break;
                    case "timeentryMapFormat":
                      this.params.timeentryMapFormat = item
                        .split("=")[1]
                        .split("'")[1];
                      break;
                    case "hasRateTypeList":
                      this.params.hasRateTypeList = item
                        .split("=")[1]
                        .split("'")[1];
                      break;
                    case "rateTtypeList":
                      this.params.rateTtypeList = item
                        .split("=")[1]
                        .split("'")[1];
                      break;
                    case "hoursPercentage":
                      this.params.hoursPercentage = item
                        .split("=")[1]
                        .split("'")[1];
                      break;
                    case "showComments":
                      this.params.showComments = item
                        .split("=")[1]
                        .split("'")[1];
                      break;
                    case "showOrderId":
                      this.params.showOrderId = item
                        .split("=")[1]
                        .split("'")[1];
                      break;
                    case "showJobId":
                      this.params.showJobId = item.split("=")[1].split("'")[1];
                      break;
                  }
                }

                this.params.glFiscalTypeEnumId =
                  this.selectedTimesheet.workEffortTypePeriod[
                  "glFiscalTypeEnumId"
                  ];
              }
            });
          });

          if (
            (this.params.timeentryMapFormat == "DMY" ||
              this.params.timeentryMapFormat == "MMY") &&
            window.location.href.includes("/C/")
          ) {
            this._location.back();
          }

          if (this.params.hasRateTypeList == "N") {
            this.headRateTypeArray[0].display = "none";
          }

          await this.footerArray.push({
            head: this.i18nService.translate("Totals"),
            fieldName: "totals",
            actionInput: ActionInput.null,
            actionOutput: ActionOutput.outputLabelData,
            filter: HeadFilter.null,
            width:
              this.selectedTimesheet.tsByUserLogin["updateable"] == "N"
                ? "30%"
                : "33%",
            colorHeader: "#E8151E",
            content: "center",
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

    await lastValueFrom(
      this.tsHolidaysDatesService.tsHolidaysDates(this.selectedTimesheetId)
    )
      .then((data) => {
        data.forEach((x) => {
          this.tsHolidaysDates.push({
            holidayDate: new Date(x.holidayDate),
            timesheetId: x.timesheetId,
          });
        });
      })
      .catch((error) => {
        console.log("error", error);
        this.messagesError = [
          {
            severity: "error",
            summary: "Error",
            detail: error,
          },
        ];
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

    await lastValueFrom(
      this.timesheetService.timesheetTimeEntry(this.selectedTimesheetId)
    ).then((data) => (this.selectedTimesheet = data));

    const d = new Date(this.selectedTimesheet.fromDate);
    this.daysOfThisMonth = getDaysInMonth(d.getMonth(), d.getFullYear());

    this.daysOfThisMonth.forEach(async (item) => {
      if (
        getDayName(item, this.languages, this.secondaryLang) == this.dayLang2 ||
        this.tsHolidaysDates.filter(
          (x) => x.holidayDate.getDate() === item.getDate()
        ).length > 0
      ) {
        this.numberArray.push({
          head: item.getDate(),
          subHead: getDayName(item, this.languages, this.secondaryLang),
          fieldName: item.getDate(),
          actionInput:
            this.selectedTimesheet.tsByUserLogin["updateable"] == "N"
              ? ActionInput.null
              : ActionInput.inputLabelNumber,
          actionOutput: ActionOutput.outputLabelNumber,
          filter: HeadFilter.null,
          width: "3vw",
          colorHeader: "rgb(210, 199, 199)",
          content: "center",
        });
      } else if (
        getDayName(item, this.languages, this.secondaryLang) == this.dayLang1
      ) {
        this.numberArray.push({
          head: item.getDate(),
          subHead: getDayName(item, this.languages, this.secondaryLang),
          fieldName: item.getDate(),
          actionInput:
            this.selectedTimesheet.tsByUserLogin["updateable"] == "N"
              ? ActionInput.null
              : ActionInput.inputLabelNumber,
          actionOutput: ActionOutput.outputLabelNumber,
          filter: HeadFilter.null,
          width: "3vw",
          colorHeader: "rgb(219, 219, 219)",
          content: "center",
        });
      } else {
        this.numberArray.push({
          head: item.getDate(),
          subHead: getDayName(item, this.languages, this.secondaryLang),
          fieldName: item.getDate(),
          actionInput:
            this.selectedTimesheet.tsByUserLogin["updateable"] == "N"
              ? ActionInput.null
              : ActionInput.inputLabelNumber,
          actionOutput: ActionOutput.outputLabelNumber,
          filter: HeadFilter.null,
          width: "3vw",
          content: "center",
        });
      }
    });

    let data = this.rateTypeService.rateTypes();

    (await data).forEach((x) => {
      this.rateTypeStartArray.push({
        label: this.secondaryLang ? x.descriptionLang : x.description,
        id: x.rateTypeId,
      });
    });

    this.timesheetServiceSubscription = timeEntryObs.subscribe({
      next: async (data) => {
        // azzero il campo hours di numberArray
        this.numberArray.forEach((x) => {
          x.hours = null;
          x.labelHours = null;
        });

        if (data) {
          this.gridArray = [];

          this.today = new Date(this.selectedTimesheet.fromDate);

          data.forEach((element) => {
            let te: TimeEntry = {
              timeEntryId: element.timeEntryId,
              timesheetId: this.selectedTimesheetId,
              workEffortId: element.workEffortId,
              partyId: element.partyId,
              rateTypeId: element.rateTypeId,
              effortUomId: this.selectedEffortUomId,
              hours: element.hours,
              planHours: element.planHours,
              fromDate: element.fromDate,
              thruDate: element.thruDate,
              comments: element.comments,
              orderId: element.orderId,
              jobId: element.jobId,
              variableGridArray: {
                id: element.timeEntryId,
                buttonDetails: false,
                dropdownData: true,
                inputLabeldata: false,
                inputLabelNumber: true,
                inputNotes: true,
                outputData: true,
                updated: false,
              },
            };

            let d = new Date(element.fromDate);

            if (this.today.getMonth() == d.getMonth()) {
              if (
                this.gridArray.filter(
                  (x) =>
                    x.workEffortId === element.workEffortId &&
                    x.rateTypeId === element.rateTypeId
                ).length == 0
              ) {
                let timeEntriesCellArray = [];
                let sumHours = 0;
                this.daysOfThisMonth.forEach((day) => {
                  if (
                    day.getMonth() == d.getMonth() &&
                    day.getDate() == d.getDate()
                  ) {
                    let tetemp: TimeEntryCellCalendar = {
                      fromDate: day,
                      thruDate: day,
                      timeEntryCellId: day.getDate(),
                      timeEntry: te,
                      hours: element.hours,
                      labelHours: element.hours.toString(),
                      comments: null,
                      underline: element.comments ? true : false,
                    };
                    timeEntriesCellArray.push(tetemp);
                    sumHours += element.hours;
                  } else if (day.getMonth() == d.getMonth()) {
                    let tetemp: TimeEntryCellCalendar = {
                      fromDate: day,
                      thruDate: day,
                      timeEntryCellId: day.getDate(),
                      timeEntry: null,
                      hours: null,
                      labelHours: null,
                      comments: null,
                    };
                    timeEntriesCellArray.push(tetemp);
                  }
                });

                this.gridArray.push({
                  TimeEntryCalendarId:
                    element.workEffortId + element.rateTypeId,
                  workEffortId: element.workEffortId,
                  workEffortName: this.secondaryLang
                    ? element.workEffort["workEffortNameLang"]
                    : element.workEffort["workEffortName"],
                  partyId: element.partyId,
                  timesheetId: element.timesheetId,
                  rateTypeId: element.rateTypeId,
                  rateTypeIdDescription: this.rateTypeStartArray
                    .filter((x) => x.id === element.rateTypeId)
                    .map((y) => y.label)[0],
                  timeEntriesCell: timeEntriesCellArray,
                  totalHoursRow: sumHours,
                  labeltotalHoursRow: sumHours.toFixed(1).toString(),
                  variableGridArray: {
                    id: element.workEffortId + element.rateTypeId,
                    buttonDetails: false,
                    dropdownData: false,
                    inputLabeldata: false,
                    inputLabelNumber: true,
                    inputNotes: true,
                    outputData: true,
                    updated: false,
                  },
                });
              } else {
                let tetemp: TimeEntryCellCalendar;
                this.daysOfThisMonth.forEach((day, index) => {
                  if (
                    day.getMonth() == d.getMonth() &&
                    day.getDate() == d.getDate()
                  ) {
                    tetemp = {
                      fromDate: element.fromDate,
                      thruDate: element.thruDate,
                      timeEntryCellId: day.getDate(),
                      timeEntry: te,
                      comments: null,
                      hours: element.hours,
                      labelHours: element.hours.toString(),
                      underline: element.comments ? true : false,
                    };
                    this.gridArray.filter(
                      (x) =>
                        x.workEffortId === element.workEffortId &&
                        x.rateTypeId === element.rateTypeId
                    )[0].timeEntriesCell[index] = tetemp;

                    let totHourRow = (this.gridArray.filter(
                      (x) =>
                        x.workEffortId === element.workEffortId &&
                        x.rateTypeId === element.rateTypeId
                    )[0].totalHoursRow += tetemp.hours);

                    this.gridArray.filter(
                      (x) =>
                        x.workEffortId === element.workEffortId &&
                        x.rateTypeId === element.rateTypeId
                    )[0].labeltotalHoursRow = totHourRow.toFixed(1).toString();
                  }
                });
              }
            }

            this.numberArray.forEach((x, index) => {
              if (x.head == d.getDate()) {
                if (this.numberArray[index].hours == null) {
                  this.numberArray[index].hours = element.hours;
                  this.numberArray[index].labelHours = element.hours.toString();
                } else {
                  this.numberArray[index].hours += element.hours;
                  this.numberArray[index].labelHours =
                    this.numberArray[index].hours.toString();
                }
              }
            });
          });
          this.reloadInfoBar();
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
  }

  handleResize() {
    window.location.reload();
  }

  notifyInputChanges(item) {
    if (typeof item.index === "number") {
      this.gridArray.filter(
        (x) => x.variableGridArray.id === item.item.variableGridArray.id
      )[0].variableGridArray.updated = true;

      // copy Timeentrycellcalendar hours to labelhours
      let h = this.gridArray.filter(
        (x) => x.variableGridArray.id === item.item.variableGridArray.id
      )[0].timeEntriesCell[item.index - 1].hours;
      if (!!h) {
        this.gridArray.filter(
          (x) => x.variableGridArray.id === item.item.variableGridArray.id
        )[0].timeEntriesCell[item.index - 1].labelHours = h.toString();
      }

      this.save(item);
    }

    this.itemCurrent = item.item;
    this.rowCurrent = item.item.variableGridArray.id;

    if (
      this.gridArray[0]?.variableGridArray.id.includes("new") &&
      !!this.gridArray[0].workEffortId && this.gridArray[0].rateTypeId == null
    ) {
      this.reloadRateType(
        this.gridArray[0].workEffortId,
        this.selectedTimesheet.workEffortId
      );
    }
  }

  reloadInfoBar() {
    this.bodyBarArray.forEach((element) => {
      if (element.head == "totalFinalHours") {
        element.label = 0;
        this.numberArray.forEach((e) => {
          if (!!e.hours) {
            element.label += e.hours;
          }
        });
        element.label = element.label.toFixed(1).toString().replace(/\./g, ",");
      }
    });
  }

  async reloadRateType(workEffortId: string, workEffortIdTimesheet: string) {
    this.rateTypeSubscription = this.rateTypeService
      .rateTypesWorkEffortId(workEffortId)
      .subscribe((data) => {       

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
          }        

          if (element.rate == 1) {
            this.defaultRate.description = this.secondaryLang
              ? element.rateType.descriptionLang
              : element.rateType.description;
            this.defaultRate.id = element.rateTypeId;
          }
        });

        if (this.defaultRate.id) {
          this.gridArray[0].rateTypeId = this.defaultRate.id;
          this.gridArray[0].rateTypeIdDescription = this.defaultRate.description;
        }
      });

    if (this.rateTypeArray.length == 0) {

      this.rateTypeSubscription = this.rateTypeService
        .rateTypesWorkEffortId(workEffortIdTimesheet)
        .subscribe((data) => {
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
            }

            if (element.rate == 1) {
              this.defaultRate.description = this.secondaryLang
                ? element.rateType.descriptionLang
                : element.rateType.description;
              this.defaultRate.id = element.rateTypeId;

            }
          });
         

          if (this.defaultRate.id) {
            this.gridArray[0].rateTypeId = this.defaultRate.id;
            this.gridArray[0].rateTypeIdDescription = this.defaultRate.description;
          }
          

        });
    }

  }

  existTimeEntryInRow(timeEntryCellArray: TimeEntryCellCalendar[]): boolean {
    timeEntryCellArray.forEach((x) => {
      if (x.timeEntry != null) {
        return true;
      }
    });
    return false;
  }

  openNew() {
    if (
      this.gridArray.length > 0 &&
      this.gridArray[0].variableGridArray.id.includes("new")
    ) {
      if (
        !this.existTimeEntryInRow(
          this.gridArray.filter(
            (x) => !x.variableGridArray.id.includes("new")
          )[0].timeEntriesCell
        )
      ) {
        this.gridArray.shift();
      }
    }

    if (
      this.gridArray.length > 0 &&
      this.gridArray.filter((x) => x.variableGridArray.id.includes("new"))
        .length > 0 &&
      !this.validGridArray(this.gridArray, undefined, this.itemCurrent)
    ) {
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
      this.gridArray.forEach((element) => {
        element.variableGridArray.dropdownData = false;
      });

      if (this.workEfforts.length > 0) {
        let timeEntriesCellArray = [];
        this.daysOfThisMonth.forEach((day) => {
          let tetemp: TimeEntryCellCalendar = {
            fromDate: day,
            thruDate: day,
            timeEntryCellId: day.getDate(),
            timeEntry: null,
            hours: null,
            comments: null,
          };
          timeEntriesCellArray.push(tetemp);
        });
        let tempId = "new" + Math.random();
        this.elementToAdd = {
          workEffortName: "",
          TimeEntryCalendarId: tempId,
          timesheetId: this.selectedTimesheetId,
          partyId: this.selectedTimesheet.party["partyId"],
          effortUomId: this.selectedEffortUomId,
          rateTypeId: null,
          rateTypeIdDescription: null,
          timeEntriesCell: timeEntriesCellArray,
          totalHoursRow: null,
          labeltotalHoursRow: "",
          variableGridArray: {
            id: tempId,
            buttonDetails: false,
            dropdownData: true,
            inputLabeldata: false,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            updated: false,
          },
        };
        this.gridArray = [this.elementToAdd, ...this.gridArray];
        this.editingKeyId = this.elementToAdd.variableGridArray.id;
        setTimeout(() => {
          this.dataTable.editingRowKeys = {
            [this.editingKeyId]: true,
          };
        }, 0);
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

  collapseSidebar() {
    const dom: any = document.querySelector("body");
    const menu: any = document.querySelector("#sidebar");
    dom.classList.add("push-right");
    menu.classList.add("collapse");
  }

  validGridArray(
    array: TimeEntryRowCalendar[],
    id?: number,
    item?: TimeEntryRowCalendar
  ): boolean {
    var control = 0;
    array.forEach((element) => {
      if (id == undefined || item == undefined) {
        if (
          element.workEffortName == "" ||
          element.workEffortName == null ||
          element.rateTypeIdDescription == "" ||
          element.rateTypeIdDescription == null
        ) {
          control = 1;
        }
      } else if (
        item.TimeEntryCalendarId == element.TimeEntryCalendarId &&
        (item.timeEntriesCell[id - 1].timeEntry == null ||
          item.timeEntriesCell[id - 1].timeEntry == undefined) &&
        (item.workEffortName == null ||
          item.workEffortName == undefined ||
          item.workEffortName == "")
      ) {
        if (
          element.workEffortName == "" ||
          element.workEffortName == null ||
          element.rateTypeIdDescription == "" ||
          element.rateTypeIdDescription == null ||
          element.timeEntriesCell[id - 1].hours == undefined ||
          element.timeEntriesCell[id - 1].hours == null
        ) {
          control = 1;
        }
      }
    });

    if (control == 0) {
      return true;
    } else {
      return false;
    }
  }

  async saveSingleTimeEntry(item: TimeEntryCellCalendar) {
    let id = item.timeEntryCellId;
    let control: boolean = true;

    if (item.timeEntry) {
      if (!!item.hours) {
        item.timeEntry.hours = item.hours;
        item.timeEntry.comments = item.comments;
        item.timeEntry.orderId = item.orderId;
        item.timeEntry.jobId = item.jobId;

        await this.timesheetService
          .updateTimeEntry(item.timeEntry)
          .then(() => {
            this.reloadInfoBar();
          })
          .catch((error) => {
            console.log("error", error);
            this.messagesError = [
              {
                severity: "error",
                summary: "Error",
                detail: error,
              },
            ];
          });
        this.reload();
        this.displayModal = false;
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
      if (!!item.hours) {
        let te: TimeEntry = {
          timesheetId: this.selectedTimesheetId,
          workEffortId: this.timeEntryRowDblClicked.workEffortId,
          partyId: this.timeEntryRowDblClicked.partyId,
          rateTypeId: this.timeEntryRowDblClicked.rateTypeId,
          effortUomId: this.selectedEffortUomId,
          hours: item.hours,
          planHours: 0,
          fromDate: item.fromDate,
          thruDate: item.thruDate,
          comments: item.comments,
          orderId: item.orderId,
          jobId: item.jobId,
          variableGridArray: {
            id: "new" + Math.random(),
            buttonDetails: false,
            dropdownData: false,
            inputLabeldata: false,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            updated: false,
          },
        };

        if (!!this.numberArray[id - 1].hours) {
          this.numberArray[id - 1].hours =
            this.numberArray[id - 1].hours +
            this.timeEntryRowDblClicked.timeEntriesCell[id - 1].hours;
          this.numberArray[id - 1].labelHours =
            this.numberArray[id - 1].hours.toString();
        } else {
          this.numberArray[id - 1].hours = item.hours;
          this.numberArray[id - 1].labelHours =
            this.numberArray[id - 1].hours.toString();
        }

        this.timeEntryRowDblClicked.timeEntriesCell[id - 1].hours = item.hours;

        if (
          this.gridArray.length > 1 &&
          this.gridArray[0].variableGridArray.id.includes("new")
        ) {
          control = this.duplicateManageSingleTimeEntry(
            this.timeEntryRowDblClicked
          );
        }

        if (control) {
          await this.timesheetService
            .createTimeEntry(te)
            .then((data) => {
              te.timeEntryId = data;
              te.variableGridArray.id = data;
              this.reloadInfoBar();
              this.reload();
              this.displayModal = false;
            })
            .catch((error) => {
              console.log("error", error);
              this.messagesError = [
                {
                  severity: "error",
                  summary: "Error",
                  detail: error,
                },
              ];
            });
        } else {
          this.displayModal = false;
        }
        this._reload.next();
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
  }

  duplicateManageSingleTimeEntry(te?): boolean {
    let control: boolean = true;
    if (te) {
      this.gridArray.forEach((x, indexGrid) => {
        if (
          x.workEffortId == te.workEffortId &&
          x.rateTypeId == te.rateTypeId &&
          !x.variableGridArray.id.includes("new")
        ) {
          te.timeEntriesCell.forEach((item, index) => {
            if (item.hours != null && x.timeEntriesCell[index].hours != null) {
              this.gridArray.shift();
              let dateMsg = new Date(
                x.timeEntriesCell[index].timeEntry.fromDate
              );
              this.messagesError = [
                {
                  severity: "warn",
                  summary: "Warning",
                  detail:
                    this.i18nService.translate("Duplicate detection: ") +
                    x.workEffortName +
                    " " +
                    x.rateTypeIdDescription +
                    " " +
                    dateMsg.getDate() +
                    " / " +
                    (dateMsg.getMonth() + 1) +
                    " / " +
                    dateMsg.getFullYear(),
                },
              ];
              control = false;
            }
          });
        }
      });
      return control;
    }
  }

  async save(data) {
    if (!this.displayModal) {
      let id = data.index;
      let item: TimeEntryRowCalendar = data.item;

      let control: boolean = false;

      if (typeof id === "number") {
        if (this.validGridArray(this.gridArray, id, item)) {
          if (
            this.gridArray.filter((x) => x.variableGridArray.id.includes("new"))
              .length > 0 &&
            this.gridArray.filter((x) =>
              x.variableGridArray.id.includes("new")
            )[0].variableGridArray.updated
          ) {
            if (this.gridArray.length > 1) {
              control = this.duplicateManage();
            }

            let tec = this.gridArray.filter((x) =>
              x.variableGridArray.id.includes("new")
            )[0];

            let te: TimeEntry;

            if (tec && tec.timeEntriesCell[id - 1].hours != null) {
              te = {
                timesheetId: this.selectedTimesheetId,
                workEffortId: tec.workEffortId,
                partyId: tec.partyId,
                rateTypeId: tec.rateTypeId,
                effortUomId: this.selectedEffortUomId,
                hours: tec.timeEntriesCell[id - 1].hours,
                planHours: 0,
                fromDate: tec.timeEntriesCell[id - 1].fromDate,
                thruDate: tec.timeEntriesCell[id - 1].thruDate,
                comments: tec.timeEntriesCell[id - 1].comments,
                variableGridArray: {
                  id: "new" + Math.random(),
                  buttonDetails: false,
                  dropdownData: false,
                  inputLabeldata: false,
                  inputLabelNumber: true,
                  inputNotes: true,
                  outputData: true,
                  updated: false,
                },
              };

              if (!!this.numberArray[id - 1].hours) {
                this.numberArray[id - 1].hours =
                  this.numberArray[id - 1].hours +
                  tec.timeEntriesCell[id - 1].hours;
                this.numberArray[id - 1].labelHours =
                  this.numberArray[id - 1].hours.toString();
              } else {
                this.numberArray[id - 1].hours =
                  tec.timeEntriesCell[id - 1].hours;
                this.numberArray[id - 1].labelHours =
                  this.numberArray[id - 1].hours.toString();
              }

              if (control) {
                this.gridArray.shift();
              }

              tec.variableGridArray.dropdownData = false;

              item.totalHoursRow = 0;
              item.timeEntriesCell.forEach(
                (x) => (
                  (item.totalHoursRow += x.hours),
                  (item.labeltotalHoursRow = item.totalHoursRow
                    .toFixed(1)
                    .toString())
                )
              );

              await this.timesheetService
                .createTimeEntry(te)
                .then((data) => {
                  te.timeEntryId = data;
                  te.variableGridArray.id = data;
                  te.variableGridArray.dropdownData = false;
                  this.gridArray
                    .filter((x) => x.variableGridArray.id.includes("new"))
                    .forEach((y) => {
                      y.TimeEntryCalendarId =
                        y.TimeEntryCalendarId.substring(3);
                      y.variableGridArray.id =
                        y.variableGridArray.id.substring(3);
                    });

                  this.reloadInfoBar();
                })
                .catch((error) => {
                  console.log("error", error);
                  this.messagesError = [
                    {
                      severity: "error",
                      summary: "Error",
                      detail: error,
                    },
                  ];
                });
              item.timeEntriesCell[id - 1].timeEntry = te;
            }
          } else {
            if (
              item.timeEntriesCell[id - 1].timeEntry == null &&
              item.timeEntriesCell[id - 1].timeEntry == undefined
            ) {
              let te: TimeEntry = {
                timesheetId: this.selectedTimesheetId,
                workEffortId: item.workEffortId,
                partyId: item.partyId,
                rateTypeId: item.rateTypeId,
                effortUomId: this.selectedEffortUomId,
                hours: item.timeEntriesCell[id - 1].hours,
                planHours: 0,
                fromDate: item.timeEntriesCell[id - 1].fromDate,
                thruDate: item.timeEntriesCell[id - 1].thruDate,
                comments: item.timeEntriesCell[id - 1].comments,
                variableGridArray: {
                  id: "new" + Math.random(),
                  buttonDetails: false,
                  dropdownData: false,
                  inputLabeldata: false,
                  inputLabelNumber: true,
                  inputNotes: true,
                  outputData: true,
                  updated: false,
                },
              };

              if (!!te.hours) {
                if (!!this.numberArray[id - 1].hours) {
                  this.numberArray[id - 1].hours =
                    this.numberArray[id - 1].hours +
                    item.timeEntriesCell[id - 1].hours;
                  this.numberArray[id - 1].labelHours =
                    this.numberArray[id - 1].hours.toString();
                } else {
                  this.numberArray[id - 1].hours =
                    item.timeEntriesCell[id - 1].hours;
                  this.numberArray[id - 1].labelHours =
                    this.numberArray[id - 1].hours.toString();
                }
                item.totalHoursRow = 0;
                item.timeEntriesCell.forEach(
                  (x) => (
                    (item.totalHoursRow += x.hours),
                    (item.labeltotalHoursRow = item.totalHoursRow
                      .toFixed(1)
                      .toString())
                  )
                );

                await this.timesheetService
                  .createTimeEntry(te)
                  .then((data) => {
                    te.timeEntryId = data;
                    te.variableGridArray.id = data;
                    te.variableGridArray.dropdownData = false;

                    this.reloadInfoBar();
                  })
                  .catch((error) => {
                    console.log("error", error);
                    this.messagesError = [
                      {
                        severity: "error",
                        summary: "Error",
                        detail: error,
                      },
                    ];
                  });
                item.timeEntriesCell[id - 1].timeEntry = te;
              }
            } else {
              if (item.timeEntriesCell[id - 1].hours == null) {
                let arrayIdTimesheed = [];
                arrayIdTimesheed.push(
                  item.timeEntriesCell[id - 1].timeEntry.timeEntryId
                );

                this.numberArray[id - 1].hours = 0;
                this.gridArray.forEach((x) => {
                  if (!!x.timeEntriesCell[id - 1].hours) {
                    this.numberArray[id - 1].hours =
                      this.numberArray[id - 1].hours +
                      x.timeEntriesCell[id - 1].hours;
                    this.numberArray[id - 1].labelHours =
                      this.numberArray[id - 1].hours.toString();
                  }
                });

                if (this.numberArray[id - 1].hours == 0) {
                  this.numberArray[id - 1].hours = null;
                  this.numberArray[id - 1].labelHours = null;
                }

                this.timesheetService
                  .deleteTimeEntry(arrayIdTimesheed)
                  .then((data) => {
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
              } else {
                this.numberArray[id - 1].hours =
                  this.numberArray[id - 1].hours -
                  item.timeEntriesCell[id - 1].timeEntry.hours;

                item.timeEntriesCell[id - 1].timeEntry.hours =
                  item.timeEntriesCell[id - 1].hours;

                this.numberArray[id - 1].hours =
                  this.numberArray[id - 1].hours +
                  item.timeEntriesCell[id - 1].hours;

                this.numberArray[id - 1].labelHours =
                  this.numberArray[id - 1].hours.toString();

                item.totalHoursRow = 0;
                item.timeEntriesCell.forEach(
                  (x) => (
                    (item.totalHoursRow += x.hours),
                    (item.labeltotalHoursRow = item.totalHoursRow
                      .toFixed(1)
                      .toString())
                  )
                );

                await this.timesheetService
                  .updateTimeEntry(item.timeEntriesCell[id - 1].timeEntry)
                  .then(() => {
                    this.reloadInfoBar();
                  })
                  .catch((error) => {
                    console.log("error", error);
                    this.messagesError = [
                      {
                        severity: "error",
                        summary: "Error",
                        detail: error,
                      },
                    ];
                  });
              }
            }
          }
        } else {
          this.gridArray.shift();
          this.messagesError = [
            {
              severity: "warn",
              summary: "Warning",
              detail: this.i18nService.translate(
                "All mandatory fields must be filled in"
              ),
            },
          ];
        }
      }

      if (control) {
        this._reload.next();
      }
    }
  }

  reload() {
    this.selectionTimeEntrys = [];
    this.numberArray.forEach((x) => ((x.hours = null), (x.labelHours = null)));
    this._reload.next();
  }

  duplicateManage(singleTimeEntryRow?): boolean {
    let te: TimeEntryRowCalendar = this.gridArray.filter((x) =>
      x.variableGridArray.id.includes("new")
    )[0];

    if (te) {
      var control = false;
      this.gridArray.forEach((x, indexGrid) => {
        if (
          x.workEffortId == te.workEffortId &&
          x.rateTypeId == te.rateTypeId &&
          !x.variableGridArray.id.includes("new")
        ) {
          te.timeEntriesCell.forEach((item, index) => {
            if (
              item.hours != null &&
              x.timeEntriesCell[index].hours == null &&
              control == false
            ) {
              x.timeEntriesCell[index].hours == item.hours;
              x.variableGridArray.updated = true;
              control = true;
            }

            if (
              item.hours != null &&
              x.timeEntriesCell[index].hours != null &&
              control == false
            ) {
              this.gridArray.shift();
              let dateMsg = new Date(
                x.timeEntriesCell[index].timeEntry.fromDate
              );
              this.messagesError = [
                {
                  severity: "warn",
                  summary: "Warning",
                  detail:
                    this.i18nService.translate("Duplicate detection: ") +
                    x.workEffortName +
                    " " +
                    x.rateTypeIdDescription +
                    " " +
                    dateMsg.getDate() +
                    " / " +
                    (dateMsg.getMonth() + 1) +
                    " / " +
                    dateMsg.getFullYear(),
                },
              ];
            }
          });
        }
      });
      if (control) {
        return true;
      } else {
        return false;
      }
    }
  }

  deleteRowSelected() {
    if (this.selectionTimeEntrys.length > 0) {
      this.selectionTimeEntrys.forEach((element) => {
        if (this.existTimeEntries(element)) {
          this.gridArray.shift();
        } else {
          this.confirmationService.confirm({
            message: this.i18nService.translate(
              "Are you sure you want to proceed with the deletion?"
            ),
            header: this.i18nService.translate("Attention"),
            icon: "pi pi-question",
            accept: async () => {
              let arrayIdTimeEntries = [];
              this.selectionTimeEntrys.forEach((element) => {
                element.timeEntriesCell.forEach((x) => {
                  if (x.timeEntry) {
                    arrayIdTimeEntries.push(x.timeEntry.timeEntryId);
                  }
                });
              });

              await this.timesheetService
                .deleteTimeEntry(arrayIdTimeEntries)
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
                  this.selectionTimeEntrys = [];
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
            },
          });
        }
      });
    }
  }

  existTimeEntries(element: TimeEntryRowCalendar): boolean {
    let control: boolean = true;
    element.timeEntriesCell.forEach((y) => {
      if (y.timeEntry) {
        control = false;
      }
    });
    return control;
  }

  dblclickRowEvent(element) {
    if (
      typeof element[0] === "number" &&
      this.selectedTimesheet.tsByUserLogin["updateable"] != "N"
    ) {
      this.headerDateDialog =
        getDayName(
          this.daysOfThisMonth[element[0]],
          this.languages,
          this.secondaryLang
        ) +
        " " +
        element[0] +
        "/" +
        (this.today.getMonth() + 1) +
        "/" +
        this.today.getFullYear();

      this.timeEntryRowDblClicked = element[1];
      this.displayModal = true;
      if (
        !!this.gridArray.filter(
          (x) => x.TimeEntryCalendarId === element[1].TimeEntryCalendarId
        )[0].timeEntriesCell[element[0] - 1].timeEntry
      ) {
        this.timeEntryDisplayModal = this.gridArray.filter(
          (x) => x.TimeEntryCalendarId === element[1].TimeEntryCalendarId
        )[0].timeEntriesCell[element[0] - 1];

        this.timeEntryDisplayModal.orderId = this.gridArray.filter(
          (x) => x.TimeEntryCalendarId === element[1].TimeEntryCalendarId
        )[0].timeEntriesCell[element[0] - 1].timeEntry.orderId;

        this.timeEntryDisplayModal.jobId = this.gridArray.filter(
          (x) => x.TimeEntryCalendarId === element[1].TimeEntryCalendarId
        )[0].timeEntriesCell[element[0] - 1].timeEntry.jobId;

        let te: TimeEntry = this.gridArray.filter(
          (x) => x.TimeEntryCalendarId === element[1].TimeEntryCalendarId
        )[0].timeEntriesCell[element[0] - 1].timeEntry;

        this.timeEntryDisplayModal.comments = te.comments;
        this.timeEntryDisplayModal.hours = te.hours;
      } else {
        this.timeEntryDisplayModal.hours = null;
        this.timeEntryDisplayModal.comments = null;
        this.timeEntryDisplayModal.timeEntryCellId = element[0];
        this.timeEntryDisplayModal.timeEntry = null;
        this.timeEntryDisplayModal.orderId = null;
        this.timeEntryDisplayModal.jobId = null;
      }

      this.timeEntryDisplayModal.fromDate = new Date(
        this.today.getFullYear(),
        this.today.getMonth(),
        element[0]
      );
      this.timeEntryDisplayModal.thruDate = new Date(
        this.today.getFullYear(),
        this.today.getMonth(),
        element[0]
      );
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
