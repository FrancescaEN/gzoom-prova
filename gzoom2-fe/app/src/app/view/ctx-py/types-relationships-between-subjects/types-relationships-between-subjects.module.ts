import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToolbarModule } from 'primeng/toolbar';
import { ConfirmationService, SharedModule } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { LayoutModule } from 'app/layout/layout.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ApiModule } from 'app/api/api.module';
import { CommonsModule } from 'app/commons/commons.module';
import { I18nModule } from 'app/i18n/i18n.module';
import { AccordionModule } from 'primeng/accordion';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { MenuModule } from 'primeng/menu';
import { RippleModule } from 'primeng/ripple';
import { SlideMenuModule } from 'primeng/slidemenu';
import { SpeedDialModule } from 'primeng/speeddial';
import { SpinnerModule } from 'primeng/spinner';
import { SplitButtonModule } from 'primeng/splitbutton';
import { ToastModule } from 'primeng/toast';
import { TooltipModule } from 'primeng/tooltip';
import { TreeSelectModule } from 'primeng/treeselect';
import { TreeTableModule } from 'primeng/treetable';
import { TabViewModule } from 'primeng/tabview';
import { FileUploadModule } from 'primeng/fileupload';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { TypesRelationshipsBetweenSubjectsComponent } from './types-relationships-between-subjects.component';
import { TypesRelationshipsBetweenSubjectsDetailsComponent } from './types-relationships-between-subjects-details/types-relationships-between-subjects-details.component';
import { TypesRelationshipsBetweenSubjectsRoutingModule } from './types-relationships-between-subjects-routing.module';
import { PartyRelationshipTypeService } from 'app/api/service/party-relationship-type.service';
import { PartyRelationshipRoleService } from 'app/api/service/party-relationship-role.service';
import { RoleTypeService } from 'app/api/service/role-type.service';
import { CanDeactivateGuard } from 'app/shared/can-deactivate.guard';
import { MsgService } from 'app/commons/service/message.service';

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
    TabViewModule,
    FileUploadModule,
    MessageModule,
    MessagesModule,
    TypesRelationshipsBetweenSubjectsRoutingModule

  ],
  declarations: [
    TypesRelationshipsBetweenSubjectsComponent,
    TypesRelationshipsBetweenSubjectsDetailsComponent
  ],
  providers: [
    ConfirmationService,
    DataStorageService,
    PartyRelationshipTypeService,
    PartyRelationshipRoleService,
    RoleTypeService,
    CanDeactivateGuard,
    MsgService
  ]
})
export default class TypesRelationshipsBetweenSubjectsModule { }
