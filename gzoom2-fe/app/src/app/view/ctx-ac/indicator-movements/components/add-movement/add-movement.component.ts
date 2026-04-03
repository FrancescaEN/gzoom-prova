import {
  Component,
  DestroyRef,
  OnInit,
  computed,
  effect,
  inject,
  signal,
} from "@angular/core";
import { takeUntilDestroyed, toSignal } from "@angular/core/rxjs-interop";
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from "@angular/forms";
import { GlAccount } from "app/api/model/glAccount";
import { Uom } from "app/api/model/uom";
import { GlAccountService } from "app/api/service/gl-account.service";
import { UomService } from "app/api/service/uom.service";
import {
  dropdownCustomTimePeriod,
  dropdownGlAccount,
  dropdownGlFiscalType,
  dropdownParty,
  dropdownPartyRoleEx,
  dropdownUomRatingScale,
} from "app/commons/utils/dropdownSelectItem";
import { I18NService } from "app/i18n/i18n.service";
import { LayoutModule } from "app/layout/layout.module";
import { DynamicDialogConfig, DynamicDialogRef } from "primeng/dynamicdialog";
import { Observable, filter, map, of, startWith, switchMap, tap } from "rxjs";
import { UomType } from "../../../uom-type/uom_type";
import { fromDateGetDaysFromGiulianDate } from "app/commons/utils/dateUtils";
import { CustomTimePeriodService } from "app/api/service/custom-time-period.service";
import { CustomTimePeriod } from "app/api/model/customTimePeriod";
import { SelectItem } from "primeng/api";
import { UomRatingScaleService } from "app/api/service/uom-rating-scale.service";
import { GlFiscalTypeService } from "app/api/service/gl-fiscal-type.service";
import { WorkEffortMeasureService } from "app/api/service/work-effort-measure.service";
import { orderBy } from "lodash";
import { WorkEffortMeasure } from "app/api/model/workEffortMeasure";
import { PartyService } from "app/api/service/party.service";
import { CheckboxModule } from "primeng/checkbox";
import { AcctgTransEntryService } from "app/api/service/acctg-trans-entry.service";
import { MsgService } from "app/commons/service/message.service";
import { PartyRoleService } from "app/api/service/party-role.service";
import { PartyRole } from "app/api/model/partyRole";
import { LoadingPipe } from "app/commons/pipe/loading.pipe";
import { MessageInfoComponent } from "app/layout/message/message-info/message-info.component";
import { MessageWarnComponent } from "app/layout/message/message-warn/message-warn.component";
import {GzoomLabelComponent} from "../../../../../layout/form/gzoom-label/gzoom-label.component";
@Component({
  selector: "gzoom-add-movement",
  standalone: true,
  imports: [LayoutModule, CheckboxModule, LoadingPipe, MessageWarnComponent, GzoomLabelComponent],
  templateUrl: "./add-movement.component.html",
  styleUrl: "./add-movement.component.scss",
})
export class AddMovementComponent implements OnInit {
  destroy = inject(DestroyRef);
  public dialogConfig = inject(DynamicDialogConfig);
  public ref = inject(DynamicDialogRef);
  formBuilder = inject(FormBuilder);
  i18n = inject(I18NService);
  glAccountService = inject(GlAccountService);
  uomService = inject(UomService);
  customTimePeriodService = inject(CustomTimePeriodService);
  uomRatingScaleService = inject(UomRatingScaleService);
  glFiscalTypeService = inject(GlFiscalTypeService);
  workEffortMeasureService = inject(WorkEffortMeasureService);
  acctgTransEntryService = inject(AcctgTransEntryService);
  msgService = inject(MsgService);
  partyRoleService = inject(PartyRoleService);

  keyMsg = "add-movement";
  secondaryLang = this.i18n.getIsSecondaryLang();
  loadingSave = signal<boolean>(false);
  glAccountItems: GlAccount[] = [];
  glAccount = computed(() =>
    this.glAccountService
      .getGlAccountByAccountTypeEnumIdAndIsReservedAccount(
        this.accountTypeEnumId(),
        this.isReservedAccount(),
      )
      .pipe(
        tap((x) => {
          this.glAccountItems = x;
          if (this.defaultGlAccountId()) {
            this.glAccountId.setValue(this.defaultGlAccountId());
          }
          this.glAccountSelected.set(
            this.glAccountItems.find(
              (x) => x.glAccountId === this.defaultGlAccountId(),
            ),
          );
        }),
        map((x) => dropdownGlAccount(x, this.secondaryLang)),
      ),
  );

  glAccountSelected = signal<GlAccount>(null);
  inputEnumId = computed(() => this.glAccountSelected()?.inputEnumId);
  detectOrgUnitIdFlag = computed(
    () => this.glAccountSelected()?.detectOrgUnitIdFlag,
  );

  accountTypeEnumId = signal<string>(this.dialogConfig.data.accountTypeEnumId);
  isReservedAccount = signal<string>(this.dialogConfig.data.isReservedAccount);
  defaultGlAccountId = signal<string>(this.dialogConfig.data.glAccountId);

  roleTypeId = signal<string>(this.dialogConfig.data.roleTypeId);
  voucherRefId = signal<string>(this.dialogConfig.data.voucherRefId);
  partyId = signal<string>(this.dialogConfig.data.partyId);

  gpMenu = signal<string>(this.dialogConfig.data.gpMenu);
  context = signal<string>(this.dialogConfig.data.context);
  labelGlAccount = computed(() =>
    this.gpMenu() && this.context()
      ? `glAccount.${this.context()}.${this.gpMenu()}`
      : null,
  );
  labelSelectGlAccountForUom = computed(() =>
    this.gpMenu() && this.context()
      ? `selectGlAccountForUom.${this.context()}.${this.gpMenu()}`
      : null,
  );
  labelValueExists = computed(() =>
    this.gpMenu() && this.context()
      ? `valueExists.${this.context()}.${this.gpMenu()}`
      : null,
  );
  showLabelValueExists = false;

  isBySingleObjective = computed(
    () =>
      this.inputEnumId() === "ACCINP_OBJ" && this.detectOrgUnitIdFlag() === "N",
  );
  isByOrganizationalUnit = computed(
    () =>
      this.inputEnumId() === "ACCINP_UO" && this.detectOrgUnitIdFlag() === "Y",
  );
  isUniqueByIndicator = computed(
    () =>
      this.inputEnumId() === "ACCINP_UO" && this.detectOrgUnitIdFlag() === "N",
  );

  isByGlFiscalType = computed(
    () =>
      this.glAccountId?.value ||
      (this.inputEnumId() === "ACCINP_UO" &&
        this.detectOrgUnitIdFlag() === "Y" &&
        this.partyRole?.value) ||
      (this.inputEnumId() === "ACCINP_OBJ" && this.voucherRef?.value),
  );

  uom = signal<Uom>(null);
  uomType = signal<UomType>(null);
  uomIsRatingScale = computed(() => {
    return this.uomType()?.uomTypeId === "RATING_SCALE";
  });

  customTimePeriodItems = signal<CustomTimePeriod[]>(null);
  customTimePeriod = computed(() => {
    if (this.glAccountSelected()) {
      return this.customTimePeriodService
        .customTimePeriods(this.glAccountSelected().periodTypeId)
        .pipe(
          tap((x) => {
            this.customTimePeriodItems.set(x);
          }),
          map((x) => dropdownCustomTimePeriod(x, this.secondaryLang)),
        );
    }
    return of([] as SelectItem[]);
  });

  uomRatingScale = computed(() => {
    if (this.uom()?.uomTypeId === "RATING_SCALE") {
      return this.uomRatingScaleService
        .getUomRatingScale(this.uom().uomId)
        .pipe(
          map((x) =>
            dropdownUomRatingScale(x, this.secondaryLang).map((x) => {
              return { label: x.label, value: x.value.uomRatingValue };
            }),
          ),
        );
    }
    return of([] as SelectItem[]);
  });

  glFiscalType: Observable<SelectItem[]> = of([]);

  partyRoleEx = toSignal(
    this.partyRoleService
      .getPartyRoleExRoleType(this.dialogConfig.data.accountTypeEnumId)
      .pipe(map((x) => dropdownPartyRoleEx(x, this.secondaryLang))),
  );

  clone = false;

  form: FormGroup = this.formBuilder.group({
    glAccountId: new FormControl<string>(null, Validators.required),
    customTimePeriodId: new FormControl<string>(null, Validators.required),
    voucherRef: new FormControl<string>(
      null,
      this.isBySingleObjective()
        ? Validators.required
        : Validators.nullValidator,
    ),
    partyRole: new FormControl<Partial<PartyRole>>(
      null,
      this.isByOrganizationalUnit()
        ? Validators.required
        : Validators.nullValidator,
    ),
    amount: new FormControl<number>(null),
    selectedDate: new FormControl<Date>(null),
    glFiscalTypeId: new FormControl<string>(null, Validators.required),
  });

  get glAccountId() {
    return this.form.get("glAccountId");
  }
  get voucherRef() {
    return this.form.get("voucherRef");
  }
  get partyRole() {
    return this.form.get("partyRole");
  }
  get amount() {
    return this.form.get("amount");
  }
  get glFiscalTypeId() {
    return this.form.get("glFiscalTypeId");
  }
  get selectedDate() {
    return this.form.get("selectedDate");
  }
  get customTimePeriodId() {
    return this.form.get("customTimePeriodId");
  }

  customTimePeriodSignal = toSignal(this.customTimePeriodId.valueChanges);

  voucherRefSignal = toSignal(this.voucherRef.valueChanges);
  partyRoleSignal = toSignal(this.partyRole.valueChanges);

  workEffortMeasure$ = this.workEffortMeasureService
    .dropdownWorkEffortMeasure()
    .pipe(
      map((x) =>
        this.dropdownWorkEffortMeasureJoinWorkEffort(x, this.secondaryLang),
      ),
      tap((list) => {
        if (!list.find((x) => x.value === this.voucherRef.value))
          this.voucherRef.reset();
      }),
    );

  constructor() {
    effect(() => {
      if (this.customTimePeriodSignal() && this.glAccountSelected())
        this.reloadGlFiscalType();
    });

    effect(() => {
      if (this.glAccountSelected()) {
        this.uomService
          .getUomById(this.glAccountSelected().defaultUomId)
          .pipe(tap(console.log), takeUntilDestroyed(this.destroy))
          .subscribe((x) => {
            this.uom.set(x);
            this.uomType.set(x.uomType);
          });
      }
    });

    effect(() => {
      if (this.isByOrganizationalUnit()) {
        this.partyRole.setValidators(Validators.required);
      } else {
        this.partyRole.setValidators(Validators.nullValidator);
      }
    });

    effect(() => {
      if (this.isBySingleObjective()) {
        this.voucherRef.setValidators(Validators.required);
      } else {
        this.voucherRef.setValidators(Validators.nullValidator);
      }
    });
  }

  ngOnInit(): void {
    this.glAccountId.setValue(this.defaultGlAccountId());

    this.glAccountId.valueChanges
      .pipe(
        startWith(this.defaultGlAccountId()),
        switchMap((glAccountId) =>
          of(this.glAccountItems.find((x) => x.glAccountId === glAccountId)),
        ),
        takeUntilDestroyed(this.destroy),
      )
      .subscribe((glAccountSelected) => {
        this.glAccountSelected.set(glAccountSelected);
        this.amount.reset();
        this.voucherRef.reset();
        this.partyRole.reset();
        this.customTimePeriodId.reset();
        this.glFiscalTypeId.reset();
        this.selectedDate.reset();
      });

    this.customTimePeriodId.valueChanges
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe((value) => {
        this.glFiscalTypeId.reset();
        this.voucherRef.reset();
        this.partyRole.reset();
      });

    this.partyRole.valueChanges
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe((value) => {
        this.glFiscalTypeId.reset();
      });

    this.voucherRef.valueChanges
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe((value) => {
        this.glFiscalTypeId.reset();
      });

    this.selectedDate.valueChanges
      .pipe(
        takeUntilDestroyed(this.destroy),
        filter((x) => x),
      )
      .subscribe((value) =>
        this.amount.setValue(fromDateGetDaysFromGiulianDate(value)),
      );
  }

  create() {
    const acctgTranEntry = {
      ...this.form.value,
      acctgTrans: {
        partyId: this.partyRole.value?.partyId,
        roleTypeId: this.partyRole.value?.roleTypeId,
        glAccountTypeId: this.glAccountSelected().glAccountTypeId,
      },
    };
    this.loadingSave.set(true);

    this.acctgTransEntryService
      .createAcctgTransEntryEx(this.customTimePeriodId.value, acctgTranEntry)
      .subscribe({
        next: (result) => {
          this.loadingSave.set(false);
          this.msgService.success(this.i18n.translate("addedMovement"));
          this.reloadGlFiscalType();
          if (this.clone) {
            this.form.reset();
          } else {
            this.ref.close(result);
          }
        },
        error: (err) => {
          this.loadingSave.set(false);
          this.msgService.error(err.message ?? err, this.keyMsg);
        },
      });
  }

  reloadGlFiscalType() {
    this.glFiscalType = this.glFiscalTypeService
      .getGlFiscalTypeForNewIndicatorMovement(
        this.accountTypeEnumId() ?? null,
        this.glAccountSelected()?.glAccountId ?? null,
        this.customTimePeriodSignal() ?? null,
        this.partyRoleSignal()?.roleTypeId ?? null,
        this.partyRoleSignal()?.partyId ?? null,
        this.voucherRefSignal() ?? null,
      )
      .pipe(
        map((x) => dropdownGlFiscalType(x, this.secondaryLang)),
        tap((x) => {
          this.showLabelValueExists = x.length == 0;
          x.find((x) => x.value === this.glFiscalTypeId.value)
            ? null
            : this.glFiscalTypeId.reset();
        }),
      );
  }

  dropdownWorkEffortMeasureJoinWorkEffort(
    types: WorkEffortMeasure[],
    secondaryLang: boolean,
  ): SelectItem[] {
    if (types) {
      const selItem = types.map((x: WorkEffortMeasure) => {
        return {
          label: !secondaryLang
            ? x.workEffort.workEffortName
            : x.workEffort.workEffortNameLang,
          value: x.workEffortMeasureId,
          title:
            `${(this.secondaryLang ? x.uomDescrLang : x.uomDescr) ?? ""}\n` +
            `${x.workEffort.workEffortParent.etch ?? ""} - ` +
            `${(this.secondaryLang ? x.workEffort.workEffortParent.workEffortNameLang : x.workEffort.workEffortParent.workEffortName) ?? ""}\n` +
            `${(this.secondaryLang ? x.workEffort.descriptionLang : x.workEffort.description) ?? ""}\n` +
            `${(this.secondaryLang ? x.workEffortType?.descriptionLang : x.workEffortType?.description) ?? ""}\n` +
            `${(this.secondaryLang ? x.workEffortType2?.descriptionLang : x.workEffortType2?.description) ?? ""}`,
        };
      });
      return orderBy(selItem, ["label"], ["asc"]);
    }
    return [];
  }
}
