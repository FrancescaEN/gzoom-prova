/* eslint-disable @angular-eslint/use-lifecycle-interface */
import { NgClass } from '@angular/common';
import { Component, ElementRef, ViewEncapsulation } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { MessageLevel } from '../../../../api/service/goal-file/dto/enum/message-level';
import { ResultMessage } from '../../../../api/service/goal-file/dto/results/result-message';
import { StringUtils } from '../../../../commons/utils/string-utils';
import { I18nModule } from '../../../../i18n/i18n.module';
import { GzoomButtonComponent } from '../../../../layout/gzoom/gzoom-button/gzoom-button.component';
import { GzoomModalBodyComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-body/gzoom-modal-body.component';
import { GzoomModalFooterComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-footer/gzoom-modal-footer.component';
import { GzoomModalHeaderComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal-header/gzoom-modal-header.component';
import { GzoomModalComponent } from '../../../../layout/gzoom/gzoom-modal/gzoom-modal.component';

@Component({
  selector: 'gzoom-result-message-modal',
  standalone: true,
  imports: [
    GzoomButtonComponent,
    GzoomModalBodyComponent,
    GzoomModalComponent,
    GzoomModalFooterComponent,
    GzoomModalHeaderComponent,
    InputTextareaModule,
    ReactiveFormsModule,
    I18nModule,
    NgClass
  ],
  templateUrl: './result-message-modal.component.html',
  styleUrl: './result-message-modal.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ResultMessageModalComponent extends GzoomModalComponent {
  protected messages: ResultMessage[];

  constructor(protected config: DynamicDialogConfig,
              protected elementRef: ElementRef,
              protected modalDialogRef: DynamicDialogRef) {
    super(elementRef, modalDialogRef);
    this.messages = [...this.config.data?.messages];
  }

  protected readonly StringUtils = StringUtils;
  protected readonly MessageLevel = MessageLevel;
}
