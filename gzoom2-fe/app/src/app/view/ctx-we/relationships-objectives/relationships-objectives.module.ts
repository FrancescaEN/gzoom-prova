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
import { CanDeactivateGuard, canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { MessagesModule } from 'primeng/messages';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { WorkEffortAssocService } from 'app/api/service/work-effort-assoc.service';
import { RelationshipsObjectivesComponent } from './relationships-objectives.component';
import { WorkEffortMeasureService } from 'app/api/service/work-effort-measure.service';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { MsgService } from 'app/commons/service/message.service';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { RouterModule, Routes } from '@angular/router';
import { WorkEffortAssocTypeService } from 'app/api/service/work-effort-assoc-type.service';
import { WorkEffortService } from 'app/api/service/work-effort.service';

const routes: Routes = [
  {
    path: '',
    component: RelationshipsObjectivesComponent,
    canDeactivate: [canDeactivateUnsavedGuard]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
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
    AutoCompleteModule
  ],
  declarations: [
    RelationshipsObjectivesComponent
  ],
  providers: [
    WorkEffortAssocService,
    ConfirmationService,
    // MessageService,
    CanDeactivateGuard,
    WorkEffortMeasureService,
    UserPreferenceService,
    TableEditingCellService,
    MsgService,
    WorkEffortAssocTypeService,
    WorkEffortService
  ]
})
export default class RelationshipsObjectivesModule {}
