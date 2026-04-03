import { Component, Input, input, numberAttribute } from '@angular/core';
import { FormGroup, FormGroupDirective } from '@angular/forms';

@Component({
  selector: 'gzoom-number',
  templateUrl: './gzoom-number.component.html',
  styleUrls: ['./gzoom-number.component.css']
})
export class GzoomNumberComponent {
  controlName = input<string>();
  mode = input<string>();
  minFractionDigits = input(0, { transform: numberAttribute });
  maxFractionDigits = input(null, { transform: numberAttribute });
  min = input(null, { transform: numberAttribute });
  max = input(null, { transform: numberAttribute });

  form: FormGroup;

  constructor(private rootFormGroup: FormGroupDirective) { }

  ngOnInit(): void {
    this.form = this.rootFormGroup.control as FormGroup;
  }

}
