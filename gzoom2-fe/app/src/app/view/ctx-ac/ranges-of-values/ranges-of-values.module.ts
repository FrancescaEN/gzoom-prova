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
import { RangesOfValuesComponent } from './ranges-of-values.component';
import { UomRangeService } from 'app/api/service/uom-range.service';
import { RangesOfValuesDetailComponent } from './ranges-of-values-detail/ranges-of-values-detail.component';
import { RangesOfValuesRoutingModule } from './ranges-of-values-routing.module';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { UomService } from 'app/api/service/uom.service';
import { UomRangeValuesService } from 'app/api/service/uom-range-values.service';
import { ContentService } from 'app/api/service/content.service';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
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
    CardModule,
    MessagesModule,
    ProgressSpinnerModule,
    RangesOfValuesRoutingModule
  ],
  declarations: [
    RangesOfValuesComponent,
    RangesOfValuesDetailComponent
  ],
  providers: [
    ConfirmationService,
    // MessageService,
    CanDeactivateGuard,
    UomRangeService,
    DataStorageService,
    UomService,
    UomRangeValuesService,
    ContentService,
    EnumerationService,
    TableEditingCellService,
    MsgService
  ]
})
export default class RangesOfValuesModule {}
