import { Component, OnInit, inject, input, model, numberAttribute } from '@angular/core';
import ChartDetailRiskStateComponent from './components/chart-detail-risk-state/chart-detail-risk-state.component';
import { ButtonBarTableComponent } from 'app/layout/button/button-bar-table/button-bar-table.component';
import { TableDetailRiskStateComponent } from './components/table-detail-risk-state/table-detail-risk-state.component';
import { CardModule } from 'primeng/card';
import { DropdownModule } from 'primeng/dropdown';
import { LayoutModule } from 'app/layout/layout.module';
import { ActivatedRoute, Router } from '@angular/router';
import { DetailChartComponent } from 'app/shared/components/detail-chart/detail-chart.component';
import { I18NService } from 'app/i18n/i18n.service';
import { REFURBISHED_PAGES } from 'app/shared/menu.service';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { Organization } from 'app/commons/model/organization-unit/Organization';
import { omitBy } from 'lodash';
import { GzoomLabelComponent } from '../../../../../../layout/form/gzoom-label/gzoom-label.component';
import { OrganizationUnitFilter, OrganizationUnitService } from 'app/api/service/goal-file/organization-unit.service';
import { Context } from 'app/commons/enum/context';
import { DataStorageService } from 'app/commons/service/data-storage.service';

@Component({
  selector: 'gzoom-detail-risk-state',
  standalone: true,
  imports: [
    ButtonBarTableComponent,
    ChartDetailRiskStateComponent,
    TableDetailRiskStateComponent,
    CardModule,
    DropdownModule,
    LayoutModule,
    DetailChartComponent,
    GzoomLabelComponent
  ],
  templateUrl: './detail-risk-state.component.html',
  styles: `
  p-card {
    width: 100%;
    height: 100%;
  }
  `
})
export default class DetailRiskStateComponent implements OnInit {
  /* Services */
  router = inject(Router);
  i18n = inject(I18NService);
  activatedRoute = inject(ActivatedRoute);
  riskService = inject(RiskService);

  /* Inputs */
  year = input.required({ transform: numberAttribute });
  orgUnitId = input<string>();
  selectedOrgUnitId = model<string>();

  organizationUnits: Organization[] = [];

  private organizationUnitFilter: OrganizationUnitFilter = {
    context: Context.CTX_CO,
    secondaryLang: false
  }

  /* Variables */
  secondaryLang = this.i18n.getIsSecondaryLang();
  menu = REFURBISHED_PAGES;
  date: Date = new Date();
  maxDate = new Date();

  constructor(private readonly organizationUnitService: OrganizationUnitService,
              private readonly dataStorageService: DataStorageService) { }


  ngOnInit(): void {
    this.date.setFullYear(this.year());

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

  /**
   * Updates the current route by navigating to a new URL that includes the year extracted from the provided date.
   * If a valid date is passed, it retrieves the year from the date and appends it to the current route.
   * The navigation is performed relative to the current activated route.
   *
   * @param {Date} valueDate - The date from which the year will be extracted for navigation. If the date is null or undefined, no action is taken.
   */
  setYear(valueDate: Date) {
    if (valueDate) {
      this.dataStorageService.setSessionData(this.menu.GP_MENU_00626[0], 'year', JSON.stringify(valueDate));
      const queryParams: any = {}
      if (this.orgUnitId() !== null && this.orgUnitId() !== undefined && this.orgUnitId() !== '') {
        queryParams.orgUnitId = this.orgUnitId();
      }
      this.router.navigate([`../${valueDate.getFullYear()}`], {
        queryParams: omitBy(queryParams, isNaN),
        relativeTo: this.activatedRoute
      });
    } else {
      this.dataStorageService.removeSessionData(this.menu.GP_MENU_00626[0], 'year');
    }
  }

  /**
   * Return to the dashboard page.
   */
  back() {
    this.router.navigate([`/c/${this.menu.GP_MENU_00626}`]);
  }
}
