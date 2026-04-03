import { Component, ElementRef, ViewEncapsulation } from '@angular/core';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { GoalService } from '../../../../api/service/goal.service';
import { Goal } from '../../../../commons/model/goal-file/Goal';
import { NotificationService } from '../../../../commons/service/notification/notification.service';
import { GzoomButtonComponent } from '../../../../layout/gzoom/gzoom-button/gzoom-button.component';
import { GzoomLabelComponent } from '../../../../layout/gzoom/gzoom-label/gzoom-label.component';
import { GzoomModalBodyComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-body/gzoom-modal-body.component';
import { GzoomModalFooterComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-footer/gzoom-modal-footer.component';
import { GzoomModalHeaderComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-header/gzoom-modal-header.component';
import { GzoomModalComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal.component';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { FormsModule } from '@angular/forms';
import { GzoomFormComponent } from '../../../../layout/gzoom/gzoom-form/gzoom-form.component';
import { Note } from '../../../../commons/model/goal-file/note/Note';
import { PortalPermission } from '../../portal-permissions';

@Component({
  selector: 'gzoom-data-integration-modal',
  standalone: true,
  imports: [
    GzoomButtonComponent,
    GzoomModalBodyComponent,
    GzoomModalFooterComponent,
    GzoomModalHeaderComponent,
    InputTextareaModule,
    FormsModule,
    GzoomFormComponent,
    GzoomModalComponent,
    GzoomLabelComponent
  ],
  templateUrl: './data-integration-modal.component.html',
  styleUrls: ['./data-integration-modal.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DataIntegrationModalComponent extends GzoomModalComponent {
  private readonly goal: Goal = null;

  private note: Note = null;
  protected text: string = null;
  protected noteMaxLength: number = 6000;

  protected editEnabled: boolean = false;

  constructor(private config: DynamicDialogConfig,
              protected modalDialogRef: DynamicDialogRef,
              protected elementRef: ElementRef,
              private readonly goalService: GoalService,
              private readonly notificationService: NotificationService) {
    super(elementRef, modalDialogRef);
    this.goal = this.config.data?.goal;
    this.editEnabled = PortalPermission.generatePortalPermissions(this.goal?.goalStatus.id)[PortalPermission.IntegrationNoteWrite]
  }

  override loadData() {
    this.goalService.getNotes(this.goal.id).subscribe(notes => {
      this.note = notes.find(note => note.name === 'Integrazione dati');
      this.text = this.note?.info;
      this.loading = false;
    });
  }

  save(): void {
    const note = new Note();
    note.info = this.text;

    this.goalService.updateNote(this.note?.id, note).subscribe({
      next: _ => {
        this.notificationService.handleSuccess('Dati aggiornati')
      },
      error: _ => {
        this.notificationService.handleError('La modifica non è andata a buon fine. Riprova.')
      }
    });
  }
}
