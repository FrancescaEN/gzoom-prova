import { ChangeDetectionStrategy, Component, OnDestroy, OnInit, computed } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { I18NService } from 'app/i18n/i18n.service';
import { SelectItem } from 'primeng/api';
import { DetectionMode } from '../../indicators.component';
import { UomService } from 'app/api/service/uom.service';
import { dropdownCustomMethod, dropdownDataSource, dropdownEnumeration, dropdownGlAccount, dropdownGlAccountClass, dropdownGlAccountType, dropdownGlResourceType, dropdownPartyEx, dropdownPeriodType, dropdownRoleType, dropdownUom, dropdownUomRange } from 'app/commons/utils/dropdownSelectItem';
import { BehaviorSubject, Observable, Subject, distinctUntilChanged, lastValueFrom, map, of, switchMap, takeUntil, tap } from 'rxjs';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { MsgService } from 'app/commons/service/message.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { IndicatorService } from '../../indicator.service';
import { ButtonService } from '../../../../../commons/service/button.service';
import { WorkEffortPurposeAccountService } from 'app/api/service/work-effort-purpose-account.service';
import { DateMoreThanValidator } from 'app/commons/validators/custom-validator';
import { GlAccountTypeService } from 'app/api/service/gl-account-type.service';
import { GlResourceTypeService } from 'app/api/service/gl-resource-type.service';
import { GlAccountClassService } from 'app/api/service/gl-account-class.service';
import { DataSourceService } from 'app/api/service/data-source.service';
import { CustomMethodService } from 'app/api/service/custom-method.service';
import { PeriodTypeService } from 'app/api/service/period-type.service';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { UomRangeService } from 'app/api/service/uom-range.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { GlAccount } from 'app/api/model/glAccount';
import { GlAccountRoleService } from 'app/api/service/gl-account-role.service';
import { GlAccountInputCalcService } from 'app/api/service/gl-account-input-calc.service';
import { GlAccountMeasRatScService } from 'app/api/service/gl-account-meas-rat-sc.service';
import { RoleTypeService } from 'app/api/service/role-type.service';
import { PartyService } from 'app/api/service/party.service';
import { Context } from 'app/commons/enum/context';
import { Permission, UserPermissionService } from 'app/shared/user-permission.service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'gzoom-indicator-tab-indicator',
  templateUrl: './indicator-tab-indicator.component.html',
  styleUrls: ['./indicator-tab-indicator.component.scss'],
  changeDetection: ChangeDetectionStrategy.Default,
})
export default class IndicatorTabIndicatorComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  isFullAdmin$: Observable<boolean>;
  BILING: boolean = this.i18nService.getLanguageType() == "BILING";
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  visible: boolean = true;
  activeIndex: number[] = [0, 1, 2, 3, 4];
  form: FormGroup = this.formBuilder.group({
    accountName: new FormControl<string>(null, Validators.required),
    accountNameLang: new FormControl<string>(null, this.BILING ? Validators.required : Validators.nullValidator),
    accountCode: new FormControl<string>(null, [Validators.required, Validators.maxLength(100)]),
    sequenceId: new FormControl<number>(null, [Validators.maxLength(20)]),
    description: new FormControl<string>(null),
    descriptionLang: new FormControl<string>(null),
    source: new FormControl<string>(null),
    sourceLang: new FormControl<string>(null),
    glAccountTypeId: new FormControl<string>(null, Validators.required),
    detectionMode: new FormControl<DetectionMode>(null, Validators.required),
    defaultUomId: new FormControl<string>(null, Validators.required),
    periodTypeId: new FormControl<string>(null, Validators.required),
    currentStatusId: new FormControl<string>(null, Validators.required),
    fromDate: new FormControl<Date>(null),
    thruDate: new FormControl<Date>(null, DateMoreThanValidator('fromDate')),
    glResourceTypeId: new FormControl<string>(null),
    glAccountClassId: new FormControl<string>(null),
    referencedAccountId: new FormControl<string>(null),
    dataSourceId: new FormControl<string>(null),
    calcCustomMethodId: new FormControl<string>(null),
    debitCreditDefault: new FormControl<string>(null, Validators.required),
    weMeasureTypeEnumId: new FormControl<string>(null, Validators.required),
    weWithoutTarget: new FormControl<string>(null, Validators.required),
    periodicalAbsoluteEnumId: new FormControl<string>(null, Validators.required),
    weWithoutPerf: new FormControl<string>(null, Validators.required),
    weScoreConvEnumId: new FormControl<string>(null, Validators.required),
    weScoreRangeEnumId: new FormControl<string>(null, Validators.required),
    uomRangeId: new FormControl<string>(null),
    targetPeriodEnumId: new FormControl<string>(null, Validators.required),
    prioCalc: new FormControl<number>(null, [Validators.maxLength(20)]),
    respCenterRoleTypeId: new FormControl<string>(null),
    respCenterId: new FormControl<string>(null),
  });

  glAccount: GlAccount;
  editMode: boolean;
  accountTypeEnumId: string;
  isReservedAccount: string;

  glAccountType: SelectItem[];
  workEffortPurposeType: SelectItem[];
  detectionModeItems: SelectItem<DetectionMode>[] = [
    { label: this.i18nService.translate("By single objective"), value: { inputEnumId: "ACCINP_OBJ", detectOrgUnitIdFlag: "N" } },
    { label: this.i18nService.translate("By organizational unit"), value: { inputEnumId: "ACCINP_UO", detectOrgUnitIdFlag: "Y" } },
    { label: this.i18nService.translate("Unique by indicator"), value: { inputEnumId: "ACCINP_UO", detectOrgUnitIdFlag: "N" } },
  ]

  periodType: SelectItem[];
  periodType$: Observable<SelectItem[]>;

  uom$: Observable<SelectItem[]>;
  statusItems$: Observable<SelectItem[]> = this.indicatorService.getStatusItems().pipe(takeUntil(this.destroy$));
  glAccountId: string;
  glAccountType$: Observable<SelectItem[]>;
  glResourceType$: Observable<SelectItem[]>;
  glAccountClass$: Observable<SelectItem[]>;
  referencedAccount$: Observable<SelectItem[]>;
  dataSource$: Observable<SelectItem[]>;
  customMethod$: Observable<SelectItem[]>;
  calcCustomMethodIdIsNotNull$: Observable<boolean>;
  uomIsRatingScale$: Observable<boolean>;
  isByOrganizationUnit$: Observable<boolean>;
  creditDefault$: Observable<SelectItem[]>;
  weMeasureTypeEnumId$: Observable<SelectItem[]>;
  warningPurpose$: Observable<boolean>;
  warningUODetected$: Observable<boolean>;
  warningCalculationFormula$: Observable<boolean>;
  warningValueList$: Observable<boolean>;
  targetPeriodEnumId$: Observable<SelectItem[]>;
  weWithoutTarget$: Observable<SelectItem[]>;
  periodicalAbsoluteEnumId$: Observable<SelectItem[]>;
  weWithoutPerf$: Observable<SelectItem[]>;
  weScoreConvEnumId$: Observable<SelectItem[]>;
  weScoreRangeEnumId$: Observable<SelectItem[]>;
  uomRangeId$: Observable<SelectItem[]>;
  respCenterRoleType$: Observable<SelectItem[]>;
  respCenterId$: Observable<SelectItem[]>;
  disabledRespCenterId$: Observable<boolean>;
  cleanCalcCustomMethod: boolean = false;
  requiredRespCenter$: Observable<boolean>;


  get accountName() { return this.form.get("accountName") }
  get accountNameLang() { return this.form.get("accountNameLang") }
  get accountCode() { return this.form.get("accountCode") }
  get sequenceId() { return this.form.get("sequenceId") }
  get description() { return this.form.get("description") }
  get descriptionLang() { return this.form.get("descriptionLang") }
  get glAccountTypeId() { return this.form.get("glAccountTypeId") }
  get workEffortPurposeTypeId() { return this.form.get("workEffortPurposeTypeId") }
  get currentStatusId() { return this.form.get("currentStatusId") }
  get detectionMode() { return this.form.get("detectionMode") }
  get defaultUomId() { return this.form.get("defaultUomId") }
  get fromDate() { return this.form.get("fromDate") }
  get thruDate() { return this.form.get("thruDate") }
  get glResourceTypeId() { return this.form.get("glResourceTypeId") }
  get glAccountClassId() { return this.form.get("glResourceTypeId") }
  get referencedAccountId() { return this.form.get("referencedAccountId") }
  get dataSourceId() { return this.form.get("dataSourceId") }
  get source() { return this.form.get("source") }
  get sourceLang() { return this.form.get("sourceLang") }
  get calcCustomMethodId() { return this.form.get("calcCustomMethodId") }
  get debitCreditDefault() { return this.form.get("debitCreditDefault") }
  get targetPeriodEnumId() { return this.form.get("targetPeriodEnumId") }
  get weWithoutTarget() { return this.form.get("weWithoutTarget") }
  get periodicalAbsoluteEnumId() { return this.form.get("periodicalAbsoluteEnumId") }
  get weWithoutPerf() { return this.form.get("weWithoutPerf") }
  get weScoreConvEnumId() { return this.form.get("weScoreConvEnumId") }
  get weScoreRangeEnumId() { return this.form.get("weScoreRangeEnumId") }
  get uomRangeId() { return this.form.get("uomRangeId") }
  get prioCalc() { return this.form.get("prioCalc") }
  get respCenterRoleTypeId() { return this.form.get("respCenterRoleTypeId") }
  get respCenterId() { return this.form.get("respCenterId") }

  gpMenu = toSignal<string>(this.route.parent.data.pipe(map(x => x.gpMenu)));
  context = toSignal<string>(this.route.parent.data.pipe(map(x => x.context)));
  labelAccordionGlAccountManagement = computed(() => this.gpMenu() && this.context() ? `accordionGlAccountManagement.${this.context()}.${this.gpMenu()}` : null);


  constructor(private router: Router,
    private route: ActivatedRoute, private i18nService: I18NService,
    private formBuilder: FormBuilder,
    private uomService: UomService,
    private location: Location,
    private glAccountService: GlAccountService,
    private msgService: MsgService,
    private indicatorService: IndicatorService,
    private buttonService: ButtonService,
    private workEffortPurposeAccountService: WorkEffortPurposeAccountService,
    private glAccountTypeService: GlAccountTypeService,
    private glResourceTypeService: GlResourceTypeService,
    private glAccountClassService: GlAccountClassService,
    private dataSourceService: DataSourceService,
    private customMethodService: CustomMethodService,
    private periodTypeService: PeriodTypeService,
    private enumservice: EnumerationService,
    private uomRangeService: UomRangeService,
    private confirmDialogService: ConfirmDialogService,
    private glAccountRoleService: GlAccountRoleService,
    private glAccountInputCalcService: GlAccountInputCalcService,
    private glAccountMeasRatScService: GlAccountMeasRatScService,
    private roleTypeService: RoleTypeService,
    private partyService: PartyService,
    private userPermissionService: UserPermissionService
  ) {

    this.buttonService.showSaveButton(true);
    this.glAccountId = this.route.snapshot.paramMap.get('glAccountId');
    this.accountTypeEnumId = this.route.snapshot.paramMap.get('accountTypeEnumId');
    this.isFullAdmin$ = this.userPermissionService.hasPermission(this.route.parent.snapshot.data.context as Context, Permission.ADMIN);
  }


  ngOnInit(): void {


    this.isFullAdmin$
      .pipe(
        takeUntil(this.destroy$),
      ).subscribe(isTrue => {
        if (!isTrue) {
          this.respCenterId.setValidators(Validators.required);
          this.respCenterRoleTypeId.setValidators(Validators.required);
        }
      })

    this.reload$.pipe(
      switchMap(() => this.glAccountService.getGlAccount(this.glAccountId)),
      takeUntil(this.destroy$),
      tap(glAccount => {
        this.glAccount = glAccount;
        this.setForm(this.glAccount);
        this.cleanCalcCustomMethod = false;
      }),
    ).subscribe()

    this.warningValueList$ = this.reload$.pipe(
      switchMap(() => this.uomService.isRatingScaleByGlAccount(this.glAccountId)),
      tap((isRatingScale) => this.indicatorService.setEnableValueList(isRatingScale)),
      switchMap((isRatingScale) => {
        if (isRatingScale) return this.glAccountMeasRatScService.showWarningValueList(this.glAccountId)
        return of(false);
      })
    )


    this.warningPurpose$ = this.reload$.pipe(
      switchMap(() => this.workEffortPurposeAccountService.existWorkEffortPurposeAccountByGlAccountId(this.glAccountId))
    ).pipe(map(x => { return !x }));

    this.warningUODetected$ = this.reload$.pipe(
      switchMap(() => this.glAccountRoleService.showWarningUODetected(this.glAccountId))
    )

    this.warningCalculationFormula$ = this.reload$.pipe(
      switchMap(() => this.glAccountInputCalcService.showWarningCalculationFormula(this.glAccountId))
    )

    this.currentStatusId.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(
      value => this.indicatorService.setCurrentStatus(value)
    );

    this.accountCode.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(
      value => this.indicatorService.setCode(value)
    );

    if (!this.secondaryLang) {
      this.accountName.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(
        value => this.indicatorService.setTitle(value)
      );
    }
    else {
      this.accountNameLang.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(
        value => this.indicatorService.setTitle(value)
      );
    }

    this.glAccountType$ = this.glAccountTypeService.getGlAccountTypeByReservedAccount(this.accountTypeEnumId, this.isReservedAccount).pipe(
      map(x => dropdownGlAccountType(x, this.secondaryLang)),
    );

    this.glResourceType$ = this.glAccountTypeId.valueChanges.pipe(
      distinctUntilChanged(),
      switchMap((glAccountTypeId) => this.glResourceTypeService.getByGlAccountTypeId(glAccountTypeId)),
      map(x => dropdownGlResourceType(x, this.secondaryLang)));

    this.glAccountClass$ = this.glAccountClassService.getByAccountTypeEnumId(this.accountTypeEnumId).pipe(
      map(x => dropdownGlAccountClass(x))
    );

    this.referencedAccount$ = this.glAccountService.getGlAccountByOrganizationPartyId().pipe(
      map(x => dropdownGlAccount(x, this.secondaryLang)),
    );

    this.dataSource$ = this.dataSourceService.getDataSource().pipe(
      map(x => dropdownDataSource(x)),
    );

    this.customMethod$ = this.customMethodService.getCustomMethodList().pipe(
      map(x => dropdownCustomMethod(x)),
    );

    this.uom$ = this.uomService.uoms().pipe(
      map(x => dropdownUom(x, this.secondaryLang)),
    );

    this.periodType$ = this.periodTypeService.periodTypes()
      .pipe(
        map(x => dropdownPeriodType(x)));


    this.creditDefault$ = this.enumservice.enumerations("D_C").pipe(
      map(x => dropdownEnumeration(x, this.secondaryLang))
    );

    this.weMeasureTypeEnumId$ = this.enumservice.enumerations("WE_MEASURE_TYPE").pipe(
      map(x => dropdownEnumeration(x, this.secondaryLang))
    );

    this.targetPeriodEnumId$ = this.enumservice.enumerations("TARGET_PERIOD").pipe(
      map(x => dropdownEnumeration(x, this.secondaryLang))
    );

    this.weWithoutTarget$ = this.enumservice.enumerations("WE_WITHOUT_TARGET").pipe(
      map(x => dropdownEnumeration(x, this.secondaryLang))
    );

    this.periodicalAbsoluteEnumId$ = this.enumservice.enumerations("PRD_ABS").pipe(
      map(x => dropdownEnumeration(x, this.secondaryLang))
    );

    this.weWithoutPerf$ = this.enumservice.enumerations("WE_WITHOUT_PERF").pipe(
      map(x => dropdownEnumeration(x, this.secondaryLang))
    );

    this.weScoreConvEnumId$ = this.enumservice.enumerations("WE_SCORE_CONVERSION").pipe(
      map(x => dropdownEnumeration(x, this.secondaryLang))
    );

    this.weScoreRangeEnumId$ = this.enumservice.enumerations("WE_SCORE_RULE").pipe(
      map(x => dropdownEnumeration(x, this.secondaryLang))
    );

    this.uomRangeId$ = this.uomRangeService.getUomRangeList().pipe(
      map(x => dropdownUomRange(x))
    );

    this.respCenterRoleType$ = this.roleTypeService.getRoleTypeByParentTypeId("ORGANIZATION_UNIT").pipe(
      map(x => dropdownRoleType(x, this.secondaryLang))
    );


    this.respCenterId$ = this.respCenterRoleTypeId.valueChanges.pipe(
      distinctUntilChanged(),
      switchMap((value) => {
        if (value) return this.partyService.getUOGestoreByRespCenterRoleTypeId(this.route.parent.snapshot.data.context, value)
        return of([])
      }),
      map(x => dropdownPartyEx(x, this.secondaryLang))
    );

    this.respCenterRoleTypeId.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(
      value => {
        if (value) {
          this.respCenterId.enable()
        }
        else {
          this.respCenterId.setValue(null);
          this.respCenterId.disable()
        }
      }
    )

    this.calcCustomMethodIdIsNotNull$ = this.calcCustomMethodId.valueChanges.pipe(
      tap((x) => {
        if (this.glAccount.calcCustomMethodId != null && x == null) {
          this.cleanCalcCustomMethod = true;
        }
      }),
      map(x => !!x)
    );

    this.uomIsRatingScale$ = this.defaultUomId.valueChanges.pipe(
      switchMap((uomId) => this.uomService.isRatingScale(uomId))
    );

    this.isByOrganizationUnit$ = this.detectionMode.valueChanges.pipe(
      map((value: DetectionMode) => value.detectOrgUnitIdFlag === "Y" && value.inputEnumId === "ACCINP_UO")
    );


    this.buttonService.clickReset().pipe(takeUntil(this.destroy$)).subscribe(x => {

      this.setForm(this.glAccount)
    });

    this.buttonService.clickSave().pipe(takeUntil(this.destroy$))
      .subscribe(
        x => {
          if (this.form.valid) {
            const { detectionMode, ...values } = this.form.value;
            const { inputEnumId, detectOrgUnitIdFlag } = detectionMode;

            const updatedGlAccount: GlAccount = { ...this.glAccount, ...values, inputEnumId, detectOrgUnitIdFlag }
            /* console.log("OLD", this.glAccount);
             console.log("NEW", updatedGlAccount);*/
            this.glAccountService.updateGlAccount(updatedGlAccount)
              .subscribe({
                next: x => {
                  this._reload.next();
                  this.msgService.successUpdate();
                },
                error: err => {
                  this.msgService.error(err.error.message ?? err.message)
                }

              });

          }
          else {
            if (this.form.controls['thruDate'].errors != null && this.form.controls['thruDate'].errors['date_more_than']) {

              this.msgService.errorDate(this.fromDate.value, this.thruDate.value);
            }
            else {
              this.msgService.errorFieldsRequired();
            }
          }

        }
      );

    this.form.valueChanges.pipe(
      takeUntil(this.destroy$),
      map(() => { return this.form.pristine })).subscribe(value => {
        this.buttonService.disableSaveButton(value);
        this.editMode = !value;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
  }

  setForm(glAccount: GlAccount) {
    this.indicatorService.setGlAccount(glAccount);
    this.form.patchValue({
      accountName: glAccount.accountName,
      accountNameLang: glAccount.accountNameLang,
      accountCode: glAccount.accountCode,
      sequenceId: glAccount.sequenceId,
      description: glAccount.description,
      descriptionLang: glAccount.descriptionLang,
      source: glAccount.source,
      sourceLang: glAccount.sourceLang,
      glAccountTypeId: glAccount.glAccountTypeId,
      detectionMode: { inputEnumId: glAccount.inputEnumId, detectOrgUnitIdFlag: glAccount.detectOrgUnitIdFlag },
      defaultUomId: glAccount.defaultUomId,
      periodTypeId: glAccount.periodTypeId,
      currentStatusId: glAccount.currentStatusId,
      fromDate: glAccount.fromDate ? new Date(glAccount.fromDate) : null,
      thruDate: glAccount.thruDate ? new Date(glAccount.thruDate) : null,
      glResourceTypeId: glAccount.glResourceTypeId,
      glAccountClassId: glAccount.glAccountClassId,
      referencedAccountId: glAccount.referencedAccountId,
      dataSourceId: glAccount.dataSourceId,
      calcCustomMethodId: glAccount.calcCustomMethodId,
      debitCreditDefault: glAccount.debitCreditDefault,
      weMeasureTypeEnumId: glAccount.weMeasureTypeEnumId,
      targetPeriodEnumId: glAccount.targetPeriodEnumId,
      weWithoutTarget: glAccount.weWithoutTarget,
      periodicalAbsoluteEnumId: glAccount.periodicalAbsoluteEnumId,
      weWithoutPerf: glAccount.weWithoutPerf,
      weScoreConvEnumId: glAccount.weScoreConvEnumId,
      weScoreRangeEnumId: glAccount.weScoreRangeEnumId,
      uomRangeId: glAccount.uomRangeId,
      prioCalc: glAccount.prioCalc,
      respCenterRoleTypeId: glAccount.respCenterRoleTypeId,
      respCenterId: glAccount.respCenterId
    })
    this.form.markAsPristine();
    this.buttonService.disableSaveButton(true);
    this.editMode = false;
  }

  canDeactivate(): Promise<boolean> {
    if (!this.form.pristine) {
      return this.confirmDialogService.unsaved()
        .then(x => {
          if (x) this.indicatorService.setGlAccount(this.glAccount)
          return x
        }).catch(() => {
          return true
        })
    }
    else {
      return lastValueFrom(of(true))
    }

  }


}
