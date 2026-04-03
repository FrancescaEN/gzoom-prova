import { Component, DestroyRef, computed, inject, input, model, signal } from '@angular/core';
import { takeUntilDestroyed, toObservable, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { LayoutModule } from 'app/layout/layout.module';
import { DetailChartComponent } from 'app/shared/components/detail-chart/detail-chart.component';
import { omitBy, orderBy } from 'lodash';
import { catchError, map, of, switchMap, tap } from 'rxjs';
import TableDetailPreventionTrendComponent from './components/table-detail-prevention-trend/table-detail-prevention-trend.component';
import { ChartDetailPreventionTrendComponent } from './components/chart-detail-prevention-trend/chart-detail-prevention-trend.component';
import { REFURBISHED_PAGES } from 'app/shared/menu.service';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { Organization } from 'app/commons/model/organization-unit/Organization';
import { GzoomLabelComponent } from '../../../../../../layout/form/gzoom-label/gzoom-label.component';
import { OrganizationUnitFilter, OrganizationUnitService } from 'app/api/service/goal-file/organization-unit.service';
import { Context } from 'app/commons/enum/context';
import { DataStorageService } from 'app/commons/service/data-storage.service';

@Component({
  selector: 'gzoom-detail-prevention-trend',
  standalone: true,
  imports: [DetailChartComponent, ChartDetailPreventionTrendComponent, LayoutModule, TableDetailPreventionTrendComponent, GzoomLabelComponent],
  templateUrl: './detail-prevention-trend.component.html'
})
export default class DetailPreventionTrendComponent {
  private destroy = inject(DestroyRef);
  i18n = inject(I18NService);
  router = inject(Router);
  activatedRoute = inject(ActivatedRoute);
  riskService = inject(RiskService);
  menu = REFURBISHED_PAGES;

  date: Date = new Date();
  maxDate = new Date();
  defaultFromYear = this.date.getFullYear() - 3;
  fromDate = new Date();
  secondaryLang = this.i18n.getIsSecondaryLang();
  selectedArea = model<string>();
  selectedOrgUnitId = model<string>();

  areaId = input.required<string>();
  year = input(this.date.getFullYear(), { transform: (value: number) => value ?? this.date.getFullYear() });
  fromYear = input(this.defaultFromYear, { transform: (value: number) => value ?? this.defaultFromYear });
  orgUnitId = input<string>();

  organizationUnits: Organization[] = [];

  private organizationUnitFilter: OrganizationUnitFilter = {
    context: Context.CTX_CO,
    secondaryLang: false
  }

  aree = toSignal(
    toObservable(this.orgUnitId).pipe(
      switchMap(orgUnitId =>
        this.riskService.getAreaMis(orgUnitId)
          .pipe(
            catchError(() => of([])),
            map(aree => (aree || []).map(x => {
              return {
                label: this.secondaryLang ? x.areaNameLang : x.areaName,
                value: x.areaId
              }
            })),
            map(aree => orderBy(aree, 'label'))
          )
      )
    ),
    {
      initialValue: [] as { label: string; value: string }[]
    }
  );

  chartTitle = computed<string>(() => this.aree()?.find(x => x.value === this.areaId())?.label);

  constructor(private readonly organizationUnitService: OrganizationUnitService,
              private readonly dataStorageService: DataStorageService) { }

  ngOnInit(): void {
    this.selectedArea.set(this.areaId());

    if (this.year()) {
      this.date.setFullYear(this.year());
    }


    if (this.fromYear()) {
      this.fromDate.setFullYear(this.fromYear());
    }

    this.organizationUnitFilter.year = this.year();
    this.organizationUnitFilter.fromYear = this.fromYear();
    this.organizationUnitFilter.secondaryLang = this.secondaryLang;
    this.organizationUnitService.getOrganizationUnits(this.organizationUnitFilter).subscribe(data => {
      this.organizationUnits = data;

      this.selectedOrgUnitId.set(this.orgUnitId());
    });
  }

  setYear(valueDate: Date) {
    const queryParams: any = {
      fromYear: this.fromYear()
    }
    if (valueDate) {
      this.dataStorageService.setSessionData(this.menu.GP_MENU_00626[0], 'year', JSON.stringify(valueDate));
      queryParams.year = valueDate.getFullYear();
      if (this.orgUnitId() !== null && this.orgUnitId() !== undefined && this.orgUnitId() !== '') {
        queryParams.orgUnitId = this.orgUnitId();
      }
    } else {
      this.dataStorageService.removeSessionData(this.menu.GP_MENU_00626[0], 'year');
    }
    this.router.navigate([], { queryParams: omitBy(queryParams, isNaN), relativeTo: this.activatedRoute });
  }

  setFromYear(valueDate: Date) {
    const queryParams: any = {
      year: this.year()
    }
    if (valueDate) {
      this.dataStorageService.setSessionData(this.menu.GP_MENU_00626[0], 'fromYear', JSON.stringify(valueDate));
      queryParams.fromYear = valueDate.getFullYear();
      if (this.orgUnitId() !== null && this.orgUnitId() !== undefined && this.orgUnitId() !== '') {
        queryParams.orgUnitId = this.orgUnitId();
      }
    } else {
      this.dataStorageService.removeSessionData(this.menu.GP_MENU_00626[0], 'fromYear');
    }
    this.router.navigate([], { queryParams: omitBy(queryParams, isNaN), relativeTo: this.activatedRoute });
  }

  setAreaId(areaId: string) {
    if (areaId) {
      const queryParams: any = {
        year: this.year(), fromYear: this.fromYear()
      }
      if (this.orgUnitId() !== null && this.orgUnitId() !== undefined && this.orgUnitId() !== '') {
        queryParams.orgUnitId = this.orgUnitId();
      }
      this.router.navigate([`../${areaId}`], {
        queryParams: omitBy(queryParams, isNaN),
        relativeTo: this.activatedRoute
      });
    }
  }

  back() {
    this.router.navigate([`/c/${this.menu.GP_MENU_00626}`]);
  }
}
