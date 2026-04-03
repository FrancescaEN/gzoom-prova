import { Component, ElementRef, ViewEncapsulation } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { GoalService } from '../../../../api/service/goal.service';
import { Goal } from '../../../../commons/model/goal-file/Goal';
import { Indicator } from '../../../../commons/model/goal-file/indicator/Indicator';
import { Note } from '../../../../commons/model/goal-file/note/Note';
import { NotificationService } from '../../../../commons/service/notification/notification.service';
import { StringUtils } from '../../../../commons/utils/string-utils';
import { GzoomButtonComponent } from '../../../../layout/gzoom/gzoom-button/gzoom-button.component';
import { GzoomFormComponent } from '../../../../layout/gzoom/gzoom-form/gzoom-form.component';
import { GzoomModalBodyComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-body/gzoom-modal-body.component';
import { GzoomModalFooterComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-footer/gzoom-modal-footer.component';
import { GzoomModalHeaderComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-header/gzoom-modal-header.component';
import { GzoomModalComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal.component';

@Component({
  selector: 'gzoom-contradictory-modal',
  standalone: true,
  imports: [
    GzoomModalComponent,
    GzoomModalHeaderComponent,
    GzoomModalBodyComponent,
    GzoomFormComponent,
    GzoomModalFooterComponent,
    GzoomButtonComponent,
    InputTextareaModule,
    FormsModule
  ],
  templateUrl: './contradictory-modal.component.html',
  styleUrl: './contradictory-modal.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ContradictoryModalComponent extends GzoomModalComponent {
  private goal: Goal = null;

  protected employeeComment: Note = null;
  protected employeeCommentText: string = null;
  protected evaluatorComment: Note = null;
  protected outcome: Indicator = null;
  protected outcomeValue: string = null;

  protected textMaxLength: number = 6000;

  protected editEnabled: boolean = false;

  constructor(private config: DynamicDialogConfig,
              protected modalDialogRef: DynamicDialogRef,
              protected elementRef: ElementRef,
              private readonly goalService: GoalService,
              private readonly notificationService: NotificationService) {
    super(elementRef, modalDialogRef);
    this.goal = this.config.data?.goal;
    // commented for GN-8349 2.a:
    // this.editEnabled = PortalPermission.generatePortalPermissions(this.goal?.goalStatus.id)[PortalPermission.ContradictoryWrite]
  }

  protected loadData() {
    this.goalService.getNotes(this.goal?.id).subscribe(notes => {
      this.employeeComment = notes.find(note => note.sequenceId === 500);
      this.employeeCommentText = this.employeeComment?.info;
      this.evaluatorComment = notes.find(note => note.sequenceId === 600);
      this.outcome = this.goal.indicators?.find(i => i.accountCode === 'ESITO_CONTR');
      this.outcomeValue = this.outcome?.value?.[0]?.goalIndicator?.valueCode;
      this.loading = false;
    });
  }

  save(): void {
    const note = new Note();
    note.info = this.employeeCommentText;

    this.goalService.updateNote(this.employeeComment?.id, note).subscribe({
      next: _ => {
        this.notificationService.handleSuccess('Commento valutato aggiornato');
        this.closeModal();
      },
      error: _ => {
        this.notificationService.handleError('Non è stato possibile aggiornare il commento valutato');
      }
    });
  }

  protected readonly StringUtils = StringUtils;
}
