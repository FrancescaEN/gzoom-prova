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
import { AccountingAndExtraAccountingUnitsComponent } from './accounting-and-extra-accounting-units.component';
import { AccountingAndExtraAccountingUnitsRoutingModule } from './accounting-and-extra-accounting-units-routing.module';
import { GlAccountTypeService } from 'app/api/service/gl-account-type.service';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { GlAccountTypeGlFiscalTypeService } from 'app/api/service/gl-account-type-gl-fiscal-type.service';
import { GlFiscalTypeService } from 'app/api/service/gl-fiscal-type.service';
import { NatureComponent } from './nature/nature.component';
import { GlAccountResourceService } from 'app/api/service/gl-account-resource.service';
import { GlResourceTypeService } from 'app/api/service/gl-resource-type.service';
import { DetectionTypesComponent } from './detection-types/detection-types.component';
import { CanDeactivateGuard } from 'app/shared/can-deactivate.guard';
import { MsgService } from 'app/commons/service/message.service';
import { OverDetailAccountingAndExtraAccountingUnitsComponent } from './over-detail-accounting-and-extra-accounting-units/over-detail-accounting-and-extra-accounting-units.component';
import { PrimengTableService } from 'app/commons/service/primeng-table.service';

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
        AccountingAndExtraAccountingUnitsRoutingModule
    ],
    declarations: [
        AccountingAndExtraAccountingUnitsComponent,
        DetectionTypesComponent,
        NatureComponent,
        OverDetailAccountingAndExtraAccountingUnitsComponent
    ],
    providers: [
        ConfirmationService,
        GlAccountTypeService,
        DataStorageService,
        GlAccountTypeGlFiscalTypeService,
        GlFiscalTypeService,
        GlAccountResourceService,
        GlResourceTypeService,
        CanDeactivateGuard,
        MsgService,
        PrimengTableService
    ]
})
export default class AccountingAndExtraAccountingUnitsModule { }