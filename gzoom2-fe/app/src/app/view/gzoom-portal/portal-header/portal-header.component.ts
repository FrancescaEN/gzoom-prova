import { NgClass } from '@angular/common';
import { Component, OnInit, ViewEncapsulation, computed, effect, input, model } from '@angular/core';
import { SelectItem } from 'primeng/api';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { GoalFileStatusService } from '../../../api/service/goal-file/goal-file-status.service';
import { GoalService } from '../../../api/service/goal.service';
import { PartyService } from '../../../api/service/party.service';
import { ReportService } from '../../../api/service/report.service';
import { CommonsModule } from '../../../commons/commons.module';
import { AccountCode } from '../../../commons/enum/AccountCode';
import { Goal } from '../../../commons/model/goal-file/Goal';
import { Note } from '../../../commons/model/goal-file/note/Note';
import { GoalStatus } from '../../../commons/model/goal-file/status/goal-status';
import { GoalStatusId } from '../../../commons/model/goal-file/status/goal-status-id';
import { GoalStatusChangeDirection } from '../../../commons/model/goal-file/status/GoalStatusDirection';
import { AuthService } from '../../../commons/service/auth.service';
import { ArrayUtils } from '../../../commons/utils/array-utils';
import { StringUtils } from '../../../commons/utils/string-utils';
import { VarUtils } from '../../../commons/utils/var-utils';
import { I18nModule } from '../../../i18n/i18n.module';
import { GzoomButtonComponent } from '../../../layout/gzoom/gzoom-button/gzoom-button.component';
import { GzoomDropdownComponent } from '../../../layout/gzoom/gzoom-form/gzoom-dropdown/gzoom-dropdown.component';
import { GzoomTooltipComponent } from '../../../layout/gzoom/gzoom-tooltip/gzoom-tooltip.component';
import { ReportPopupService } from '../../../shared/report-popup/report-popup.service';
import { SharedModule } from '../../../shared/shared.module';
import { Report, ReportParam } from '../../report-print/report';
import { ConciliationModalComponent } from '../modal/conciliation-modal/conciliation-modal.component';
import { ContextDataModalComponent } from '../modal/context-data/context-data-modal.component';
import { ContradictoryModalComponent } from '../modal/contradictory-modal/contradictory-modal.component';
import { StatusChangeModalComponent } from '../modal/status-change-modal/status-change-modal.component';
import { PortalPermission } from '../portal-permissions';


@Component({
  selector: 'gzoom-portal-header',
  standalone: true,
  imports: [
    CommonsModule,
    GzoomButtonComponent,
    GzoomDropdownComponent,
    GzoomTooltipComponent,
    I18nModule,
    SharedModule,
    NgClass
  ],
  templateUrl: './portal-header.component.html',
  styleUrl: './portal-header.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class PortalHeaderComponent implements OnInit {
  goals = input<Goal[]>([]);
  selectedGoalId = model<string>('');
  goalRoot = model<Goal>(null);

  protected contextDataModal: DynamicDialogRef | undefined;
  protected contradictoryModal: DynamicDialogRef | undefined;
  protected statusChangeModal: DynamicDialogRef | undefined;
  protected conciliationModal: DynamicDialogRef | undefined;

  protected yearsDropdown: SelectItem[] = [];
  protected header = { user: null, school: null, usr: null }
  protected integrationNote: Note = null;

  protected statusDropdown: SelectItem[] = [];
  protected selectedStatus: string = '';

  protected reports = model<Report[]>([]);
  protected printButtonEnabled = computed(() => ArrayUtils.notEmpty(this.reports()));
  protected contradictoryInfo: {
    employeeComment: string | null;
    evaluatorComment: string | null;
    outcome: string | null;
  } = {
    employeeComment: null,
    evaluatorComment: null,
    outcome: null
  }; // needed for the button badge
  protected conciliationInfo: {
    reason: string | null;
    guaranteeInstitutionReport: string | null;
    outcome: string | null;
  } = {
    reason: null,
    guaranteeInstitutionReport: null,
    outcome: null
  }; // needed for the button badge

  protected hasPermission = computed(() =>
    PortalPermission.generatePortalPermissions(this.goalRoot()?.goalStatus.id));

  constructor(private readonly authSrv: AuthService,
              private readonly dialogService: DialogService,
              private readonly goalFileStatusService: GoalFileStatusService,
              private readonly goalService: GoalService,
              private readonly partyService: PartyService,
              private readonly reportPopupService: ReportPopupService,
              private readonly reportService: ReportService) {
    this.header.user = this.authSrv.userProfile();

    effect(() => {
      if (VarUtils.isDefined(this.goalRoot())) {
        if (this.hasPermission()[PortalPermission.StatusWrite]) {
          this.loadStatuses();
        }
        this.selectedStatus = this.goalRoot()?.goalStatus.id;
        this.loadReports();
        this.loadIntegrationNote();
        this.loadContradictoryNotes();
        this.loadConciliationData();
      }
    });

    effect(() => {
      if (this.goals().length > 1) {
        // all goal list received --> init years dropdown
        this.yearsDropdown = this.goals().map(goal => {
          return {
            label: `${goal.goalPeriod.fromDate.getFullYear()}-${goal.goalPeriod.thruDate.getFullYear() % 100}`,
            value: goal.id
          };
        });
      }
    });
  }

  ngOnInit() {
    this.partyService.getHeaderPortalPage().subscribe(data => {
      if (data && data['schoolName'] && data['usrName']) {
        this.header.school = data['schoolName'];
        this.header.usr = data['usrName'];
      } else {
        console.error('Dati mancanti o non validi nella risposta del servizio:', data);
        this.header.school = 'Informazione non disponibile';
        this.header.usr = 'Informazione non disponibile';
      }
    });
  }

  statusSelectedHandler = (_: string) => {
    this.statusChangeModal = this.dialogService.open(StatusChangeModalComponent, {
      data: {
        goal: this.goalRoot(),
        statusId: this.selectedStatus
      },
      styleClass: 'gzoom-modal gzoom-portal',
      style: { 'width': '100px', 'min-width': '500px' },
      width: 'unset'
    });

    this.statusChangeModal.onClose.subscribe((statusChanged: boolean) => {
      if (statusChanged) {
        this.reloadData();
      } else {
        this.selectedStatus = this.goalRoot().goalStatus.id;
      }
    });
  };

  private reloadData(): void {
    const id = this.goalRoot()?.id;
    this.selectedGoalId.set(null);
    this.selectedGoalId.set(id);
  }

  extractTotalResult(goal: Goal): number {
    return goal?.indicators?.filter(i => i.accountCode === AccountCode.SCORE)[0]?.value[0]?.amount;
  }

  print() {
    const obj: ReportParam = {
      workEffortTypeId: this.goalRoot()?.goalType.id,
      workEffortId: this.goalRoot()?.id
    };
    this.reportPopupService.openPopup(obj);
  }

  showContextDataModal() {
    this.contextDataModal = this.dialogService.open(ContextDataModalComponent, {
      data: {
        goal: this.goalRoot()
      },
      styleClass: 'gzoom-modal gzoom-portal'
    });

    this.contextDataModal.onClose.subscribe((_) => {
      this.loadIntegrationNote();
    });
  }

  private loadStatuses(): void {
    this.goalFileStatusService.getGoalFileAvailableStatuses(this.goalRoot()?.id, this.goalRoot()?.goalStatus?.id).subscribe(statuses => {
      // sorting from backward to forward
      // as for 19/11/2025, it is not necessary because we don't change status to the previous one,
      // but it won't hurt anyone if I leave it here for the future :)
      statuses.sort((a, b) =>
        (a.direction === GoalStatusChangeDirection.BACKWARD && b.direction === GoalStatusChangeDirection.FORWARD) ? -1 :
          // sorting by sequence ID asc
          (+a.sequenceId < +b.sequenceId) ? -1 : 1);

      // inserting current status after all BACKWARD and before all FORWARD if not present,
      // so it results as chosen in the dropdown list
      const currentStatusId = this.goalRoot()?.goalStatus?.id;
      if (!VarUtils.isDefined(statuses.find(status => status.id === currentStatusId))) {
        statuses.splice(
          statuses.findIndex(status => status.direction === GoalStatusChangeDirection.FORWARD),
          0,
          new GoalStatus().setId(currentStatusId).setDescription(this.goalRoot()?.goalStatus.description));
      }

      this.statusDropdown = statuses
        .filter(status => status.direction !== GoalStatusChangeDirection.BACKWARD
          && status.id !== GoalStatusId.Conciliation)
        .map(status => {
          return {
            label: `${status.direction === GoalStatusChangeDirection.FORWARD ? '→' :
              status.direction === GoalStatusChangeDirection.BACKWARD ? '←' : ''} ${status.description}`,
            value: status.id
          };
        });
    })
  }

  private loadReports(): void {
    this.reportService.reportsByWorkEffortTypeId(this.goalRoot().goalType?.id).subscribe(reports => {
      this.reports.set([...reports]);
    });
  }

  private loadIntegrationNote(): void {
    this.goalService.getNotes(this.goalRoot()?.id).subscribe(notes => {
      this.integrationNote = notes.find(note => note.name === 'Integrazione dati'); // needed for the button badge
    });
  }

  showContradictoryModal() {
    this.contradictoryModal = this.dialogService.open(ContradictoryModalComponent, {
      data: {
        goal: this.goalRoot()
      },
      styleClass: 'gzoom-modal gzoom-portal'
    });

    this.contradictoryModal.onClose.subscribe((_) => {
      // this.loadContradictoryNotes(); // GN-8349: la sezione contraddittorio quando appare è sempre in sola consultazione
    });
  }

  private loadContradictoryNotes(): void {
    this.goalService.getNotes(this.goalRoot()?.id).subscribe(notes => {
      this.contradictoryInfo.employeeComment = notes.find(note => note.sequenceId === 500)?.info;
      this.contradictoryInfo.evaluatorComment = notes.find(note => note.sequenceId === 600)?.info;
      this.contradictoryInfo.outcome = this.goalRoot()?.indicators?.find(i => i.accountCode === 'ESITO_CONTR')?.value?.[0]?.goalIndicator?.valueCode;
    });
  }

  private loadConciliationData(): void {
    this.goalService.getNotes(this.goalRoot()?.id).subscribe(notes => {
      this.conciliationInfo.reason = notes.find(note => note.sequenceId === 700)?.info;
      this.conciliationInfo.guaranteeInstitutionReport = notes.find(note => note.sequenceId === 800)?.info;
      this.conciliationInfo.outcome = this.goalRoot()?.indicators?.find(i => i.accountCode === 'ESITO_CONCI')?.value?.[0]?.goalIndicator?.valueCode;
    });
  }

  showConciliationModal() {
    this.conciliationModal = this.dialogService.open(ConciliationModalComponent, {
      data: {
        goal: this.goalRoot()
      },
      styleClass: 'gzoom-modal gzoom-portal'
    });
  }

  protected readonly PortalPermission = PortalPermission;
  protected readonly StringUtils = StringUtils;
}
