import { Component, DestroyRef, computed, inject, input, model, numberAttribute } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { LayoutModule } from 'app/layout/layout.module';
import { DetailChartComponent } from 'app/shared/components/detail-chart/detail-chart.component';
import { omitBy } from 'lodash';
import { catchError, map, mergeMap, of, reduce, switchMap } from 'rxjs';
import { ChartDetailStateRiskFactorsComponent } from './components/chart-detail-state-risk-factors/chart-detail-state-risk-factors.component';
import { TableDetailStateRiskFactorsComponent } from './components/table-detail-state-risk-factors/table-detail-state-risk-factors.component';
import { SelectItem } from 'primeng/api';
import { REFURBISHED_PAGES } from 'app/shared/menu.service';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { takeUntilDestroyed, toObservable, toSignal } from '@angular/core/rxjs-interop';
import { Organization } from 'app/commons/model/organization-unit/Organization';
import { GzoomLabelComponent } from '../../../../../../layout/form/gzoom-label/gzoom-label.component';
import { OrganizationUnitFilter, OrganizationUnitService } from 'app/api/service/goal-file/organization-unit.service';
import { Context } from 'app/commons/enum/context';
import { DataStorageService } from 'app/commons/service/data-storage.service';

@Component({
  selector: 'gzoom-detail-state-risk-factors',
  standalone: true,
  imports: [DetailChartComponent, ChartDetailStateRiskFactorsComponent, LayoutModule, TableDetailStateRiskFactorsComponent, GzoomLabelComponent],
  templateUrl: './detail-state-risk-factors.component.html'
})
export default class DetailStateRiskFactorsComponent {
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
  selectedRisk = model<number>();
  selectedOrgUnitId = model<string>();

  levelId = input.required({ transform: numberAttribute });
  year = input(this.date.getFullYear(), { transform: (value: number) => value ?? this.date.getFullYear() });
  fromYear = input(this.defaultFromYear, { transform: (value: number) => value ?? this.defaultFromYear });
  orgUnitId = input<string>();

  organizationUnits: Organization[] = [];

  private organizationUnitFilter: OrganizationUnitFilter = {
    context: Context.CTX_CO,
    secondaryLang: false
  }

  risks = toSignal(
    toObservable(this.orgUnitId).pipe(
      switchMap(orgUnitId =>
        this.riskService.getRiskLevels(orgUnitId)
          .pipe(
            mergeMap(levels => levels),
            map(level => (
              {
                label: this.secondaryLang ? level.levelNameLang : level.levelName,
                value: level.levelId
              }
            )),
            reduce((acc: SelectItem[], val) => [...acc, val], []),
            catchError(() => of([]))
          )
      )
    ),
    {
      initialValue: [] as { label: string; value: string }[]
    }
  );

  riskLabel = computed(() => this.risks()?.find(x => x.value === this.levelId())?.label)

  constructor(private readonly organizationUnitService: OrganizationUnitService,
              private readonly dataStorageService: DataStorageService
  ) { }

  ngOnInit(): void {
    this.selectedRisk.set(this.levelId());

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

  setRiskId(riskId: string) {
    if (riskId) {
      const queryParams: any = {
        year: this.year(), fromYear: this.fromYear()
      }
      if (this.orgUnitId() !== null && this.orgUnitId() !== undefined && this.orgUnitId() !== '') {
        queryParams.orgUnitId = this.orgUnitId();
      }
      this.router.navigate([`../${riskId}`], {
        queryParams: omitBy(queryParams, isNaN),
        relativeTo: this.activatedRoute
      });
    }
  }

  back() {
    this.router.navigate([`/c/${this.menu.GP_MENU_00626[0]}`]);
  }
}
