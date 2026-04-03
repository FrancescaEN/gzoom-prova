import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommonsModule } from '../../commons/commons.module';
import { I18nModule } from 'app/i18n/i18n.module';
import { GzoomConfirmDialogComponent } from '../../layout/gzoom/gzoom-confirm-dialog/gzoom-confirm-dialog.component';
import { GzoomDropdownComponent } from '../../layout/gzoom/gzoom-form/gzoom-dropdown/gzoom-dropdown.component';
import { GzoomToasterComponent } from '../../layout/gzoom/gzoom-toaster/gzoom-toaster.component';
import { ReportPopupComponent } from '../../shared/report-popup/report-popup.component';
import { LegacyModule } from '../legacy/legacy.module';
import { GzoomPortalComponent } from './gzoom-portal.component';
import { GzoomPortalRoutingModule } from './gzoom-portal-routing.module';
import { AccordionModule } from 'primeng/accordion';
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';
import { GzoomTooltipComponent } from '../../layout/gzoom/gzoom-tooltip/gzoom-tooltip.component';
import { BadgeModule } from 'primeng/badge';
import { GzoomButtonComponent } from '../../layout/gzoom/gzoom-button/gzoom-button.component';
import { SharedModule } from '../../shared/shared.module';
import { PortalHeaderComponent } from './portal-header/portal-header.component';

@NgModule({
  imports: [
    CommonModule,
    CommonsModule,
    I18nModule,
    GzoomPortalRoutingModule,
    AccordionModule,
    TableModule,
    CardModule,
    DividerModule,
    ButtonModule,
    TooltipModule,
    GzoomTooltipComponent,
    BadgeModule,
    GzoomButtonComponent,
    SharedModule,
    LegacyModule,
    ReportPopupComponent,
    GzoomDropdownComponent,
    GzoomConfirmDialogComponent,
    GzoomToasterComponent,
    PortalHeaderComponent
  ],
  declarations: [
    GzoomPortalComponent
  ],
  exports: [
    SharedModule
  ],
  providers: []
})
export class GzoomPortalModule {
}
