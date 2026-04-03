import { Component, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, Subject, Subscription, filter, lastValueFrom, map, mergeMap, mergeWith, startWith, switchMap, tap } from 'rxjs';
import { FilterMatchMode, MenuItem } from "primeng/api";
import { Table } from 'primeng/table';
import { WorkEffortEx } from 'app/api/model/workEffortEx';
import { ActivatedRoute, NavigationEnd, Params, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { LanguageService } from 'app/api/service/language.service';
import { WorkEffortService } from 'app/api/service/work-effort.service';
import { LoaderService } from 'app/shared/loader/loader.service';
import { WorkEffortTypeService } from 'app/api/service/work-effort-type.service';
import { StatusItemService } from 'app/api/service/status-item.service';
import { PartyRoleService } from 'app/api/service/party-role.service';
import { WorkEffort } from 'app/api/model/work-effort';
import { MsgService } from 'app/commons/service/message.service';
import { ToolbarService } from 'app/commons/service/toolbar.service';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Location } from "@angular/common";
import { Filter, InfoPage, ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';

interface MenuItemCustom {
  label: string;
  id: any;
}

@Component({
  selector: 'app-goals',
  templateUrl: './goals.component.html',
  styleUrls: ['./goals.component.css']
})
export class GoalsComponent implements OnInit, OnDestroy {
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  reload: boolean = false;
  elementToAdd: any;
  dataTable: Table;
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

  gridArray: WorkEffortEx[] = [];
  newRow: any;
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();

  dropdownActualStatus$: Observable<MenuItem[]>;
  dropdownOrgUnit$: Observable<MenuItem[]>;
  dropdownWorkEffortType$: Observable<MenuItem[]>;
  dropdownActualStatusArray: MenuItem[] = [];
  dropdownOrgUnitArray: MenuItemCustom[] = [];
  dropdownWorkEffortTypeArray: MenuItem[] = [];

  itemDWSelected: any;
  dataSourceTypeService: any;
  itemsButtonSlideMenu: MenuItem[];
  itemsButtonLinkMenu: MenuItem[];
  workEffortEx$: Subscription;
  workEffortIdSelect: string;

  workEffortEx: Observable<WorkEffortEx[]>

  collapseFilters: boolean = false;
  collapseTab: boolean = true;
  filterTotal: boolean = false;
  labelInsertFilter: boolean = true;
  disableResetFilter: boolean = false;
  filterStartArray = [];

  filterForm: FormGroup = this.formBuilder.group({
    matchModeSearch: new FormControl<string>(FilterMatchMode.CONTAINS, { nonNullable: true }),
    search: new FormControl<string>(null),
    workEffortTypeIdDescFilter: new FormControl<string>(null),
    currentStatusDescFilter: new FormControl<string>(null),
    orgDescFilter: new FormControl<string>(null),
  });

  state$: Observable<unknown> = this.router.events.pipe(
    tap(() => (this.loading = true)),
    filter((event) => event instanceof NavigationEnd),
    map(() => {
      return this.location.getState();
    }),
    startWith(this.location.getState())
  );
  memState: { [key: string]: string | string[] | number };
  infoCurrentPage: InfoPage = { offset: 0, limit: 50, filter: [] };
  totalRecords: Number;

  constructor(
    private route: ActivatedRoute,
    private readonly router: Router,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly workEffortService: WorkEffortService,
    private readonly loaderService: LoaderService,
    private readonly workEffortTypeService: WorkEffortTypeService,
    private readonly statusItemService: StatusItemService,
    private readonly partyRoleService: PartyRoleService,
    private msgService: MsgService,
    private location: Location,
    private formBuilder: FormBuilder,
    private toolbarService: ToolbarService

  ) {
    this.toolbarService.setPrimaryBoardComponentButton();
  }

  async ngOnInit(): Promise<void> {
    this.loaderService.hide();
    this.infoCurrentPage.secondaryLang = this.secondaryLang;



    this.filterForm.setValue({
      matchModeSearch: FilterMatchMode.CONTAINS,
      search: null,
      workEffortTypeIdDescFilter: null,
      currentStatusDescFilter: null,
      orgDescFilter: null
    })
    this.filterForm.markAsPristine();

    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();
      this.headArray[3].pathIconFlag = lang[0];
      this.headArray[4].pathIconFlag = lang[1];

    } else { this.setHeadArray(); }

    await this.setFilter();

    this.workEffortEx = this.reload$.pipe(
      filter(() => this.filterTotal),
      switchMap(() => {
        return this.workEffortService.workEffortExFilter(this.infoCurrentPage)
      })
    );

    this.dropdownActualStatus$ = this.setcurrentStatusDescDropdown();
    this.dropdownOrgUnit$ = this.setOrgDescDescDropdown();
    this.dropdownWorkEffortType$ = this.setworkEffortTypeIdDescDropdown();

    this.workEffortEx$ = this.workEffortEx.subscribe((data) => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);

      this.setGridArray(data);

      this.collapseTab = false;
      this.collapseFilters = true;
      this.labelInsertFilter = false;

    });

    this.itemsButtonLinkMenu = [
      {
        label: this.i18nService.translate("Attachments-Objectives"),
        icon: "pi pi-search",
        command: () => this.onRowDetail("AO"),
      },
      {
        label: this.i18nService.translate("Subjects Objectives"),
        icon: "pi pi-search",
        command: () => this.onRowDetail("SO"),
      },
      {
        label: this.i18nService.translate("Relations Origin Objective"),
        icon: "pi pi-search",
        command: () => this.onRowDetail("ROO"),
      },
      {
        label: this.i18nService.translate("Relations Destination  Objective"),
        icon: "pi pi-search",
        command: () => this.onRowDetail("RDO"),
      },
      {
        label: this.i18nService.translate("Target Notes"),
        icon: "pi pi-search",
        command: () => this.onRowDetail("TN"),
      },
      {
        label: this.i18nService.translate("Measures Objective"),
        icon: "pi pi-search",
        command: () => this.onRowDetail("MO"),
      },
    ];

    this.itemsButtonSlideMenu = [
      {
        label: this.i18nService.translate("Details objective"),
        icon: "pi pi-search",
        command: () => this.onRowDetail("DO"),
      },
      {
        label: this.i18nService.translate("Data History"),
        icon: "pi pi-search",
        command: () => this.onRowDetail("SD"),
      },
      // {
      //   label: "Communications Objective",
      //   icon: "pi pi-search",
      //   command: () => this.onRowDetail("CO"),
      // },
    ];
  }

  ngOnDestroy(): void {
    this.workEffortEx$?.unsubscribe();
  }

  setGridArray(data) {
    data.forEach((e) => {

      this.totalRecords = e["totalRow"];

      let ID = e.workEffortId;
      this.gridArray.push({
        workEffortId: e.workEffortId,
        organizationId: e.organizationId,
        sourceReferenceId: e.sourceReferenceId,
        workEffortName: e.workEffortName,
        workEffortNameLang: e.workEffortNameLang,
        workEffortTypeId: e.workEffortTypeId,
        workEffortTypeIdDesc: this.secondaryLang ? e.workEffortType.descriptionLang : e.workEffortType.description,
        statusItemDesc: this.secondaryLang ? e.statusItem.descriptionLang : e.statusItem.description,
        statusTypeDesc: e.statusType.description,
        currentStatusId: e.currentStatusId,
        currentStatusDesc: this.secondaryLang ? e.statusType.description + " - " + e.statusItem.descriptionLang : e.statusType.description + " - " + e.statusItem.description,
        estimatedStartDate: (!!e.estimatedStartDate) ? new Date(e.estimatedStartDate) : null,
        estimatedCompletionDate: (!!e.estimatedCompletionDate) ? new Date(e.estimatedCompletionDate) : null,
        orgUnitId: e.orgUnitId,
        orgUnitRoleTypeId: e.orgUnitRoleTypeId,
        partyParentRoleCode: e.partyParentRole.parentRoleCode,
        partyName: this.secondaryLang ? e.party.partyNameLang : e.party.partyName,
        orgDesc: this.secondaryLang ? e.partyParentRole.parentRoleCode + " - " + e.party.partyNameLang : e.partyParentRole.parentRoleCode + " - " + e.party.partyName,

        variableGridArray: {
          id: ID,
          updated: false,
          buttonMultipleDetails: true,
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
  }


  async setHeadArray() {

    this.headArray.push(
      {
        head: "Id",
        fieldName: "workEffortId",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
        required: true,
        unique: true,
        width: '10vw'
      },
      {
        head: "Code",
        fieldName: "sourceReferenceId",
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
        required: true,
        width: '10vw'
      },
      {
        head: "Title",
        fieldName: "workEffortName",
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
        required: true,
        width: '10vw',
        flag: this.flag
      });

    if (this.i18nService.getLanguageType() == "BILING") {
      this.headArray.push(
        {
          head: "Title",
          fieldName: "workEffortNameLang",
          actionInput: ActionInput.inputLabeldata,
          actionOutput: ActionOutput.outputLabelData,
          filter: HeadFilter.textFilter,
          textLength: 20,
          required: true,
          width: '10vw',
          flag: this.flag
        });
    }

    this.headArray.push(
      {
        head: "Tipology",
        fieldName: "workEffortTypeIdDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        required: true,
        textLength: 255,
        width: '11vw',
        dropdown: {
          item: this.dropdownWorkEffortTypeArray,
          clear: true,
          key: "workEffortTypeId",
          virtualScrollItemSize: 10,
          virtualScroll: true,
          disabled: false,
          command: async () => {
            this.dropdownWorkEffortTypeArray = [];
            this.headArray.filter(x => x.fieldName == 'workEffortTypeIdDesc').forEach(y => y.dropdown.loading = true);
            this.setworkEffortTypeIdDescDropdown().subscribe((x) =>
              this.headArray
                .filter((x) => x.fieldName == "workEffortTypeIdDesc")
                .forEach((y) => (y.dropdown.item = x)),
            );
            this.headArray.filter(x => x.fieldName == 'workEffortTypeIdDesc').forEach(y => y.dropdown.loading = false);
          },
        },
      },
      {
        head: "Actual State",
        fieldName: "currentStatusDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        required: true,
        textLength: 255,
        width: '11vw',
        dropdown: {
          item: this.dropdownActualStatusArray,
          clear: true,
          key: "currentStatusId",
          virtualScrollItemSize: 10,
          virtualScroll: true,
          disabled: false,
          command: async () => {
            this.dropdownActualStatusArray = [];
            this.headArray.filter(x => x.fieldName == 'currentStatusDesc').forEach(y => y.dropdown.loading = true);
            this.setcurrentStatusDescDropdown().subscribe((x) =>
              this.headArray
                .filter((x) => x.fieldName == "currentStatusDesc")
                .forEach((y) => (y.dropdown.item = x)),
            );
            this.headArray.filter(x => x.fieldName == 'currentStatusDesc').forEach(y => y.dropdown.loading = false);
          },
        },
      },
      {
        head: "Planned Start",
        fieldName: "estimatedStartDate",
        actionInput: ActionInput.inputDate,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.dateFilter,
        required: true,
        width: '8vw'
      },
      {
        head: "Expected End",
        fieldName: "estimatedCompletionDate",
        actionInput: ActionInput.inputDate,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.dateFilter,
        required: true,
        width: '8vw'
      },
      {
        head: "orgUnitId",
        fieldName: "orgDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        textLength: 20,
        width: '11vw',
        required: true,
        dropdown: {
          item: this.dropdownOrgUnitArray,
          clear: true,
          key: "orgUnitId",
          virtualScrollItemSize: 10,
          virtualScroll: true,
          disabled: false,
          command: async () => {
            this.dropdownOrgUnitArray = [];
            this.headArray.filter(x => x.fieldName == 'orgDesc').forEach(y => y.dropdown.loading = true);
            this.setOrgDescDescDropdown().subscribe((x) => {
              this.headArray
                .filter((x) => x.fieldName == "orgDesc")
                .forEach((y) => (y.dropdown.item = x))
            }

            );
            this.headArray.filter(x => x.fieldName == 'orgDesc').forEach(y => y.dropdown.loading = false);
          },
        },
      },
      { head: '', fieldName: 'null', actionInput: ActionInput.null, actionOutput: ActionOutput.actionDetails, filter: HeadFilter.null }
    );
  }

  setFilter() {

    this.state$.pipe(
      map(async (state: { [key: string]: string | string[] | number }) => {
        state = this.location.getState() as {
          [key: string]: string | string[] | number;
        };
        this.memState = state;

        this.filterForm.setValue({
          matchModeSearch: (state.matchModeSearch as string) ?? FilterMatchMode.CONTAINS,
          search: (state.search as string) ?? null,
          workEffortTypeIdDescFilter: (state.workEffortTypeIdDescFilter as string) ?? null,
          currentStatusDescFilter: (state.currentStatusDescFilter as string) ?? null,
          orgDescFilter: (state.orgDescFilter as string) ?? null,
        });

        this.filterForm.markAsPristine();
        return state;
      })).subscribe(async (data) => {

        if (this.filterForm.value.search != null || this.filterForm.value.workEffortTypeIdDescFilter != null || this.filterForm.value.currentStatusDescFilter != null || this.filterForm.value.orgDescFilter != null) {


          this.headArray.filter(x => x.dropdown).forEach(y => y.dropdown.disabled = false);
          await this.setInfoCurrentPage();

          this.collapseTab = false;
          this.collapseFilters = true;
          this.labelInsertFilter = false;

        }
      }
      )
  }

  setInfoCurrentPage() {

    this.loading = true;
    this.gridArray = [];

    this.filterStartArray = [];
    this.infoCurrentPage.filter = [];
    this.infoCurrentPage.filterGenericLabel = [];

    if (this.infoCurrentPage.filter.length == 0) {
      if (this.filterForm.value.search) {
        this.infoCurrentPage.matchModeSearch = this.filterForm.value.matchModeSearch;
        this.infoCurrentPage.filterGenericLabel.push(
          { field: 'workEffortNameMatch', value: this.filterForm.value.search },
          { field: 'currentStatusDescMatch', value: this.filterForm.value.search },
          { field: 'orgDescMatch', value: this.filterForm.value.search })
      }
      if (this.filterForm.value.workEffortTypeIdDescFilter) {
        this.filterStartArray.push({ field: 'workEffortTypeIdDesc', value: this.filterForm.value.workEffortTypeIdDescFilter.id })
        this.infoCurrentPage.filter.push({ field: 'workEffortTypeIdDesc', value: this.filterForm.value.workEffortTypeIdDescFilter.id })
        this.headArray.filter(x => x.fieldName == 'workEffortTypeIdDesc').forEach(y => y.dropdown.disabled = true);
      }
      if (this.filterForm.value.currentStatusDescFilter) {
        this.filterStartArray.push({ field: 'currentStatusDesc', value: this.filterForm.value.currentStatusDescFilter.id })
        this.infoCurrentPage.filter.push({ field: 'currentStatusDesc', value: this.filterForm.value.currentStatusDescFilter.id })
        this.headArray.filter(x => x.fieldName == 'currentStatusDesc').forEach(y => y.dropdown.disabled = true);
      }
      if (this.filterForm.value.orgDescFilter) {
        let obj = [];
        obj.push({ field: "partyId", value: this.filterForm.value.orgDescFilter.id.partyId }, { field: "roleTypeId", value: this.filterForm.value.orgDescFilter.id.roleTypeId })
        this.headArray.filter(x => x.fieldName == 'orgDesc').forEach(y => y.dropdown.disabled = true);
        this.filterStartArray.push({ field: 'orgDesc', objValue: obj })
        this.infoCurrentPage.filter.push({ field: 'orgDesc', objValue: obj })
      }
    }

    this.filterTotal = true;
  }

  setworkEffortTypeIdDescDropdown(): Observable<MenuItem[]> {
    this.dropdownWorkEffortTypeArray = [];
    const obs$ = this.workEffortTypeService.getAllWorkEffortTypes();
    return obs$.pipe(map(data => data.map(x => {
      return {
        label: this.secondaryLang ? x.descriptionLang : x.description,
        id: x.workEffortTypeId,
      }
    })))
  }

  setcurrentStatusDescDropdown(): Observable<MenuItem[]> {
    this.dropdownActualStatusArray = [];
    const obs$ = this.statusItemService.getStatusItemStateTo();
    return obs$.pipe(
      map(data => data.map(x => {
        return {
          label: this.secondaryLang ? x.statusType.description + "-" + x.descriptionLang : x.statusType.description + "-" + x.description,
          id: x.statusId,
        }
      }))
    )
  }

  setOrgDescDescDropdown(): Observable<MenuItemCustom[]> {
    this.dropdownOrgUnitArray = [];
    const obs$ = this.partyRoleService.getPartyRoleOrgId();
    return obs$.pipe(map(data => data.map(x => {
      return {
        label: this.secondaryLang ? x.partyParentRole.parentRoleCode + "-" + x.party.partyNameLang : x.partyParentRole.parentRoleCode + "-" + x.party.partyName, id: { partyId: x.partyId, roleTypeId: x.partyParentRole.roleTypeId },
      }
    })))
  }

  openNew() {
    let ID = "new" + Math.random();
    let tmpElAdd: WorkEffortEx = {
      workEffortId: null,
      organizationId: null,
      sourceReferenceId: null,
      workEffortNameLang: null,
      workEffortName: null,
      workEffortTypeId: null,
      workEffortTypeIdDesc: null,
      statusItemDesc: null,
      statusTypeDesc: null,
      currentStatusId: null,
      currentStatusDesc: null,
      estimatedStartDate: null,
      estimatedCompletionDate: null,
      orgUnitId: null,
      orgUnitRoleTypeId: null,
      partyParentRoleCode: null,
      partyName: null,
      variableGridArray: {
        id: ID,
        updated: true,
        buttonMultipleDetails: false,
        inputLabeldata: true,
        inputLabelNumber: true,
        inputNotes: true,
        outputData: false,
        inputNew: true,
        dropdownData: true,
      }

    }

    this.elementToAdd = tmpElAdd;
    this.gridArray = [this.elementToAdd, ...this.gridArray];
  }

  saveNewAndOpen(gridElement) {
    if (this.create(gridElement)) {
      this.openNew();
    }
    else this.er = false;
  }

  saveAllElement(elementUpdated: WorkEffortEx[]) {
    let newElement = elementUpdated.filter(x => x.variableGridArray.id.includes("new"));
    if (newElement.length > 0) {
      this.reload = true;
      this.create(newElement);
      this.reload = false;
    }
    elementUpdated = elementUpdated.filter(x => !x.variableGridArray.id.includes("new"));
    if (elementUpdated.length > 0) {

      this.update(elementUpdated);
    }

  }

  create(gridElement): boolean {
    gridElement.forEach(async e => {

      let obj = new WorkEffort();

      if (e.estimatedCompletionDate >= e.estimatedStartDate) {
        obj.workEffortId = e.workEffortId;
        obj.sourceReferenceId = e.sourceReferenceId;
        obj.workEffortNameLang = e.workEffortNameLang;
        obj.workEffortName = e.workEffortName;
        obj.workEffortTypeId = e.workEffortTypeId;
        obj.currentStatusId = e.currentStatusId;
        obj.estimatedStartDate = e.estimatedStartDate;
        obj.estimatedCompletionDate = e.estimatedCompletionDate;
        obj.orgUnitId = e.orgUnitId.partyId;
        obj.orgUnitRoleTypeId = e.orgUnitId.roleTypeId;

        await this.workEffortService.createWorkEffort(obj)
          .then((x) => {
            this.msgService.successCreate();
            let ID = x.workEffortId;
            e.variableGridArray.id = ID;
            e.variableGridArray.updated = false;
            e.variableGridArray.inputNew = false;
            e.variableGridArray.outputData = true;
            e.variableGridArray.buttonMultipleDetails = true;

            if (this.reload) this._reload.next()
          })
          .catch((error) => {
            this.msgService.errorWithId(error, obj.workEffortId);
            this.er = true;
          });
      } else {
        this.msgService.errorDate(e.estimatedStartDate, e.estimatedCompletionDate);
        this.er = true;
      }
    })
    return !this.er;

  }

  update(gridElement) {

    gridElement.forEach(async e => {
      if (e.estimatedCompletionDate >= e.estimatedStartDate) {

        await this.workEffortService.updateWorkEffort(e)
          .then(() => {
            this.msgService.successUpdate();
            this._reload.next()
          })
          .catch((error) => {
            this.msgService.errorWithId(error, e.workEffortId);
            this.er = true;
          });
      } else {
        this.msgService.errorDate(e.estimatedStartDate, e.estimatedCompletionDate);
        this.er = true;
      }
    });
    return !this.er;
  }

  delete(listGridElement: WorkEffortEx[]) {
    if (listGridElement.filter(x => x.variableGridArray.id.includes("new")).length > 0) {
      this.newRow = listGridElement.filter(x => x.variableGridArray.id.includes("new"));
      listGridElement = listGridElement.filter(x => !this.newRow.includes(x));
      this.gridArray = this.gridArray.filter(x => !x.variableGridArray.id.includes("new"));
      this.newRow = null;
    }
    if (listGridElement.length > 0) {
      listGridElement.forEach(e => {

        this.workEffortService.deleteWorkEffortTree(e.workEffortId)
          .then(() => {
            this.msgService.successDelete();
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.error(error.message);
            this._reload.next();
          });

      });

    }
    else {
      this.msgService.successDelete();
    }
  }

  shareDropdownItemRow(item) {
    this.itemDWSelected = item;
  }

  shareInfoPagination(data) {
    const queryParams: Params = { limit: data.rows, offset: data.first };
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: "merge",
      state: { ...this.filterForm.value, isSecondaryLang: this.secondaryLang, infoPage: this.infoCurrentPage }
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

  // toSubsystemDetails(itemClick) {
  //   let etch = itemClick.dataSourceId ?? "";
  //   let encodedURI = encodeURIComponent(itemClick.dataSourceId);

  //   this.dataStorageService.setData(this.router.url + `/${itemClick.dataSourceId}`, 'title', etch + ".");
  //   this.router.navigate([`${encodedURI}`], { relativeTo: this.route });
  // }

  shareItemEvent(data: WorkEffort) {
    this.workEffortIdSelect = data.workEffortId;
  }

  onRowDetail(type) {

    switch (type) {
      // case "CO":
      //   this.dataStorageService.setData(this.router.url + `/CO/${this.workEffortIdSelect}`, 'title', this.workEffortIdSelect + ".");
      //   this.router.navigate([`CO/${encodedURI}`], { relativeTo: this.route });
      //   break;
      case "AO":
        this.router.navigate([`../attachments-objectives`], { queryParams: { code: "AO", id: this.workEffortIdSelect }, relativeTo: this.route });
        break;
      case "MO":
        this.router.navigate([`../measures-objectives`], { queryParams: { workEffortId: this.workEffortIdSelect }, relativeTo: this.route });
        break;
      case "TN":
        this.router.navigate([`../notes-objectives`], { queryParams: { code: "TN", id: this.workEffortIdSelect }, relativeTo: this.route });
        break;
      case "RDO":
        this.router.navigate([`../relationship-objectives`], { queryParams: { code: "RDO", id: this.workEffortIdSelect }, relativeTo: this.route });
        break;
      case "ROO":
        this.router.navigate([`../relationship-objectives`], { queryParams: { code: "ROO", id: this.workEffortIdSelect }, relativeTo: this.route });
        break;
      case "SO":
        this.router.navigate([`../subjects-objectives`], { queryParams: { code: "SO", id: this.workEffortIdSelect }, relativeTo: this.route });
        break;
      case "DO":
        this.router.navigate([`${this.workEffortIdSelect}/DO`], { relativeTo: this.route });
        break;
      case "SD":
        this.router.navigate([`${this.workEffortIdSelect}/SD`], { relativeTo: this.route });
        break;
      default:
    }

  };

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  resetAllElement() {
    this.loading = true;
    this._reload.next();
  }

  async filter() {
    if ((this.filterForm.value.search != null && this.filterForm.value.search != "") || this.filterForm.value.workEffortTypeIdDescFilter != null || this.filterForm.value.currentStatusDescFilter != null || this.filterForm.value.orgDescFilter != null) {

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

  resetFilter() {
    this.filterForm.reset();
    this.collapseTab = false;
    this.collapseFilters = false;
    this.labelInsertFilter = true;
    this.disableResetFilter = false;
  }
}
