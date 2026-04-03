import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToolbarModule } from 'primeng/toolbar';
import { ConfirmationService, MessageService, SharedModule } from 'primeng/api';
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
import { NgxGaugeModule } from 'ngx-gauge';
import { TabViewModule } from 'primeng/tabview';
import { FileUploadModule } from 'primeng/fileupload';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { PositionsEconomicsComponent } from './positions-economics.component';
import { CardModule } from 'primeng/card';
import { LanguageService } from 'app/api/service/language.service';
import { CanDeactivateGuard } from 'app/shared/can-deactivate.guard';
import { PositionsEconomicsRoutingModule } from './positions-economics-routing.module'
import { EmplPositionTypeService } from '../../../api/service/empl-position-type.service'
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { WorkEffortService } from 'app/api/service/work-effort.service';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
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
    NgxGaugeModule,
    TabViewModule,
    FileUploadModule,
    MessageModule,
    MessagesModule,
    CardModule,
    PositionsEconomicsRoutingModule,
    ProgressSpinnerModule

  ],
  declarations: [
    PositionsEconomicsComponent
  ],
  providers: [
    EmplPositionTypeService,
    ConfirmationService,
    // MessageService,
    LanguageService,
    CanDeactivateGuard,
    TableEditingCellService,
    WorkEffortService,
    MsgService
  ]
})
export default class PositionsEconomicsModule {}
