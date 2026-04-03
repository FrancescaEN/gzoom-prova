import { Component, OnDestroy, OnInit } from "@angular/core";
import { ActivatedRoute, NavigationEnd, Router } from "@angular/router";
import { LanguageService } from "app/api/service/language.service";
import { UserPreferenceService } from "app/api/service/user-preference.service";
import { I18NService } from "app/i18n/i18n.service";
import {
  BehaviorSubject,
  Observable,
  Subject,
  Subscriber,
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
import { FilterMatchMode, MenuItem } from "primeng/api";
import { Table } from "primeng/table";
import {
  ActionInput,
  ActionOutput,
  HeadArray,
  HeadFilter,
  Dropdown,
} from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { WorkEffortPartyAssignmentService } from "app/api/service/work-effort-party-assignment.service";
import { WorkEffortPartyAssignment } from "app/api/model/workEffortPartyAssignment";
import { WorkEffortViewService } from "app/api/service/work-effort-view.service";
import { UserPreference } from "app/shared/user-preference";
import { WorkEffortView } from "app/api/model/workEffortView";
import { RoleTypeService } from "app/api/service/role-type.service";
import { PartyRoleViewService } from "app/api/service/party-role-view.service";
import { TableEditingCellService } from "app/commons/service/table-editing-cell.service";
import { LoaderService } from "app/shared/loader/loader.service";
import { getDate } from "app/commons/utils/dateUtils";
import { MsgService } from "app/commons/service/message.service";
import { FormBuilder, FormControl, FormGroup } from "@angular/forms";
import { InfoPage } from "app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration";
import { RoleType } from "app/api/model/role-type";
import { Location } from "@angular/common";
import { WorkEffortService } from "app/api/service/work-effort.service";


@Component({
  selector: "app-subjects-objectives",
  templateUrl: "./subjects-objectives.component.html",
  styleUrls: ["./subjects-objectives.component.css"],
})
export class SubjectsObjectivesComponent implements OnInit, OnDestroy {
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  reload: boolean = false;

  elementToAdd: any;
  dataTable: Table;
  flag: boolean = false;
  loading: boolean = true;
  loadingFilter: boolean = false;
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


  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();

  gridArray: WorkEffortPartyAssignment[] = [];
  newRow: any;

  dropdownWorkEffortView: Observable<MenuItem[]>;
  dropdownRoleType: Observable<MenuItem[]>;
  dropdownParty: Observable<MenuItem[]>;

  dropdownWEArray: MenuItem[] = [];
  dropdownRoleTypeArray: MenuItem[] = [];
  dropdownPartyArray: MenuItem[] = [];


  workEffortView: WorkEffortView[] = [];
  dropdown: Dropdown[] = [];
  itemDWSelected: any;

  langType: string;
  languages: string[] = [];

  selectedWorkEffortId: string;
  selectedCode: string;

  buttonBack: boolean = false;

  workEffortPartyAssignments$: Subscription;

  collapseFilters: boolean = false;
  collapseTab: boolean = true;
  filterTotal: boolean = false;
  labelInsertFilter: boolean = true;
  disableResetFilter: boolean = false;

  filterForm: FormGroup = this.formBuilder.group({
    matchModeSearch: new FormControl<string>(FilterMatchMode.CONTAINS, { nonNullable: true }),
    search: new FormControl<string>(null),
    workEffortId: new FormControl<string>(null),
    roleTypeId: new FormControl<string>(null),
    partyId: new FormControl<string>(null),
  });

  workEffortPartyAssignments: Observable<WorkEffortPartyAssignment[]>

  infoCurrentPage: InfoPage = { offset: 0, limit: 0, filter: [] };

  totalRecords: Number = 0;

  state$: Observable<unknown> = this.router.events.pipe(
    tap(() => (this.loading = true)),
    filter((event) => event instanceof NavigationEnd),
    map(() => {
      return this.location.getState();
    }),
    startWith(this.location.getState())
  );

  memState: { [key: string]: string | string[] | number };


  constructor(
    private route: ActivatedRoute,
    private readonly usrPreferenceService: UserPreferenceService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly workEffortPartyAssignmntService: WorkEffortPartyAssignmentService,
    private readonly weService: WorkEffortService,
    private readonly roleTypeService: RoleTypeService,
    private readonly partyRoleViewService: PartyRoleViewService,
    private readonly msgService: MsgService,
    private location: Location,
    private readonly router: Router,
    private formBuilder: FormBuilder,
    private readonly loaderService: LoaderService

  ) {
  }

  ngOnDestroy(): void {
    this.workEffortPartyAssignments$?.unsubscribe();
  }

  async ngOnInit(): Promise<void> {
    this.loaderService.hide();
    this.infoCurrentPage.secondaryLang = this.secondaryLang;

    this.filterForm.setValue({
      matchModeSearch: FilterMatchMode.CONTAINS,
      search: null,
      workEffortId: null,
      roleTypeId: null,
      partyId: null
    })
    this.filterForm.markAsPristine();

    this.route.queryParams
      .subscribe(params => {
        this.selectedWorkEffortId = params.id;
        this.selectedCode = params.code;
      }
      );

    await this.setHeadArray();

    this.dropdownWorkEffortView = this.setWorkEffortDropdown();
    this.dropdownRoleType = this.setRoleTypeDropdown();
    this.dropdownParty = this.setPartyRoleTypeDropdown();

    this.gridArray = [];
    if (this.newRow) this.gridArray.push(this.newRow);

    if (this.selectedCode == "SO") {
      this.filterTotal = true;
      this.collapseFilters = true;
      this.collapseTab = false;
      this.buttonBack = true;
      this.labelInsertFilter = false;
      this.infoCurrentPage.filter.push({ field: "workEffortId", value: this.selectedWorkEffortId })
    }

    await this.setFilter();

    this.workEffortPartyAssignments = this.reload$.pipe(
      filter(() => this.filterTotal),
      switchMap(() => {
        return this.workEffortPartyAssignmntService.getWorkEffortPartyAssignmentListFilter(this.infoCurrentPage)
      })
    );

    this.workEffortPartyAssignments.subscribe((data) => {
      if (this.selectedCode == "SO") {
        this.setGridArrayWithId(data, this.selectedWorkEffortId)
      } else {
        this.setGridArray(data)
      }
    })


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
          await lastValueFrom(this.dropdownWorkEffortView).then(x => labelWv = x.filter(y => y.id == this.selectedWorkEffortId)[0].label)
        }

        this.filterForm.setValue({
          matchModeSearch:
            (state.matchModeSearch as string) ?? FilterMatchMode.CONTAINS,
          search: (state.search as string) ?? null,
          workEffortId: this.selectedWorkEffortId ? { label: labelWv, id: this.selectedWorkEffortId } : (state.workEffortId as string) ?? null,
          roleTypeId: (state.roleTypeId as string) ?? null,
          partyId: (state.partyId as string) ?? null,
        });

        this.filterForm.markAsPristine();
        return state;
      })).subscribe(async (data) => {

        if (this.filterForm.value.search != null || this.filterForm.value.workEffortId != null || this.filterForm.value.partyId != null || this.filterForm.value.roleTypeId != null) {

          this.headArray
            .filter((x) => x.dropdown)
            .forEach((y) => (y.dropdown.disabled = false));
          await this.setInfoCurrentPage();

          this.collapseTab = false;
          this.collapseFilters = true;
          this.labelInsertFilter = false;

        }
      }
      )
  }

  setGridArray(data) {

    this.gridArray = []
    if (this.filterTotal) {

      data.results.forEach((e) => {
        let ID = e.workEffortId + e.roleTypeId + e.partyId + e.fromDate;
        this.dropdown = [];

        this.dropdown["partyRoleViewDescription"] = {
          id: ID,
          fieldName: "partyRoleViewDescription",
          item: [
            { label: e.partyRoleView.partyName, id: e.partyRoleView.partyId },
          ],
          clear: true,
          loading: false,
          key: "partyId",
          command: async () => {
            let tmp = this.gridArray;

            tmp.filter((x) => x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].loading = true;
            this.gridArray = tmp;

            await this.setPartyRoleTypeIdDropdown(this.itemDWSelected.roleTypeId);

            let dp = tmp.filter((x) => x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].item;

            let keyExist = dp.map((item) => item["partyRoleViewDescription"]);
            let ray = [];
            this.dropdownParty.forEach((y) => {
              if (!keyExist.includes(y)) {
                ray.push(y);
              }
            });

            tmp.filter(
              (x) =>
                x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].item = ray;
            tmp.filter(
              (x) => x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].loading = false;
            this.gridArray = tmp;
          },
        };

        let record: WorkEffortPartyAssignment = {
          workEffortName: this.secondaryLang ? e.workEffortView.etch + " - " + e.workEffortView.workEffortNameLang : e.workEffortView.etch + " - " + e.workEffortView.workEffortName,
          workEffortId: e.workEffortId,
          roleTypeDescription: this.secondaryLang? e.roleType.descriptionLang : e.roleType.description,
          roleTypeId: e.roleTypeId,
          partyRoleViewDescription: e.partyRoleView.parentRoleCode + " - " + e.partyRoleView.partyName,
          partyId: e.partyId,
          fromDate: (!!e.fromDate) ? new Date(e.fromDate) : null,
          thruDate: (!!e.thruDate) ? new Date(e.thruDate) : null,
          roleTypeWeight: e.roleTypeWeight,
          comments: e.comments,
          commentsLang: e.commentsLang,

          dropdown: this.dropdown,
          variableGridArray: {
            id: ID,
            updated: false,
            buttonDetails: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            inputNew: false,
            dropdownData: false,
          },
        }

        this.gridArray = [record, ...this.gridArray];
      });
    }

    this.loading = false;
  }

  setGridArrayWithId(data, id) {
    this.gridArray = []
    if (this.filterTotal) {

      data.results.forEach((e) => {

        if (e.workEffortId == id) {
          let ID = e.workEffortId + e.roleTypeId + e.partyId + e.fromDate;
          this.dropdown = [];

          this.dropdown["partyRoleViewDescription"] = {
            id: ID,
            fieldName: "partyRoleViewDescription",
            item: [
              { label: e.partyRoleView.partyName, id: e.partyRoleView.partyId },
            ],
            clear: true,
            loading: false,
            key: "partyId",
            command: async () => {
              let tmp = this.gridArray;

              tmp.filter((x) => x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].loading = true;
              this.gridArray = tmp;

              await this.setPartyRoleTypeIdDropdown(this.itemDWSelected.roleTypeId);

              let dp = tmp.filter((x) => x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].item;

              let keyExist = dp.map((item) => item["partyRoleViewDescription"]);
              let ray = [];
              this.dropdownParty.forEach((y) => {
                if (!keyExist.includes(y)) {
                  ray.push(y);
                }
              });

              tmp.filter(
                (x) =>
                  x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].item = ray;
              tmp.filter(
                (x) => x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].loading = false;
              this.gridArray = tmp;
            },
          };

          let record: WorkEffortPartyAssignment = {
            workEffortName: this.secondaryLang ? e.workEffortView.etch + " - " + e.workEffortView.workEffortNameLang : e.workEffortView.etch + " - " + e.workEffortView.workEffortName,
            workEffortId: e.workEffortId,
            roleTypeDescription: this.secondaryLang ? e.roleType.descriptionLang : e.roleType.description,
            roleTypeId: e.roleTypeId,
            partyRoleViewDescription: e.partyRoleView.parentRoleCode + " - " + e.partyRoleView.partyName,
            partyId: e.partyId,
            fromDate: (!!e.fromDate) ? new Date(e.fromDate) : null,
            thruDate: (!!e.thruDate) ? new Date(e.thruDate) : null,
            roleTypeWeight: e.roleTypeWeight,
            comments: e.comments,
            commentsLang: e.commentsLang,

            dropdown: this.dropdown,
            variableGridArray: {
              id: ID,
              updated: false,
              buttonDetails: false,
              inputLabeldata: true,
              inputLabelNumber: true,
              inputNotes: true,
              outputData: true,
              inputNew: false,
              dropdownData: false,
            },
          }

          this.gridArray = [record, ...this.gridArray];
        }
      });
      this.loading = false;
    }
  }

  async setHeadArray() {

    this.headArray.push(
      {
        head: "Objective",
        fieldName: "workEffortName",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.dropdownFilter,
        textLength: 255,
        unique: true,
        required: true,
        width: '25vw',
        dropdown: {
          item: this.dropdownWEArray,
          clear: true,
          key: "workEffortId",
          virtualScrollItemSize: 10,
          virtualScroll: true,
          command: async () => {
            this.dropdownWEArray = [];
            this.headArray.filter(x => x.fieldName == 'workEffortName').forEach(y => y.dropdown.loading = true);
            this.setWorkEffortDropdown().subscribe((x) =>
              this.headArray
                .filter((x) => x.fieldName == "workEffortName")
                .forEach((y) => (y.dropdown.item = x)),
            );
            this.headArray
              .filter((x) => x.fieldName == "workEffortName")
              .forEach((y) => (y.dropdown.item = this.dropdownWEArray));
            this.headArray.filter(x => x.fieldName == 'workEffortName').forEach(y => y.dropdown.loading = false);
          },
        },
      },
      {
        head: "Role",
        fieldName: "roleTypeDescription",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.dropdownFilter,
        required: true,
        textLength: 255,
        unique: true,
        width: '11vw',
        dropdown: {
          item: this.dropdownRoleTypeArray,
          clear: true,
          key: "roleTypeId",
          virtualScrollItemSize: 10,
          virtualScroll: true,
          command: async () => {
            this.dropdownRoleTypeArray = [];
            this.headArray.filter(x => x.fieldName == 'roleTypeDescription').forEach(y => y.dropdown.loading = true);
            this.setRoleTypeDropdown().subscribe((x) =>
              this.headArray
                .filter((x) => x.fieldName == "roleTypeDescription")
                .forEach((y) => (y.dropdown.item = x)),
            );
            this.headArray
              .filter((x) => x.fieldName == "roleTypeDescription")
              .forEach((y) => (y.dropdown.item = this.dropdownRoleTypeArray));
            this.headArray.filter(x => x.fieldName == 'roleTypeDescription').forEach(y => y.dropdown.loading = false);
          },
        },
      },
      {
        head: "Subject",
        fieldName: "partyRoleViewDescription",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.dropdownFilter,
        required: true,
        textLength: 255,
        readonly: true,
        width: '11vw',
        unique: true,
        dropdown: {
          item: this.dropdownPartyArray,
          clear: true,
          key: "partyId",
          virtualScrollItemSize: 10,
          virtualScroll: true,
          command: async () => {
            this.dropdownPartyArray = [];
            this.headArray.filter(x => x.fieldName == 'partyRoleViewDescription').forEach(y => y.dropdown.loading = true);
            this.setPartyRoleTypeDropdown().subscribe((x) =>
              this.headArray
                .filter((x) => x.fieldName == "partyRoleViewDescription")
                .forEach((y) => (y.dropdown.item = x)),
            );
            this.headArray
              .filter((x) => x.fieldName == "partyRoleViewDescription")
              .forEach((y) => (y.dropdown.item = this.dropdownPartyArray));
            this.headArray.filter(x => x.fieldName == 'partyRoleViewDescription').forEach(y => y.dropdown.loading = false);
          },
        },
      },
      {
        head: "Estimated Start Date",
        fieldName: "fromDate",
        actionInput: ActionInput.inputDate,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.dateFilter,
        required: true,
        textLength: 255,
        unique: true
      },
      {
        head: "Estimated Completion Date",
        fieldName: "thruDate",
        actionInput: ActionInput.inputDate,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.dateFilter,
        required: true,
        textLength: 255
      },
      {
        head: "Weight",
        fieldName: "roleTypeWeight",
        actionInput: ActionInput.inputLabelNumber,
        actionOutput: ActionOutput.outputLabelNumber,
        filter: HeadFilter.popUpFilter,
        required: false,
        textLength: 255,
        content: 'center'
      },

    );



    this.langType = this.i18nService.getLanguageType();

    if (this.langType != "NONE") {
      await this.languageService.language().subscribe((data) => {
        this.languages = data;

        this.flag = true;
        this.headArray.push(
          {
            head: this.i18nService.translate('Comment'),
            fieldName: 'comments',
            flag: this.flag,
            actionInput: ActionInput.inputNotes,
            actionOutput: ActionOutput.outputNotes,
            filter: HeadFilter.textFilter,
            required: false,
            pathIconFlag: this.languages[1],
            textLength: 2000
          },
          {
            head: this.i18nService.translate('Comment'),
            fieldName: 'commentsLang',
            flag: this.flag,
            actionInput: ActionInput.inputNotes,
            actionOutput: ActionOutput.outputNotes,
            filter: HeadFilter.textFilter,
            required: false,
            pathIconFlag: this.languages[0],
            textLength: 2000
          },
          {
            head: "",
            fieldName: "null",
            actionInput: ActionInput.null,
            actionOutput: ActionOutput.null,
            filter: HeadFilter.null,
            width: '0.5vw'
          }
        )
      });
    } else {
      this.headArray.push(
        {
          head: this.i18nService.translate('Comment'),
          fieldName: 'comments',
          flag: this.flag,
          actionInput: ActionInput.inputNotes,
          actionOutput: ActionOutput.outputNotes,
          filter: HeadFilter.textFilter,
          required: false,
          textLength: 2000
        },
        {
          head: "",
          fieldName: "null",
          actionInput: ActionInput.null,
          actionOutput: ActionOutput.null,
          filter: HeadFilter.null,
          width: '0.5vw'
        }
      )
    }

    this.headArray.push({
      head: "",
      fieldName: "null",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.null,
      filter: HeadFilter.null,
      width: '0.5vw'
    })
  }

  setWorkEffortDropdown(): Observable<MenuItem[]> {
    this.dropdownWEArray = [];

    const obs$ = this.usrPreferenceService.getUserPreference("ORGANIZATION_PARTY").pipe(
      switchMap((data) =>
        this.weService.getWorkEffortDropdown(data.userPrefValue)
      )
    );

    return obs$.pipe(
      map(data => this.workEffortView = data),
      map(data => data.map(x => {
        let lab;
        if (!!x.etch) {
          let title = !this.secondaryLang
            ? x.workEffortName
            : x.workEffortNameLang;
          lab = x.etch + (!!title ? " - " + title : "") + ' (' + (this.secondaryLang ? x.party.partyNameLang : x.party.partyName + ' - ' + (this.secondaryLang ? x.workEffortType.descriptionLang : x.workEffortType.description)) + ' )';
        } else
          lab = !this.secondaryLang ? x.workEffortName : x.workEffortNameLang + ' (' + (this.secondaryLang ? x.party.partyNameLang : x.party.partyName + ' - ' + (this.secondaryLang ? x.workEffortType.descriptionLang : x.workEffortType.description)) + ' )';

        return { label: lab, id: x.workEffortId };
      }))
    )
  }

  setRoleTypeDropdown(): Observable<MenuItem[]> {
    this.dropdownRoleTypeArray = [];
    const obs$ = this.roleTypeService.roleTypes();

    return obs$.pipe(
      map(data => data.map(x => {
        this.dropdownRoleTypeArray.push({
          label: !this.secondaryLang ? x.description : x.descriptionLang,
          id: x.roleTypeId,
        });

        return { label: !this.secondaryLang ? x.description : x.descriptionLang, id: x.roleTypeId };
      })
      ))
  }


  setPartyRoleTypeIdDropdown(roleTypeId: string): Observable<MenuItem[]> {
    this.dropdownPartyArray = [];
    const obs$ = this.usrPreferenceService
      .getUserPreference("ORGANIZATION_PARTY")
      .pipe(
        switchMap((data) =>
          this.partyRoleViewService.getPartyRoleViewRoleTypeId(
            "PARTY_ENABLED",
            data.userPrefValue,
            roleTypeId
          )
        )
      );

    return obs$.pipe(
      map(data => data.map((x) => {
        return { label: x.parentRoleCode + " - " + x.partyName, id: x.partyId }
      })
      ))
  }

  setPartyRoleTypeDropdown(): Observable<MenuItem[]> {
    this.dropdownPartyArray = [];
    const obs$ = this.usrPreferenceService
      .getUserPreference("ORGANIZATION_PARTY")
      .pipe(
        switchMap((data) =>
          this.partyRoleViewService.getPartyRoleView(
            "PARTY_ENABLED",
            data.userPrefValue
          )
        )
      );

    return obs$.pipe(
      map(data => data.map((x) => {
        return { label: x.parentRoleCode + " - " + x.partyName, id: x.partyId }
      })
      ))
  }

  openNew() {
    let ID = "new" + Math.random();
    let tmpElAdd: WorkEffortPartyAssignment = {
      workEffortName: null,
      workEffortId: null,
      roleTypeId: null,
      partyId: null,
      roleTypeDescription: null,
      partyRoleViewDescription: null,
      fromDate: null,
      thruDate: null,
      roleTypeWeight: null,
      dropdown: null,
      comments: null,
      commentsLang: null,
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
      }

    }

    this.dropdown = [];
    this.dropdown["partyRoleViewDescription"] = {
      id: ID,
      fieldName: "partyRoleViewDescription",
      item: [],
      clear: true,
      loading: false,
      key: "partyId",
      command: async () => {
        let tmp = this.gridArray;

        tmp.filter((x) => x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].loading = true;
        this.gridArray = tmp;

        await this.setPartyRoleTypeIdDropdown(this.itemDWSelected.roleTypeId);

        let dp = tmp.filter((x) => x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].item;

        let keyExist = dp.map((item) => item["partyRoleViewDescription"]);
        let ray = [];
        this.dropdownParty.forEach((y) => {
          if (!keyExist.includes(y)) {
            ray.push(y);
          }
        });

        tmp.filter(
          (x) =>
            x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].item = ray;
        tmp.filter(
          (x) => x.variableGridArray.id == this.itemDWSelected.dropdown["partyRoleViewDescription"].id)[0].dropdown["partyRoleViewDescription"].loading = false;
        this.gridArray = tmp;
      },
    };
    tmpElAdd.dropdown = this.dropdown;
    this.elementToAdd = tmpElAdd;
    this.gridArray = [this.elementToAdd, ...this.gridArray];

  }

  saveNewAndOpen(gridElement) {
    if (this.create(gridElement)) {
      this.openNew();
    }
    else this.er = false;
  }

  saveAllElement(elementUpdated) {
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
      let obj = new WorkEffortPartyAssignment();

      obj.workEffortId = e.workEffortId;
      obj.roleTypeId = e.roleTypeId;
      obj.partyId = e.partyId;
      obj.fromDate = e.fromDate;
      obj.thruDate = e.thruDate;
      obj.comments = e.comments;
      obj.commentsLang = e.commentsLang;
      obj.roleTypeWeight = e.roleTypeWeight != null ? e.roleTypeWeight : 0;

      if ((!!obj.thruDate && obj.fromDate <= obj.thruDate) || !!!obj.thruDate) {
        await this.workEffortPartyAssignmntService.createWorkEffortPartyAssignment(obj)
          .then((x) => {
            this.msgService.successCreate();
            let ID = x.workEffortId + x.roleTypeId + x.partyId + x.fromDate;
            e.workEffortId = x.workEffortId;
            e.variableGridArray.id = ID;
            e.dropdown['partyRoleViewDescription'].id = ID;
            e.variableGridArray.updated = false;
            e.variableGridArray.inputNew = false;
            e.variableGridArray.outputData = true;

            if (this.reload) this._reload.next()
          })
          .catch((error) => {
            this.msgService.error(error);
            this.er = true;
          });
      }
      else {
        this.msgService.errorDate(obj.fromDate, obj.thruDate);
        this.er = true;
      }
    })

    return !this.er;
  }

  update(gridElement) {

    gridElement.forEach(async e => {
      let obj = new WorkEffortPartyAssignment();

      obj.workEffortId = e.workEffortId;
      obj.roleTypeId = e.roleTypeId;
      obj.partyId = e.partyId;
      obj.fromDate = e.fromDate;
      obj.thruDate = e.thruDate;
      obj.workEffortId = e.workEffortId;
      obj.comments = e.comments;
      obj.commentsLang = e.commentsLang;
      obj.roleTypeWeight = e.roleTypeWeight;

      if ((!!obj.thruDate && obj.fromDate <= obj.thruDate) || !!!obj.thruDate) {
        await this.workEffortPartyAssignmntService.updateWorkEffortPartyAssignment(obj)
          .then(() => {
            this.msgService.successUpdate();
            this._reload.next()
          })
          .catch((error) => {
            this.msgService.error(error);
            this.er = true;
          });
      }
      else {
        this.msgService.errorDate(obj.fromDate, obj.thruDate);
        this.er = true;
      }
    });
    return !this.er;
  }


  delete(listGridElement: WorkEffortPartyAssignment[]) {
    if (listGridElement.filter(x => x.variableGridArray.id.includes("new")).length > 0) {
      this.newRow = listGridElement.filter(x => x.variableGridArray.id.includes("new"));
      listGridElement = listGridElement.filter(x => !this.newRow.includes(x));
      this.gridArray = this.gridArray.filter(x => !x.variableGridArray.id.includes("new"));
      this.newRow = null;
    }
    if (listGridElement.length > 0) {
      listGridElement.forEach(e => {

        let obj = new WorkEffortPartyAssignment();
        obj.workEffortId = e.workEffortId;
        obj.partyId = e.partyId;
        obj.roleTypeId = e.roleTypeId;
        obj.fromDate = e.fromDate;

        this.workEffortPartyAssignmntService.deleteWorkEffortPartyAssignment(obj)
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

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }


  resetAllElement() {
    this.loading = true;
    this._reload.next();
  }

  setInfoCurrentPage() {

    this.loading = true;
    this.gridArray = [];

    this.infoCurrentPage.filter = [];
    this.infoCurrentPage.filterGenericLabel = [];

    if (this.infoCurrentPage.filter.length == 0) {
      if (this.filterForm.value.search) {
        this.infoCurrentPage.matchModeSearch = this.filterForm.value.matchModeSearch;
        this.infoCurrentPage.filterGenericLabel.push(
          { field: 'roleTypeIdMatch', value: this.filterForm.value.search },
          { field: 'partyIdMatch', value: this.filterForm.value.search },
          { field: 'workEffortIdMatch', value: this.filterForm.value.search })
      }
      if (this.filterForm.value.workEffortId) {
        this.headArray
          .filter((x) => x.fieldName == "workEffortName")
          .forEach((y) => (y.dropdown.disabled = true));
        this.infoCurrentPage.filter.push({ field: 'workEffortId', value: this.filterForm.value.workEffortId.id })
      }
      if (this.filterForm.value.partyId) {
        this.headArray
          .filter((x) => x.fieldName == "partyRoleViewDescription")
          .forEach((y) => (y.dropdown.disabled = true));
        this.infoCurrentPage.filter.push({ field: 'partyId', value: this.filterForm.value.partyId.id })
      }
      if (this.filterForm.value.roleTypeId) {
        this.headArray
          .filter((x) => x.fieldName == "roleTypeDescription")
          .forEach((y) => (y.dropdown.disabled = true));
        this.infoCurrentPage.filter.push({ field: 'roleTypeId', value: this.filterForm.value.roleTypeId.id })
      }
    }

    this.filterTotal = true;
  }

  async filter() {
    if ((this.filterForm.value.search != null && this.filterForm.value.search != "") || this.filterForm.value.workEffortId != null || this.filterForm.value.partyId != null || this.filterForm.value.roleTypeId != null) {

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
