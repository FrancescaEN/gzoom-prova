import { TableModule } from 'primeng/table';
import { TreeTableModule } from 'primeng/treetable';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AccordionModule } from 'primeng/accordion';     //accordion and accordion tab
import { DropdownModule } from 'primeng/dropdown';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { ConfirmationService, MessageService, SharedModule } from 'primeng/api';
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
import { CanDeactivateGuard, canDeactivateGuard, canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { MessagesModule } from 'primeng/messages';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { QueryConfigService } from 'app/api/service/query-config.service';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { WorkEffortTypeService } from 'app/api/service/work-effort-type.service';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { SchedulerComponent } from './scheduler.component';
import { PlannerService } from 'app/api/service/scheduler/planner.service';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { CheckboxModule } from 'primeng/checkbox';
import { ChipsModule } from 'primeng/chips';
import { InputNumberModule } from 'primeng/inputnumber';
import { MsgService } from 'app/commons/service/message.service';
import { RouterModule, Routes } from '@angular/router';
import { qrtzJobDetailsResolver } from 'app/api/resolver/qrtz-job-details-resolver.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { GzoomLabelComponent } from '../../../layout/form/gzoom-label/gzoom-label.component';

const routes: Routes = [
  {
    path: '',
    component: SchedulerComponent,
    resolve: { obss: qrtzJobDetailsResolver },
    canDeactivate: [canDeactivateGuard]
  }
]
@NgModule({
  imports: [
    RouterModule.forChild(routes),
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
    FormsModule,
    InputTextModule,
    CalendarModule,
    CheckboxModule,
    ChipsModule,
    InputNumberModule,
    GzoomLabelComponent
  ],
  declarations: [
    SchedulerComponent
  ],
  providers: [
    ConfirmDialogService,
    ConfirmationService,
    // MessageService,
    CanDeactivateGuard,
    QueryConfigService,
    EnumerationService,
    DataStorageService,
    WorkEffortTypeService,
    MsgService,
    PlannerService
  ]
})
export default class SchedulerModule {}
