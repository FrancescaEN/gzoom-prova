import { Component, Input } from '@angular/core';

@Component({
  selector: 'gzoom-label-input-date',
  templateUrl: './gzoom-label-input-date.component.html',
  styleUrls: ['./gzoom-label-input-date.component.css']
})
export class GzoomLabelInputDateComponent {
  /*Input gzoom-label-input*/
  @Input() label: string;
  @Input() required: boolean;
  @Input() flag: number;

  /*Input gzoom-date*/
  @Input() controlName: string;
  @Input() minDate: Date;
  @Input() maxDate: Date;
  @Input() showClear: boolean;
}
