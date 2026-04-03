import { Component, Input, OnInit,ViewEncapsulation } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { GzoomButtonComponent } from 'app/layout/gzoom/gzoom-button/gzoom-button.component';
import { I18nModule } from 'app/i18n/i18n.module';
import {FormField} from "../model/FormField";

@Component({
  selector: 'gzoom-textarea',
  standalone: true,
  imports: [
    GzoomButtonComponent,
    InputTextareaModule,
    CommonModule,
    ReactiveFormsModule,
    I18nModule
  ],
  templateUrl: './gzoom-textarea.component.html',
  styleUrl: './gzoom-textarea.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class GzoomTextareaComponent implements OnInit {
  @Input() form!: FormGroup;
  @Input() field!: FormField;

  constructor() { }

  ngOnInit(): void {
    this.form.addControl(this.field.name, new FormControl(this.field.value || '', this.field.validators || []));
    this.form.get(this.field.name)?.valueChanges.subscribe((value) => {
      //console.log(`Value changed for field ${this.field.name}:`, value);
      this.field.value = value;
    });

    if (this.field.disabled) {
      this.form.get(this.field.name)?.disable();
    }
  }

  isStyleObject(style: any): style is { [key: string]: string } {
    return style && typeof style === 'object' && !Array.isArray(style);
  }
}
