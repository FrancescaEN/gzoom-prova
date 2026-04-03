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
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { SecurityGroupsRoutingModule } from './security-groups-routing.module';
import { SecurityGroupsComponent } from './security-groups.component';
import { PermissionComponent } from './permission/permission.component';
import { FunctionsExcludedComponent } from './functions-excluded/functions-excluded.component';
import { EnabledUsersComponent } from './enabled-users/enabled-users.component';
import { SecurityGroupService } from 'app/api/service/security-group.service';
import { SecurityGroupPermissionService } from 'app/api/service/security-group-permission.service';
import { SecurityGroupContentService } from 'app/api/service/security-group-content.service';
import { UserLoginSecurityGroupService } from 'app/api/service/user-login-security-group.service';
import { PortalPageService } from 'app/api/service/portal-page.service';
import { SecurityPermissionService } from 'app/api/service/security-permission.service';
import { ContentService } from 'app/api/service/content.service';
import { UserLoginService } from 'app/api/service/user-login.service';
import { MsgService } from 'app/commons/service/message.service';
import { OverDetailsSecurityGroupsComponent } from './over-details-security-groups/over-details-security-groups.component';

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
    SecurityGroupsRoutingModule,
    InputTextareaModule
  ],
  declarations: [
    SecurityGroupsComponent,
    PermissionComponent,
    FunctionsExcludedComponent,
    EnabledUsersComponent,
    OverDetailsSecurityGroupsComponent
  ],
  providers: [
    ConfirmationService,
    // MessageService,
    CanDeactivateGuard,
    DataStorageService,
    SecurityGroupService,
    SecurityGroupPermissionService,
    SecurityGroupContentService,
    UserLoginSecurityGroupService,
    PortalPageService,
    SecurityPermissionService,
    ContentService,
    UserLoginService,
    MsgService
  ]
})
export default class SecurityGroupsModule {}
