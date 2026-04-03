import { Component, OnDestroy, OnInit } from "@angular/core";
import {
  map,
  lastValueFrom,
  Observable,
  switchMap,
  Subscription,
  BehaviorSubject,
  filter,
  tap,
  startWith,
} from "rxjs";
import {
  FilterMatchMode,
  MenuItem,
  Message as MessageError,
} from "primeng/api";
import {
  ActionInput,
  ActionOutput,
  Dropdown,
  Filter,
  HeadArray,
  HeadFilter,
  InfoPage,
} from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { ActivatedRoute, NavigationEnd, Router } from "@angular/router";
import { LanguageService } from "app/api/service/language.service";
import { I18NService } from "app/i18n/i18n.service";
import { WorkEffortViewService } from "app/api/service/work-effort-view.service";
import { UserPreferenceService } from "app/api/service/user-preference.service";
import { TableEditingCellService } from "app/commons/service/table-editing-cell.service";
import { WorkEffortContentService } from "app/api/service/work-effort-content.service";
import { WorkEffortContentTypeService } from "app/api/service/work-effort-content-type.service";
import { WorkEffortContentEx } from "app/api/model/workEffortContentEx";
import { shareFile } from "app/layout/upload/upload";
import { DataResourceService } from "app/api/service/data-resource.service";
import { LoaderService } from "app/shared/loader/loader.service";
import { MsgService } from "app/commons/service/message.service";
import { FormBuilder, FormControl, FormGroup } from "@angular/forms";
import { Location } from "@angular/common";
import { WorkEffortService } from "app/api/service/work-effort.service";

@Component({
  selector: "app-attachments-objectives",
  templateUrl: "./attachments-objectives.component.html",
  styleUrls: ["./attachments-objectives.component.css"],
})
export class AttachmentsObjectivesComponent implements OnInit, OnDestroy {
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();

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
  secondaryLang: boolean;
  languages: [] = [];

  dropdownWEV: Observable<MenuItem[]>;
  dropdownWECT: Observable<MenuItem[]>;

  dropdownWEVSearch: MenuItem[] = [];
  dropdownWEVAutoCompl: MenuItem[] = [];
  dropdownWECTAutoCompl: MenuItem[] = [];

  itemDWSelected: any;
  dropdown: Dropdown[] = [];
  indexLen: number;

  selectedWorkEffortId: string;
  selectedCode: string;

  workEffortContentEx$: Subscription;

  buttonBack: boolean = false;

  collapseFilters: boolean = false;
  collapseTab: boolean = true;
  labelInsertFilter: boolean = true;
  disableResetFilter: boolean = false;

  organizationId: string;
  reload: boolean;

  filterForm: FormGroup = this.formBuilder.group({
    matchModeSearch: new FormControl<string>(FilterMatchMode.CONTAINS, {
      nonNullable: true,
    }),
    search: new FormControl<string>(null),
    workEffortId: new FormControl<string>(null),
    workEffortContentTypeId: new FormControl<string>(null),
  });
  filterTotal: boolean = false;

  infoCurrentPage: InfoPage = {
    offset: 0,
    limit: 0,
    filter: [],
    filterGenericLabel: [],
  };

  query: string = null;

  workEffortTypeContent: Observable<WorkEffortContentEx[]>;

  state$: Observable<unknown> = this.router.events.pipe(
    tap(() => (this.loading = true)),
    filter((event) => event instanceof NavigationEnd),
    map(() => {
      return this.location.getState();
    }),
    startWith(this.location.getState()),
  );

  memState: { [key: string]: string | string[] | number };

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly workEffortContentService: WorkEffortContentService,
    private readonly weService: WorkEffortService,
    private readonly usrPreferenceService: UserPreferenceService,
    private readonly tbService: TableEditingCellService,
    private readonly wectService: WorkEffortContentTypeService,
    private readonly dataResourceService: DataResourceService,
    private formBuilder: FormBuilder,
    private readonly loaderService: LoaderService,
    private msgService: MsgService,
    private readonly router: Router,
    private location: Location,
  ) { }
  ngOnDestroy(): void {
    this.workEffortContentEx$?.unsubscribe();
  }

  async ngOnInit() {
    this.loaderService.hide();
    this.secondaryLang = await this.languageService.secondaryLang();

    this.filterForm.setValue({
      matchModeSearch: FilterMatchMode.CONTAINS,
      search: null,
      workEffortId: null,
      workEffortContentTypeId: null,
    });
    this.filterForm.markAsPristine();

    this.route.queryParams.subscribe((params) => {
      this.selectedWorkEffortId = params.id;
      this.selectedCode = params.code;

      if (this.selectedCode == "AO") {
        this.filterTotal = true;
        this.collapseFilters = true;
        this.collapseTab = false;
        this.infoCurrentPage.filter.push({
          field: "workEffortIdDesc",
          value: this.selectedWorkEffortId,
        });
      }
    });

    this.workEffortTypeContent = this.reload$.pipe(
      filter(() => this.filterTotal),
      switchMap(() => {
        return this.workEffortContentService.getWorkEffortContentExListFilter(
          this.infoCurrentPage,
        );
      }),
    );

    this.dropdownWEV = this.setWEVDropdown();
    this.dropdownWECT = this.setWECTDropdown();

    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();
      this.headArray[5].pathIconFlag = lang[0];
      this.headArray[6].pathIconFlag = lang[1];
    } else {
      this.setHeadArray();
    }

    await this.setFilter();

    this.workEffortTypeContent.subscribe((y) => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);

      if (this.selectedCode == "AO") {
        this.setGridArrayWithId(y, this.selectedWorkEffortId);
        this.buttonBack = true;
        this.labelInsertFilter = false;
      } else {
        this.setGridArray(y);
        this.labelInsertFilter = false;
      }

      this.loading = false;
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
            await lastValueFrom(this.dropdownWEV).then(
              (x) =>
              (labelWv = x.filter((y) => y.id == this.selectedWorkEffortId)[0]
                .label),
            );
          }

          this.filterForm.setValue({
            matchModeSearch:
              (state.matchModeSearch as string) ?? FilterMatchMode.CONTAINS,
            search: (state.search as string) ?? null,
            workEffortId: this.selectedWorkEffortId
              ? { label: labelWv, id: this.selectedWorkEffortId }
              : (state.workEffortId as string) ?? null,
            workEffortContentTypeId:
              (state.workEffortContentTypeId as string) ?? null,
          });

          this.filterForm.markAsPristine();
          return state;
        }),
      )
      .subscribe(async (data) => {
        if (
          this.filterForm.value.search != null ||
          this.filterForm.value.workEffortId != null ||
          this.filterForm.value.workEffortContentTypeId != null
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
    this.gridArray = [];
    this.infoCurrentPage.filter = [];
    this.infoCurrentPage.filterGenericLabel = [];
    this.infoCurrentPage.organizationId = this.organizationId;

    if (this.infoCurrentPage.filter.length == 0) {
      if (this.filterForm.value.search) {
        this.infoCurrentPage.matchModeSearch =
          this.filterForm.value.matchModeSearch;
        this.infoCurrentPage.filterGenericLabel.push(
          { field: "workEffortIdMatch", value: this.filterForm.value.search },
          {
            field: "workEffortContentTypeIdMatch",
            value: this.filterForm.value.search,
          },
        );
      }
      if (this.filterForm.value.workEffortId) {
        this.infoCurrentPage.filter.push({
          field: "workEffortIdDesc",
          value: this.filterForm.value.workEffortId.id,
        });
        this.headArray
          .filter((x) => x.fieldName == "workEffortIdDesc")
          .forEach((y) => (y.dropdown.disabled = true));
      }
      if (this.filterForm.value.workEffortContentTypeId) {
        this.infoCurrentPage.filter.push({
          field: "workEffortContentTypeId",
          value: this.filterForm.value.workEffortContentTypeId.id,
        });
        this.headArray
          .filter((x) => x.fieldName == "workEffortContentTypeIdDesc")
          .forEach((y) => (y.dropdown.disabled = true));
      }
    }
  }

  setGridArray(y) {
    this.indexLen = y.length;
    y.forEach((e, index) => {
      let ID =
        e.workEffortId +
        e.contentId +
        e.workEffortContentTypeId +
        e.fromDate +
        index;

      this.dropdown = [];
      this.dropdown["workEffortContentTypeIdDesc"] = {
        id: ID,
        fieldName: "workEffortContentTypeIdDesc",
        item: [
          {
            label: !this.secondaryLang
              ? e.workEffortContentType.description
              : e.workEffortContentType.descriptionLang,
            id: e.workEffortContentTypeId,
          },
        ],
        clear: false,
        loading: false,
        key: "workEffortContentTypeId",
        command: async () => {
          let tmp = this.gridArray;

          tmp.filter(
            (x) =>
              x.variableGridArray.id ==
              this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
          )[0].dropdown["workEffortContentTypeIdDesc"].loading = true;
          this.gridArray = tmp;
          if (!!this.itemDWSelected.workEffortId)
            await this.setWECTDropdown(this.itemDWSelected.workEffortId);

          let dp = tmp.filter(
            (x) =>
              x.variableGridArray.id ==
              this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
          )[0].dropdown["workEffortContentTypeIdDesc"].item;

          let keyExist = dp.map((item) => item["workEffortContentTypeIdDesc"]);
          let ray = [];
          this.dropdownWECT.forEach((y) => {
            if (!keyExist.includes(y)) {
              ray.push(y);
            }
          });

          tmp.filter(
            (x) =>
              x.variableGridArray.id ==
              this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
          )[0].dropdown["workEffortContentTypeIdDesc"].item = ray;
          tmp.filter(
            (x) =>
              x.variableGridArray.id ==
              this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
          )[0].dropdown["workEffortContentTypeIdDesc"].loading = false;
          this.gridArray = tmp;
        },
      };

      this.gridArray.push({
        workEffortId: e.workEffortId,
        workEffortIdDesc:
          e.workEffortView.etch +
          "-" +
          (!this.secondaryLang
            ? e.workEffortView.workEffortName
            : e.workEffortView.workEffortNameLang),
        workEffortContentTypeId: e.workEffortContentTypeId,
        workEffortContentTypeIdDesc: !this.secondaryLang
          ? e.workEffortContentType.description
          : e.workEffortContentType.descriptionLang,
        fromDate: e.fromDate ? new Date(e.fromDate) : null,
        thruDate: !!e.thruDate ? new Date(e.thruDate) : null,
        description: e.content.description,
        descriptionLang: e.content.descriptionLang,
        contentName: e.content.contentName,
        objectInfo: e.dataResource.objectInfo,
        contentId: e.content.contentId,
        contentTypeId: e.content.contentTypeId,
        mimeTypeId: e.content.mimeTypeId,
        dataResourceTypeId: e.dataResource.dataResourceTypeId,
        dataTemplateTypeId: e.dataResource.dataTemplateTypeId,
        dataResourceName: e.dataResource.dataResourceName,
        dataResourceId: e.dataResource.dataResourceId,

        dropdown: this.dropdown,
        variableGridArray: {
          id: ID,
          updated: false,
          inputLabeldata: true,
          inputLabelNumber: true,
          inputNotes: true,
          outputData: true,
          inputNew: false,
          dropdownData: true,
          upload: true,
        },
      });
    });
    this.loading = false;
  }

  setGridArrayWithId(y, id) {
    this.indexLen = y.length;
    y.forEach((e, index) => {
      if (e.workEffortId == id) {
        let ID =
          e.workEffortId +
          e.contentId +
          e.workEffortContentTypeId +
          e.fromDate +
          index;

        this.dropdown = [];
        this.dropdown["workEffortContentTypeIdDesc"] = {
          id: ID,
          fieldName: "workEffortContentTypeIdDesc",
          item: [
            {
              label: !this.secondaryLang
                ? e.workEffortContentType.description
                : e.workEffortContentType.descriptionLang,
              id: e.workEffortContentTypeId,
            },
          ],
          clear: false,
          loading: false,
          key: "workEffortContentTypeId",
          command: async () => {
            let tmp = this.gridArray;

            tmp.filter(
              (x) =>
                x.variableGridArray.id ==
                this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
            )[0].dropdown["workEffortContentTypeIdDesc"].loading = true;
            this.gridArray = tmp;
            if (!!this.itemDWSelected.workEffortId)
              await this.setWECTDropdown(this.itemDWSelected.workEffortId);

            let dp = tmp.filter(
              (x) =>
                x.variableGridArray.id ==
                this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
            )[0].dropdown["workEffortContentTypeIdDesc"].item;

            let keyExist = dp.map(
              (item) => item["workEffortContentTypeIdDesc"],
            );
            let ray = [];
            this.dropdownWECT.forEach((y) => {
              if (!keyExist.includes(y)) {
                ray.push(y);
              }
            });

            tmp.filter(
              (x) =>
                x.variableGridArray.id ==
                this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
            )[0].dropdown["workEffortContentTypeIdDesc"].item = ray;
            tmp.filter(
              (x) =>
                x.variableGridArray.id ==
                this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
            )[0].dropdown["workEffortContentTypeIdDesc"].loading = false;
            this.gridArray = tmp;
          },
        };

        this.gridArray.push({
          workEffortId: e.workEffortId,
          workEffortIdDesc: !this.secondaryLang
            ? e.workEffortView.description
            : e.workEffortView.descriptionLang,
          workEffortContentTypeId: e.workEffortContentTypeId,
          workEffortContentTypeIdDesc: !this.secondaryLang
            ? e.workEffortContentType.description
            : e.workEffortContentType.descriptionLang,
          fromDate: e.fromDate ? new Date(e.fromDate) : null,
          thruDate: !!e.thruDate ? new Date(e.thruDate) : null,
          description: e.content.description,
          descriptionLang: e.content.descriptionLang,
          contentName: e.content.contentName,
          objectInfo: e.dataResource.objectInfo,
          contentId: e.content.contentId,
          contentTypeId: e.content.contentTypeId,
          mimeTypeId: e.content.mimeTypeId,
          dataResourceTypeId: e.dataResource.dataResourceTypeId,
          dataTemplateTypeId: e.dataResource.dataTemplateTypeId,
          dataResourceName: e.dataResource.dataResourceName,
          dataResourceId: e.dataResource.dataResourceId,

          dropdown: this.dropdown,
          variableGridArray: {
            id: ID,
            updated: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            inputNew: false,
            dropdownData: true,
            upload: true,
          },
        });
      }
    });
    this.loading = false;
  }

  setHeadArray() {
    this.headArray.push(
      {
        head: "Work Effort",
        fieldName: "workEffortIdDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.dropdownFilter,
        required: true,
        unique: true,
        width: "10vw",
        dropdown: {
          item: this.dropdownWEVAutoCompl,
          clear: false,
          disableSort: true,
          loading: false,
          virtualScrollItemSize: 10,
          virtualScroll: true,
          key: "workEffortId",
          disabled: false,
          command: async () => {
            this.headArray
              .filter((x) => x.fieldName == "workEffortIdDesc")
              .forEach((y) => (y.dropdown.loading = true));
            this.dropdownWEV.subscribe((x) =>
              this.headArray
                .filter((x) => x.fieldName == "workEffortIdDesc")
                .forEach((y) => (y.dropdown.item = x)),
            );
            this.headArray
              .filter((x) => x.fieldName == "workEffortIdDesc")
              .forEach((y) => (y.dropdown.loading = false));
          },
        },
      },
      {
        head: "WorkEffortContentType_workEffortContentTypeId",
        fieldName: "workEffortContentTypeIdDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.dropdownFilter,
        required: true,
        unique: true,
        width: "8vw",
        dropdown: {
          item: this.dropdownWECTAutoCompl,
          clear: false,
          disableSort: true,
          loading: false,
          key: "workEffortContentTypeId",
          virtualScrollItemSize: 10,
          virtualScroll: true,
          disabled: false,
          command: async () => {
            this.headArray
              .filter((x) => x.fieldName == "workEffortContentTypeIdDesc")
              .forEach((y) => (y.dropdown.loading = true));
            this.dropdownWECT.subscribe((x) =>
              this.headArray
                .filter((x) => x.fieldName == "workEffortContentTypeIdDesc")
                .forEach((y) => (y.dropdown.item = x)),
            );
            this.headArray
              .filter((x) => x.fieldName == "workEffortContentTypeIdDesc")
              .forEach((y) => (y.dropdown.loading = false));
          },
        },
      },
      {
        head: "Start Date",
        fieldName: "fromDate",
        actionInput: ActionInput.inputDate,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.dateFilter,
        required: true,
        unique: true,
        width: "5vw",
      },
      {
        head: "End Date",
        fieldName: "thruDate",
        clearCalendar: true,
        actionInput: ActionInput.inputDate,
        actionOutput: ActionOutput.outputDate,
        filter: HeadFilter.dateFilter,
        width: "5vw",
      },
      {
        head: "Description",
        fieldName: "description",
        flag: this.flag,
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        required: true,
        width: "5vw",
        textLength: 255,
      },
    );
    if (this.i18nService.getLanguageType() == "BILING")
      this.headArray.push({
        head: "Description",
        fieldName: "descriptionLang",
        flag: true,
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        required: true,
        width: "5vw",
        textLength: 255,
      });
    this.headArray.push(
      {
        head: "Upload Attachment",
        fieldName: "upload",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.fileUpload,
        filter: HeadFilter.null,
        width: "3vw",
        content: "center",
        sortIcon: false,
      },
      {
        head: "File Name",
        fieldName: "contentName",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.popUpFilter,
        width: "5vw",
        textLength: 255,
      },
      {
        head: "Path",
        fieldName: "objectInfo",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        width: "10vw",
        textLength: 255,
      },
      {
        head: "",
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.null,
        filter: HeadFilter.null,
        width: "0.5vw",
      },
    );
  }

  setWEVDropdown(): Observable<MenuItem[]> {
    const obs$ = this.usrPreferenceService
      .getUserPreference("ORGANIZATION_PARTY")
      .pipe(
        switchMap((data) => {
          
            this.dropdownWEVAutoCompl = [];
            return this.weService.getWorkEffortDropdown(data.userPrefValue);
          
        }),
      );

    return obs$.pipe(
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
    );
  }

  setWECTDropdown(workEffortId?: {
    label: string;
    id: string;
  }): Observable<MenuItem[]> {
    this.dropdownWECTAutoCompl = [];

    if (workEffortId) {
      const obs$ = this.wectService.getContentTypeList(workEffortId.id);
      return obs$.pipe(
        map((data) =>
          data.map((x) => {
            this.dropdownWECTAutoCompl.push({
              label: !this.secondaryLang ? x.description : x.descriptionLang,
              id: x.workEffortContentTypeId,
            });
            return {
              label: !this.secondaryLang ? x.description : x.descriptionLang,
              id: x.workEffortContentTypeId,
            };
          }),
        ),
      );
    } else {
      const obs$ = this.wectService.getWorkEffortContentType();
      return obs$.pipe(
        map((data) =>
          data.map((x) => {
            this.dropdownWECTAutoCompl.push({
              label: !this.secondaryLang ? x.description : x.descriptionLang,
              id: x.workEffortContentTypeId,
            });

            return {
              label: !this.secondaryLang ? x.description : x.descriptionLang,
              id: x.workEffortContentTypeId,
            };
          }),
        ),
      );
    }
  }

  shareDropdownItemRow(item) {
    this.itemDWSelected = item;
  }

  openNew() {
    this.dropdownWECTAutoCompl = [];
    let ID = "new" + Math.random();
    let tmpElAdd = {
      workEffortId: null,
      workEffortIdDesc: null,
      workEffortContentTypeId: null,
      workEffortContentTypeIdDesc: null,
      fromDate: null,
      thruDate: null,
      description: null,
      descriptionLang: null,
      contentName: null,
      objectInfo: null,
      contentId: null,
      contentTypeId: null,
      mimeTypeId: null,
      dataResourceTypeId: null,
      dataTemplateTypeId: null,
      dataResourceName: null,
      dataResourceId: null,

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
        upload: false,
      },
    };

    this.dropdown = [];
    this.dropdown["workEffortContentTypeIdDesc"] = {
      id: ID,
      fieldName: "workEffortContentTypeIdDesc",
      item: [],
      clear: false,
      loading: false,
      key: "workEffortContentTypeId",
      command: async () => {
        let tmp = this.gridArray;

        tmp.filter(
          (x) =>
            x.variableGridArray.id ==
            this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
        )[0].dropdown["workEffortContentTypeIdDesc"].loading = true;
        this.gridArray = tmp;

        if (!!this.itemDWSelected.workEffortId)
          await this.setWECTDropdown(this.itemDWSelected.workEffortId);

        let dp = tmp.filter(
          (x) =>
            x.variableGridArray.id ==
            this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
        )[0].dropdown["workEffortContentTypeIdDesc"].item;

        let keyExist = dp.map((item) => item["workEffortContentTypeIdDesc"]);
        let ray = [];
        this.dropdownWECTAutoCompl.forEach((y) => {
          if (!keyExist.includes(y)) {
            ray.push(y);
          }
        });

        tmp.filter(
          (x) =>
            x.variableGridArray.id ==
            this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
        )[0].dropdown["workEffortContentTypeIdDesc"].item = ray;
        tmp.filter(
          (x) =>
            x.variableGridArray.id ==
            this.itemDWSelected.dropdown["workEffortContentTypeIdDesc"].id,
        )[0].dropdown["workEffortContentTypeIdDesc"].loading = false;
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
      let obj = new WorkEffortContentEx(
        e.workEffortId,
        null,
        e.workEffortContentTypeId,
        e.fromDate,
        e.thruDate,
        null,
        "CTNT_INITIAL_DRAFT",
        e.description,
        e.descriptionLang,
        e.contentName,
        null,
        e.objectInfo,
        null,
        null,
        null,
        "CTNT_IN_PROGRESS",
        null,
        null,
        null,
      );

      if ((!!obj.thruDate && obj.fromDate <= obj.thruDate) || !!!obj.thruDate) {
        await this.workEffortContentService
          .createWorkEffortContentEx(obj)
          .then((x) => {
            this.msgService.successCreate();
            let ID =
              e.workEffortId +
              e.contentId +
              e.workEffortContentTypeId +
              e.fromDate +
              (this.indexLen + 1);

            this.indexLen += 1;

            e.dataResourceId = x.dataResource.dataResourceId;
            e.contentId = x.content.contentId;
            e.variableGridArray.upload = true;
            e.variableGridArray.id = ID;
            e.dropdown["workEffortContentTypeIdDesc"].id = ID;
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
      let obj = new WorkEffortContentEx(
        e.workEffortId,
        e.contentId,
        e.workEffortContentTypeId,
        e.fromDate,
        e.thruDate,
        e.contentTypeId,
        "CTNT_INITIAL_DRAFT",
        e.description,
        e.descriptionLang,
        e.contentName,
        e.mimeTypeId,
        e.objectInfo,
        e.dataResourceId,
        e.dataResourceTypeId,
        e.dataTemplateTypeId,
        "CTNT_IN_PROGRESS",
        e.dataResourceName,
      );
      if ((!!obj.thruDate && obj.fromDate <= obj.thruDate) || !!!obj.thruDate) {
        await this.workEffortContentService
          .updateWorkEffortContentEx(obj)
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
        let obj = new WorkEffortContentEx(
          e.workEffortId,
          e.contentId,
          e.workEffortContentTypeId,
          e.fromDate,
          e.thruDate,
          e.contentTypeId,
          "CTNT_INITIAL_DRAFT",
          e.description,
          e.descriptionLang,
          e.contentName,
          e.mimeTypeId,
          e.objectInfo,
          e.dataResourceId,
          e.dataResourceTypeId,
          e.dataTemplateTypeId,
          "CTNT_IN_PROGRESS",
          e.dataResourceName,
        );

        this.workEffortContentService
          .deleteWorkEffortContentEx(obj)
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

  myUploader(value: shareFile) {
    let file = value.file;

    this.dataResourceService
      .uploadFile(file.files[0], value.item.dataResourceId)
      .then((x) => {
        this.gridArray
          .filter((y) => y.dataResourceId == value.item.dataResourceId)
          .forEach((z) => {
            z.contentName = x.dataResource.dataResourceName;
            z.dataResourceName = x.dataResource.dataResourceName;
            z.objectInfo = x.dataResource.objectInfo;
          });
        this.msgService.successCreate();
      })
      .catch((error) => {
        console.log(error);
      });
  }

  canDeactivate(): boolean {
    return (
      this.gridArray.filter((x) => x.variableGridArray.updated == true).length >
      0
    );
  }

  resetAllElement() {
    this._reload.next();
  }

  async filter() {
    if (
      (this.filterForm.value.search != null &&
        this.filterForm.value.search != "") ||
      this.filterForm.value.workEffortId != null ||
      this.filterForm.value.workEffortContentTypeId != null
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
    this.router.navigate(["."], {
      relativeTo: this.route,
      state: {
        ...this.filterForm.value,
        isSecondaryLang: this.secondaryLang,
        infoPage: this.infoCurrentPage,
      },
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
