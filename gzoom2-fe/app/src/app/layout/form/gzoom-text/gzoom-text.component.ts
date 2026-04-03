import { Component, Input } from '@angular/core';
import { FormGroup, FormGroupDirective } from '@angular/forms';

@Component({
  selector: 'gzoom-text',
  templateUrl: './gzoom-text.component.html',
  styleUrls: ['./gzoom-text.component.css']
})
export class GzoomTextComponent {
  @Input() controlName: string;
  @Input() maxlength: number;

  form: FormGroup;

  constructor(private rootFormGroup: FormGroupDirective) { }

  ngOnInit(): void {
    this.form = this.rootFormGroup.control as FormGroup;
  }

}
