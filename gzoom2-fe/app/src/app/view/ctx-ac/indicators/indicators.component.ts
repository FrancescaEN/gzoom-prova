import { Location } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnDestroy, OnInit, computed, signal } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { GlAccount } from 'app/api/model/glAccount';
import { GlAccountType } from 'app/api/model/glAccountType';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { GlAccountTypeService } from 'app/api/service/gl-account-type.service';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { PeriodTypeService } from 'app/api/service/period-type.service';
import { StatusItemService } from 'app/api/service/status-item.service';
import { WorkEffortPurposeTypeService } from 'app/api/service/work-effort-purpose-type.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { MsgService } from 'app/commons/service/message.service';
import { dropdownEnumeration, dropdownGlAccountType, dropdownPartyEx, dropdownPeriodType, dropdownStatusItem, dropdownWorkEffortPurposeType } from 'app/commons/utils/dropdownSelectItem';
import { I18NService } from 'app/i18n/i18n.service';
import { findIndex, isEmpty, isNull, omitBy, orderBy, remove } from 'lodash';
import { FilterMatchMode, SelectItem } from 'primeng/api';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { BehaviorSubject, Observable, Subject, Subscription, combineLatest, distinctUntilChanged, filter, map, of, startWith, switchMap, takeUntil, tap } from 'rxjs';
import { NewIndicatorComponent } from './new-indicator/new-indicator.component';
import { PartyService } from 'app/api/service/party.service';
import { NewIndicatorComponentSuccess } from './new-indicator/new-indicator-success.component';
import { IndicatorService } from './indicator.service';
import { toSignal } from '@angular/core/rxjs-interop';

export interface DetectionMode {
  inputEnumId: string,
  detectOrgUnitIdFlag: string
}
@Component({
  selector: 'gzoom-indicators',
  templateUrl: './indicators.component.html',
  styleUrls: ['./indicators.component.scss'],
  changeDetection: ChangeDetectionStrategy.Default
})
export class IndicatorsComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  loading: boolean = false;
  loadingDelete: boolean = false;
  disableResetFilter: boolean = true;
  visible: boolean = true;
  reloadOnCloseNew: boolean = false;
  secondaryLang: boolean;
  accountTypeEnumId: string;
  indicatorsReq$: Observable<GlAccount[]>;
  indicators$: Observable<GlAccount[]>;
  selectedIndicators: GlAccount[] = [];
  indicators: GlAccount[];
  indicatorsWithFilter: GlAccount[];
  statusItems: SelectItem[];
  statusItem$: Observable<SelectItem[]> = this.statusItemService.getStatusItemList("GL_ACCOUNT")
    .pipe(
      takeUntil(this.destroy$),
      map(x => dropdownStatusItem(x, this.secondaryLang)));
  glAccountType: SelectItem[];
  glAccountTypeObs$: Observable<GlAccountType[]>;
  glAccountType$: Subscription;

  workEffortPurposeType$: Observable<SelectItem[]> =
    this.workEffortPurposeTypeService.getWorkEffortPurposeTypeByPurposeTypeEnumId('PT_INDICATOR')
      .pipe(
        takeUntil(this.destroy$),
        map(x => dropdownWorkEffortPurposeType(x, this.secondaryLang)));
  workEffortPurposeType: SelectItem[];

  debitCreditDefault$: Observable<SelectItem[]> =
    this.enumService.enumerations('D_C')
      .pipe(
        map(x => dropdownEnumeration(x, this.secondaryLang)));

  periodType$: Observable<SelectItem[]> =
    this.periodTypeService.periodTypes()
      .pipe(
        takeUntil(this.destroy$),
        map(x => dropdownPeriodType(x)));
  periodType: SelectItem[];

  uoGestore$: Observable<SelectItem[]> =
    this.partyService.orgUOGestore(this.route.parent.snapshot.data.context)
      .pipe(
        takeUntil(this.destroy$),
        map(x => dropdownPartyEx(x, this.secondaryLang)));
  uoGestore: SelectItem[];

  filterForm: FormGroup = this.formBuilder.group({
    matchModeSearch: new FormControl<string>(FilterMatchMode.CONTAINS, { nonNullable: true }),
    search: new FormControl<string>(null),
    glAccountTypeId: new FormControl<string>(null),
    workEffortPurposeTypeId: new FormControl<string[]>(null),
    orderBy: new FormControl<string>(null),
    orderType: new FormControl<string>(null),
    detectionMode: new FormControl<DetectionMode>(null),
    debitCreditDefault: new FormControl<string>(null),
    periodTypeId: new FormControl<string>(null),
    currentStatusId: new FormControl<string>(null),
    respCenterId: new FormControl<string>(null)
  });


  ind$: Subscription;

  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();

  state$: Observable<unknown> = this.router.events
    .pipe(
      tap(() => this.loading = true),
      filter(event => event instanceof NavigationEnd),
      map(() => { return this.location.getState() }),
      startWith(this.location.getState())
    );

  orderByColumn: SelectItem[];

  collapseFilters: boolean = true;

  detectionModeItems: SelectItem<DetectionMode>[] = [
    { label: this.i18nService.translate("By single objective"), value: { inputEnumId: "ACCINP_OBJ", detectOrgUnitIdFlag: "N" } },
    { label: this.i18nService.translate("By organizational unit"), value: { inputEnumId: "ACCINP_UO", detectOrgUnitIdFlag: "Y" } },
    { label: this.i18nService.translate("Unique by indicator"), value: { inputEnumId: "ACCINP_UO", detectOrgUnitIdFlag: "N" } },
  ]

  isReservedAccount: string;

  chipFilter: SelectItem[] = [];

  ref: DynamicDialogRef = new DynamicDialogRef();
  refSuccess: DynamicDialogRef = new DynamicDialogRef();

  tableTitle$ = this.route.data.pipe(map(x => { return x.breadcrumb }));

  memState: { [key: string]: string | string[] | number | DetectionMode };

  get search() { return this.filterForm.get('search') }
  get matchModeSearch() { return this.filterForm.get('matchModeSearch') }
  get orderBy() { return this.filterForm.get('orderBy') }
  get orderType() { return this.filterForm.get('orderType') }
  get workEffortPurposeTypeId() { return this.filterForm.get('workEffortPurposeTypeId') }
  get glAccountTypeId() { return this.filterForm.get('glAccountTypeId') }
  get detectionMode() { return this.filterForm.get('detectionMode') }
  get debitCreditDefault() { return this.filterForm.get('debitCreditDefault') }
  get periodTypeId() { return this.filterForm.get('periodTypeId') }
  get currentStatusId() { return this.filterForm.get('currentStatusId') }
  get respCenterId() { return this.filterForm.get('respCenterId') }

  gpMenu = toSignal<string>(this.route.data.pipe(map(x => x.gpMenu)));
  context = toSignal<string>(this.route.data.pipe(map(x => x.context)));
  labelGlAccountList = computed(() => this.gpMenu() && this.context() ? `glAccountList.${this.context()}.${this.gpMenu()}` : null);
  labelNewGlAccount = computed(() => this.gpMenu() && this.context() ? `newGlAccount.${this.context()}.${this.gpMenu()}` : null);
  labelFilterSearchGlAccount = computed(() => this.gpMenu() && this.context() ? `filterSearchGlAccount.${this.context()}.${this.gpMenu()}` : null);
  labelToGlAccountTab = computed(() => this.gpMenu() && this.context() ? `toGlAccountTab.${this.context()}.${this.gpMenu()}` : null);
  labelToAcctgTrans = computed(() => this.gpMenu() && this.context() ? `toAcctgTrans.${this.context()}.${this.gpMenu()}` : null);

  constructor(
    private route: ActivatedRoute,
    private readonly router: Router,
    private readonly i18nService: I18NService,
    private glAccountService: GlAccountService,
    private statusItemService: StatusItemService,
    private glAccountTypeService: GlAccountTypeService,
    private enumService: EnumerationService,
    private formBuilder: FormBuilder,
    private workEffortPurposeTypeService: WorkEffortPurposeTypeService,
    private location: Location,
    private periodTypeService: PeriodTypeService,
    private msgService: MsgService,
    private confirmDialogService: ConfirmDialogService,
    public dialogService: DialogService,
    private partyService: PartyService
  ) {
    this.secondaryLang = this.i18nService.getIsSecondaryLang();
    this.orderByColumn = [
      { label: "Identificativo", value: "gl_account_id" },
      { label: "Codice", value: "account_code" },
      { label: "Titolo", value: this.secondaryLang ? "account_name_lang" : "account_name" }
    ]


    this.accountTypeEnumId = this.route.snapshot.paramMap.get('accountTypeEnumId');
    this.isReservedAccount = this.route.snapshot.paramMap.get('isReservedAccount');

    this.glAccountTypeObs$ = this.glAccountTypeService.getGlAccountTypeByReservedAccount(this.accountTypeEnumId, this.isReservedAccount);
  }

  ngOnDestroy(): void {
    this.ind$?.unsubscribe();

    this.destroy$.next();
    this.destroy$.unsubscribe();
    this.ref?.close();
    this.refSuccess?.close();
  }

  toIndicatorTab(glAccountId: string) {
    let labelRoute: string;
    switch (this.accountTypeEnumId) {
      case "ACCOUNT":
        labelRoute = "economic-tab";
        break;
      case "FINANCIAL":
        labelRoute = "financial-tab";
        break;
      case "INDICATOR":
        if (this.isReservedAccount === 'Y')
          labelRoute = "reserved-indicator-tab";
        else
          labelRoute = "indicator-tab";
        break;
    }
    this.router.navigate(
      [`${labelRoute}/${glAccountId}`], {
      state: {
        ...this.memState
      },
      relativeTo: this.route
    }
    );
  }

  getQueryParamValue(formControlName: string) {
    return this.route.snapshot.queryParamMap.get(formControlName);
  }

  ngOnInit(): void {
    this.setChip$();

    this.indicatorsReq$ = this.state$
      .pipe(

        map((state: { [key: string]: string | string[] | number | DetectionMode }) => {
          if (state.currentStatusId === undefined) {
            state.currentStatusId = "GLACC_ACTIVE"
          }

          state = this.location.getState() as { [key: string]: string | string[] | number | DetectionMode };
          this.memState = state;
          this.filterForm.setValue({
            matchModeSearch: state.matchModeSearch as string ?? FilterMatchMode.CONTAINS,
            search: state.search as string ?? null,
            glAccountTypeId: state.glAccountTypeId as string ?? null,
            workEffortPurposeTypeId: state.workEffortPurposeTypeId as string[] ?? null,
            orderBy: state.orderBy as string ?? null,
            orderType: state.orderType as string ?? null,
            detectionMode: state.detectionMode as DetectionMode ?? null,
            debitCreditDefault: state.debitCreditDefault as string ?? null,
            periodTypeId: state.periodTypeId as string ?? null,
            currentStatusId: state.currentStatusId as string ?? null,
            respCenterId: state.respCenterId as string ?? null
          })
          this.filterForm.markAsPristine();
          return state;
        }),
        map((state: { [key: string]: string | DetectionMode }) => {
          let { navigationId, detectionMode, ...params } = state;
          if (detectionMode) {
            const { inputEnumId, detectOrgUnitIdFlag } = detectionMode as DetectionMode;
            params = detectOrgUnitIdFlag ? { ...params, inputEnumId, detectOrgUnitIdFlag } : { ...params, inputEnumId };
          }
          params = omitBy(params, isNull);
          const { matchModeSearch, isSecondaryLang, ...values } = params;

          this.disableResetFilter = isEmpty(values);

          return this.disableResetFilter ? {} : params;
        }),
        switchMap((params: { [key: string]: string }) => this.glAccountService.getGlAccountByParam(this.accountTypeEnumId, this.isReservedAccount, { ...params, ...{ isSecondaryLang: this.secondaryLang } })
        ),
        tap(() => this.loading = false),
      )

    this.ind$ = this.reload$.pipe(
      takeUntil(this.destroy$),
      switchMap(() => this.indicatorsReq$)
    ).subscribe(values => this.indicators = values);

    this.statusItem$.subscribe(x => this.statusItems = x);

    this.glAccountTypeObs$.pipe(
      takeUntil(this.destroy$),
      map(x => dropdownGlAccountType(x, this.secondaryLang)),
    ).subscribe(x => this.glAccountType = x);


    this.uoGestore$.subscribe(x => this.uoGestore = x);
    this.workEffortPurposeType$.subscribe(x => this.workEffortPurposeType = x);
    this.periodType$.subscribe(x => this.periodType = x);
  }

  setChip$() {
    combineLatest({
      currentStatusId: this.currentStatusId.valueChanges.pipe(distinctUntilChanged()),
      statusItems: this.statusItem$,
      label: of(this.i18nService.translate("State"))
    })
      .pipe(takeUntil(this.destroy$), map(({ currentStatusId, statusItems, label }) => this.setChipFilter(currentStatusId, statusItems, label)))
      .subscribe()

    combineLatest({
      periodTypeId: this.periodTypeId.valueChanges.pipe(distinctUntilChanged()),
      periodTypeItems: this.periodType$,
      label: of(this.i18nService.translate("Periodicity"))
    })
      .pipe(takeUntil(this.destroy$), map(({ periodTypeId, periodTypeItems, label }) => this.setChipFilter(periodTypeId, periodTypeItems, label)))
      .subscribe()

    combineLatest({
      debitCreditDefault: this.debitCreditDefault.valueChanges.pipe(distinctUntilChanged()),
      debitCreditDefaultItems: this.debitCreditDefault$,
      label: of(this.i18nService.translate("Debit Credit Default"))
    })
      .pipe(takeUntil(this.destroy$), map(({ debitCreditDefault, debitCreditDefaultItems, label }) => this.setChipFilter(debitCreditDefault, debitCreditDefaultItems, label)))
      .subscribe()

    combineLatest({
      respCenterId: this.respCenterId.valueChanges.pipe(distinctUntilChanged()),
      respCenterIdItems: this.uoGestore$,
      label: of(this.i18nService.translate("respCenterId"))
    })
      .pipe(takeUntil(this.destroy$), map(({ respCenterId, respCenterIdItems, label }) => this.setChipFilter(respCenterId, respCenterIdItems, label)))
      .subscribe()

    combineLatest({
      detectionMode: this.detectionMode.valueChanges.pipe(distinctUntilChanged()),
      detectionModeItems: of(this.detectionModeItems),
      label: of(this.i18nService.translate('Detection mode'))
    })
      .pipe(takeUntil(this.destroy$), map(({ detectionMode, detectionModeItems, label }) => this.setChipFilterDetectionMode(detectionMode, detectionModeItems, label)))
      .subscribe()
  }

  setChipFilterMultipleChoise(value: string | string[], selectItems: SelectItem[], label: string) {

    if (value) {
      let newItem = selectItems.filter(x => value.indexOf(x.value) >= 0);
      let index = findIndex(this.chipFilter, { label });
      let finalLabel: string;
      newItem.forEach(x => {

        finalLabel ? finalLabel = `${finalLabel}, ${x.label}` : finalLabel = x.label;
      })
      if (index >= 0)
        this.chipFilter.splice(index, 1, { label, value: finalLabel })
      else
        this.chipFilter.push({ label, value: finalLabel })
    }
    else this.chipFilter = this.chipFilter.filter(item => item.label !== label);
  }

  setChipFilter(value: string | string[], selectItems: SelectItem[], label: string) {

    if (value) {
      let newItem = selectItems.find(x => value === x.value);
      let index = findIndex(this.chipFilter, { label });
      let finalLabel: string = newItem.label;
      if (index >= 0)
        this.chipFilter.splice(index, 1, { label, value: finalLabel })
      else
        this.chipFilter.push({ label, value: finalLabel })
    }
    else this.chipFilter = this.chipFilter.filter(item => item.label !== label);
  }

  setChipFilterDetectionMode(value: DetectionMode, selectItems: SelectItem<DetectionMode>[], label: string) {
    if (value) {
      let newItem = selectItems.find(x => x.value.detectOrgUnitIdFlag == value.detectOrgUnitIdFlag && x.value.inputEnumId == value.inputEnumId);
      let index = findIndex(this.chipFilter, { label });
      let finalLabel: string = newItem.label;
      if (index >= 0)
        this.chipFilter.splice(index, 1, { label, value: finalLabel })
      else
        this.chipFilter.push({ label, value: finalLabel })
    }
    else this.chipFilter = this.chipFilter.filter(item => item.label !== label);
  }

  getStatusItemDescription(currentStatusId: string): string {
    let status = this.statusItems.find(x => x.value === currentStatusId);
    if (status) {
      return status.label;
    }
    return currentStatusId ?? this.i18nService.translate("No state");
  }

  getStatusItemSeverity(currentStatusId: string): string {

    return getStatusItemSeverity(currentStatusId);
  }

  getUOGestoreDescription(respCenterId: string): string {
    let uoGestore = this.uoGestore.find(x => x.value === respCenterId);
    return uoGestore ? uoGestore.label : '';
  }


  getGlAccountTypeDescription(glAccountTypeId: string): string {
    let glAccountType = this.glAccountType.find(x => x.value === glAccountTypeId);
    return glAccountType ? glAccountType.label : glAccountTypeId;
  }

  getTypeDetection(inputEnumId: string, detectionOrgUnitIdFlag: string): string {
    let detectionMode: SelectItem<DetectionMode>;
    if (inputEnumId === "ACCINP_OBJ") {
      detectionMode = this.detectionModeItems.find(x => x.value.inputEnumId === inputEnumId)

    }
    else {
      detectionMode = this.detectionModeItems.find(x => x.value.inputEnumId === inputEnumId && x.value.detectOrgUnitIdFlag === detectionOrgUnitIdFlag)
    }
    return detectionMode ? detectionMode.label : `${inputEnumId}_${detectionOrgUnitIdFlag}`;
  }

  filter() {

    this.router.navigate(
      ['.'], {
      relativeTo: this.route,
      state: { ...this.filterForm.value, isSecondaryLang: this.secondaryLang },
    }
    );
  }

  resetFilter() {
    this.filterForm.reset();
    this.filter();

  }

  newIndicator() {
    this.ref = this.dialogService.open(NewIndicatorComponent, {
      header: this.i18nService.translate(`newGlAccount.${this.context()}.${this.gpMenu()}`),

      styleClass: 'min-width p-dialog-width',
      contentStyle: { overflow: 'auto' },
      data: {
        glAccountType: this.glAccountType,
        workEffortPurposeType: this.workEffortPurposeType,
        detectionModeItems: this.detectionModeItems,
        periodType: this.periodType,
        accountTypeEnumId: this.accountTypeEnumId,
        isReservedAccount: this.isReservedAccount
      }
    });

    this.ref.onClose.subscribe((newGlAccount: GlAccount) => {

      if (newGlAccount) {

        this.newItemAdded(newGlAccount);

      } else if (this.reloadOnCloseNew) {
        this.reloadOnCloseNew = false;
        this._reload.next();
      }
    });

  }

  newItemAdded(glAccount: GlAccount) {

    this.refSuccess = this.dialogService.open(NewIndicatorComponentSuccess, {
      showHeader: false,
      styleClass: 'min-width p-dialog-width-min',
      contentStyle: { overflow: 'auto', borderRadius: '10px' },
      data: {
        glAccount: glAccount,
        gpMenu: this.gpMenu(),
        context: this.context()
      }
    });

    this.refSuccess.onClose.subscribe(value => {
      switch (value?.command) {
        case "new":
          this.reloadOnCloseNew = true;
          this.newIndicator();
          break;
        case "reload":
          this._reload.next();
          break;
        case "toTab":
          this.toIndicatorTab(value.id);
          break;
      }
    })

  }

  delete() {

    this.confirmDialogService.deleteMultiElement().then(x => {
      if (x) {
        this.loadingDelete = true;

        this.glAccountService.deleteGlAccount(this.selectedIndicators.map(x => { return x.glAccountId }))
          .subscribe({
            next: (value) => {
              this.loadingDelete = false;
              this.msgService.successDeleteWithId(this.selectedIndicators.map(x => { return x.glAccountId }).toString());
              this.selectedIndicators = [];
              this._reload.next();
            },
            error: (err) => {
              this.loadingDelete = false;
              this.msgService.error(err.error.message);

            },
          })


      }

    });


  }

  toMeasuresObjectives(glAccountId: string) {
    this.router.navigate(["/c/CTX_WE/management/measures-objectives"], { queryParams: { glAccountId: glAccountId } })
  }

  toIndicatorMovements(glAccountId: string) {
    let url = "/c/CTX_AC/management"
    switch (this.accountTypeEnumId) {
      case "ACCOUNT":
        url = `${url}/economic-indicator-movements/${this.accountTypeEnumId}`
        break;
      case "FINANCIAL":
        url = `${url}/financial-indicator-movements/${this.accountTypeEnumId}`
        break;
      case "INDICATOR":
        this.isReservedAccount === 'Y' ?
          url = `${url}/reserved-indicator-movements/${this.accountTypeEnumId}/${this.isReservedAccount}` :
          url = `${url}/indicator-movements/${this.accountTypeEnumId}/${this.isReservedAccount}`

        break;
    }

    console.log(url)
    this.router.navigate([url], { state: { glAccountId: glAccountId } })

  }
}

export function getStatusItemSeverity(currentStatusId: string): string {

  switch (currentStatusId) {
    case 'GLACC_ACTIVE':
      return 'success';

    case 'GLACC_CLOSED':
      return 'info';

    case 'GLACC_OPEN':
      return 'warning';
    default:
      return null;
  }
}
