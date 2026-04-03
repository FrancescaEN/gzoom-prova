import { NgModule } from '@angular/core';
import { ConfirmationService } from 'primeng/api';
import { CanDeactivateGuard } from 'app/shared/can-deactivate.guard';
import { MsgService } from 'app/commons/service/message.service';
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
import { DynamicDialogModule } from 'primeng/dynamicdialog';
import { DialogService } from 'primeng/dynamicdialog';
import { FormGroupDirective } from '@angular/forms';
import { UomService } from 'app/api/service/uom.service';
import { PartyService } from 'app/api/service/party.service';
import { DockModule } from 'primeng/dock';
import { MenuModule } from 'primeng/menu';
import { PanelMenuModule } from 'primeng/panelmenu';
import { MessageModule } from 'primeng/message';
import { TabMenuModule } from 'primeng/tabmenu';
import { WorkEffortPurposeAccountService } from 'app/api/service/work-effort-purpose-account.service';
import { IndicatorTabPurposeComponent } from './indicator-tab-purpose/indicator-tab-purpose.component';
import IndicatorTabRoutingModule from './indicator-tab-routing.module';
import { GlResourceTypeService } from 'app/api/service/gl-resource-type.service';
import { GlAccountClassService } from 'app/api/service/gl-account-class.service';
import { DataSourceService } from 'app/api/service/data-source.service';
import { CustomMethodService } from 'app/api/service/custom-method.service';
import { IndicatorTabCalculationParametersComponent } from './indicator-tab-calculation-parameters/indicator-tab-calculation-parameters.component';
import { IndicatorTabOrganizationComponent } from './indicator-tab-organization/indicator-tab-organization.component';
import IndicatorTabIndicatorComponent from './indicator-tab-indicator/indicator-tab-indicator.component';
import { UomRangeService } from 'app/api/service/uom-range.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { GlAccountRoleService } from 'app/api/service/gl-account-role.service';
import { GlAccountInputCalcService } from 'app/api/service/gl-account-input-calc.service';
import { GlAccountMeasRatScService } from 'app/api/service/gl-account-meas-rat-sc.service';
import { NewPurposeComponent } from './indicator-tab-purpose/new-purpose/new-purpose.component';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { GlAccountOrganizationService } from 'app/api/service/gl-account-organization.service';
import { NewOrganizationComponent } from './indicator-tab-organization/new-organization/new-organization.component';
import IndicatorTabValueListComponent from './indicator-tab-value-list/indicator-tab-value-list.component';
import { UomRatingScaleService } from 'app/api/service/uom-rating-scale.service';
import { NewValueComponent } from './indicator-tab-value-list/new-value/new-value.component';
import { CalculationParametersComponent } from './indicator-tab-calculation-parameters/calculation-parameters/calculation-parameters.component';
import { CalculationElementForComponent } from './indicator-tab-calculation-parameters/calculation-element-for/calculation-element-for.component';
import { NewParameterComponent } from './indicator-tab-calculation-parameters/calculation-parameters/new-parameter/new-parameter.component';
import { SelectionIndicatorComponent } from './indicator-tab-calculation-parameters/calculation-parameters/selection-indicator/selection-indicator.component';
import { UpdateOperandComponent } from './indicator-tab-calculation-parameters/calculation-parameters/update-operand/update-operand.component';
import IndicatorTabUoDetectedComponent from './indicator-tab-uo-detected/indicator-tab-uo-detected.component';
import { NewUoDetectedComponent } from './indicator-tab-uo-detected/new-uo-detected/new-uo-detected.component';
import { PartyRoleService } from 'app/api/service/party-role.service';
import { UserPermissionService } from 'app/shared/user-permission.service';
import { RoleTypeService } from 'app/api/service/role-type.service';
import { GlFiscalTypeService } from 'app/api/service/gl-fiscal-type.service';
import {GzoomLabelComponent} from "../../../../layout/form/gzoom-label/gzoom-label.component";

@NgModule({
  imports: [
    CommonModule,
    TableModule,
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
    IndicatorTabRoutingModule,
    GzoomLabelComponent,

  ],
    declarations: [
        IndicatorTabIndicatorComponent,
        IndicatorTabPurposeComponent,
        IndicatorTabCalculationParametersComponent,
        IndicatorTabOrganizationComponent,
        IndicatorTabValueListComponent,
        NewPurposeComponent,
        NewOrganizationComponent,
        NewValueComponent,
        CalculationParametersComponent,
        CalculationElementForComponent,
        NewParameterComponent,
        SelectionIndicatorComponent,
        UpdateOperandComponent,
        IndicatorTabUoDetectedComponent,
        NewUoDetectedComponent
    ],
    providers: [

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
        WorkEffortPurposeAccountService,
        GlResourceTypeService,
        GlAccountClassService,
        DataSourceService,
        CustomMethodService,
        UomRangeService,
        ConfirmDialogService,
        GlAccountRoleService,
        GlAccountInputCalcService,
        GlAccountMeasRatScService,
        ScrollableTabService,
        GlAccountOrganizationService,
        UomRatingScaleService,
        PartyRoleService,
        UserPermissionService,
        RoleTypeService,
        GlFiscalTypeService
    ],
})
export default class IndicatorTabModule { }
