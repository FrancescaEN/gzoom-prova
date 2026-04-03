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
import { QueryConfigurationComponent } from './query-configuration.component';
import { QueryConfigurationRoutingModule } from './query-configuration-routing.module';
import { QueryConfigService } from 'app/api/service/query-config.service';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { WorkEffortTypeService } from 'app/api/service/work-effort-type.service';
import { DetailQueryComponent } from './detail-query/detail-query.component';
import { DetailConditionsComponent } from './detail-conditions/detail-conditions.component';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { MsgService } from 'app/commons/service/message.service';
import { OverDetailsQueryConfigurationComponent } from './over-details-query-configuration/over-details-query-configuration.component';

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
    QueryConfigurationRoutingModule,
    InputTextareaModule
  ],
  declarations: [
    QueryConfigurationComponent,
    DetailQueryComponent,
    DetailConditionsComponent,
    OverDetailsQueryConfigurationComponent
  ],
  providers: [
    ConfirmationService,
    // MessageService,
    CanDeactivateGuard,
    QueryConfigService,
    EnumerationService,
    DataStorageService,
    WorkEffortTypeService,
    MsgService
  ]
})
export default class QueryConfigurationModule {}
