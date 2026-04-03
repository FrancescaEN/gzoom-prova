import { ChangeDetectionStrategy, Component, DestroyRef, computed, inject, input, model, numberAttribute, signal } from '@angular/core';
import { ChartDetailMeasurementTypesStatusComponent } from './components/chart-detail-measurement-types-status/chart-detail-measurement-types-status.component';
import { TableDetailMeasurementTypesStatusComponent } from './components/table-detail-measurement-types-status/table-detail-measurement-types-status.component';
import { LayoutModule } from 'app/layout/layout.module';
import { DetailChartComponent } from 'app/shared/components/detail-chart/detail-chart.component';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { omitBy } from 'lodash';
import { SelectItem } from 'primeng/api';
import { map, mergeMap, reduce, switchMap } from 'rxjs';
import { REFURBISHED_PAGES } from 'app/shared/menu.service';
import { takeUntilDestroyed, toObservable, toSignal } from '@angular/core/rxjs-interop';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { GzoomLabelComponent } from '../../../../../../layout/form/gzoom-label/gzoom-label.component';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { Organization } from 'app/commons/model/organization-unit/Organization';
import { Context } from 'app/commons/enum/context';
import { OrganizationUnitFilter, OrganizationUnitService } from 'app/api/service/goal-file/organization-unit.service';

@Component({
  selector: 'gzoom-detail-measurement-types-status',
  standalone: true,
  imports: [DetailChartComponent, ChartDetailMeasurementTypesStatusComponent, LayoutModule, TableDetailMeasurementTypesStatusComponent, GzoomLabelComponent],
  templateUrl: './detail-measurement-types-status.component.html',
  styles: ``,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export default class DetailMeasurementTypesStatusComponent {
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
            reduce((acc, val) => [...acc, val], [])
          )
      )
    ),
    {
      initialValue: [] as { label: string; value: string }[]
    }
  );

  riskLabel = computed<string>(() => {
    if (this.risks()) {
      return this.risks().find(x => x.value === this.levelId())?.label
    }
  });

  constructor(private readonly organizationUnitService: OrganizationUnitService,
              private readonly dataStorageService: DataStorageService) { }

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
      console.log('organization units:') // todo remove
      console.log(data.length) // todo remove
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
    this.router.navigate([`/c/${this.menu.GP_MENU_00626}`]);
  }
}
