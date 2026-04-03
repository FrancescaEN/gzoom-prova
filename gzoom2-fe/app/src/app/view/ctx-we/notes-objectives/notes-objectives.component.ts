import { Component, OnDestroy, OnInit } from "@angular/core";
import {
  Subject,
  map,
  mergeMap,
  mergeWith,
  lastValueFrom,
  Observable,
  switchMap,
  catchError,
  of,
  Subscription,
  BehaviorSubject,
  filter,
  tap,
  startWith,
} from "rxjs";
import { FilterMatchMode, MenuItem } from "primeng/api";
import {
  ActionInput,
  ActionOutput,
  Dropdown,
  Filter,
  HeadArray,
  HeadFilter,
} from "app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration";
import { ActivatedRoute, NavigationEnd, Params, Router } from "@angular/router";
import { LanguageService } from "app/api/service/language.service";
import { I18NService } from "app/i18n/i18n.service";
import { WorkEffortNoteService } from "app/api/service/work-effort-note.service";
import { WorkEffortNoteExNoteData } from "app/api/model/workEffortNoteExNoteData";
import { WorkEffortViewService } from "app/api/service/work-effort-view.service";
import { UserPreferenceService } from "app/api/service/user-preference.service";
import { TableEditingCellService } from "app/commons/service/table-editing-cell.service";
import { WorkEffortTypeAttrService } from "app/api/service/work-effort-type-attr.service";
import { InfoPage } from "app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration";
import { LoaderService } from "app/shared/loader/loader.service";
import { MsgService } from "app/commons/service/message.service";
import { FormBuilder, FormControl, FormGroup } from "@angular/forms";
import { Location } from "@angular/common";
import { WorkEffortService } from "app/api/service/work-effort.service";

@Component({
  selector: "app-notes-objectives",
  templateUrl: "./notes-objectives.component.html",
  styleUrls: ["./notes-objectives.component.css"],
})
export class NotesObjectivesComponent implements OnInit, OnDestroy {
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();

  reload: boolean = false;
  elementToAdd: any;
  flag: boolean = false;
  loading: boolean = false;
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
  dropdownNN: Observable<MenuItem[]>;

  dropdownWEVSearch: MenuItem[] = [];
  dropdownWEVAutoCompl: MenuItem[] = [];
  dropdownNNAutoCompl: MenuItem[] = [];

  itemDWSelected: any;
  dropdown: Dropdown[] = [];
  dropdownY_N: MenuItem[] = [
    { label: this.i18nService.translate("Y"), id: "Y" },
    { label: this.i18nService.translate("N"), id: "N" },
  ];

  loadingLazy: boolean = false;
  totalRecords: Number;
  infoCurrentPage: InfoPage = { offset: 0, limit: 50, filter: [] };

  selectedWorkEffortId: string;
  selectedCode: string;

  buttonBack: boolean = false;

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
    noteName: new FormControl<string>(null),
    workEffortId: new FormControl<string>(null),
  });
  filterTotal: boolean = false;
  filterStartArray = [];

  query: string = null;
  workEffortNoteEx$: Subscription;

  state$: Observable<unknown> = this.router.events.pipe(
    tap(() => (this.loading = true)),
    filter((event) => event instanceof NavigationEnd),
    map(() => {
      return this.location.getState();
    }),
    startWith(this.location.getState())
  );

  memState: { [key: string]: string | string[] | number };

  loadingDropdownWE: boolean = false;
  loadingDropdownNN: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly router: Router,
    private readonly workEffortNoteService: WorkEffortNoteService,
    private readonly usrPreferenceService: UserPreferenceService,
    private readonly tbService: TableEditingCellService,
    private readonly loaderService: LoaderService,
    private readonly wetaService: WorkEffortTypeAttrService,
    private readonly weService: WorkEffortService,
    private location: Location,
    private formBuilder: FormBuilder,
    private msgService: MsgService,
  ) { }

  ngOnDestroy(): void {
    this.workEffortNoteEx$?.unsubscribe();
  }

  async ngOnInit(): Promise<void> {
    this.loaderService.hide();
    this.infoCurrentPage.secondaryLang = this.secondaryLang;

    this.route.queryParams.subscribe((params) => {
      this.selectedWorkEffortId = params.id;
      this.selectedCode = params.code;
    });

    const obs$ =
      this.usrPreferenceService.getUserPreference("ORGANIZATION_PARTY");
    obs$.subscribe((data) => {
      this.organizationId = data.userPrefValue;
    });


    if (this.selectedCode == "TN") {
      this.filterTotal = true;
      this.infoCurrentPage.filter.push({
        field: "workEffortId",
        value: this.selectedWorkEffortId,
      });
      this.buttonBack = true;
      this.labelInsertFilter = false;
      this.collapseFilters = true;
      this.collapseTab = false;
    }

    this.filterForm.setValue({
      matchModeSearch: FilterMatchMode.CONTAINS,
      search: null,
      noteName: null,
      workEffortId: null,
    });
    this.filterForm.markAsPristine();

    this.dropdownWEV = this.setWEDropdown();
    this.dropdownNN = this.setNNDropdown();

    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());
      this.setHeadArray();
      this.headArray[5].pathIconFlag = lang[0];
      this.headArray[6].pathIconFlag = lang[1];
    } else {
      this.setHeadArray();
    }

    const w$ = this.reload$.pipe(
      filter(() => this.filterTotal),
      switchMap(() => {
        return this.workEffortNoteService.getWorkEffortNoteExNoteDataListPagination(
          this.infoCurrentPage,
        );
      }),
    );

    await this.setFilter();

    this.workEffortNoteEx$ = w$.subscribe((y) => {
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

        let labelWv = " ";
        if (this.selectedWorkEffortId) {
          await lastValueFrom(this.dropdownWEV).then(x => labelWv = x.filter(y => y.id == this.selectedWorkEffortId)[0].label)
        }

        this.filterForm.setValue({
          matchModeSearch:
            (state.matchModeSearch as string) ?? FilterMatchMode.CONTAINS,
          search: (state.search as string) ?? null,
          noteName: (state.noteName as string) ?? null,
          workEffortId: this.selectedWorkEffortId ? { label: labelWv, id: this.selectedWorkEffortId } : (state.workEffortId as string) ?? null,
        });

        this.filterForm.markAsPristine();
        return state;
      })).subscribe(async (data) => {
        if (
          this.filterForm.value.search != null ||
          this.filterForm.value.workEffortId != null ||
          this.filterForm.value.noteName != null
        ) {

          await this.setInfoCurrentPage();

        }
      }
      )
  }

  setGridArray(y) {
    if (y.total > 0) {

      const wean$ = this.workEffortNoteService.getWorkEffortNoteExNoteDataListPaginationTotal(this.infoCurrentPage)
      lastValueFrom(wean$).then(x => {
        this.totalRecords = x
      })


      y.results.forEach((e) => {
        let ID = e.workEffortId + e.noteId;

        this.gridArray.push({
          noteId: e.noteId,
          workEffortId: e.workEffortId,
          workEffortIdDesc: this.secondaryLang
            ? e.workEffortView.weEtch +
            " - " +
            e.workEffortView?.workEffortNameLang
            : e.workEffortView.weEtch +
            " - " +
            e.workEffortView?.workEffortName,
          noteDateTime: !!e.noteData.noteDateTime
            ? new Date(e.noteData.noteDateTime)
            : null,

          noteName: e.noteData.noteName,
          noteNameDesc: !this.secondaryLang
            ? e.noteData.noteName
            : e.noteData.noteNameLang,
          noteInfo: e.noteData.noteInfo,
          noteInfoLang: e.noteData.noteInfoLang,
          sequenceId: e.sequenceId,
          internalNote: e.internalNote,
          internalNoteDesc: e.internalNote ? this.i18nService.translate(e.internalNote) : e.internalNote,
          isMain: e.isMain,
          isMainDesc: e.isMain ? this.i18nService.translate(e.isMain) : e.isMain,
          isHtml: e.isHtml,
          isHtmlDesc: e.isHtml ? this.i18nService.translate(e.isHtml) : e.isHtml,

          dropdown: this.dropdown,
          variableGridArray: {
            id: ID,
            updated: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            inputNew: false,
            dropdownData: false,
            isHtml: e.isHtml == "Y",
          },
        });
      });
    }
    this.loading = false;
  }

  shareInfoPagination(data) {
    const queryParams: Params = { limit: data.rows, offset: data.first };

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: "merge",
    });
  }

  setHeadArray() {
    this.headArray.push(
      {
        head: "Work Effort",
        fieldName: "workEffortIdDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        required: true,
        width: "20vw",
        inputWidth: "17vw",
        dropdown: {
          item: this.dropdownWEVAutoCompl,
          clear: false,
          disableSort: true,
          loading: false,
          key: "workEffortId",
          virtualScrollItemSize: 10,
          virtualScroll: true,
          command: async (filter?: boolean) => {
            this.headArray
              .filter((x) => x.fieldName == "workEffortIdDesc")
              .forEach((y) => (filter ? y.dropdown.loadingFilter = true : y.dropdown.loading = true));
            this.dropdownWEV.subscribe((x) => {
              this.headArray
                .filter((x) => x.fieldName == "workEffortIdDesc")
                .forEach((y) => (y.dropdown.item = x))
              this.headArray
                .filter((x) => x.fieldName == "workEffortIdDesc")
                .forEach((y) => (filter ? y.dropdown.loadingFilter = false : y.dropdown.loading = false));
            });
          },
        },
        readonly: true,
      },
      {
        head: "Date",
        fieldName: "noteDateTime",
        actionInput: ActionInput.inputDate,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.dateFilter,
        required: true,
        width: "8vw",
      },
      {
        head: "Note Name",
        fieldName: "noteNameDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        required: true,
        width: "8vw",
        dropdown: {
          item: this.dropdownNNAutoCompl,
          clear: false,
          disableSort: true,
          loading: false,
          virtualScrollItemSize: 10,
          virtualScroll: true,
          key: "noteName",
          command: async (filter: boolean) => {
            this.headArray
              .filter((x) => x.fieldName == "noteNameDesc")
              .forEach((y) => (filter ? y.dropdown.loadingFilter = true : y.dropdown.loading = true));
            this.setNNDropdown(this.itemDWSelected.workEffortId).subscribe((x) =>
              this.headArray
                .filter((x) => x.fieldName == "noteNameDesc")
                .forEach((y) => (y.dropdown.item = x)),
            );
            this.headArray
              .filter((x) => x.fieldName == "noteNameDesc")
              .forEach((y) => (filter ? y.dropdown.loadingFilter = false : y.dropdown.loading = false));
          },
        },
      },
      {
        head: "HTML",
        fieldName: "isHtmlDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        required: true,
        dropdown: { item: this.dropdownY_N, clear: false, key: "isHtml" },
        sortIcon: false,
        content: "center",
        width: "5vw",
      },
      {
        head: "Description",
        fieldName: "noteInfo",
        flag: this.flag,
        actionInput: ActionInput.inputHTML,
        actionOutput: ActionOutput.outputHTML,
        filter: HeadFilter.textFilter,
        required: true,
      },
    );
    if (this.i18nService.getLanguageType() == "BILING")
      this.headArray.push({
        head: "Description",
        fieldName: "noteInfoLang",
        flag: true,
        actionInput: ActionInput.inputHTML,
        actionOutput: ActionOutput.outputHTML,
        filter: HeadFilter.textFilter,
        required: true,
      });
    this.headArray.push(
      {
        head: "Sequence",
        fieldName: "sequenceId",
        actionInput: ActionInput.inputLabelNumber,
        actionOutput: ActionOutput.outputLabelNumber,
        filter: HeadFilter.popUpFilter,
        content: "center",
        required: true,
        width: "5vw",
      },
      {
        head: "Main",
        fieldName: "isMainDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        required: true,
        dropdown: { item: this.dropdownY_N, clear: false, key: "isMain" },
        sortIcon: false,
        content: "center",
        width: "5vw",
      },
    );
  }

  async loadData(data) {
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

    if (this.selectedCode == "TN") {
      this.infoCurrentPage.filter.push({
        field: "workEffortId",
        value: this.selectedWorkEffortId,
      });
      this.buttonBack = true;
    }

    this._reload.next();
  }

  setWEDropdown(): Observable<MenuItem[]> {
    this.dropdownWEVAutoCompl = [];
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
      map((data) =>
        data.sort((a, b) =>
          this.tbService.sortDataDW(
            a,
            b,
            !this.secondaryLang ? "workEffortName" : "workEffortNameLang",
          ),
        ),
      ),
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

  setNNDropdown(workEffortId?): Observable<MenuItem[]> {
    this.dropdownNNAutoCompl = [];
    this.loadingDropdownNN = true;
    if (workEffortId) {
      const obs$ = this.wetaService.getWorkEffortTypeAttrList(workEffortId);
      return obs$.pipe(
        tap(() => this.loadingDropdownNN = true),
        map((data) =>
          data.map((x) => {
            this.dropdownNNAutoCompl.push({
              label: !this.secondaryLang ? x.attrName : x.attrNameLang,
              id: x.attrName,
            });

            return {
              label: !this.secondaryLang ? x.attrName : x.attrNameLang,
              id: x.attrName,
            };
          }),
        ),
        tap(() => this.loadingDropdownNN = false),
      )
    } else {
      const obs$ = this.wetaService.getWorkEffortTypeAttrListAll();
      return obs$.pipe(
        tap(() => this.loadingDropdownNN = true),
        map((data) =>
          data.map((x) => {
            this.dropdownNNAutoCompl.push({
              label: !this.secondaryLang ? x.attrName : x.attrNameLang,
              id: x.attrName,
            });

            return {
              label: !this.secondaryLang ? x.attrName : x.attrNameLang,
              id: x.attrName,
            };
          }),
        ),
        tap(() => this.loadingDropdownNN = false)
      );
    }
  }

  shareDropdownItemRow(item) {
    this.itemDWSelected = item;    
  }

  notifyInputChanges(id) {
    this.gridArray.forEach((x) => {
      if (x.variableGridArray.id == id) {
        x.variableGridArray.isHtml = x.isHtml == "Y";
      }
    });
  }

  openNew() {
    let ID = "new" + Math.random();
    let tmpElAdd = {
      noteId: null,
      workEffortId: null,
      workEffortIdDesc: null,
      noteDateTime: null,

      noteName: null,
      noteNameDesc: null,
      noteInfo: null,
      noteInfoLang: null,
      sequenceId: null,
      internalNote: "N",
      internalNoteDesc: "N",
      isMain: "N",
      isMainDesc: "N",
      isHtml: "N",
      isHtmlDesc: "N",

      dropdown: null,
      variableGridArray: {
        id: ID,
        updated: false,
        inputLabeldata: true,
        inputLabelNumber: true,
        inputNotes: true,
        outputData: true,
        inputNew: false,
        dropdownData: true,
        isHtml: false,
      },
    };

    tmpElAdd.dropdown = this.dropdown;
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

  create(gridElement): boolean {
    // Add for autocomplete column for set columnid
    this.setIdColumnAutoComplete(gridElement);

    gridElement.forEach(async (e) => {
      let obj = new WorkEffortNoteExNoteData(
        e.workEffortId,
        e.noteId,
        e.internalNote,
        e.isMain,
        e.isHtml,
        e.sequenceId,
        e.isPosted,
        e.noteInfo,
        e.noteInfoLang,
        e.noteName,
        e.noteNameLang,
        e.noteParty,
        e.noteDateTime,
      );

      await this.workEffortNoteService
        .createWorkEffortNoteExNoteData(obj)
        .then((x) => {
          this.msgService.successCreate();
          let ID = x.workEffortId + x.noteId;
          e.workEffortId = x.workEffortId;
          e.noteId = x.noteId;
          e.variableGridArray.id = ID;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.dropdownData = false;

          if (this.reload) this._reload.next();
        })
        .catch((error) => {
          this.msgService.error(error);
          this.er = true;
        });
    });

    return !this.er;
  }

  update(gridElement) {
    gridElement.forEach(async (e) => {
      let obj = new WorkEffortNoteExNoteData(
        e.workEffortId,
        e.noteId,
        e.internalNote,
        e.isMain,
        e.isHtml,
        e.sequenceId,
        e.isPosted,
        e.noteInfo,
        e.noteInfoLang,
        e.noteName,
        e.noteNameLang,
        e.noteParty,
        e.noteDateTime,
      );

      await this.workEffortNoteService
        .updateWorkEffortNoteExNoteData(obj)
        .then(() => {
          this.msgService.successUpdate();
          this._reload.next();
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
        this.workEffortNoteService
          .deleteWorkEffortNoteExNoteData(e.workEffortId, e.noteId)
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
      this.filterForm.value.workEffortId != null ||
      this.filterForm.value.noteName != null
    ) {

      this.headArray
        .filter((x) => x.dropdown)
        .forEach((y) => (y.dropdown.disabled = false));
      this.setInfoCurrentPage();

      this.reloadData();

      this.collapseTab = false;
      this.labelInsertFilter = false;
      this.collapseFilters = true;
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

    this.filterTotal = true;
    this.gridArray = [];
    this.filterStartArray = [];
    this.infoCurrentPage.filter = [];
    this.infoCurrentPage.filterGenericLabel = [];
    this.infoCurrentPage.organizationId = this.organizationId;

    if (this.infoCurrentPage.filter.length == 0) {
      if (this.filterForm.value.search) {
        this.infoCurrentPage.matchModeSearch =
          this.filterForm.value.matchModeSearch;
        this.infoCurrentPage.filterGenericLabel.push(
          { field: "workEffortIdMatch", value: this.filterForm.value.search },
          { field: "noteNameMatch", value: this.filterForm.value.search },
        );
      }
      if (this.filterForm.value.workEffortId) {
        this.filterStartArray.push({
          field: "workEffortIdDesc",
          value: this.filterForm.value.workEffortId.id,
        });
        this.infoCurrentPage.filter.push({
          field: "workEffortIdDesc",
          value: this.filterForm.value.workEffortId.id,
        });
        this.headArray
          .filter((x) => x.fieldName == "workEffortIdDesc")
          .forEach((y) => (y.dropdown.disabled = true));
      }
      if (this.filterForm.value.noteName) {
        this.filterStartArray.push({
          field: "noteNameDesc",
          value: this.filterForm.value.noteName.label,
        });
        this.infoCurrentPage.filter.push({
          field: "noteNameDesc",
          value: this.filterForm.value.noteName.label,
        });
      }
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
