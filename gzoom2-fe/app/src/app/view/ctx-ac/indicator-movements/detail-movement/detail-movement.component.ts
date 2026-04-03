import { ChangeDetectionStrategy, Component, DestroyRef, OnInit, computed, effect, inject, input, signal } from '@angular/core';
import { ButtonBarTableComponent } from 'app/layout/button/button-bar-table/button-bar-table.component';
import { LayoutModule } from 'app/layout/layout.module';
import { CardDetail } from 'app/layout/gzoom-card-detail/gzoom-card-detail.component';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { AcctgTransEntryService } from 'app/api/service/acctg-trans-entry.service';
import { BehaviorSubject, combineLatest, filter, map, switchMap, tap } from 'rxjs';
import { I18NService } from 'app/i18n/i18n.service';
import { UomService } from 'app/api/service/uom.service';
import { UomAmoutComponent } from 'app/shared/components/uom-amout/uom-amout.component';
import { UomRatingScaleService } from 'app/api/service/uom-rating-scale.service';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { dropdownGlFiscalType, dropdownParty, dropdownPartyRoleEx, dropdownUomRatingScaleByUomId } from 'app/commons/utils/dropdownSelectItem';
import { addDaysFromGiulianDate, fromDateGetDaysFromGiulianDate } from 'app/commons/utils/dateUtils';
import { Uom } from 'app/api/model/uom';
import { SelectItem } from 'primeng/api';
import { GlFiscalTypeService } from 'app/api/service/gl-fiscal-type.service';
import { CustomTimePeriodService } from 'app/api/service/custom-time-period.service';
import { Permission, UserPermissionService } from 'app/shared/user-permission.service';
import { Context } from 'app/commons/enum/context';
import { ActivatedRoute, Router } from '@angular/router';
import { PartyService } from 'app/api/service/party.service';
import { GlAccount } from 'app/api/model/glAccount';
import { WorkEffortMeasureService } from 'app/api/service/work-effort-measure.service';
import { WorkEffortMeasure } from 'app/api/model/workEffortMeasure';
import { orderBy } from 'lodash';
import { AcctgTransEntry } from 'app/api/model/acctgTransEntry';
import { MsgService } from 'app/commons/service/message.service';
import { Location } from "@angular/common";
import { PartyRole } from 'app/api/model/partyRole';
import { PartyRoleService } from 'app/api/service/party-role.service';
import {GzoomLabelComponent} from "../../../../layout/form/gzoom-label/gzoom-label.component";

@Component({
  selector: 'gzoom-detail-movement',
  standalone: true,
  imports: [LayoutModule,
    ButtonBarTableComponent,
    UomAmoutComponent, GzoomLabelComponent,
  ],
  templateUrl: './detail-movement.component.html',
  styleUrl: './detail-movement.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export default class DetailMovementComponent implements OnInit {
  //inject services
  destroy = inject(DestroyRef);
  i18n = inject(I18NService);
  location = inject(Location);
  router = inject(Router);
  msgService = inject(MsgService);
  activatedRoute = inject(ActivatedRoute);
  userPermissionService = inject(UserPermissionService)
  acctgTransEntryService = inject(AcctgTransEntryService);
  glAccountService = inject(GlAccountService);
  uomService = inject(UomService);
  uomRatingScaleService = inject(UomRatingScaleService);
  formBuilder = inject(FormBuilder);
  glFiscalTypeService = inject(GlFiscalTypeService);
  customTimePeriodService = inject(CustomTimePeriodService);
  partyService = inject(PartyService);
  partyRoleService = inject(PartyRoleService);
  workEffortMeasureService = inject(WorkEffortMeasureService);

  backLabel = toSignal(this.activatedRoute.parent.data.pipe(map(x => x.breadcrumb)));
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  _reloadCardDetail = new BehaviorSubject<void>(null);
  reloadCardDetail$ = this._reload.asObservable();

  loadingSave = signal<boolean>(false);

  //input by route
  acctgTransId = input.required<string>();
  acctgTransEntrySeqId = input.required<string>();
  accountTypeEnumId = input.required<string>();
  context = input.required<Context>();

  dropdownYN: SelectItem[] = [
    { label: this.i18n.translate('Y'), value: 'Y' },
    { label: this.i18n.translate('N'), value: 'N' }];

  acctgTransEntryForm = this.formBuilder.group({
    amount: new FormControl<number>(null),
    selectedDate: new FormControl<Date>(null),
    amountLocked: new FormControl<string>(null, Validators.required),
    glFiscalTypeId: new FormControl<string>(null, Validators.required),
    customTimePeriodId: new FormControl<string>(null, Validators.required),
    glAccountFinId: new FormControl<string>(null),
    description: new FormControl<string>(null),
    descriptionLang: new FormControl<string>(null),
    voucherRef: new FormControl<string>(null),
    acctgTrans: this.formBuilder.group({
      voucherRef: new FormControl<string>(null),
      partyId: new FormControl<string>(null, Validators.required),
      partyRole: new FormControl<Partial<PartyRole>>(null, Validators.required),
      description: new FormControl<string>(null),
      descriptionLang: new FormControl<string>(null),
      glFiscalTypeId: new FormControl<string>(null, Validators.required),
      isPosted: new FormControl<string>(null, Validators.required),

    })
  });

  get acctgTransForm() { return this.acctgTransEntryForm.get('acctgTrans') }

  get amount() {
    return this.acctgTransEntryForm.get('amount');
  }

  get selectedDate() {
    return this.acctgTransEntryForm.get('selectedDate');
  }

  get amountLocked() {
    return this.acctgTransEntryForm.get('amountLocked');
  }

  get glFiscalTypeId() {
    return this.acctgTransEntryForm.get('glFiscalTypeId');
  }

  get customTimePeriodId() {
    return this.acctgTransEntryForm.get('customTimePeriodId');
  }

  get glAccountFinId() {
    return this.acctgTransEntryForm.get('glAccountFinId');
  }

  get description() {
    return this.acctgTransEntryForm.get('description');
  }

  get descriptionLang() {
    return this.acctgTransEntryForm.get('descriptionLang');
  }

  get voucherRef() {
    return this.acctgTransEntryForm.get('voucherRef');
  }

  // Getters for nested form group acctgTrans
  get acctgTransVoucherRef() {
    return this.acctgTransEntryForm.get('acctgTrans.voucherRef');
  }

  get partyId() {
    return this.acctgTransEntryForm.get('acctgTrans.partyId');
  }

  get partyRole() {
    return this.acctgTransEntryForm.get('acctgTrans.partyRole');
  }

  get acctgTransDescription() {
    return this.acctgTransEntryForm.get('acctgTrans.description');
  }

  get acctgTransDescriptionLang() {
    return this.acctgTransEntryForm.get('acctgTrans.descriptionLang');
  }

  get acctgTransGlFiscalTypeId() {
    return this.acctgTransEntryForm.get('acctgTrans.glFiscalTypeId');
  }

  get isPosted() {
    return this.acctgTransEntryForm.get('acctgTrans.isPosted')
  }

  posted = signal<boolean>(false);

  acctgTransEntry = signal<AcctgTransEntry>(null);
  acctgTransEntry$ = computed(() =>
    this.reload$
      .pipe(
        switchMap(() => this.acctgTransEntryService.getMovementByPrimaryKey(this.acctgTransId(), this.acctgTransEntrySeqId())
        ),
        tap(x => {
          this.acctgTransEntry.set(x);
          const { amount, amountLocked, glFiscalTypeId, description, descriptionLang, acctgTrans, voucherRef: acctgTransEntryVoucherRef } = x;
          this.acctgTransEntryForm.patchValue(
            {
              amount,
              amountLocked,
              glFiscalTypeId,
              description,
              descriptionLang,
              voucherRef: acctgTransEntryVoucherRef
            });
          const { description: acctgTransDescription, descriptionLang: acctgTransDescriptionLang, voucherRef, partyId, roleTypeId, glFiscalTypeId: acctgTransGlFiscalTypeId, isPosted } = acctgTrans
          this.acctgTransForm.patchValue({
            description: acctgTransDescription,
            descriptionLang: acctgTransDescriptionLang,
            voucherRef,
            partyId,
            partyRole: { partyId, roleTypeId },
            glFiscalTypeId: acctgTransGlFiscalTypeId,
            isPosted
          });
          this.posted.set(isPosted === 'Y');
          this.acctgTransEntryForm.markAsPristine();

        })
      )
  );

  showCRUD = computed(() => {
    if (this.userPermissionService.hasPermission(this.context(), Permission.ADMIN)
      || this.userPermissionService.hasPermission(this.context(), Permission.RESP)) {
      if (this.posted()) {
        this.acctgTransEntryForm.disable();
        this.isPosted.enable();
      }
      else {
        this.acctgTransEntryForm.enable();
      }
      return true
    }
    this.acctgTransEntryForm.disable();
    return false;
  }
  );

  //utilities variables
  secondaryLang = signal(this.i18n.getIsSecondaryLang());

  //button bar variables
  disableSave = signal<boolean>(true);

  //accordion variables
  activeIndex = [0, 1];

  //uom-amount variables
  origUomRatingScales$ = computed(() =>
    this.acctgTransEntry$()
      .pipe(
        switchMap(value => this.uomRatingScaleService.uomRatingScales(value.origCurrencyUomId))
      ));

  uomRatingScales$ = computed(() =>
    this.acctgTransEntry$()
      .pipe(
        switchMap(value => this.uomRatingScaleService.uomRatingScales(value.currencyUomId)),
        map(x => dropdownUomRatingScaleByUomId(x, this.secondaryLang()))
      )
  );


  //origin uom
  origUom$ = computed(() => {
    this.acctgTransEntry$()
      .pipe(
        switchMap(value => this.uomService.getUomById(value?.origCurrencyUomId)),
      )
  });

  origCurrentUomId$ = computed(() =>
    this.acctgTransEntry$()
      .pipe(
        filter(x => !!x),
        map(value => value.origCurrencyUomId)
      )
  )

  glAccount = signal<GlAccount>(null);
  glAccount$ = computed(() =>
    this.acctgTransEntry$()
      .pipe(
        switchMap(value => this.glAccountService.getGlAccount(value.glAccountId)),
        tap(value => this.glAccount.set(value))
      )
  );

  isByOrganizationalUnit = computed(() => this.glAccount()?.inputEnumId === 'ACCINP_UO' && this.glAccount()?.detectOrgUnitIdFlag === 'Y');
  isBySingleObjective = computed(() => this.glAccount()?.inputEnumId === 'ACCINP_OBJ' && this.glAccount()?.detectOrgUnitIdFlag === 'N');
  isUniqueByIndicator = computed(() => this.glAccount()?.inputEnumId === 'ACCINP_UO' && this.glAccount()?.detectOrgUnitIdFlag === 'N');

  uom$ = computed(() =>
    this.acctgTransEntry$()
      .pipe(
        switchMap(value => this.uomService.getUomById(value.currencyUomId))
      )
  );

  uom = signal<Uom>(null);

  workEffortMeasure$ = computed(() => combineLatest({
    glAccount: this.glAccount$(),
    customTimePeriod: this.customTimePeriod$()
  })
    .pipe(
      switchMap(({ glAccount, customTimePeriod }) => this.workEffortMeasureService.dropdownWorkEffortMeasure(
      )),
      map(x => this.dropdownWorkEffortMeasureJoinWorkEffort(x, this.secondaryLang())),
      tap(x => this.workEffortMeasure = x)
    ))
    ;

  workEffortMeasure: SelectItem[] = [];

  cardDetails$ = computed(() => {
    return combineLatest({
      glAccount: this.glAccount$(),
      customTimePeriod: this.customTimePeriod$(),
      workEffortMeasure: this.workEffortMeasure$(),
      partyRole: this.partyRole$
    })
      .pipe(
        map(({ glAccount, customTimePeriod, workEffortMeasure, partyRole }) => {
          const { gpMenu, context } = this.activatedRoute.snapshot.data;
          let cardDetails: CardDetail[] = [
            {
              title: this.i18n.translate(`glAccount.${context}.${gpMenu}`),
              description: (glAccount?.accountCode + ' - ') + (this.secondaryLang() ? glAccount?.accountNameLang : glAccount?.accountName) ?? ''
            },
            {
              title: this.i18n.translate('customTimePeriod'),
              description: (this.secondaryLang() ? customTimePeriod?.periodNameLang : customTimePeriod?.periodName) ?? ''
            }
          ];
          if (this.isBySingleObjective()) {
            cardDetails.push({
              title: this.i18n.translate('workEffortMeasure'),
              description: workEffortMeasure.find(x => x.value === this.voucherRef?.value)?.label
            })
          }

          if (this.isByOrganizationalUnit()) {
            cardDetails.push({
              title: this.i18n.translate('acctgTransPartyId'),
              description: partyRole.find(x => x.value?.partyId == this.partyRole?.value?.partyId && x.value?.roleTypeId == this.partyRole?.value?.roleTypeId)?.label
            })
          }

          return cardDetails;
        })
      )


  });
  cardDetail = signal<CardDetail[]>([]);

  glFiscalType = computed(() => {
    return this.glFiscalTypeService.getGlFiscalTypeByAccountTypeEnumId(this.accountTypeEnumId())
      .pipe(
        map(x => dropdownGlFiscalType(x, this.secondaryLang()))
      )
  });

  customTimePeriod$ = computed(() => this.customTimePeriodService.getCustomTimePeriodForIndicatorMovement(this.acctgTransId(), this.acctgTransEntrySeqId()))


  partyRole$ = this.partyRoleService.getPartyRoleExRoleType()
    .pipe(
      map(x => dropdownPartyRoleEx(x, this.secondaryLang())),
      tap(x => this.partyRoleSelectItems = x)
    )

  partyRoleSelectItems: SelectItem[] = [];

  constructor() {

    effect(() => {
      this.uom$().subscribe(value => this.uom.set(value))
    })

    effect(() => {
      if (this.uom()?.uomTypeId === 'DATE_MEASURE') {
        this.selectedDate.setValue(addDaysFromGiulianDate(this.amount.value))
      }
    })

  }

  ngOnInit(): void {
    this.cardDetails$().subscribe(x => this.cardDetail.set(x))

    this.selectedDate.valueChanges
      .pipe(
        takeUntilDestroyed(this.destroy),
      )
      .subscribe(value => this.amount.setValue(fromDateGetDaysFromGiulianDate(value)));

    this.voucherRef.valueChanges
      .pipe(
        takeUntilDestroyed(this.destroy)
      )
      .subscribe(value => {
        if (this.isBySingleObjective())
          this.cardDetail.update(x => {
            x.pop()
            x.push({
              title: this.i18n.translate('workEffortMeasure'),
              description: this.workEffortMeasure.find(x => x.value === value)?.label
            })
            return x;
          })

        this.acctgTransVoucherRef.setValue(value)
      });


    this.partyRole.valueChanges
      .pipe(
        takeUntilDestroyed(this.destroy)
      )
      .subscribe(value => {
        if (this.isByOrganizationalUnit())
          this.cardDetail.update(x => {
            x.pop()
            x.push({
              title: this.i18n.translate('acctgTransPartyId'),
              description: this.partyRoleSelectItems.find(x => x.value?.partyId == value?.partyId && x.value?.roleTypeId == value?.roleTypeId)?.label
            })
            return x;
          })

      });
  }

  reset() {
    this._reload.next();
  }

  save() {
    this.loadingSave.set(true);
    let acctgTransEntry = new AcctgTransEntry();
    acctgTransEntry = { ...this.acctgTransEntry(), ...this.acctgTransEntryForm.value as AcctgTransEntry }
    acctgTransEntry.acctgTrans.acctgTransId = this.acctgTransEntry().acctgTrans.acctgTransId;
    acctgTransEntry.acctgTrans.partyId = this.partyRole.value.partyId;
    acctgTransEntry.acctgTrans.roleTypeId = this.partyRole.value.roleTypeId;
    this.acctgTransEntryService.updateDetailMovement(acctgTransEntry)
      .subscribe({
        next: () => {
          this.msgService.successUpdate();
          this.loadingSave.set(false);
          this._reload.next();

        },
        error: (err) => {
          this.loadingSave.set(false);
          this.msgService.error(err.error.message ?? err.message, "error");
        }
      })
  }

  /**
   *  This function sends you to the previous page
   */
  back() {
    this.location.back()
    //this.router.navigate(['../../'], { relativeTo: this.activatedRoute })
  }

  dropdownWorkEffortMeasureJoinWorkEffort(types: WorkEffortMeasure[], secondaryLang: boolean): SelectItem[] {
    if (types) {
      const selItem = types.map((x: WorkEffortMeasure) => { return { label: ((!secondaryLang) ? x.workEffort.workEffortName : x.workEffort.workEffortNameLang), value: x.workEffortMeasureId, title: `${(secondaryLang ? x.uomDescrLang : x.uomDescr) ?? ''}\n${x.workEffort.workEffortParent.etch ?? ''} - ${(secondaryLang ? x.workEffort.workEffortParent.workEffortNameLang : x.workEffort.workEffortParent.workEffortName) ?? ''}` } });
      return orderBy(selItem, ['label'], ['asc']);
    }
    return []
  }
}
