import { Component, DestroyRef, effect, inject, input, model, signal } from '@angular/core';
import { LayoutModule } from 'app/layout/layout.module';
import { DetailChartComponent } from 'app/shared/components/detail-chart/detail-chart.component';
import { I18NService } from 'app/i18n/i18n.service';
import { ActivatedRoute, Router } from '@angular/router';
import TableDetailPreventionStateComponent from './components/table-detail-prevention-state/table-detail-prevention-state.component';
import ChartDetailPreventionStateComponent from './components/chart-detail-prevention-state/chart-detail-prevention-state.component';
import { REFURBISHED_PAGES } from 'app/shared/menu.service';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { PeriodArea } from 'app/api/model/dashboards/period';
import { mergeMap } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { GzoomLabelComponent } from '../../../../../../layout/form/gzoom-label/gzoom-label.component';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { OrganizationUnitFilter, OrganizationUnitService } from 'app/api/service/goal-file/organization-unit.service';
import { Context } from 'app/commons/enum/context';
import { Organization } from 'app/commons/model/organization-unit/Organization';

@Component({
  selector: 'gzoom-detail-prevention-state',
  standalone: true,
  templateUrl: './detail-prevention-state.component.html',
  imports: [DetailChartComponent, LayoutModule, ChartDetailPreventionStateComponent, TableDetailPreventionStateComponent, GzoomLabelComponent]
})
export default class DetailPreventionStateComponent {
  private destroy = inject(DestroyRef);
  i18n = inject(I18NService);
  router = inject(Router);
  activatedRoute = inject(ActivatedRoute);
  riskService = inject(RiskService);
  secondaryLang = this.i18n.getIsSecondaryLang();
  date: Date = new Date();
  year = input(this.date.getFullYear(), { transform: (value: number) => value ?? this.date.getFullYear() });
  orgUnitId = input<string>();
  selectedOrgUnitId = model<string>();

  organizationUnits: Organization[] = [];

  private organizationUnitFilter: OrganizationUnitFilter = {
    context: Context.CTX_CO,
    secondaryLang: false
  }

  preventionStates = signal<PeriodArea>(null);
  maxDate = new Date();

  loading = true;
  loadingTable = true

  menu = REFURBISHED_PAGES;

  constructor(private readonly organizationUnitService: OrganizationUnitService,
              private readonly dataStorageService: DataStorageService) {
    effect(() => {
      if (this.year()) {
        //this.loading = true;
        this.loadingTable = true;
        this.riskService.getRiskPreventionStateByYear(this.year(), this.orgUnitId())
          .pipe(
            mergeMap(x => x),
            takeUntilDestroyed(this.destroy)
          )
          .subscribe({
            next: x => {
              this.loading = false;
              this.loadingTable = false;
              this.preventionStates.set(x)
            },
            error: e => {
              this.loadingTable = false;
              this.loading = false;
              console.error(e);
              this.preventionStates.set(null);
            }
          })
      }
    })
  }

  ngOnInit(): void {
    if (this.year()) {
      this.date.setFullYear(this.year());
    }

    this.organizationUnitFilter.year = this.year();
    this.organizationUnitFilter.fromYear = this.year();
    this.organizationUnitFilter.secondaryLang = this.secondaryLang;
    this.organizationUnitService.getOrganizationUnits(this.organizationUnitFilter).subscribe(data => {
      console.log('organization units:') // todo remove
      console.log(data.length) // todo remove
      this.organizationUnits = data;

      this.selectedOrgUnitId.set(this.orgUnitId());
    });
  }


  setYear(valueDate: Date) {
    if (valueDate) {
      this.dataStorageService.setSessionData(this.menu.GP_MENU_00626[0], 'year', JSON.stringify(valueDate));
      let year = valueDate.getFullYear()

      const queryParams: any = {}
      if (this.orgUnitId() !== null && this.orgUnitId() !== undefined && this.orgUnitId() !== '') {
        queryParams.orgUnitId = this.orgUnitId();
      }

      this.router.navigate([`../${year}`], { queryParams: queryParams, relativeTo: this.activatedRoute });
    } else {
      this.dataStorageService.removeSessionData(this.menu.GP_MENU_00626[0], 'year');
    }


  }

  back() {
    this.router.navigate([`/c/${this.menu.GP_MENU_00626}`]);
  }
}




