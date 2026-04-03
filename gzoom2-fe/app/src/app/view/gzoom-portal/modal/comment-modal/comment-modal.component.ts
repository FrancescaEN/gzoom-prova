import { Component, ElementRef, ViewEncapsulation } from '@angular/core';
import { NotificationService } from '../../../../commons/service/notification/notification.service';
import { GzoomModalComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal.component';
import { GzoomButtonComponent } from '../../../../layout/gzoom/gzoom-button/gzoom-button.component';
import { GzoomModalBodyComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-body/gzoom-modal-body.component';
import { GzoomModalFooterComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-footer/gzoom-modal-footer.component';
import { GzoomModalHeaderComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-header/gzoom-modal-header.component';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { GzoomFormComponent } from '../../../../layout/gzoom/gzoom-form/gzoom-form.component';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { GoalService } from '../../../../api/service/goal.service';
import { Note } from '../../../../commons/model/goal-file/note/Note';
import { Goal } from '../../../../commons/model/goal-file/Goal';
import { PortalPermission } from '../../portal-permissions';

@Component({
  selector: 'gzoom-comment-modal',
  standalone: true,
  imports: [
    GzoomButtonComponent,
    GzoomModalBodyComponent,
    GzoomModalComponent,
    GzoomModalFooterComponent,
    GzoomModalHeaderComponent,
    ScrollPanelModule,
    GzoomFormComponent,
    InputTextareaModule,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './comment-modal.component.html',
  styleUrls: ['./comment-modal.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CommentModalComponent extends GzoomModalComponent {
  protected goal: Goal = null;

  private comment: Note = null;
  protected text: string = null;
  protected textMaxLength: number = 6000;

  protected editEnabled: boolean = false;

  constructor(private config: DynamicDialogConfig,
              protected modalDialogRef: DynamicDialogRef,
              protected elementRef: ElementRef,
              private readonly goalService: GoalService,
              private readonly notificationService: NotificationService) {
    super(elementRef, modalDialogRef);
    this.goal = this.config.data?.goal;
    this.editEnabled = PortalPermission.generatePortalPermissions(this.goal?.goalStatus.id)[PortalPermission.NotesWrite];
  }

  protected loadData() {
    this.goalService.getNotes(this.goal?.id).subscribe(comments => {
      this.comment = comments.find(note => note.name === 'Evidenze (note)');
      this.text = this.comment?.info;
      this.loading = false;
    });
  }

  save(): void {
    const note = new Note();
    note.info = this.text;

    this.goalService.updateNote(this.comment?.id, note).subscribe({
      next: _ => {
        this.notificationService.handleSuccess('Commento aggiornato');
        // this.closeModal();
      },
      error: _ => {
        this.notificationService.handleError('Non è stato possibile aggiornare il commento. Ripeti l’inserimento.');
      }
    });
  }
}
