import { Component, ElementRef, ViewEncapsulation } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DialogService, DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { GoalFileStatusService } from '../../../../api/service/goal-file/goal-file-status.service';
import { GoalService } from '../../../../api/service/goal.service';
import { Goal } from '../../../../commons/model/goal-file/Goal';
import { Note } from '../../../../commons/model/goal-file/note/Note';
import { GoalStatus } from '../../../../commons/model/goal-file/status/goal-status';
import { GoalStatusId } from '../../../../commons/model/goal-file/status/goal-status-id';
import { NotificationService } from '../../../../commons/service/notification/notification.service';
import { ArrayUtils } from '../../../../commons/utils/array-utils';
import { StringUtils } from '../../../../commons/utils/string-utils';
import { VarUtils } from '../../../../commons/utils/var-utils';
import { I18nModule } from '../../../../i18n/i18n.module';
import { I18NService } from '../../../../i18n/i18n.service';
import { GzoomButtonComponent } from '../../../../layout/gzoom/gzoom-button/gzoom-button.component';
import { GzoomFormComponent } from '../../../../layout/gzoom/gzoom-form/gzoom-form.component';
import { GzoomModalBodyComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-body/gzoom-modal-body.component';
import { GzoomModalFooterComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-footer/gzoom-modal-footer.component';
import { GzoomModalHeaderComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-header/gzoom-modal-header.component';
import { GzoomModalComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal.component';
import { ResultMessageModalComponent } from '../result-message-modal/result-message-modal.component';

@Component({
  selector: 'gzoom-status-change-modal',
  standalone: true,
  imports: [
    GzoomModalComponent,
    GzoomModalHeaderComponent,
    GzoomModalBodyComponent,
    GzoomFormComponent,
    InputTextareaModule,
    FormsModule,
    GzoomModalFooterComponent,
    GzoomButtonComponent,
    I18nModule
  ],
  templateUrl: './status-change-modal.component.html',
  styleUrl: './status-change-modal.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class StatusChangeModalComponent extends GzoomModalComponent {
  private goal: Goal = null;
  private readonly statusFrom: GoalStatusId = null;
  private readonly statusTo: GoalStatusId = null;

  protected commentEnabled: boolean = false;
  protected commentRequired: boolean = false;
  protected statusComment: Note = null;
  protected text: string = null;
  protected textMaxLength: number = 6000;
  private statusChanged: boolean = false;

  protected resultMessageModal: DynamicDialogRef | undefined;

  private readonly COMMENT_NOTES: {
    [key in GoalStatusId]?: {
      [key in GoalStatusId]?: {
        noteSequenceId: number,
        required: boolean
      }
    }
  } = {
    [GoalStatusId.SignedByUSR]: {
      [GoalStatusId.ContradictoryRequestFromDS]: {
        noteSequenceId: 500,
        required: true
      }
    }
  };

  constructor(private readonly dialogService: DialogService,
              private config: DynamicDialogConfig,
              protected modalDialogRef: DynamicDialogRef,
              protected elementRef: ElementRef,
              private readonly goalFileStatusService: GoalFileStatusService,
              private readonly goalService: GoalService,
              private readonly i18nService: I18NService,
              private readonly notificationService: NotificationService) {
    super(elementRef, modalDialogRef);
    this.goal = this.config.data?.goal;
    this.statusFrom = GoalStatusId.fromString(this.goal?.goalStatus?.id);
    this.statusTo = GoalStatusId.fromString(this.config.data?.statusId);
    this.commentEnabled = this.statusCommentEnabled();
    if (this.commentEnabled) {
      this.commentRequired = this.COMMENT_NOTES[this.statusFrom][this.statusTo].required;
    }
  }

  protected loadData() {
    const noteSequenceId = this.COMMENT_NOTES[this.statusFrom]?.[this.statusTo]?.noteSequenceId ?? null;

    if (VarUtils.isDefined(noteSequenceId)) {
      // we should save status comment in a note
      this.goalService.getNotes(this.goal?.id).subscribe(notes => {
        this.statusComment = notes.find(n => n.sequenceId === noteSequenceId);
        this.loading = false;
      });
    } else {
      this.loading = false;
    }
  }

  private statusCommentEnabled(): boolean {
    return Object.keys(this.COMMENT_NOTES).includes(this.statusFrom) &&
      Object.keys(this.COMMENT_NOTES[this.statusFrom]).includes(this.statusTo);
  }

  protected async save() {
    const status = new GoalStatus().setId(this.statusTo);

    if (this.commentEnabled) {
      status.setReason(this.text);
    }

    this.goalFileStatusService.updateGoalFileStatus(this.goal?.id, status, this.statusComment?.id).subscribe({
      next: result => {
        if (ArrayUtils.notEmpty(result?.messages)) {
          this.resultMessageModal = this.dialogService.open(ResultMessageModalComponent, {
            header: this.i18nService.translate('goalFileStatusUpdateError'),
            data: {
              messages: result.messages
            },
            styleClass: 'gzoom-modal gzoom-portal'
          });

        } else {
          this.notificationService.handleSuccess(this.i18nService.translate('goalFileStatusUpdatedSuccessfully'));
        }
        this.statusChanged = true;
        this.closeModal();
      },
      error: _ => {
        this.notificationService.handleError(this.i18nService.translate('goalFileStatusUpdateError'), 'status_change');
        this.closeModal();
      }
    });
  }

  override closeModal() {
    this.modalDialogRef.close(this.statusChanged);
  }

  protected readonly VarUtils = VarUtils;
  protected readonly StringUtils = StringUtils;
}
