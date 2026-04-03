import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { ConfirmationService, FilterService, SharedModule } from 'primeng/api';
import { CanDeactivateGuard } from 'app/shared/can-deactivate.guard';
import { MsgService } from 'app/commons/service/message.service';
import { IndicatorsComponent } from './indicators.component';
import { IndicatorsRoutingModule } from './indicators-routing.module';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { TableModule } from 'primeng/table';
import { CommonModule } from '@angular/common';
import { LayoutModule } from 'app/layout/layout.module';
import { TagModule } from 'primeng/tag';
import { SkeletonModule } from 'primeng/skeleton'
import { StatusItemService } from 'app/api/service/status-item.service';
import { GlAccountTypeService } from 'app/api/service/gl-account-type.service';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CheckboxModule } from 'primeng/checkbox';
import { RadioButtonModule } from 'primeng/radiobutton';
import { PeriodTypeService } from 'app/api/service/period-type.service';
import { ChipModule } from 'primeng/chip';
import { NewIndicatorComponent } from './new-indicator/new-indicator.component';
import { DynamicDialogModule } from 'primeng/dynamicdialog';
import { DialogService } from 'primeng/dynamicdialog';
import { FormGroupDirective } from '@angular/forms';
import { UomService } from 'app/api/service/uom.service';
import { NewIndicatorComponentSuccess } from './new-indicator/new-indicator-success.component';
import { IndicatorTabComponent } from './indicator-tab/indicator-tab.component';
import { DockModule } from 'primeng/dock';
import { MenuModule } from 'primeng/menu';
import { PanelMenuModule } from 'primeng/panelmenu';
import { MessageModule } from 'primeng/message';
import { TabMenuModule } from 'primeng/tabmenu';
import { PartyService } from 'app/api/service/party.service';
import { WorkEffortPurposeTypeService } from 'app/api/service/work-effort-purpose-type.service';
import { RoleTypeService } from 'app/api/service/role-type.service';
import {GzoomLabelComponent} from "../../../layout/form/gzoom-label/gzoom-label.component";

@NgModule({
    imports: [
        CommonModule,
        TableModule,
        IndicatorsRoutingModule,
        LayoutModule,
        TagModule,
        SkeletonModule,
        ButtonModule,
        InputTextModule,
        CheckboxModule,
        RadioButtonModule,
        ChipModule,
        DynamicDialogModule,
        DockModule,
        MenuModule,
        PanelMenuModule,
        MessageModule,
        TabMenuModule,
        GzoomLabelComponent
    ],
    declarations: [
        IndicatorsComponent,
        NewIndicatorComponent,
        NewIndicatorComponentSuccess,
        IndicatorTabComponent,
    ],
    providers: [
        FilterService,
        ConfirmationService,
        CanDeactivateGuard,
        MsgService,
        GlAccountService,
        StatusItemService,
        GlAccountTypeService,
        UserPreferenceService,
        PeriodTypeService,
        DialogService,
        FormGroupDirective,
        UomService,
        PartyService,
        WorkEffortPurposeTypeService,
        RoleTypeService
    ],
})
export default class IndicatorsModule { }
