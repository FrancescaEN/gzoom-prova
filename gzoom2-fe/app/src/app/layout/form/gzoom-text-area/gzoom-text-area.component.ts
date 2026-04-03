import { Component, Input } from '@angular/core';
import { FormGroup, FormGroupDirective } from '@angular/forms';

@Component({
  selector: 'gzoom-text-area',
  templateUrl: './gzoom-text-area.component.html',
  styleUrls: ['./gzoom-text-area.component.css']
})
export class GzoomTextAreaComponent {
  @Input() controlName: string;
  @Input() maxLength: number;

  form: FormGroup;

  constructor(private rootFormGroup: FormGroupDirective) { }

  ngOnInit(): void {
    this.form = this.rootFormGroup.control as FormGroup;
  }

}