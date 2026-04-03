import { Component, ElementRef, ViewEncapsulation } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { GoalService } from '../../../../api/service/goal.service';
import { Goal } from '../../../../commons/model/goal-file/Goal';
import { Indicator } from '../../../../commons/model/goal-file/indicator/Indicator';
import { Note } from '../../../../commons/model/goal-file/note/Note';
import { StringUtils } from '../../../../commons/utils/string-utils';
import { GzoomButtonComponent } from '../../../../layout/gzoom/gzoom-button/gzoom-button.component';
import { GzoomFormComponent } from '../../../../layout/gzoom/gzoom-form/gzoom-form.component';
import { GzoomModalBodyComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-body/gzoom-modal-body.component';
import { GzoomModalFooterComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-footer/gzoom-modal-footer.component';
import { GzoomModalHeaderComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-header/gzoom-modal-header.component';
import { GzoomModalComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal.component';

@Component({
  selector: 'gzoom-conciliation-modal',
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
  templateUrl: './conciliation-modal.component.html',
  styleUrl: './conciliation-modal.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ConciliationModalComponent extends GzoomModalComponent {
  private goal: Goal = null;

  protected motivation: Note = null;
  protected response: Note = null;
  protected outcome: Indicator = null;
  protected outcomeValue: string = null;

  constructor(private config: DynamicDialogConfig,
              protected modalDialogRef: DynamicDialogRef,
              protected elementRef: ElementRef,
              private readonly goalService: GoalService) {
    super(elementRef, modalDialogRef);
    this.goal = this.config.data?.goal;
  }

  protected loadData() {
    this.goalService.getNotes(this.goal?.id).subscribe(notes => {
      this.motivation = notes.find(note => note.sequenceId === 700);
      this.response = notes.find(note => note.sequenceId === 800);
      this.outcome = this.goal.indicators?.find(i => i.accountCode === 'ESITO_CONCI');
      this.outcomeValue = this.outcome?.value?.[0]?.goalIndicator?.valueCode;
      this.loading = false;
    });
  }

  protected readonly StringUtils = StringUtils;
}
