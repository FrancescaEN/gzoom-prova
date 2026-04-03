import { Component, ElementRef, ViewEncapsulation } from '@angular/core';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { Goal } from '../../../../commons/model/goal-file/Goal';
import { GzoomModalHeaderComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-header/gzoom-modal-header.component';
import { GzoomModalBodyComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-body/gzoom-modal-body.component';
import { GzoomButtonComponent } from '../../../../layout/gzoom/gzoom-button/gzoom-button.component';
import { GzoomModalFooterComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-footer/gzoom-modal-footer.component';
import { DialogService, DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { GoalService } from '../../../../api/service/goal.service';
import { GzoomModalComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal.component';
import { PortalPermission } from '../../portal-permissions';
import { DataIntegrationModalComponent } from '../data-integration-modal/data-integration-modal.component';
import { Note } from '../../../../commons/model/goal-file/note/Note';
import { NgClass } from '@angular/common';
import { StringUtils } from '../../../../commons/utils/string-utils';

@Component({
  selector: 'gzoom-context-data-modal',
  standalone: true,
  imports: [
    ProgressSpinnerModule,
    ScrollPanelModule,
    GzoomModalHeaderComponent,
    GzoomModalBodyComponent,
    GzoomButtonComponent,
    GzoomModalFooterComponent,
    GzoomModalComponent,
    NgClass
  ],
  templateUrl: './context-data-modal.component.html',
  styleUrls: ['./context-data-modal.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContextDataModalComponent extends GzoomModalComponent {
  private readonly goal: Goal = null;

  private notes: Note[] = [];
  protected noteNames: string[] = [];
  protected integrationNote: Note = null;

  private dataIntegrationModal: DynamicDialogRef | undefined;

  protected editEnabled: boolean = false;

  constructor(private dialogService: DialogService,
              private config: DynamicDialogConfig,
              protected modalDialogRef: DynamicDialogRef,
              protected elementRef: ElementRef,
              private readonly goalService: GoalService) {
    super(elementRef, modalDialogRef);
    this.goal = this.config.data?.goal;
    this.editEnabled = PortalPermission.generatePortalPermissions(this.goal?.goalStatus.id)[PortalPermission.IntegrationNoteWrite]
  }

  protected loadData() {
    this.goalService.getNotes(this.goal.id).subscribe(notes => {
      this.notes = notes;

      // leaving unique titles in order to manage opportunity and obstacle notes:
      this.noteNames = [...new Set(
        notes
          .filter(note => note.sequenceId < 100)
          .map(note => note.name)
          .filter(name => name !== 'Integrazione dati')
          .map(name => name.endsWith('(opportunità)')
            ? StringUtils.replaceLast(name, '(opportunità)', '').trim()
            : (name.endsWith('(vincoli)')
              ? StringUtils.replaceLast(name, '(vincoli)', '').trim()
              : name))
      )];

      this.integrationNote = notes.find(note => note.name === 'Integrazione dati'); // needed for the button banner
      this.loading = false;
    });
  }

  opportunityNoteInfo(name: string): string {
    return this.noteInfo(name, '(opportunità)');
  }

  obstacleNoteInfo(name: string): string {
    return this.noteInfo(name, '(vincoli)');
  }

  private noteInfo(name: string, noteType: string): string {
    const noteInfo = this.notes.find(note => note.name.startsWith(name) && note.name.endsWith(noteType))?.info;
    return StringUtils.isBlank(noteInfo) ? 'Dato non disponibile' : noteInfo;
  }

  openDataIntegrationModal(): void {
    this.dataIntegrationModal = this.dialogService.open(DataIntegrationModalComponent, {
      data: {
        goal: this.goal
      },
      styleClass: 'gzoom-modal gzoom-portal'
    });
    this.dataIntegrationModal.onClose.subscribe((_) => {
      this.loadData();
    });
  }
}
