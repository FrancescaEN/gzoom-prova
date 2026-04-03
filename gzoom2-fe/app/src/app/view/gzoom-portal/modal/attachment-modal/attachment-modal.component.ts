import { Component, ElementRef, ViewEncapsulation } from '@angular/core';
import { NotificationService } from '../../../../commons/service/notification/notification.service';
import { DateUtils } from '../../../../commons/utils/date-utils';
import { VarUtils } from '../../../../commons/utils/var-utils';
import { GzoomModalComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal.component';
import { GzoomModalHeaderComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-header/gzoom-modal-header.component';
import { GzoomModalBodyComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-body/gzoom-modal-body.component';
import { GzoomFormComponent } from '../../../../layout/gzoom/gzoom-form/gzoom-form.component';
import { GzoomModalFooterComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-footer/gzoom-modal-footer.component';
import { GzoomButtonComponent } from '../../../../layout/gzoom/gzoom-button/gzoom-button.component';
import { FormsModule } from '@angular/forms';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { GoalService } from '../../../../api/service/goal.service';
import { FileSelectEvent, FileUploadModule } from 'primeng/fileupload';
import { FileRemoveEvent } from 'primeng/fileupload/fileupload.interface';
import { Goal } from '../../../../commons/model/goal-file/Goal';
import { Attachment } from '../../../../commons/model/goal-file/content/Attachment';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { FileUtils } from '../../../../commons/utils/fileUtils';
import { PortalPermission } from '../../portal-permissions';

@Component({
  selector: 'gzoom-attachment-modal',
  standalone: true,
  imports: [
    GzoomModalComponent,
    GzoomModalHeaderComponent,
    GzoomModalBodyComponent,
    GzoomFormComponent,
    GzoomModalFooterComponent,
    GzoomButtonComponent,
    FormsModule,
    InputTextareaModule,
    FileUploadModule,
    ConfirmDialogModule
  ],
  templateUrl: './attachment-modal.component.html',
  styleUrls: ['./attachment-modal.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AttachmentModalComponent extends GzoomModalComponent {
  protected goal: Goal = null;

  protected description: string = null;
  protected descriptionMaxLength: number = 255;
  protected attachment: Attachment = null;
  protected file: File = null;
  protected fileMaxSize: number = 10485760; // 10 MB
  protected isSaving: boolean = false;

  protected editEnabled: boolean = false;

  constructor(private confirmationService: ConfirmationService,
              private config: DynamicDialogConfig,
              protected modalDialogRef: DynamicDialogRef,
              protected elementRef: ElementRef,
              private readonly goalService: GoalService,
              private readonly notificationService: NotificationService) {
    super(elementRef, modalDialogRef);
    this.goal = this.config.data?.goal;
    this.editEnabled = PortalPermission.generatePortalPermissions(this.goal?.goalStatus.id)[PortalPermission.AttachmentsWrite]
  }

  protected loadData() {
    this.loading = true;
    this.goalService.getAttachments(this.goal?.id).subscribe(attachments => {
      this.attachment = attachments[0];
      this.description = this.attachment?.description;
      this.loading = false;
    });
  }

  onFileSelect(event: FileSelectEvent) {
    const file = event.files[0];
    if (file && (file.size <= this.fileMaxSize)) {
      this.file = file;
    }
  }

  onFileClear(_: FileRemoveEvent) {
    this.file = null;
  }

  save(): void {
    this.isSaving = true;
    if (VarUtils.isDefined(this.attachment)) {
      const attachment = new Attachment();
      attachment.description = this.description;

      this.goalService.updateAttachment(this.attachment.id, attachment).subscribe({
        next: _ => {
          this.notificationService.handleSuccess('Allegato aggiornato');
          this.closeModal();
        },
        error: _ => {
          this.notificationService.handleError('Non è stato possibile salvare l\'allegato.');
          this.isSaving = false;
        }
      });

    } else {
      const formData = new FormData();
      formData.append('file', this.file);
      formData.append('goalId', this.goal.id);
      formData.append('description', this.description);
      formData.append('contentType', 'DOCUMENTO');
      formData.append('fromDate', DateUtils.dateToISO(this.goal.fromDate));
      formData.append('thruDate', DateUtils.dateToISO(this.goal.thruDate));

      this.goalService.uploadAttachment(formData).subscribe({
        next: _ => {
          this.notificationService.handleSuccess('Allegato salvato');
          this.closeModal();
        },
        error: _ => {
          this.notificationService.handleError('Non è stato possibile salvare l\'allegato.');
          this.isSaving = false;
        }
      });
    }
  }

  confirmDelete(event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Vuoi cancellare l\'allegato?',
      header: 'Richiesta di conferma',
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: 'none',
      acceptLabel: 'Sì',
      rejectIcon: 'none',
      rejectLabel: 'No',
      rejectButtonStyleClass: 'p-button-text',
      accept: () => {
        this.delete();
      },
      reject: () => { }
    });
  }

  delete(): void {
    this.goalService.deleteAttachment(this.attachment.id).subscribe({
      next: _ => {
        this.notificationService.handleSuccess('Allegato cancellato');
        this.closeModal();
      },
      error: _ => {
        this.notificationService.handleError('Non è stato possibile cancellare l\'allegato.');
      }
    });
  }

  download(): void {
    this.goalService.downloadAttachment(this.attachment?.id).subscribe(
      {
        next: response => {
          FileUtils.downloadFile(response);
        },
        error: err => {
          this.notificationService.handleError(err.message);
        }
      });
  }

  protected readonly isDefined = VarUtils.isDefined;
}
