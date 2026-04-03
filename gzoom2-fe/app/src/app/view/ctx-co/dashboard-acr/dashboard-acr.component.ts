import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { LayoutModule } from 'app/layout/layout.module';
import { CardModule } from 'primeng/card';
import { ChartMeasurementTypesStatusComponent } from './charts/chart-measurement-types-status/chart-measurement-types-status.component';
import ChartPreventionTrendComponent from './charts/chart-prevention-trend/chart-prevention-trend.component';
import RiskStateComponent from './charts/chart-risk-state/chart-risk-state.component';
import ChartRiskTrendComponent from './charts/chart-risk-trend/chart-risk-trend.component';
import { ChartStateRiskFactorsComponent } from './charts/chart-state-risk-factors/chart-state-risk-factors.component';
import ChartPreventionStateComponent from './charts/chart-prevention-state/chart-prevention-state.component';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { REFURBISHED_PAGES } from 'app/shared/menu.service';
import { OrganizationUnitFilter, OrganizationUnitService } from '../../../api/service/goal-file/organization-unit.service';
import { Context } from '../../../commons/enum/context';
import { Organization } from '../../../commons/model/organization-unit/Organization';
import { I18NService } from 'app/i18n/i18n.service';
import { GzoomLabelComponent } from '../../../layout/gzoom/gzoom-label/gzoom-label.component';


@Component({
  selector: 'gzoom-dashboard-acr',
  standalone: true,
  imports: [
    RiskStateComponent,
    ChartRiskTrendComponent,
    CardModule,
    ChartPreventionTrendComponent,
    ChartStateRiskFactorsComponent,
    ChartMeasurementTypesStatusComponent,
    ChartPreventionStateComponent,
    LayoutModule,
    GzoomLabelComponent
  ],
  templateUrl: './dashboard-acr.component.html',
  styleUrl: './dashboard-acr.component.scss',
  encapsulation: ViewEncapsulation.None
})
export default class DashboardAcrComponent implements OnInit {
  urlMenu = REFURBISHED_PAGES.GP_MENU_00626[0];
  date: Date = new Date();
  maxDate = new Date();
  defaultFromYear = this.date.getFullYear() - 3;
  fromDate = new Date();
  year = this.date.getFullYear();
  fromYear = this.defaultFromYear;
  selectedOrgUnitId: string;

  private readonly organizationUnitFilter: OrganizationUnitFilter = {
    context: Context.CTX_CO,
    secondaryLang: false
  };

  organizationUnits: Organization[] = [];

  secondaryLang = this.i18n.getIsSecondaryLang();

  constructor(private readonly organizationUnitService: OrganizationUnitService,
              private readonly dataStorageService: DataStorageService,
              private readonly i18n: I18NService
  ) { }

  ngOnInit(): void {
    this.fromDate.setFullYear(JSON.parse(this.dataStorageService.getSessionData(this.urlMenu, 'fromYear')) ?? this.defaultFromYear);
    this.fromYear = this.fromDate.getFullYear()
    this.date.setFullYear(JSON.parse(this.dataStorageService.getSessionData(this.urlMenu, 'year')) ?? this.date.getFullYear());
    this.year = this.date.getFullYear()

    this.organizationUnitFilter.fromYear = this.fromYear;
    this.organizationUnitFilter.year = this.year;
    this.organizationUnitFilter.secondaryLang = this.secondaryLang;
    this.organizationUnitService.getOrganizationUnits(this.organizationUnitFilter).subscribe(data => {
      //console.log('organization units:') // todo remove
      //console.log(data.length) // todo remove
      this.organizationUnits = data;

      this.selectedOrgUnitId = JSON.parse(this.dataStorageService.getSessionData(this.urlMenu, 'orgUnitId'));
      console.log('this.selectedOrgUnitId=' + JSON.parse(this.selectedOrgUnitId)) // todo remove
    });
  }

  setFromYear(valueDate: Date) {
    this.fromYear = valueDate.getFullYear();
    this.dataStorageService.setSessionData(this.urlMenu, 'fromYear', JSON.stringify(this.fromYear))
  }

  setYear(valueDate: Date) {
    this.year = valueDate.getFullYear();
    this.dataStorageService.setSessionData(this.urlMenu, 'year', JSON.stringify(this.year))
  }

  setOrganizationUnit(id: string) {
    this.selectedOrgUnitId = id;
    this.dataStorageService.setSessionData(this.urlMenu, 'orgUnitId', JSON.stringify(id))
  }
}
