import {ModuleWithProviders, NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';

import { CommonsModule } from '../commons/commons.module';
import { ApiModule } from '../api/api.module';

import { ReportPopupService } from './report-popup/report-popup.service';
import { MenuService } from './menu.service';
import { NodeService } from './node.service';
import { LoaderService } from './loader/loader.service';
import { SafeResPipe } from './safe-res.pipe';
import { DownloadActivityService } from './report-download/download-activity.service';
import { ChangePasswordService } from './change-password/change-password.service';
import { CustomTimePeriodService } from 'app/api/service/custom-time-period.service';
import { ReportService } from 'app/api/service/report.service';
import { QueryConfigService } from 'app/api/service/query-config.service';
import { InputTextModule } from 'primeng/inputtext';
import { UserPermissionService } from './user-permission.service';
import {FormatNumberITPipe} from "../commons/pipe/formatNumberIT.pipe";

@NgModule({
  imports: [
    CommonModule,
    CommonsModule,
    ApiModule,
    InputTextModule
  ],
  declarations: [
    FormatNumberITPipe,
    SafeResPipe
  ],
  exports: [
    FormatNumberITPipe,
    SafeResPipe
  ]
})
export class SharedModule {

  /**
   * Declares providers for child inclusion.
   *
   * @return {ModuleWithProviders} The module with the providers
   */
  static forRoot(): ModuleWithProviders<SharedModule> {
    return {
      ngModule: SharedModule,
      providers: [
        MenuService,
        NodeService,
        LoaderService,
        ReportPopupService,
        ReportService,
        DownloadActivityService,
        ChangePasswordService,
        CustomTimePeriodService,
        QueryConfigService,
        UserPermissionService
      ]
    };
  }
}
