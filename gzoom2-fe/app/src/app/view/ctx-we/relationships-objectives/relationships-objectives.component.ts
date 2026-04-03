import { Component, OnDestroy, OnInit } from "@angular/core";
import { ActivatedRoute, NavigationEnd, Params, Router } from "@angular/router";
import { I18NService } from "app/i18n/i18n.service";
import { FilterMatchMode, MenuItem, SelectItem } from "primeng/api";
import {
  Observable,
  Subject,
  Subscription,
  catchError,
  filter,
  from,
  lastValueFrom,
  map,
  mergeWith,
  of,
  startWith,
  switchMap,
  tap,
} from "rxjs";
import {
  HeadArray,
  ActionInput,
  ActionOutput,
  HeadFilter,
  Dropdown,
  Filter,
} from "app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration";
import { LanguageService } from "app/api/service/language.service";
import { WorkEffortAssocService } from "app/api/service/work-effort-assoc.service";
import { WorkEffortAssoc } from "app/api/model/workEffortAssoc";
import { WorkEffortAssocTypeService } from "app/api/service/work-effort-assoc-type.service";
import { WorkEffortViewService } from "app/api/service/work-effort-view.service";
import { WorkEffortMeasureService } from "app/api/service/work-effort-measure.service";
import { WorkEffortAssocEx } from "app/api/model/workEffortAssocEx";
import { UserPreferenceService } from "app/api/service/user-preference.service";
import { TableEditingCellService } from "app/commons/service/table-editing-cell.service";
import { LoaderService } from "app/shared/loader/loader.service";
import { InfoPage } from "app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration";
import { MsgService } from "app/commons/service/message.service";
import { FormBuilder, FormControl, FormGroup } from "@angular/forms";
import { Location } from "@angular/common";
import { WorkEffortService } from "app/api/service/work-effort.service";

@Component({
  selector: "app-relationships-objectives",
  templateUrl: "./relationships-objectives.component.html",
  styleUrls: ["./relationships-objectives.component.css"],
})
export class RelationshipsObjectivesComponent implements OnInit, OnDestroy {
  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  flag: boolean = false;
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

  gridArray: any[] = [];
  newRow: any;
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  languages: [] = [];
  infoCurrentPage: InfoPage = { offset: 0, limit: 50, filter: [] };

  dropdownWEATArray: MenuItem[] = [];
  dropdownWEArray: MenuItem[] = [];
  dropdownWEM: MenuItem[] = [];

  dropdownWEAT$: Observable<MenuItem[]>;
  dropdownWEV$: Observable<MenuItem[]>;

  itemDWSelected: any;
  dropdown: Dropdown[] = [];

  totalRecords: Number;

  selectedWorkEffortId: string;
  selectedCode: string;

  buttonBack: boolean = false;

  workEffortAssocEx$: Subscription;

  collapseFilters: boolean = false;
  collapseTab: boolean = true;
  labelInsertFilter: boolean = true;
  disableResetFilter: boolean = false;

  organizationId: string;

  filterForm: FormGroup = this.formBuilder.group({
    matchModeSearch: new FormControl<string>(FilterMatchMode.CONTAINS, {
      nonNullable: true,
    }),
    search: new FormControl<string>(null),
    workEffortAssocTypeId: new FormControl<string>(null),
    workEffortIdFrom: new FormControl<string>(null),
    workEffortIdTo: new FormControl<string>(null),
  });

  orderByColumn: SelectItem[];

  get search() {
    return this.filterForm.get("search");
  }
  get matchModeSearch() {
    return this.filterForm.get("matchModeSearch");
  }
  get workEffortAssocTypeId() {
    return this.filterForm.get("workEffortAssocTypeId");
  }
  get workEffortIdFrom() {
    return this.filterForm.get("workEffortIdFrom");
  }
  get workEffortIdTo() {
    return this.filterForm.get("workEffortIdTo");
  }

  filterTotal: boolean = false;

  state$: Observable<unknown> = this.router.events.pipe(
    tap(() => (this.loading = true)),
    filter((event) => event instanceof NavigationEnd),
    map(() => {
      return this.location.getState();
    }),
    startWith(this.location.getState()),
  );

  memState: { [key: string]: string | string[] | number };

  loadingDropdownWE = false;
  loadingDropdownWEAT = false;

  constructor(
    private route: ActivatedRoute,
    private readonly weaService: WorkEffortAssocService,
    private readonly weatService: WorkEffortAssocTypeService,
    private readonly weService: WorkEffortService,
    private readonly wemService: WorkEffortMeasureService,
    private readonly usrPreferenceService: UserPreferenceService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly tbService: TableEditingCellService,
    private readonly loaderService: LoaderService,
    private readonly router: Router,
    private formBuilder: FormBuilder,
    private location: Location,
    private msgService: MsgService,
  ) {
    this._reload = new Subject<void>();
  }

  ngOnDestroy(): void {
    this.workEffortAssocEx$?.unsubscribe();
  }

  async ngOnInit() {
    this.loaderService.hide();
    this.dropdownWEM.push({ label: "", id: "" });


    try {
      const userPref = await lastValueFrom(
        this.usrPreferenceService.getUserPreference("ORGANIZATION_PARTY").pipe(
          map((data) => data?.userPrefValue || '')
        )
      );
      this.organizationId = userPref;
    } catch (error) {
      console.error("Errore nel recupero dell'organizationId", error);
      this.organizationId = '';
    }

    this.infoCurrentPage.secondaryLang = this.secondaryLang;
    this.infoCurrentPage.organizationId = this.organizationId;

    this.orderByColumn = [
      { label: "Identificativo", value: "WORK_EFFORT_ASSOC_TYPE_ID" },
      { label: "Codice", value: "WORK_EFFORT_ID_TO" },
      { label: "Titolo", value: "WORK_EFFORT_ID_FROM" },
    ];

    this.filterForm.setValue({
      matchModeSearch: FilterMatchMode.CONTAINS,
      search: null,
      workEffortAssocTypeId: null,
      workEffortIdFrom: null,
      workEffortIdTo: null,
    });
    this.filterForm.markAsPristine();

    this.route.queryParams.subscribe((params) => {
      this.selectedWorkEffortId = params.id;
      this.selectedCode = params.code;
    });

    if (this.selectedCode == "ROO") {
      this.filterTotal = true;
      this.collapseFilters = true;
      this.collapseTab = false;
      this.labelInsertFilter = false;
      this.infoCurrentPage.filter.push({
        field: "workEffortIdFrom",
        value: this.selectedWorkEffortId,
      });
      this.buttonBack = true;
    } else if (this.selectedCode == "RDO") {
      this.filterTotal = true;
      this.collapseFilters = true;
      this.collapseTab = false;
      this.infoCurrentPage.filter.push({
        field: "workEffortIdTo",
        value: this.selectedWorkEffortId,
      });
      this.buttonBack = true;
      this.labelInsertFilter = false;
    }

    this.dropdownWEAT$ = this.setWEATDropdown();
    this.dropdownWEV$ = this.setWEDropdown();

    const reload = this._reload.pipe(
      filter(() => this.filterTotal),
      switchMap(() =>
        this.weaService.getWorkEffortAssocPagination(this.infoCurrentPage),
      ),
    );
    const w$ = this.route.data.pipe(
      map((data: { obss: WorkEffortAssocEx[] }) => data.obss),
      mergeWith(reload),
      catchError((error) => {
        this.msgService.error(error.error.message);
        return of([]);
      }),
    );

    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();
      this.headArray[8].pathIconFlag = lang[0];
      this.headArray[9].pathIconFlag = lang[1];
    } else {
      this.setHeadArray();
    }

    await this.setFilter();

    this.workEffortAssocEx$ = w$.subscribe((data) => {
      this.setGridArray(data);
    });
  }

  setFilter() {
    this.state$
      .pipe(
        map(async (state: { [key: string]: string | string[] | number }) => {
          state = this.location.getState() as {
            [key: string]: string | string[] | number;
          };
          this.memState = state;

          let labelWv = " ";
          if (this.selectedWorkEffortId) {
            await lastValueFrom(this.dropdownWEV$).then(x => labelWv = x.filter(y => y.id == this.selectedWorkEffortId)[0].label)
          }

          // workEffortId: this.selectedWorkEffortId? {label:  labelWv, id: this.selectedWorkEffortId} : (state.workEffortId as string) ?? null,

          this.filterForm.setValue({
            matchModeSearch:
              (state.matchModeSearch as string) ?? FilterMatchMode.CONTAINS,
            search: (state.search as string) ?? null,
            workEffortAssocTypeId:
              (state.workEffortAssocTypeId as string) ?? null,
            workEffortIdFrom: (this.selectedCode == "ROO" && this.selectedWorkEffortId) ? { label: labelWv, id: this.selectedWorkEffortId } : (state.workEffortIdFrom as string) ?? null,
            workEffortIdTo: (this.selectedCode == "RDO" && this.selectedWorkEffortId) ? { label: labelWv, id: this.selectedWorkEffortId } : (state.workEffortIdTo as string) ?? null,

          });

          this.filterForm.markAsPristine();
          return state;
        }),
      )
      .subscribe(async (data) => {

        if (
          this.filterForm.value.search != null ||
          this.filterForm.value.workEffortAssocTypeId != null ||
          this.filterForm.value.workEffortId != null ||
          this.filterForm.value.workEffortIdFrom != null ||
          this.filterForm.value.workEffortIdTo != null
        ) {
          this.headArray
            .filter((x) => x.dropdown)
            .forEach((y) => (y.dropdown.disabled = false));
          await this.setInfoCurrentPage();

          this.collapseTab = false;
          this.collapseFilters = true;
          this.labelInsertFilter = false;

        }
      });
  }

  async setInfoCurrentPage() {
    this.loading = true;
    this.filterTotal = true;
    this.infoCurrentPage.secondaryLang = this.secondaryLang;
    this.infoCurrentPage.filter = [];
    this.infoCurrentPage.filterGenericLabel = [];

    if (this.infoCurrentPage.filter.length == 0) {
      if (this.filterForm.value.search) {
        this.infoCurrentPage.matchModeSearch =
          this.filterForm.value.matchModeSearch;
        this.infoCurrentPage.filterGenericLabel.push(
          {
            field: "workEffortAssocTypeDescMatch",
            value: this.filterForm.value.search,
          },
          {
            field: "workEffortIdToDescMatch",
            value: this.filterForm.value.search,
          },
          {
            field: "workEffortIdFromDescMatch",
            value: this.filterForm.value.search,
          },
        );
      }
      if (this.filterForm.value.workEffortAssocTypeId) {
        this.infoCurrentPage.filter.push({
          field: "workEffortAssocTypeId",
          value: this.filterForm.value.workEffortAssocTypeId.id,
        });
        this.headArray
          .filter((x) => x.fieldName == "workEffortAssocTypeDesc")
          .forEach((y) => (y.dropdown.disabled = true));
      }

      if (this.filterForm.value.workEffortIdTo) {
        this.infoCurrentPage.filter.push({
          field: "workEffortIdTo",
          value: this.filterForm.value.workEffortIdTo.id,
        });
        this.headArray
          .filter((x) => x.fieldName == "workEffortIdToDesc")
          .forEach((y) => (y.dropdown.disabled = true));
      }

      if (this.filterForm.value.workEffortIdFrom) {
        this.infoCurrentPage.filter.push({
          field: "workEffortIdFrom",
          value: this.filterForm.value.workEffortIdFrom.id,
        });
        this.headArray
          .filter((x) => x.fieldName == "workEffortIdFromDesc")
          .forEach((y) => (y.dropdown.disabled = true));
      }
    } else {
      await from(this.infoCurrentPage.filter)
        .pipe(
          map((x) => {
            if (
              x.field === "workEffortAssocTypeDesc" ||
              x.field === "workEffortIdToDesc" ||
              x.field === "workEffortIdFromDesc"
            ) {
              x.value = this.filterForm.value.search;
            }
            return x;
          }),
        )
        .subscribe((updatedFilter) => {
          const filteredIndex = this.infoCurrentPage.filter.findIndex(
            (f) => f === updatedFilter,
          );
          if (filteredIndex !== -1) {
            this.infoCurrentPage.filter[filteredIndex] = updatedFilter;
          }
        });
    }
  }

  setGridArray(data) {
    if (this.filterTotal) {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      
      const weat$ = this.weaService.getWorkEffortAssocPaginationTotal(this.infoCurrentPage)
      lastValueFrom(weat$).then(x => {
        this.totalRecords = x;
      })

      data?.results.forEach((e) => {
        let ID =
          e.workEffortAssocTypeId +
          e.workEffortIdFrom +
          e.workEffortIdTo +
          e.fromDate;

        let record: any = {
          workEffortAssocTypeId: e.workEffortAssocTypeId,
          workEffortAssocTypeDesc: e.workEffortAssocType?.description,
          workEffortIdFrom: e.workEffortIdFrom,
          workEffortIdFromDesc: !this.secondaryLang
            ? e.workEffort?.workEffortName
            : e.workEffort?.workEffortNameLang,
          workEffortIdTo: e.workEffortIdTo,
          workEffortIdToDesc: !this.secondaryLang
            ? e.workEffort2?.workEffortName
            : e.workEffort2?.workEffortNameLang,
          fromDate: e.fromDate ? new Date(e.fromDate) : null,
          thruDate: !!e.thruDate ? new Date(e.thruDate) : null,
          sequenceNum: e.sequenceNum,
          assocWeight: e.assocWeight,
          comments: e.comments,
          commentsLang: e.commentsLang,
          weMeasureEvalId: e.weMeasureEvalId,
          weMeasureEvalIdDesc: !this.secondaryLang
            ? e.workEffortMeasure?.uomDescr
            : e.workEffortMeasure?.uomDescrLang,

          // dropdown: this.dropdown,
          variableGridArray: {
            id: ID,
            updated: false,
            buttonDetails: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            inputNew: false,
            dropdownData: true,
          },
        };

        this.gridArray.push(record)
      });
    }

    this.loading = false;
  }

  setHeadArray() {
    this.headArray.push(
      {
        head: this.i18nService.translate("Relationship"),
        fieldName: "workEffortAssocTypeDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        required: true,
        unique: true,
        readonly: true,
        width: "8vw",
        display: "flex",
        dropdown: {
          item: this.dropdownWEATArray,
          clear: false,
          loading: false,
          key: "workEffortAssocTypeId",
          virtualScrollItemSize: 5,
          virtualScroll: true,
          command: async () => {
            this.headArray
              .filter((x) => x.fieldName == "workEffortAssocTypeDesc")
              .forEach((y) => (y.dropdown.loading = true));
            this.setWEATDropdown().subscribe((x) =>
              this.headArray
                .filter((x) => x.fieldName == "workEffortAssocTypeDesc")
                .forEach((y) => (y.dropdown.item = x)),
            );
            this.headArray
              .filter((x) => x.fieldName == "workEffortAssocTypeDesc")
              .forEach((y) => (y.dropdown.item = this.dropdownWEATArray));
            this.headArray
              .filter((x) => x.fieldName == "workEffortAssocTypeDesc")
              .forEach((y) => (y.dropdown.loading = false));
          },
        },
      },
      {
        head: this.i18nService.translate("Work Effort Id From"),
        fieldName: "workEffortIdFromDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        required: true,
        unique: true,
        width: "15vw",
        inputWidth: "12vw",
        display: "flex",
        dropdown: {
          item: this.dropdownWEArray,
          clear: false,
          disableSort: true,
          loading: false,
          loadingFilter: false,
          key: "workEffortIdFrom",
          virtualScroll: true,
          virtualScrollItemSize: 10,
          command: async (filter?: boolean) => {
            this.headArray
              .filter((x) => x.fieldName == "workEffortIdFromDesc")
              .forEach((y) => (filter ? y.dropdown.loadingFilter = true : y.dropdown.loading = true));
            this.setWEDropdown().subscribe((x) => {
              this.headArray
                .filter((x) => x.fieldName == "workEffortIdFromDesc")
                .forEach((y) => (y.dropdown.item = x))
              this.headArray
                .filter((x) => x.fieldName == "workEffortIdFromDesc")
                .forEach((y) => (filter ? y.dropdown.loadingFilter = false : y.dropdown.loading = false))
            }
            );
            this.headArray
              .filter((x) => x.fieldName == "workEffortIdFromDesc")
              .forEach((y) => (y.dropdown.item = this.dropdownWEArray));

          },
        },
      },
      {
        head: this.i18nService.translate("Work Effort Id To"),
        fieldName: "workEffortIdToDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        required: true,
        unique: true,
        width: "15vw",
        inputWidth: "12vw",
        display: "flex",
        dropdown: {
          item: this.dropdownWEArray,
          clear: false,
          disableSort: true,
          loading: false,
          key: "workEffortIdTo",
          virtualScroll: true,
          virtualScrollItemSize: 10,
          command: async (filter?: boolean) => {
            this.headArray
              .filter((x) => x.fieldName == "workEffortIdToDesc")
              .forEach((y) => (filter ? y.dropdown.loadingFilter = true : y.dropdown.loading = true));
            this.setWEDropdown().subscribe((x) => {
              this.headArray
                .filter((x) => x.fieldName == "workEffortIdToDesc")
                .forEach((y) => (y.dropdown.item = x))
              this.headArray
                .filter((x) => x.fieldName == "workEffortIdToDesc")
                .forEach((y) => (filter ? y.dropdown.loadingFilter = false : y.dropdown.loading = false))
            }
            );
            this.headArray
              .filter((x) => x.fieldName == "workEffortIdToDesc")
              .forEach((y) => (y.dropdown.item = this.dropdownWEArray));

          },
        },
      },
      {
        head: this.i18nService.translate("Start Date"),
        fieldName: "fromDate",
        actionInput: ActionInput.inputDate,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.dateFilter,
        required: true,
        unique: true,
        display: "flex",
      },
      {
        head: this.i18nService.translate("End Date"),
        fieldName: "thruDate",
        clearCalendar: true,
        actionInput: ActionInput.inputDate,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.dateFilter,
        display: "flex",
      },
      {
        head: this.i18nService.translate("Sequence"),
        fieldName: "sequenceNum",
        actionInput: ActionInput.inputLabelNumber,
        actionOutput: ActionOutput.outputLabelNumber,
        filter: HeadFilter.popUpFilter,
        required: false,
        content: "center",
        display: "flex",
      },
      {
        head: this.i18nService.translate("Weight"),
        fieldName: "assocWeight",
        actionInput: ActionInput.inputLabelNumber,
        actionOutput: ActionOutput.outputLabelNumber,
        filter: HeadFilter.popUpFilter,
        required: true,
        content: "center",
        display: "flex",
      },
      {
        head: this.i18nService.translate("Comment"),
        fieldName: "comments",
        flag: this.flag,
        actionInput: ActionInput.inputNotes,
        actionOutput: ActionOutput.outputNotes,
        filter: HeadFilter.textFilter,
        required: false,
        textLength: 2000,
        display: "flex",
        width: "10vw",
      },
    );

    if (this.i18nService.getLanguageType() == "BILING")
      this.headArray.push({
        head: this.i18nService.translate("Comment"),
        fieldName: "commentsLang",
        flag: true,
        actionInput: ActionInput.inputNotes,
        actionOutput: ActionOutput.outputNotes,
        filter: HeadFilter.textFilter,
        required: false,
        textLength: 2000,
        display: "flex",
        width: "10vw",
      });
    this.headArray.push(
      {
        head: this.i18nService.translate("ID Measure Connected"),
        fieldName: "weMeasureEvalIdDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        width: "15vw",
        inputWidth: "12vw",
        display: "flex",
        dropdown: {
          item: this.dropdownWEArray,
          clear: false,
          disableSort: true,
          loading: false,
          key: "weMeasureEvalId",
          virtualScroll: true,
          virtualScrollItemSize: 10,
          command: async () => {
            this.headArray
              .filter((x) => x.fieldName == "weMeasureEvalIdDesc")
              .forEach((y) => (filter ? y.dropdown.loadingFilter = true : y.dropdown.loading = true));
            if (this.itemDWSelected) { await this.setWEMDropdown(this.itemDWSelected.workEffortIdFrom) }
            else { await this.setAllWEMDropdown() }
            this.headArray
              .filter((x) => x.fieldName == "weMeasureEvalIdDesc")
              .forEach((y) => (y.dropdown.item = this.dropdownWEM));
            this.headArray
              .filter((x) => x.fieldName == "weMeasureEvalIdDesc")
              .forEach((y) => (filter ? y.dropdown.loadingFilter = false : y.dropdown.loading = false));
          },
        },
      },
      {
        head: "",
        width: "0.5vw",
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.actionDetails,
        filter: HeadFilter.null,
      },
    );
  }

  combineFiltersArrays(array1, array2) {
    let combinedValue: Filter[] = [];

    for (const item of array1) {
      let e = array2.find((element) => item.field === element.field);
      let f = this.filterForm.contains(item.field);

      if (!e && f) {
        combinedValue.push(item);
      }
    }

    for (const item of array2) {
      let e = array1.find((element) => item.field === element.field);

      if (!e) {
        combinedValue.push(item);
      } else {
        combinedValue.push({ field: item.field, value: item.value });
      }
    }
    return combinedValue;
  }

  async loadData(data) {
    this.infoCurrentPage.limit = data.limit;
    this.infoCurrentPage.offset = data.offset;
    this.infoCurrentPage.sortField = data.sortField;
    this.infoCurrentPage.sortOrder = data.sortOrder;
    this.infoCurrentPage.secondaryLang = this.secondaryLang;

    this.infoCurrentPage.filter = this.combineFiltersArrays(
      this.infoCurrentPage.filter,
      data.filter,
    );

    if (this.selectedCode == "ROO") {
      this.infoCurrentPage.filter.push({
        field: "workEffortIdFrom",
        value: this.selectedWorkEffortId,
      });
      this.buttonBack = true;
    } else if (this.selectedCode == "RDO") {
      this.infoCurrentPage.filter.push({
        field: "workEffortIdTo",
        value: this.selectedWorkEffortId,
      });
      this.buttonBack = true;
    }

    this._reload.next();
  }

  shareInfoPagination(data) {
    this.infoCurrentPage.limit = data.rows;
    this.infoCurrentPage.offset = data.first;

    const queryParams: Params = { limit: data.rows, offset: data.first };

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: "merge",
    });
  }

  setWEATDropdown(): Observable<MenuItem[]> {
    this.dropdownWEATArray = [];
    this.loadingDropdownWEAT = true;
    const obs$ = this.weatService.getWorkEffortAssocType();
    return obs$.pipe(
      map((data) =>
        data.map((x) => {
          this.loadingDropdownWEAT = false;
          return { label: x.description, id: x.workEffortAssocTypeId };
        }),
      ),
    );
  }

  setWEDropdown(): Observable<MenuItem[]> {
    this.dropdownWEArray = [];
    this.loadingDropdownWE = true;
    const obs$ = this.usrPreferenceService
      .getUserPreference("ORGANIZATION_PARTY")
      .pipe(
        switchMap((data) => {
          return this.weService.getWorkEffortDropdown(data.userPrefValue)
        }),
      );

    return obs$.pipe(
      tap(() => this.loadingDropdownWE = true),
      map((data) =>
        data.map((x) => {
          let lab;
          if (!!x.etch) {
            let title = !this.secondaryLang
              ? x.workEffortName
              : x.workEffortNameLang;
            lab = x.etch + (!!title ? " - " + title : "") + ' (' + (this.secondaryLang ? x.party.partyNameLang : x.party.partyName + ' - ' + (this.secondaryLang ? x.workEffortType.descriptionLang : x.workEffortType.description)) + ' )';
          } else
            lab = !this.secondaryLang ? x.workEffortName : x.workEffortNameLang + ' (' + (this.secondaryLang ? x.party.partyNameLang : x.party.partyName + ' - ' + (this.secondaryLang ? x.workEffortType.descriptionLang : x.workEffortType.description)) + ' )';

          return { label: lab, id: x.workEffortId };
        }),
      ),
      tap(() => this.loadingDropdownWE = false),
    );
  }

  async setWEMDropdown(workEffortIdFrom) {
    this.dropdownWEM = [];
    await lastValueFrom(this.wemService.getWeMeasureEvalId(workEffortIdFrom))
      .then((data) => {
        data.forEach((x) => {
          this.dropdownWEM.push({
            label: !this.secondaryLang ? x.uomDescr : x.uomDescrLang,
            id: x.workEffortMeasureId,
          });
        });
      })
      .catch((error) => console.log(error));
  }

  async setAllWEMDropdown() {
    this.dropdownWEM = [];
    await lastValueFrom(this.wemService.dropdownWorkEffortMeasureWEA())
      .then((data) => {
        console.log(data);
        data.forEach((x) => {
          this.dropdownWEM.push({
            label: !this.secondaryLang ? x.uomDescr : x.uomDescrLang,
            id: x.workEffortMeasureId,
          });
        });
      })
      .catch((error) => console.log(error));
  }

  openNew() {
    let ID = "new" + Math.random();
    let tmpElAdd = {
      workEffortAssocTypeId: null,
      workEffortIdFrom: null,
      workEffortIdFromDesc: null,
      workEffortIdTo: null,
      workEffortIdToDesc: null,
      fromDate: null,
      thruDate: null,
      sequenceNum: null,
      assocWeight: null,
      comments: null,
      commentsLang: null,
      weMeasureEvalId: null,
      weMeasureEvalIdDesc: null,
      dropdown: null,
      variableGridArray: {
        id: ID,
        updated: true,
        buttonDetails: false,
        inputLabeldata: true,
        inputLabelNumber: true,
        inputNotes: true,
        outputData: false,
        inputNew: true,
        dropdownData: true,
      },
    };

    this.elementToAdd = tmpElAdd;
    this.gridArray = [this.elementToAdd, ...this.gridArray];

  }

  saveNewAndOpen(gridElement) {
    if (this.create(gridElement)) {
      this.openNew();
    } else this.er = false;
  }

  saveAllElement(elementUpdated) {
    let newElement = elementUpdated.filter((x) =>
      x.variableGridArray.id.includes("new"),
    );
    if (newElement.length > 0) {
      this.reload = true;
      this.create(newElement);
      this.reload = false;
    }
    elementUpdated = elementUpdated.filter(
      (x) => !x.variableGridArray.id.includes("new"),
    );
    if (elementUpdated.length > 0) {
      this.update(elementUpdated);
    }
  }

  setIdColumnAutoComplete(newElement) {
    this.headArray.forEach((item) => {
      if (item.actionInput == ActionInput.autoCompleteDropdown) {
        let keyName = this.headArray
          .filter((x) => x.fieldName == item.fieldName)
          .map((y) => y.dropdown.key)[0];

        let id = this.gridArray.filter(
          (x) => x.variableGridArray.id == newElement[0].variableGridArray.id,
        )[0][keyName].id;

        this.gridArray.filter(
          (x) => x.variableGridArray.id == newElement[0].variableGridArray.id,
        )[0][keyName] = id;
      }
    });
  }

  create(gridElement): boolean {
    // Add for autocomplete column for set columnid
    this.setIdColumnAutoComplete(gridElement);

    gridElement.forEach(async (e) => {
      let obj = new WorkEffortAssoc(
        e.workEffortIdFrom,
        e.workEffortIdTo,
        e.workEffortAssocTypeId,
        e.sequenceNum,
        e.fromDate,
        e.thruDate,
        e.assocWeight,
        e.comments,
        e.commentsLang,
        e.weMeasureEvalId,
      );

      if ((!!obj.thruDate && obj.fromDate <= obj.thruDate) || !!!obj.thruDate) {
        await this.weaService
          .createWorkEffortAssoc(obj)
          .then(() => {
            this.msgService.successCreate();
            let ID =
              e.workEffortAssocTypeId +
              e.workEffortIdFrom +
              e.workEffortIdTo +
              e.fromDate;
            e.variableGridArray.id = ID;
            e.variableGridArray.updated = false;
            e.variableGridArray.inputNew = false;
            e.variableGridArray.outputData = true;
            if (this.reload) this._reload.next();
          })
          .catch((error) => {
            this.msgService.error(error);
            this.er = true;
          });
      } else {
        this.msgService.errorDate(obj.fromDate, obj.thruDate);
        this.er = true;
      }
    });

    return !this.er;
  }

  update(gridElement) {
    gridElement.forEach(async (e) => {
      let obj = new WorkEffortAssoc(
        e.workEffortIdFrom,
        e.workEffortIdTo,
        e.workEffortAssocTypeId,
        e.sequenceNum,
        e.fromDate,
        e.thruDate,
        e.assocWeight,
        e.comments,
        e.commentsLang,
        e.weMeasureEvalId,
      );

      if ((!!obj.thruDate && obj.fromDate <= obj.thruDate) || !!!obj.thruDate) {
        await this.weaService
          .updateWorkEffortAssoc(obj)
          .then(() => {
            this.msgService.successUpdate();
            this._reload.next();
          })
          .catch((error) => {
            this.msgService.error(error);
            this.er = true;
          });
      } else {
        this.msgService.errorDate(obj.fromDate, obj.thruDate);
        this.er = true;
      }
    });

    return !this.er;
  }

  delete(listGridElement) {
    if (
      listGridElement.filter((x) => x.variableGridArray.id.includes("new"))
        .length > 0
    ) {
      this.newRow = listGridElement.filter((x) =>
        x.variableGridArray.id.includes("new"),
      );
      listGridElement = listGridElement.filter((x) => !this.newRow.includes(x));
      this.gridArray = this.gridArray.filter(
        (x) => !x.variableGridArray.id.includes("new"),
      );
      this.newRow = null;
    }
    if (listGridElement.length > 0) {
      listGridElement.forEach((e) => {
        let obj = new WorkEffortAssoc(
          e.workEffortIdFrom,
          e.workEffortIdTo,
          e.workEffortAssocTypeId,
          e.sequenceNum,
          e.fromDate,
          e.thruDate,
          e.assocWeight,
          e.comments,
          e.commentsLang,
          e.weMeasureEvalId,
        );
        this.weaService
          .deleteWorkEffortAssoc(obj)
          .then(() => {
            this.msgService.successDelete();
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(
              (r) => r.variableGridArray.id != e.variableGridArray.id,
            );
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.error(error.message);
            this._reload.next();
          });
      });
    } else {
      this.msgService.successDelete();
    }
  }

  shareDropdownItemRow(item) {
    this.itemDWSelected = item;
  }

  canDeactivate(): boolean {
    return (
      this.gridArray.filter((x) => x.variableGridArray.updated == true).length >
      0
    );
  }

  resetAllElement() {
    this.loading = true;
    this._reload.next();
  }

  async filter() {
    if (
      (this.filterForm.value.search != null && this.filterForm.value.search != "") ||
      this.filterForm.value.workEffortAssocTypeId != null ||
      this.filterForm.value.workEffortId != null ||
      this.filterForm.value.workEffortIdFrom != null ||
      this.filterForm.value.workEffortIdTo != null
    ) {
      this.setInfoCurrentPage();

      this.router.navigate(
        ['.'], {
        relativeTo: this.route,
        state: { ...this.filterForm.value, isSecondaryLang: this.secondaryLang, infoPage: this.infoCurrentPage }
      });
      this.loadData(this.infoCurrentPage);
      this.collapseTab = false;
      this.labelInsertFilter = false;
      this.collapseFilters = true;
    } else {
      this.filterForm.reset();
      this.labelInsertFilter = true;
    }
  }

  resetFilter() {
    this.filterForm.reset();
    this.collapseTab = false;
    this.collapseFilters = false;
    this.labelInsertFilter = true;
    this.disableResetFilter = false;
  }
}
