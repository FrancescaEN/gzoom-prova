import { NgModule } from '@angular/core';
import { LayoutModule } from '../../../layout/layout.module';
import { AnalysisTypeComponent } from './analysis-type.component';
import { AnalysisTypeRoutingModule } from './analysis-type-routing.module';
import { WorkEffortAnalysisService } from 'app/api/service/work-effort-analysis.service';
import { WorkEffortTypeService } from 'app/api/service/work-effort-type.service';
import { AnalysisTypeDetailComponent } from './analysis-type-detail/analysis-type-detail.component';
import { GlFiscalTypeService } from 'app/api/service/gl-fiscal-type.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { ConfirmationService } from 'primeng/api';
import { CanDeactivateGuard } from 'app/shared/can-deactivate.guard';

@NgModule({
    imports: [
        LayoutModule,
        AnalysisTypeRoutingModule
    ],
    declarations: [
        AnalysisTypeComponent,
        AnalysisTypeDetailComponent
    ],
    providers: [
        WorkEffortAnalysisService,
        WorkEffortTypeService,
        GlFiscalTypeService,
        ConfirmationService,
        ConfirmDialogService,
        CanDeactivateGuard,
    ],
})
export default class AnalysisTypeModule { }