import { ChangeDetectionStrategy, Component, DestroyRef, OnInit, ViewChild, computed, inject, input, output, signal } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { AcctgTransEntry } from 'app/api/model/acctgTransEntry';
import { AcctgTransEntryService } from 'app/api/service/acctg-trans-entry.service';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { doNothing } from 'app/commons/utils/doNothing';
import { I18NService } from 'app/i18n/i18n.service';
import { LayoutModule } from 'app/layout/layout.module';
import { Table, TableLazyLoadEvent } from 'primeng/table';
import { combineLatest, map, tap, BehaviorSubject, switchMap, of, filter, startWith } from 'rxjs';
import { addDaysFromGiulianDate, fromDateGetDaysFromGiulianDate } from 'app/commons/utils/dateUtils';
import { UomRatingScaleService } from 'app/api/service/uom-rating-scale.service';
import { ButtonBarTableComponent } from "../../../../../layout/button/button-bar-table/button-bar-table.component";
import { GlFiscalTypeService } from 'app/api/service/gl-fiscal-type.service';
import { dropdownGlFiscalType, dropdownPartyRoleEx, dropdownUomRatingScale } from 'app/commons/utils/dropdownSelectItem';
import { SelectItem, TableState } from 'primeng/api';
import { UomService } from 'app/api/service/uom.service';
import { WorkEffortMeasure } from 'app/api/model/workEffortMeasure';
import { isNull, omitBy, orderBy } from 'lodash';
import { GlAccount } from 'app/api/model/glAccount';
import { Uom } from 'app/api/model/uom';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Location } from '@angular/common';
import { MsgService } from 'app/commons/service/message.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { UomAmoutComponent } from 'app/shared/components/uom-amout/uom-amout.component';
import { UomRatingScale } from 'app/api/model/uomRatingScale';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { ButtonService } from 'app/commons/service/button.service';
import { PartyRoleService } from 'app/api/service/party-role.service';
import { PartyRole } from 'app/api/model/partyRole';

@Component({
  selector: 'gzoom-indicator-movements-table',
  standalone: true,
  templateUrl: './indicator-movements-table.component.html',
  styleUrl: './indicator-movements-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    LayoutModule,
    ButtonBarTableComponent,
    UomAmoutComponent
  ],
})
export class IndicatorMovementsTableComponent implements OnInit {
  destroy = inject(DestroyRef);
  confirmDialogService = inject(ConfirmDialogService);
  msgService = inject(MsgService);
  scrollableService = inject(ScrollableTabService);
  i18nService = inject(I18NService);
  acctgTransEntryService = inject(AcctgTransEntryService);
  glAccountService = inject(GlAccountService);
  uomRatingScaleService = inject(UomRatingScaleService);
  router = inject(Router);
  location = inject(Location);
  uomService = inject(UomService);
  glFiscalTypeService = inject(GlFiscalTypeService);
  partyRoleService = inject(PartyRoleService);
  dataStorageService = inject(DataStorageService);
  buttonService = inject(ButtonService);

  _reload = new BehaviorSubject<TableLazyLoadEvent>(null);
  reload$ = this._reload.asObservable();
  secondaryLang = signal<boolean>(this.i18nService.getIsSecondaryLang());

  accountTypeEnumId = input.required<string>();
  isReservedAccount = input<string>();
  inputEnumId = input.required<string>();
  detectOrgUnitIdFlag = input.required<string>();
  scrollable = toSignal(this.scrollableService.isScrollableWidth(960));
  stateKey = computed(() => `indicator-movements-table-${this.inputEnumId()}-${this.detectOrgUnitIdFlag()}`);
  isBySingleObjective = computed(() => this.inputEnumId() === 'ACCINP_OBJ' && this.detectOrgUnitIdFlag() === 'N');
  isByOrganizationalUnit = computed(() => this.inputEnumId() === 'ACCINP_UO' && this.detectOrgUnitIdFlag() === 'Y');
  isUniqueByIndicator = computed(() => this.inputEnumId() === 'ACCINP_UO' && this.detectOrgUnitIdFlag() === 'N');
  w50IndicatorColumn = computed(() => this.isUniqueByIndicator() && this.scrollable());
  w25IndicatorColumn = computed(() => !this.w50IndicatorColumn() && this.scrollable());
  loading = signal<boolean>(true);
  loadingSave = signal<boolean>(false);
  loadingDelete = signal<boolean>(false);
  addDialogRef = input.required<DynamicDialogRef>();
  tableLazyLoadEvent: TableLazyLoadEvent;

  selectedItems: AcctgTransEntry[] = [];

  disabledDelete = signal<boolean>(true);

  filterState$ = this.router.events.pipe(
    filter(event => event instanceof NavigationEnd),
    switchMap(() => of(this.location.getState())),
    startWith(omitBy(this.location.getState(), isNull)),
    map(({ navigationId, ...values }: { [key: string]: string | string[] | number | boolean }) => omitBy(values, isNull)),
    tap(() => this.loading.set(true))
  );

  tmpValue;

  route = inject(ActivatedRoute);
  gpMenu = toSignal<string>(this.route.data.pipe(map(x => x.gpMenu)));
  context = toSignal<string>(this.route.data.pipe(map(x => x.context)));
  labelSelectGlAccount = computed(() => this.gpMenu() && this.context() ? `selectGlAccount.${this.context()}.${this.gpMenu()}` : null);


  @ViewChild('tableMovement') tableMovement: Table;
  acctgTransEntry$ = computed(() => {
    return combineLatest({
      value: this.reload$,
      filter: this.filterState$
    })
      .pipe(
        switchMap(({ value, filter }) => value ? this.acctgTransEntryService.getIndicatorMovements(this.accountTypeEnumId(), this.inputEnumId(), this.detectOrgUnitIdFlag(), { ...value, secondaryLang: this.secondaryLang() }, this.isReservedAccount(), filter) : of([] as AcctgTransEntry[])),
        tap(() => {
          this.loading.set(false);

        })
      )
  });
  acctgTransEntry: AcctgTransEntry[];
  //updatedAcctgTransEntry: AcctgTransEntry[] = [];
  updatedAcctgTransEntry = signal<AcctgTransEntry[]>([]);
  disableSave = computed(() => !(this.updatedAcctgTransEntry().length > 0))

  isEmpty = output<boolean>();
  totalRecords$ = computed(() =>
    combineLatest({
      value: this.reload$,
      filter: this.filterState$
    })
      .pipe(
        switchMap(({ value, filter }) => this.acctgTransEntryService.countIndicatorMovements(this.accountTypeEnumId(), this.inputEnumId(), this.detectOrgUnitIdFlag(), this.tableLazyLoadEvent, this.isReservedAccount(), filter)
        ),
        tap(total => this.isEmpty.emit(total === 0))
      )
  );

  uomRatingScale$ = this.uomRatingScaleService.getAllUomRatingScale();
  uomRatingScale: SelectItem[] = [];

  uom: SelectItem[] = [];

  glFiscalType: SelectItem[] = [];

  partyRole: SelectItem<Partial<PartyRole>>[] = [];

  selectedDate: Date;

  uomRatingScaleAmount = signal<UomRatingScale[]>(null);

  ngOnInit(): void {

    this.uomRatingScaleService.getAllUomRatingScale()
      .pipe(
        takeUntilDestroyed(this.destroy),
        tap(x => this.uomRatingScaleAmount.set(x)),
        map(x => dropdownUomRatingScale(x, this.secondaryLang())),
      )
      .subscribe(values => this.uomRatingScale = values);

    this.glFiscalTypeService.getGlFiscalTypeByAccountTypeEnumId(this.accountTypeEnumId())
      .pipe(
        takeUntilDestroyed(this.destroy),
        map(x => dropdownGlFiscalType(x, this.secondaryLang()))
      )
      .subscribe(values => this.glFiscalType = values);

    if (this.isByOrganizationalUnit()) {

      this.partyRoleService.getPartyRoleExRoleType()
        .pipe(
          takeUntilDestroyed(this.destroy),
          map(x => dropdownPartyRoleEx(x, this.secondaryLang()))
        )
        .subscribe(values => this.partyRole = values);
    }

    this.buttonService.clickReload()
      .pipe(
        takeUntilDestroyed(this.destroy)
      )
      .subscribe(() => {
        this.reset();
      })
  }

  loadProducts(event: TableLazyLoadEvent) {
    this.tableLazyLoadEvent = event;
    this.loading.set(true);
    this.updatedAcctgTransEntry().length > 0 ? this.save() : this._reload.next(event);
  }

  julianDate(days: number): Date {
    return addDaysFromGiulianDate(days);
  }

  checkDisableDelete() {
    this.disabledDelete.set(!(this.selectedItems.length > 0));
  }

  changePartyRole(value: Partial<PartyRole>, itemChanged: AcctgTransEntry) {
    itemChanged.acctgTrans.partyId = value.partyId;
    itemChanged.acctgTrans.roleTypeId = value.roleTypeId;
  }

  changeValue(itemChanged: AcctgTransEntry) {
    const findItemChanged = this.updatedAcctgTransEntry().find(x => x.acctgTransId === itemChanged.acctgTransId && x.acctgTransEntrySeqId === itemChanged.acctgTransEntrySeqId)
    if (!findItemChanged) {
      this.updatedAcctgTransEntry.update(x => [...x, itemChanged]);
    }
    else {
      this.updatedAcctgTransEntry.update(y =>
        y.map(x => {
          if (x.acctgTransId === itemChanged.acctgTransId && x.acctgTransEntrySeqId === itemChanged.acctgTransEntrySeqId)
            return itemChanged;
          else
            return x;
        }));
    }
  }

  getGlAccountDescription(glAccount: GlAccount): string {
    const name = this.secondaryLang() ? glAccount.accountNameLang : glAccount.accountName;
    return `${glAccount.accountCode ?? ""} - ${name ?? ""}`;
  }

  getWorkEffortMeasureDescription(workEffortMeasure: WorkEffortMeasure): string {
    const name = this.secondaryLang() ? workEffortMeasure.workEffort.workEffortNameLang : workEffortMeasure.workEffort.workEffortName;
    return `${workEffortMeasure.workEffort.etch ?? ""} - ${name ?? ""}`;
  }

  getWorkEffortMeasureInfoDescription(workEffortMeasure: WorkEffortMeasure) {
    const { uomDescr, uomDescrLang, workEffort } = workEffortMeasure;
    const { workEffortParent } = workEffort;
    const { etch: etchParent, workEffortName: workEffortNameParent, workEffortNameLang: workEffortNameLangParent } = workEffortParent

    return `${(this.secondaryLang() ? uomDescrLang : uomDescr) ?? ''}
    ${etchParent ?? ''} - ${(this.secondaryLang() ? workEffortNameLangParent : workEffortNameParent) ?? ''}
    `
  }

  getUomDescription(uom: Uom): string {
    return (this.secondaryLang() ? uom.descriptionLang : uom.description) ?? '';
  }

  getPartyRoleDescription(partyId: string, roleTypeId: string): string {
    return this.partyRole.find(x => x.value.partyId === partyId && x.value.roleTypeId === roleTypeId)?.label;
  }

  getGlFiscalTypeDescription(glFiscalTypeId: string): string {
    return this.glFiscalType.find(x => x.value === glFiscalTypeId)?.label;
  }

  getUomRatingScaleByUomId(uomId: string): SelectItem[] {
    return this.uomRatingScale.filter(x => x.value.uomId === uomId).map(x => { return { label: x.label, value: x.value.uomRatingValue } })
  }

  getUomRatingScaleDescription(uomId: string, uomRatingValue: number) {
    return this.uomRatingScale.find(x => x.value.uomId === uomId && x.value.uomRatingValue === uomRatingValue)?.label
  }

  dropdownWorkEffortMeasureJoinWorkEffort(types: WorkEffortMeasure[]): SelectItem[] {
    if (types) {
      const selItem = types.map((x: WorkEffortMeasure) => { return { label: ((!this.secondaryLang()) ? x.workEffort.workEffortName : x.workEffort.workEffortName), value: x.workEffortMeasureId } });
      return orderBy(selItem, ['label'], ['asc']);
    }
    return []
  }

  changeAmount(date: Date, item: AcctgTransEntry) {
    item.amount = fromDateGetDaysFromGiulianDate(date);
    this.changeValue(item);
  }


  save() {
    this.loadingSave.set(true);
    this.acctgTransEntryService.updateAcctgTransEntry(this.updatedAcctgTransEntry())
      .subscribe({
        next: () => {
          this.updatedAcctgTransEntry.set([]);
          this.loadingSave.set(false);
          this.msgService.successUpdate();
          this._reload.next(this.tableLazyLoadEvent);
        },
        error: (err) => {
          this.loadingSave.set(false);
          this.msgService.error(err.error.message ?? err.message, this.stateKey())
        }
      })
  }

  reset() {
    this.tableMovement.editingCell = null;
    this.loading.set(true);
    this.updatedAcctgTransEntry.set([]);
    this._reload.next(this.tableLazyLoadEvent);

  }

  delete() {
    this.confirmDialogService.deleteMultiElement(this.stateKey()).then(
      x => {
        if (x) {
          this.loadingDelete.set(true);
          this.acctgTransEntryService.deleteAcctgTransEntries(this.selectedItems.map(values => {
            const { acctgTransId, acctgTransEntrySeqId } = values
            return { acctgTransId, acctgTransEntrySeqId }
          }))
            .subscribe({
              next: (result) => {
                if (result) {
                  this.msgService.successDelete();
                  this.selectedItems = [];

                  let tableState: TableState = JSON.parse(sessionStorage.getItem(this.stateKey()));
                  tableState.selection = [];
                  sessionStorage.setItem(this.stateKey(), JSON.stringify(tableState))
                  this.disabledDelete.set(true);
                  this.loadingDelete.set(false);
                  this._reload.next(this.tableLazyLoadEvent);

                }
              },
              error: (err) => {
                this.loadingDelete.set(false);
                this.msgService.error(err.error.message ?? err.message, this.stateKey())
              }
            });
        }
      }
    )

  }

  doNothing(e: any) {
    return doNothing(e);
  }
}
