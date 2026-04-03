import { Component, OnDestroy, OnInit } from "@angular/core";
import {
  UntypedFormArray,
  UntypedFormControl,
  UntypedFormGroup,
} from "@angular/forms";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { TimesheetService } from "app/api/service/timesheet.service";
import { I18NService } from "app/i18n/i18n.service";
import {
  ConfirmationService,
  MenuItem,
  Message,
  MessageService,
} from "primeng/api";
import { Timesheet } from "../../../../api/model/timesheet";
import {
  Subject,
  Subscription,
  lastValueFrom,
  map,
  mergeMap,
  mergeWith,
} from "rxjs";
import { UserPreferenceService } from "app/api/service/user-preference.service";
import {
  HeadArray,
  HeadFilter,
  ActionInput,
  ActionOutput,
  Filter,
  InfoPage,
} from "app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration";
import { Message as MessageError } from "primeng/api";
import { LanguageService } from "app/api/service/language.service";
import { LoaderService } from "app/shared/loader/loader.service";
import { ToolbarService } from "app/commons/service/toolbar.service";

@Component({
  selector: "app-timesheet-table",
  templateUrl: "./timesheet-table.component.html",
  styleUrls: ["./timesheet-table.component.css"],
})
export class TimesheetTableComponent implements OnInit, OnDestroy {
  timesheets: Timesheet[];
  organizationSelected = "Company";
  context: string;
  params: any;
  isAdmin: boolean;
  _reload: Subject<void>;
  gridArray: Timesheet[] = [];
  form: {
    [name: string]: UntypedFormGroup | UntypedFormControl | UntypedFormArray;
  };
  dataTable: any;
  selectionTimesheets: any[] = [];
  msgs: Message[] = [];
  error = "";
  loading: boolean = true;
  iconCustom: string;
  messagesError: MessageError[] = [];
  timeentryMapFormat: string;
  secondaryLang: boolean;
  infoCurrentPage: InfoPage = { offset: 0, limit: 50, secondaryLang: false };
  filters: Filter[] = [];
  typeDropdownInputArray = new Map();
  filterValueDefault = new Map();
  totalRecords: Number;
  dropdownWorkEffortTypePeriod: MenuItem[] = [];
  dropdownStatus: MenuItem[] = [];
  dropdownUpdateable: MenuItem[] = [];

  headArray: HeadArray[] = [
    {
      head: this.i18nService.translate("timesheetId"),
      fieldName: "timesheetId",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      display: "none",
    },
    {
      head: this.i18nService.translate("Period"),
      fieldName: "workEffortTypePeriod",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      width: "15vw",
      sortIcon: true,
      display: "flex",
      dropdown: {
        item: this.dropdownWorkEffortTypePeriod,
        clear: true,
        key: "workEffortTypePeriod",
        command: async () => {
          this.dropdownWorkEffortTypePeriod = [];
          this.headArray
            .filter((x) => x.fieldName == "workEffortTypePeriod")
            .forEach((y) => (y.dropdown.loading = true));
          await this.setsCustomTimePeriodDropdown();
          this.headArray
            .filter((x) => x.fieldName == "workEffortTypePeriod")
            .forEach(
              (y) => (y.dropdown.item = this.dropdownWorkEffortTypePeriod),
            );
          this.headArray
            .filter((x) => x.fieldName == "workEffortTypePeriod")
            .forEach((y) => (y.dropdown.loading = false));
        },
      },
    },
    {
      head: this.i18nService.translate("Subject"),
      fieldName: "partyName",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      width: "15vw",
      sortIcon: true,
      display: "flex",
    },
    {
      head: this.i18nService.translate("PartyStructure"),
      fieldName: "partyStructure",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      width: "30vw",
      sortIcon: true,
      display: "flex",
    },
    {
      head: this.i18nService.translate("Actual"),
      fieldName: "actualHours",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelNumber,
      width: "6vw",
      filter: HeadFilter.popUpFilter,
      content: "center",
      sortIcon: true,
      display: "flex",
    },
    {
      head: this.i18nService.translate("Status"),
      fieldName: "description",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      width: "13vw",
      sortIcon: true,
      display: "flex",
      dropdown: {
        item: this.dropdownStatus,
        clear: true,
        key: "description",
        command: async () => {
          this.dropdownStatus = [];
          this.headArray
            .filter((x) => x.fieldName == "description")
            .forEach((y) => (y.dropdown.loading = true));
          await this.setDropdownStatus();
          this.headArray
            .filter((x) => x.fieldName == "description")
            .forEach((y) => (y.dropdown.item = this.dropdownStatus));
          this.headArray
            .filter((x) => x.fieldName == "description")
            .forEach((y) => (y.dropdown.loading = false));
        },
      },
    },
    {
      head: this.i18nService.translate("updatable"),
      fieldName: "updatable",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      content: "center",
      width: "10vw",
      sortIcon: false,
      display: "flex",
      filter: HeadFilter.dropdownFilterCustom
    },
    {
      head: "",
      fieldName: "buttonDetails",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.actionDetails,
      content: "left",
      width: "8vw",
    }
  ];

  timesheetServiceSubscription: Subscription;
  timesheetServiceAdminSubscription: Subscription;
  userPreferenceServiceSubscription: Subscription;
  contextSubscription: Subscription;

  constructor(
    private readonly timesheetService: TimesheetService,
    private readonly confirmationService: ConfirmationService,
    private readonly userPreferenceService: UserPreferenceService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    public readonly i18nService: I18NService,
    private messageService: MessageService,
    private languageService: LanguageService,
    private readonly loaderService: LoaderService,
    private toolbarService: ToolbarService,
  ) {
    this._reload = new Subject<void>();
  }

  ngOnDestroy(): void {
    this.timesheetServiceSubscription?.unsubscribe();
    this.timesheetServiceAdminSubscription?.unsubscribe();
    this.userPreferenceServiceSubscription?.unsubscribe();
    this.contextSubscription?.unsubscribe();
  }

  async ngOnInit(): Promise<void> {
    this.loaderService.hide();
    this.collapseSidebar();
    this.secondaryLang = await this.languageService.secondaryLang();
    this.infoCurrentPage.secondaryLang = this.secondaryLang;
    const reloadedTimesheet = this._reload.pipe(
      mergeMap(() =>
        this.timesheetService.timesheetsPagination(this.infoCurrentPage),
      ),
    );

    this.typeDropdownInputArray.set("updatable", [
      { label: this.i18nService.translate("Yes"), id: "Y" },
      { label: this.i18nService.translate("No"), id: "N" },
    ]);

    this.toolbarService.setDisabledNew(true);
    this.toolbarService.setDisabledDelete(true);

    this.userPreferenceServiceSubscription = this.userPreferenceService
      .getUserPreference("ORGANIZATION_PARTY")
      .subscribe({
        next: (data) => {
          this.organizationSelected = data.userPrefValue;
        },
        error: (error) => {
          console.log("error", error);
          this.messagesError = [
            { severity: "error", summary: "Error", detail: error.message },
          ];
        },
      });

    this.context = this.route.parent.snapshot.data.context;

    this.timesheetServiceAdminSubscription = this.timesheetService
      .isAdmin(this.context)
      .subscribe({
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

    await this.setsCustomTimePeriodDropdown();
    await this.setDropdownStatus();

    const timesheetObs = this.route.data.pipe(
      map((data: { timesheets: Timesheet[] }) => data.timesheets),
      mergeWith(reloadedTimesheet),
    );

    this.timesheetServiceSubscription = timesheetObs.subscribe({
      next: (data) => {
        this.timesheets = data.results;
        this.gridArray = [];
        this.timesheets.forEach((element, index) => {
          this.totalRecords = element["totalRow"];
          this.gridArray.push({
            idNumber: index,
            workEffortTypePeriod: this.secondaryLang
              ? element.customTimePeriod.periodNameLang
              : element.customTimePeriod.periodName,
            partyName:
              element.party["partyName"] +
              " (" +
              element.partyParentRole["parentRoleCode"] +
              ")",
            partyStructure: this.secondaryLang
              ? " (" +
                element.partyParentRoleUser["parentRoleCode"] +
                ") " +
                element.partyStructure["partyNameLang"]
              : " (" +
                element.partyParentRoleUser["parentRoleCode"] +
                ") " +
                element.partyStructure["partyName"],
            contractHours: element.contractHours,
            actualHours: element.actualHours,
            abbreviation: this.secondaryLang
              ? element.uom["descriptionLang"]
              : element.uom["description"],
            description: this.secondaryLang
              ? element.statusItem["descriptionLang"]
              : element.statusItem["description"],
            timesheetId: element.timesheetId,
            updatable:
              element.tsByUserLogin["updateable"] == "Y"
                ? this.i18nService.translate("Yes")
                : this.i18nService.translate("No"),
            variableGridArray: {
              buttonDetails: true,
              loadingButtonDetails: false,
              id: element.timesheetId,
              outputData: true,
            },
          });
        });
        this.loading = false;
      },
      error: (error) => {
        console.log("error", error);
        this.messagesError = [
          { severity: "error", summary: "Error", detail: error.message },
        ];
      },
    });

  }

  async setsCustomTimePeriodDropdown() {
    const obs$ =
      this.timesheetService.timesheetsCustomTimePeriodDropdownFilter();

    await lastValueFrom(obs$)
      .then((data) => {
        data.forEach((x) => {
          this.dropdownWorkEffortTypePeriod.push({
            label: this.secondaryLang ? x.periodNameLang : x.periodName,
            id: x.customTimePeriodId,
          });
        });
      })
      .catch((error) => console.log(error));
  }

  async setDropdownStatus() {
    const obs$ = this.timesheetService.timesheetsStatusDropdownFilter();
    await lastValueFrom(obs$)
      .then((data) => {
        data.forEach((x) => {
          this.dropdownStatus.push({
            label: this.secondaryLang ? x.descriptionLang : x.description,
            id: x.statusId,
          });
        });
      })
      .catch((error) => console.log(error));
  }

  setGridArray(data) {
    this.loading = true;
    this.gridArray = [];
    this.filters = [];

    if (data.field && data.$event.value) {
      this.filters.push({
        field: data.field,
        value: data.$event.value ?? null,
        matchMode: null,
      });

      this.infoCurrentPage.filter = this.filters;
    } else if (data.$event) {
      if (data.$event.sortOrder) {
        this.infoCurrentPage.sortOrder = data.$event.sortOrder;
        this.infoCurrentPage.sortField = data.$event.sortField;
      }

      if (data.$event.filters?.workEffortTypePeriod) {
        this.infoCurrentPage.limit = data.$event.rows;
        this.infoCurrentPage.offset = data.$event.first;

        let updateable = null;

        updateable = this.typeDropdownInputArray
          .get("updatable")
          .filter((x) => x.id === data.$event.filters.updatable.value)[0]?.id;

        this.headArray.forEach((item) => {
          if (item.fieldName == "updatable") {
            if (updateable == null) {
              this.filters.push({
                field: "updatable",
                value: "Y",
                matchMode: null,
              });
              this.infoCurrentPage.filter = this.filters;
            } else {
              this.filters.push({
                field: "updatable",
                value: updateable ?? null,
                matchMode: data.$event.filters.updatable.matchMode,
              });
            }
            console.log(this.filters);
          }
          if (
            data.$event.filters[item.fieldName] &&
            item.fieldName != "updatable"
          ) {
            if (!!data.$event.filters[item.fieldName].value) {
              this.filters.push({
                field: item.fieldName,
                value: data.$event.filters[item.fieldName].value ?? null,
                matchMode: data.$event.filters[item.fieldName].matchMode,
              });
            } else {
              this.filters.push({
                field: item.fieldName,
                value: null,
                matchMode: data.$event.filters[item.fieldName].matchMode,
              });
            }
          }
        });

        this.infoCurrentPage.filter = this.filters;
      } else {
        this.infoCurrentPage.filter = [];
        this.infoCurrentPage.limit = 50;
        this.infoCurrentPage.offset = 0;
      }
    }

    this._reload.next();
  }

  shareInfoPagination(data) {
    const queryParams: Params = { limit: data.rows, offset: data.first };

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: "merge",
    });
  }

  shareDescriptorTable(data) {
    this.dataTable = data;
  }

  shareSelectionItem(data) {
    this.selectionTimesheets = data;
    if (this.selectionTimesheets.length == 0) {
      this.toolbarService.setDisabledDelete(true);
    } else {
      if (this.isAdmin) {
        this.toolbarService.setDisabledDelete(false);
      }
    }
  }

  deleteRowSelected() {
    if (this.selectionTimesheets.length > 0) {
      this.confirmationService.confirm({
        message: this.i18nService.translate(
          "Are you sure you want to proceed with the deletion?",
        ),
        header: this.i18nService.translate("Attention"),
        icon: "pi pi-question",
        accept: () => {
          let arrayIdTimesheed = [];
          this.selectionTimesheets.forEach((element) => {
            arrayIdTimesheed.push(element.timesheetId);
          });
          this.timesheetService
            .deleteTimesheet(arrayIdTimesheed)
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
              this.loading = true;
            })
            .catch((error) => {
              console.log("error", error);
              this.messagesError = [
                {
                  severity: "error",
                  summary: "Error",
                  detail: this.i18nService.translate(
                    "It is not possible to delete this timesheet",
                  ),
                },
              ];
              this.selectionTimesheets = [];
            });
        },
      });
    }
  }

  async goToTimeEntry(data) {
    this.params = this.timesheetService.params(data.timesheetId);

    await this.params.forEach((element) => {
      element[0].noteInfo.split(";").forEach((item) => {
        if (item.split("=")[0] != "") {
          var itemLeft = item.split("=")[0].replace(/\s/g, "");

          switch (itemLeft) {
            case "timeentryMapFormat":
              this.timeentryMapFormat = item.split("=")[1].split('"')[1];
              break;
          }
        }
      });
    });

    if (this.timeentryMapFormat == "DMY") {
      this.router.navigate([`M/${data.variableGridArray.id}`], {
        relativeTo: this.route,
      });
    } else if (this.timeentryMapFormat == "MMY") {
      this.router.navigate([`M/${data.variableGridArray.id}`], {
        relativeTo: this.route,
      });
    } else if (this.timeentryMapFormat == "DDM") {
      this.router.navigate([`C/${data.variableGridArray.id}`], {
        relativeTo: this.route,
      });
    }
  }

  collapseSidebar() {
    const dom: any = document.querySelector("body");
    const menu: any = document.querySelector("#sidebar");
    dom.classList.add("push-right");
    menu.classList.add("collapse");
  }
}
