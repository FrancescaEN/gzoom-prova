import { TableModule } from 'primeng/table';
import { TreeTableModule } from 'primeng/treetable';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroupDirective, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AccordionModule } from 'primeng/accordion';     //accordion and accordion tab
import { DropdownModule } from 'primeng/dropdown';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { SharedModule, ConfirmationService, MessageService, FilterService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { SpinnerModule } from 'primeng/spinner';
import { ToastModule } from 'primeng/toast';
import { TooltipModule } from 'primeng/tooltip';
import { CommonsModule } from '../../../commons/commons.module';
import { ApiModule } from '../../../api/api.module';
import { LayoutModule } from '../../../layout/layout.module';
import { I18nModule } from 'app/i18n/i18n.module';
import { ToolbarModule } from 'primeng/toolbar';
import { TreeSelectModule } from 'primeng/treeselect';
import { SpeedDialModule } from 'primeng/speeddial';
import { SplitButtonModule } from 'primeng/splitbutton';
import { SlideMenuModule } from 'primeng/slidemenu';
import { RippleModule } from 'primeng/ripple';
import { MenuModule } from 'primeng/menu';
import { CanDeactivateGuard } from 'app/shared/can-deactivate.guard';
import { MessagesModule } from 'primeng/messages';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { WorkEffortMeasureService } from 'app/api/service/work-effort-measure.service';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { MeasuresObjectivesComponent } from './measures-objectives.component';
import { WorkEffortViewService } from 'app/api/service/work-effort-view.service';
import { GlAccountWithWorkEffortPurposeTypeViewService } from 'app/api/service/gl-account-with-work-effort-purpose-type-view.service';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { PeriodTypeService } from 'app/api/service/period-type.service';
import { MeasuresObjectivesRatingScaleComponent } from './measures-objectives-rating-scale/measures-objectives-rating-scale.component';
import { MeasuresObjectivesRoutingModule } from './measures-objectives-routing.module';
import { WorkEffortMeasRatScService } from 'app/api/service/work-effort-meas-rat-sc.service';
import { WorkEffortMeasureRatScViewService } from 'app/api/service/work-effort-measures-rat-sc-view.service';
import { UomRangeService } from 'app/api/service/uom-range.service';
import { UomRatingScaleService } from 'app/api/service/uom-rating-scale.service';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { MeasuresObjectivesDetailComponent } from './measures-objectives-detail/measures-objectives-detail.component';
import { WorkEffortService } from 'app/api/service/work-effort.service';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { CalendarModule } from 'primeng/calendar';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { MsgService } from 'app/commons/service/message.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { OverDetailsMeasuresObjectivesComponent } from './over-details-measures-objectives/over-details-measures-objectives.component';
import { UomService } from 'app/api/service/uom.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
    CommonsModule,
    ApiModule,
    LayoutModule,
    AccordionModule,
    TreeTableModule,
    TableModule,
    DialogModule,
    SharedModule,
    ButtonModule,
    ConfirmDialogModule,
    SpinnerModule,
    ToastModule,
    DropdownModule,
    TooltipModule,
    I18nModule,
    ToolbarModule,
    TreeSelectModule,
    SpeedDialModule,
    SplitButtonModule,
    SlideMenuModule,
    RippleModule,
    MenuModule,
    CardModule,
    MessagesModule,
    ProgressSpinnerModule,
    MeasuresObjectivesRoutingModule,
    CalendarModule,
    InputNumberModule,
    InputTextareaModule

  ],
  declarations: [
    MeasuresObjectivesComponent,
    MeasuresObjectivesRatingScaleComponent,
    MeasuresObjectivesDetailComponent,
    OverDetailsMeasuresObjectivesComponent
  ],
  providers: [
    ConfirmationService,
    // MessageService,
    CanDeactivateGuard,
    WorkEffortMeasureService,
    UserPreferenceService,
    WorkEffortViewService,
    GlAccountWithWorkEffortPurposeTypeViewService,
    TableEditingCellService,
    PeriodTypeService,
    WorkEffortMeasRatScService,
    WorkEffortMeasureRatScViewService,
    UomRangeService,
    UomRatingScaleService,
    DataStorageService,
    WorkEffortService,
    GlAccountService,
    MsgService,
    ConfirmDialogService,
    UomService,
    FilterService,
    FormGroupDirective
  ]
})
export default class MeasuresObjectivesModule {}
