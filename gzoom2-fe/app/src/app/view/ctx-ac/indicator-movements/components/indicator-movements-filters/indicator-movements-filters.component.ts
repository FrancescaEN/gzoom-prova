import { ChangeDetectionStrategy, Component, DestroyRef, OnInit, computed, inject, input, signal } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { CustomTimePeriodService } from 'app/api/service/custom-time-period.service';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { GlFiscalTypeService } from 'app/api/service/gl-fiscal-type.service';
import { UomService } from 'app/api/service/uom.service';
import { dropdownCustomTimePeriod, dropdownGlAccount, dropdownGlFiscalType, dropdownUom } from 'app/commons/utils/dropdownSelectItem';
import { I18NService } from 'app/i18n/i18n.service';
import { LayoutModule } from 'app/layout/layout.module';
import { filter, map, of, startWith, switchMap, tap } from 'rxjs';
import { Location } from '@angular/common';
import { isEmpty, isNull, omitBy } from 'lodash';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import {GzoomLabelComponent} from "../../../../../layout/form/gzoom-label/gzoom-label.component";

export interface IndicatorMovementsFilter {
  glAccountId?: string,
  uomId?: string,
  customTimePeriodId?: string,
  glFiscalTypeId?: string
}

@Component({
  selector: 'gzoom-indicator-movements-filters',
  standalone: true,
  imports: [
    LayoutModule,
    GzoomLabelComponent
  ],
  templateUrl: './indicator-movements-filters.component.html',
  styleUrl: './indicator-movements-filters.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export default class IndicatorMovementsFiltersComponent implements OnInit {
  //unsubscribe observable
  destroy = inject(DestroyRef);
  router = inject(Router);
  route = inject(ActivatedRoute);
  location = inject(Location);
  i18nService = inject(I18NService);
  formBuilder = inject(FormBuilder);
  glAccountService = inject(GlAccountService);
  uomService = inject(UomService);
  customTimePeriodService = inject(CustomTimePeriodService);
  glFiscalTypeService = inject(GlFiscalTypeService);
  dataStorageService = inject(DataStorageService);
  keyDataStorage = "indicator-movements-filters";
  loading = signal<boolean>(false);
  disableResetFilter = signal<boolean>(true);
  disabledResetButton = computed(() => {
    return this.loading() || this.disableResetFilter();
  })

  filterState$ = this.router.events.pipe(
    filter(event => event instanceof NavigationEnd),
    tap(() => this.loading.set(true)),
    switchMap(() => of(this.location.getState())),
    startWith(this.location.getState()),
    tap(() => this.loading.set(false))
  );

  //input path
  accountTypeEnumId = input.required<string>();
  isReservedAccount = input<string>();

  //secondaryLang
  secondaryLang = signal<boolean>(this.i18nService.getIsSecondaryLang());

  //formFilter
  filterForm = this.formBuilder.group({
    glAccountId: new FormControl<string>(null),
    uomId: new FormControl<string>(null),
    customTimePeriodId: new FormControl<string>(null),
    glFiscalTypeId: new FormControl<string>(null)
  });

  //Filtro indicatori
  glAccount$ = computed(() => {
    return this.glAccountService.getGlAccountByAccountTypeEnumIdAndIsReservedAccount(this.accountTypeEnumId(), this.isReservedAccount())
      .pipe(map(x => dropdownGlAccount(x, this.secondaryLang())))
  });

  //Filtro unità di misura
  uom$ = toSignal(this.uomService.getAllUom().pipe(map(x => dropdownUom(x, this.secondaryLang()))));

  //Filtro periodo di riferimento
  customTimePeriod$ = toSignal(this.customTimePeriodService.getCustomTimePeriod().pipe(map(x => dropdownCustomTimePeriod(x, this.secondaryLang()))))

  //Filtro Tipo rilevazione
  glFiscalType$ = computed(() => {
    return this.glFiscalTypeService.getGlFiscalTypeByAccountTypeEnumId(this.accountTypeEnumId()).pipe(map(x => dropdownGlFiscalType(x, this.secondaryLang())))
  });


  gpMenu = toSignal<string>(this.route.data.pipe(map(x => x.gpMenu)));
  context = toSignal<string>(this.route.data.pipe(map(x => x.context)));
  labelFilterSearchGlAccount = computed(() => this.gpMenu() && this.context() ? `filterSearchGlAccount.${this.context()}.${this.gpMenu()}` : null);


  ngOnInit(): void {

    this.filterState$
      .pipe(
        takeUntilDestroyed(this.destroy)
      )
      .subscribe(
        (state: { [key: string]: string | string[] | number }) => {
          let { navigationId, ...filters } = state;
          this.filterForm.patchValue({
            glAccountId: filters.glAccountId as string ?? null,
            uomId: filters.uomId as string ?? null,
            customTimePeriodId: filters.customTimePeriodId as string ?? null,
            glFiscalTypeId: filters.glFiscalTypeId as string ?? null
          });
          this.filterForm.markAsPristine();

          const { isSecondaryLang, ...otherParams } = filters
          this.disableResetFilter.set(isEmpty(omitBy(otherParams, isNull)));
        }

      )
  }

  resetFilter() {
    this.filterForm.reset();
    this.filter();
  }

  filter() {
    this.router.navigate(
      ['.'], {
      relativeTo: this.route,
      state: { ...this.filterForm.value, isSecondaryLang: this.secondaryLang() },
    }
    );
  }
}
