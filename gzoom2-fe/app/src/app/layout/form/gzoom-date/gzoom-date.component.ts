import { Component, Input } from '@angular/core';
import { FormGroup, FormGroupDirective } from '@angular/forms';

@Component({
  selector: 'gzoom-date',
  templateUrl: './gzoom-date.component.html',
  styleUrls: ['./gzoom-date.component.scss']
})
export class GzoomDateComponent {
  @Input() controlName: string;
  @Input() minDate: Date;
  @Input() maxDate: Date;
  @Input() showClear: boolean;

  form: FormGroup;

  constructor(private rootFormGroup: FormGroupDirective) { }

  ngOnInit(): void {
    this.form = this.rootFormGroup.control as FormGroup;
  }

}
