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
import { CalculationFormulasComponent } from './calculation-formulas.component';
import { ValuesMatrixComponent } from './values-matrix/values-matrix.component';
import { CustomMethodMatrixService } from 'app/api/service/custom-method-matrix.service';
import { CustomMethodService } from 'app/api/service/custom-method.service';
import { CalculationFormulasRoutingModule } from './calculation-formulas-routing.module';
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
        CalculationFormulasRoutingModule

    ],
    declarations: [
        CalculationFormulasComponent,
        ValuesMatrixComponent
    ],
    providers: [
        ConfirmationService,
        DataStorageService,
        CustomMethodMatrixService,
        CustomMethodService,
        CanDeactivateGuard,
        MsgService
    ]
})
export default class CalculationFormulasModule { }