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
import { SharedModule, ConfirmationService, MessageService } from 'primeng/api';
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
import { DataResourceService } from 'app/api/service/data-resource.service';
import { ApprovalPathsComponent } from './approval-paths.component';
import { ApprovalPathsRoutingModule } from './approval-paths-routing.module';
import { StatusTypeService } from 'app/api/service/status-type.service';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { LanguageService } from 'app/api/service/language.service';
import { PredictedStatesComponent } from './predicted-states/predicted-states.component';
import { StatusItemService } from 'app/api/service/status-item.service';
import { StatusValidChangeService } from 'app/api/service/status-valid-change.service';
import { PassesAllowedComponent } from './passes-allowed/passes-allowed.component';
import { MsgService } from 'app/commons/service/message.service';
import { OverDetailsApprovalPathsComponent } from './over-details-approval-paths/over-details-approval-paths.component';
import { DataStorageService } from 'app/commons/service/data-storage.service';

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
    ApprovalPathsRoutingModule
  ],
  declarations: [
    ApprovalPathsComponent,
    PredictedStatesComponent,
    PassesAllowedComponent,
    OverDetailsApprovalPathsComponent
  ],
  providers: [
    ConfirmationService,
    // MessageService,
    CanDeactivateGuard,
    DataResourceService,
    StatusTypeService,
    EnumerationService,
    LanguageService,
    StatusItemService,
    StatusValidChangeService,
    MsgService,
    DataStorageService

  ]
})
export default class ApprovalPathsModule {}
