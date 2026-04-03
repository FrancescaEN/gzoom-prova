import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  OnInit,
} from "@angular/core";
import {
  BehaviorSubject,
  Observable,
  Subject,
  Subscription,
  filter,
  lastValueFrom,
  map,
  mergeMap,
  mergeWith,
  startWith,
  switchMap,
  tap,
} from "rxjs";
import {
  FilterMatchMode,
  MenuItem,
  Message as MessageError,
} from "primeng/api";
import {
  ActionInput,
  ActionOutput,
  Filter,
  HeadArray,
  HeadFilter,
  InfoPage,
} from "app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration";
import { ActivatedRoute, NavigationEnd, Params, Router } from "@angular/router";
import { WorkEffortMeasureService } from "app/api/service/work-effort-measure.service";
import { UserPreferenceService } from "app/api/service/user-preference.service";
import { I18NService } from "app/i18n/i18n.service";
import { LanguageService } from "app/api/service/language.service";
import { WorkEffortMeasure } from "app/api/model/workEffortMeasure";
import { WorkEffortViewService } from "app/api/service/work-effort-view.service";
import { GlAccountWithWorkEffortPurposeTypeViewService } from "app/api/service/gl-account-with-work-effort-purpose-type-view.service";
import { WorkEffortMeasExUom } from "app/api/model/workEffortMeasExUom";
import { DataStorageService } from "app/commons/service/data-storage.service";
import { WorkEffortView } from "app/api/model/workEffortView";
import { GlAccountWithWorkEffortPurposeTypeView } from "app/api/model/glAccountWithWorkEffortPurposeTypeView";
import { LoaderService } from "app/shared/loader/loader.service";
import { MsgService } from "app/commons/service/message.service";
import { ToolbarService } from "app/commons/service/toolbar.service";
import { FormBuilder, FormControl, FormGroup } from "@angular/forms";
import { Location } from "@angular/common";
import { GlAccountService } from "app/api/service/gl-account.service";
import { WorkEffortViewEx } from "app/api/model/workEffortViewEx";
import { WorkEffortService } from "app/api/service/work-effort.service";

@Component({
  selector: "app-measures-objectives",
  templateUrl: "./measures-objectives.component.html",
  styleUrls: ["./measures-objectives.component.css"],
  changeDetection: ChangeDetectionStrategy.Default,
})
export class MeasuresObjectivesComponent implements OnInit {
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();

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

  dropdownWEV: Observable<MenuItem[]>;
  dropdownWEArray: MenuItem[] = [];

  dropdownGAWWEPTV: Observable<MenuItem[]>;
  dropdownGAWWEPTVArray: MenuItem[] = [];

  workEffortView: WorkEffortViewEx[] = [];
  glAccountWithWorkEffortPurposeTypeView: GlAccountWithWorkEffortPurposeTypeView[] =
    [];
  infoCurrentPage: InfoPage = { offset: 0, limit: 50, filter: [] };
  totalRecords: Number;
  organizationId: string;
  selectedIndex;
  selectedWorkEffortId: string;
  selectedGlAccountId: string;
  buttonBack: boolean = false;

  collapseFilters: boolean = false;
  collapseTab: boolean = true;
  labelInsertFilter: boolean = true;
  disableResetFilter: boolean = false;

  filterForm: FormGroup = this.formBuilder.group({
    matchModeSearch: new FormControl<string>(FilterMatchMode.CONTAINS, {
      nonNullable: true,
    }),
    search: new FormControl<string>(null),
    glAccountId: new FormControl<string>(null),
    workEffortId: new FormControl<string>(null)
  });
  filterTotal: boolean = false;
  filterStartArray = [];

  memState: { [key: string]: string | string[] | number };

  workEffortMeasureEx: Observable<WorkEffortMeasExUom[]>;

  loadingDropdownWE: boolean = false;
  loadingDropdownGA: boolean = false;
  

  state$: Observable<unknown> = this.router.events.pipe(
    tap(() => (this.loading = true)),
    filter((event) => event instanceof NavigationEnd),
    map(() => {
      return this.location.getState();
    }),
    startWith(this.location.getState())
  );

  constructor(
    private route: ActivatedRoute,
    private readonly wemService: WorkEffortMeasureService,
    private readonly weService: WorkEffortService,
    private readonly gawweptvService: GlAccountWithWorkEffortPurposeTypeViewService,
    private readonly glAccountService: GlAccountService,
    private readonly usrPreferenceService: UserPreferenceService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly router: Router,
    private readonly dataStorageService: DataStorageService,
    private readonly loaderService: LoaderService,
    private formBuilder: FormBuilder,
    private location: Location,
    private msgService: MsgService,
    private toolbarService: ToolbarService,
  ) {
    this.toolbarService.setPrimaryBoardComponentButton();
  }

  async ngOnInit() {
    this.loaderService.hide();

    const obs$ =
      this.usrPreferenceService.getUserPreference("ORGANIZATION_PARTY");
    obs$.subscribe((data) => {
      this.organizationId = data.userPrefValue;
    });

    this.infoCurrentPage.organizationId = this.organizationId;
    this.infoCurrentPage.secondaryLang = this.secondaryLang;

    this.setHeadArray();

    this.route.queryParams.subscribe((params) => {
      this.selectedWorkEffortId = params.workEffortId;
      this.selectedGlAccountId = params.glAccountId;
    });

    if (this.selectedWorkEffortId || this.selectedGlAccountId) {
      this.labelInsertFilter = false;
      this.filterTotal = true;
      this.collapseFilters = true;
      this.collapseTab = false;
      this.buttonBack = true;

      if (this.selectedGlAccountId) {
        this.infoCurrentPage.filter.push({
          field: "workEffortId",
          value: this.selectedWorkEffortId,
        });
      }
      if (this.selectedGlAccountId) {
        this.infoCurrentPage.filter.push({
          field: "glAccountId",
          value: this.selectedGlAccountId,
        });
      }
    }

    this.dropdownWEV = this.setWEDropdown();
    this.dropdownGAWWEPTV = this.setGAWWEPTVDropdown();

    await this.setFilter();


    this.workEffortMeasureEx = this.reload$.pipe(
      filter(() => this.filterTotal),
      switchMap(() => {
        return this.wemService.getWorkEffortMeasExUomListPagination(
          this.infoCurrentPage
        )
      }),
    )

    this.workEffortMeasureEx.subscribe((y) => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);

      this.setGridArray(y);

      this.collapseTab = false;
      this.collapseFilters = true;
      this.labelInsertFilter = false;
    });


  }

  setFilter() {
    this.state$.pipe(
      map(async (state: { [key: string]: string | string[] | number }) => {
        state = this.location.getState() as {
          [key: string]: string | string[] | number;
        };
        this.memState = state;
        let labelGl = " ";
        let labelWv = " ";

        if (this.selectedGlAccountId) {
          await lastValueFrom(this.dropdownGAWWEPTV).then(x => labelGl = x.filter(y => y.id == this.selectedGlAccountId)[0].label)
        }

        if (this.selectedWorkEffortId) {
          await lastValueFrom(this.dropdownWEV).then(x => labelWv = x.filter(y => y.id == this.selectedWorkEffortId)[0].label)
        }



        this.filterForm.setValue({
          matchModeSearch:
            (state.matchModeSearch as string) ?? FilterMatchMode.CONTAINS,
          search: (state.search as string) ?? null,
          glAccountId: this.selectedGlAccountId ? { label: labelGl, id: this.selectedGlAccountId } : (state.glAccountId as string) ?? null,
          workEffortId: this.selectedWorkEffortId ? { label: labelWv, id: this.selectedWorkEffortId } : (state.workEffortId as string) ?? null,
        });

        this.filterForm.markAsPristine();
        return state;
      })).subscribe(async (data) => {
        if (
          this.filterForm.value.search != null ||
          this.filterForm.value.workEffortId != null ||
          this.filterForm.value.glAccountId != null
        ) {
          this.headArray
            .filter((x) => x.dropdown)
            .forEach((y) => (y.dropdown.disabled = false));
          await this.setInfoCurrentPage();
        }
      }
      )
  }

  setGridArray(data) {

    const weat$ = this.wemService.getWorkEffortMeasExUomListPaginationTotal(this.infoCurrentPage)
    lastValueFrom(weat$).then(x => {
      this.totalRecords = x
    })

    data.results.forEach((e) => {

      let detail = [
        {
          label: this.i18nService.translate("Measurement detail"),
          icon: "pi pi-angle-right",
          command: () => this.toDetail("detail"),
        },
      ];

      if (e.uom.uomTypeId == "RATING_SCALE")
        detail.push({
          label: this.i18nService.translate("Measurement scale"),
          icon: "pi pi-angle-right",
          command: () => this.toDetail("rating-scale"),
        });

      let record: any = {
        workEffortMeasureId: e.workEffortMeasureId,
        workEffortId: e.workEffortId,
        workEffortIdDesc: this.secondaryLang ? e.workEffort.workEffortNameLang : e.workEffort.workEffortName,
        glAccountId: e.glAccountId,
        glAccountIdDesc: this.secondaryLang
          ? e.glAccount?.accountCode + " - " + e.glAccount?.accountNameLang
          : e.glAccount?.accountCode + " - " + e.glAccount?.accountName,

        variableGridArray: {
          id: e.workEffortMeasureId,
          updated: false,
          buttonMultipleDetails: true,
          itemsDetail: detail,
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

    this.loading = false;
  }

  setHeadArray() {
    this.headArray.push(
      {
        head: "ID",
        fieldName: "workEffortMeasureId",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        width: "10vw",
        display: "flex",
      },
      {
        head: "Work Effort",
        fieldName: "workEffortIdDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        required: true,
        readonly: true,
        width: "40vw",
        inputWidth: "17vw",
        display: "flex",
        dropdown: {
          item: this.dropdownWEArray,
          clear: false,
          disableSort: true,
          loading: false,
          key: "workEffortId",
          virtualScrollItemSize: 10,
          virtualScroll: true,
          disabled: false,
          command: async (filter?: boolean) => {
            this.headArray
              .filter((x) => x.fieldName == "workEffortIdDesc")
              .forEach((y) => (filter? y.dropdown.loadingFilter = true : y.dropdown.loading = true));

            this.setWEDropdown().subscribe((x) =>{
              this.headArray
                .filter((x) => x.fieldName == "workEffortIdDesc")
                .forEach((y) => (y.dropdown.item = x))
                this.headArray
                .filter((x) => x.fieldName == "workEffortIdDesc")
                .forEach((y) => (filter? y.dropdown.loadingFilter = false : y.dropdown.loading = false));
            });
          },
        },
      },
      {
        head: "Unit Cont./Extr.",
        fieldName: "glAccountIdDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        required: true,
        readonly: true,
        width: "40vw",
        inputWidth: "17vw",
        display: "flex",
        dropdown: {
          item: this.dropdownGAWWEPTVArray,
          clear: false,
          disableSort: false,
          loading: false,
          key: "glAccountId",
          virtualScrollItemSize: 10,
          virtualScroll: true,
          disabled: false,
          command: async (filter?: boolean) => {
            this.headArray
              .filter((x) => x.fieldName == "glAccountIdDesc")
              .forEach((y) => (filter? y.dropdown.loadingFilter = true : y.dropdown.loading = true));

            this.setGAWWEPTVDropdown().subscribe((x) =>
              this.headArray
                .filter((x) => x.fieldName == "glAccountIdDesc")
                .forEach((y) => (y.dropdown.item = x)),
            );
            this.headArray
              .filter((x) => x.fieldName == "glAccountIdDesc")
              .forEach((y) => (filter? y.dropdown.loadingFilter = false : y.dropdown.loading = false));
          },
        },
      },
      {
        head: "",
        fieldName: "detail",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.actionDetails,
        filter: HeadFilter.null,
      },
    );
  }

  shareInfoPagination(data) {
    const queryParams: Params = { limit: data.rows, offset: data.first };

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: "merge",
    });
  }

  setIdColumnAutoComplete(newElement) {
    this.headArray.forEach((item) => {
      if (item.actionInput == ActionInput.autoCompleteDropdown) {
        let keyName = this.headArray
          .filter((x) => x.fieldName == item.fieldName)
          .map((y) => y.dropdown.key)[0];
        let id = this.gridArray.filter(
          (x) => x.variableGridArray.id == newElement[0].variableGridArray.id,
        )[0][keyName]?.id;
        this.gridArray.filter(
          (x) => x.variableGridArray.id == newElement[0].variableGridArray.id,
        )[0][keyName] = id;
      }
    });
  }

  shareItemEvent(data) {
    this.selectedIndex = data;
  }

  toDetail(component: string) {
    let elWorkEffortView = this.workEffortView.filter(
      (x) => x.workEffortId == this.selectedIndex.workEffortId,
    )[0];
    let elGlAccountWithWorkEffortPurposeTypeView =
      this.glAccountWithWorkEffortPurposeTypeView.filter(
        (x) => x.glAccountId == this.selectedIndex.glAccountId,
      )[0];
    let etch = elWorkEffortView.weEtch ?? "";
    let label = elWorkEffortView.workEffortName ?? "";

    this.dataStorageService.setData(
      this.router.url +
      `/${this.selectedIndex.workEffortMeasureId}/${component}`,
      "WORK_EFFORT0",
      etch + ". " + label,
    );

    label = elWorkEffortView.workEffortNameLang ?? "";
    this.dataStorageService.setData(
      this.router.url +
      `/${this.selectedIndex.workEffortMeasureId}/${component}`,
      "WORK_EFFORT1",
      etch + ". " + label,
    );

    etch = elGlAccountWithWorkEffortPurposeTypeView.accountCode ?? "";
    label = elGlAccountWithWorkEffortPurposeTypeView.accountName ?? "";
    this.dataStorageService.setData(
      this.router.url +
      `/${this.selectedIndex.workEffortMeasureId}/${component}`,
      "GL_ACCOUNT0",
      etch + ". " + label,
    );

    label = elGlAccountWithWorkEffortPurposeTypeView.accountNameLang ?? "";
    this.dataStorageService.setData(
      this.router.url +
      `/${this.selectedIndex.workEffortMeasureId}/${component}`,
      "GL_ACCOUNT1",
      etch + ". " + label,
    );

    this.router.navigate(
      [`${this.selectedIndex.workEffortMeasureId}/${component}`],
      {
        state: {
          ...this.memState,
        },
        relativeTo: this.route,
      },
    );
  }

  setWEDropdown(): Observable<MenuItem[]> {
    this.dropdownWEArray = [];
    this.loadingDropdownWE = true;
    const obs$ = this.usrPreferenceService
      .getUserPreference("ORGANIZATION_PARTY")
      .pipe(
        switchMap((data) => {
          return this.weService.getWorkEffortDropdown(data.userPrefValue);
        }),
      );

    return obs$.pipe(
      tap(() => this.loadingDropdownWE = true),
      map((data) => (this.workEffortView = data)),
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
      tap(() => this.loadingDropdownWE = false)
    )

  }

  setGAWWEPTVDropdown(): Observable<MenuItem[]> {
    this.dropdownGAWWEPTVArray = [];
    const obs$ = this.glAccountService.selectGlAccountByOrgId();
    
    return obs$.pipe(
      tap(() => this.loadingDropdownGA = true),
      map((data) => (this.glAccountWithWorkEffortPurposeTypeView = data)),
      map((data) =>
        data.map((x) => {
          let lab;
          if (!!x.accountCode) {
            let title = !this.secondaryLang ? x.accountName : x.accountNameLang;
            lab = x.accountCode + (!!title ? " - " + title : "");
          } else lab = !this.secondaryLang ? x.accountName : x.accountNameLang;

          this.dropdownGAWWEPTVArray.push({ label: lab, id: x.glAccountId })
          return { label: lab, id: x.glAccountId };
        }),
      ),
      tap(() => this.loadingDropdownGA = false),
    );
  }

  openNew() {
    this.elementToAdd = {
      workEffortMeasureId: null,
      workEffortId: null,
      workEffortIdDesc: null,
      glAccountId: null,
      glAccountIdDesc: null,
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
        buttonMultipleDetails: false,
      },
    };

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
  }

  create(gridElement): boolean {
    // Add for autocomplete column for set columnid
    this.setIdColumnAutoComplete(gridElement);

    gridElement.forEach(async (e) => {
      let obj = new WorkEffortMeasure();

      obj.workEffortMeasureId = e.workEffortMeasureId;
      obj.workEffortId = e.workEffortId;
      obj.glAccountId = e.glAccountId;

      await this.wemService
        .createWorkEffortMeasure(obj)
        .then((x) => {
          this.msgService.successCreateWithId(x.workEffortMeasureId);

          e.workEffortMeasureId = x.workEffortMeasureId;
          e.variableGridArray.id = x.workEffortMeasureId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonMultipleDetails = true;
          let detail = [
            {
              label: this.i18nService.translate("Measurement detail"),
              icon: "pi pi-angle-right",
              command: () => this.toDetail("detail"),
            },
          ];

          if (x.uom.uomTypeId && x.uom.uomTypeId == "RATING_SCALE")
            detail.push({
              label: this.i18nService.translate("Measurement scale"),
              icon: "pi pi-angle-right",
              command: () => this.toDetail("rating-scale"),
            });

          e.variableGridArray.itemsDetail = detail;

          if (this.reload) this._reload.next();
        })
        .catch((error) => {
          this.msgService.error(error);
          this.er = true;
        });
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
        this.wemService
          .deleteWorkEffortMeasure(e.workEffortMeasureId)
          .then(() => {
            this.msgService.successDeleteWithId(e.workEffortMeasureId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(
              (r) => r.variableGridArray.id != e.variableGridArray.id,
            );
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.error(error.message);
            this._reload.next();
            return;
          });
        this.toolbarService.setDisabledDelete(true);
      });
    } else {
      this.msgService.successDelete();
    }
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
      this.infoCurrentPage.organizationId = this.organizationId;

      if (this.selectedWorkEffortId) {
        this.infoCurrentPage.filter.push({
          field: "workEffortId",
          value: this.selectedWorkEffortId,
        });
        this.buttonBack = true;
      }

      if (this.selectedGlAccountId) {
        this.infoCurrentPage.filter.push({
          field: "glAccountId",
          value: this.selectedGlAccountId,
        });
        this.buttonBack = true;
      }

    }

    this._reload.next();

  }

  async filter() {
    if (
      (this.filterForm.value.search != null && this.filterForm.value.search != "") ||
      this.filterForm.value.workEffortId != null ||
      this.filterForm.value.glAccountId != null
    ) {

      this.setInfoCurrentPage();

      this.reloadData();

      this.collapseTab = false;
      this.collapseFilters = true;
      this.labelInsertFilter = false;



    } else {
      this.filterForm.reset();
      this.labelInsertFilter = true;
    }



  }

  reloadData() {
    this.router.navigate(
      ['.'], {
      relativeTo: this.route,
      state: { ...this.filterForm.value, isSecondaryLang: this.secondaryLang, infoPage: this.infoCurrentPage }
    });
    this._reload.next();
  }

  setInfoCurrentPage() {
    this.loading = true;
    this.gridArray = [];

    this.filterStartArray = [];
    this.infoCurrentPage.filter = [];
    this.infoCurrentPage.filterGenericLabel = [];
    this.infoCurrentPage.organizationId = this.organizationId;

    if (this.infoCurrentPage.filter.length == 0) {
      if (this.filterForm.value.search) {
        this.infoCurrentPage.matchModeSearch = this.filterForm.value.matchModeSearch;
        this.infoCurrentPage.filterGenericLabel.push(
          { field: "workEffortIdMatch", value: this.filterForm.value.search },
          { field: "glAccountIdMatch", value: this.filterForm.value.search },
        );
      }

      if (this.filterForm.value.workEffortId) {
        this.filterStartArray.push({
          field: "workEffortId",
          value: this.filterForm.value.workEffortId.id,
        });
        this.infoCurrentPage.filter.push({
          field: "workEffortId",
          value: this.filterForm.value.workEffortId.id,
        });
        this.headArray
          .filter((x) => x.fieldName == "workEffortIdDesc")
          .forEach((y) => (y.dropdown.disabled = true));
      }
      if (this.filterForm.value.glAccountId) {
        this.filterStartArray.push({
          field: "glAccountId",
          value: this.filterForm.value.glAccountId.id,
        });
        this.infoCurrentPage.filter.push({
          field: "glAccountId",
          value: this.filterForm.value.glAccountId.id,
        });
        this.headArray
          .filter((x) => x.fieldName == "glAccountIdDesc")
          .forEach((y) => (y.dropdown.disabled = true));
      }
    }

    this.filterTotal = true;
  }

  resetFilter() {
    this.filterForm.reset();
    this.collapseTab = false;
    this.collapseFilters = false;
    this.labelInsertFilter = true;
    this.disableResetFilter = false;
  }
}
