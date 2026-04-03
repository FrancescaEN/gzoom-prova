import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

import { GzoomLabelComponent } from 'app/layout/gzoom/gzoom-label/gzoom-label.component';

import { CommonModule } from '@angular/common';
import { DynamicFormHelperService } from '../dynamic-form-helper.service';
import { Subject, Subscription } from 'rxjs';
import {GzoomTextareaComponent} from "../gzoom-textarea/gzoom-textarea.component";
import {GzoomButtonComponent} from "../../gzoom-button/gzoom-button.component";
import {FormField} from "../model/FormField";
import {FormFieldGroup} from "../model/FormFieldGroup";
import {FieldType} from "../model/enum/FieldType";

@Component({
  selector: 'gzoom-form',
  standalone: true,
  // todo check imports
  imports: [
    // GzoomButtonComponent,
    CommonModule,
    //GzoomLabelComponent,
    ReactiveFormsModule,
    // DynamicInputTextComponent,
    // DynamicInputNumberComponent,
    // DynamicDropdownComponent,
    // DynamicInputDateComponent,
    // DynamicInputRadioComponent,
    GzoomTextareaComponent
  ],
  templateUrl: './gzoom-form.component.html',
  styleUrl: './gzoom-form.component.scss'
})
export class GzoomFormComponent implements OnInit, OnDestroy {
  @Input() formFields: FormField[] = [];
//
  @Input() columns: number[] = [1];
  @Input() formFieldGroups: FormFieldGroup[] = [];

  @Input() layout: 'single_col' | 'double_col' ; // todo enum

  @Input() handleInsideButtons: boolean = false;
  @Output() formChange: EventEmitter<FormGroup> = new EventEmitter<FormGroup>();

  @Input() index : any;

  @Input() form: FormGroup = new FormGroup({});

  resetSubscription: Subscription;

  constructor(private _helperService: DynamicFormHelperService) {
    this.form.valueChanges.subscribe((value) => {
      this.formChange.emit(this.form);
    });
  }

  ngOnInit(): void {
    this.resetSubscription = this._helperService.resetFormSubject$.subscribe(() => {
      this.form.reset();
    });
  }

  ngOnDestroy(): void {
    // necessary for avoiding modals errors
    this.resetSubscription.unsubscribe();
  }

  protected readonly FieldType = FieldType;
}

