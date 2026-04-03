import { Component, Input } from '@angular/core';

@Component({
  selector: 'gzoom-label-input-number',
  templateUrl: './gzoom-label-input-number.component.html',
  styleUrls: ['./gzoom-label-input-number.component.css']
})
export class GzoomLabelInputNumberComponent {
  @Input() width: string = '240px;'
  /*Input gzoom-label-input*/
  @Input() label: string;
  @Input() required: boolean;
  @Input() flag: number;

  /*Input gzoom-number*/
  @Input() mode: string;
  @Input() minFractionDigits: number;
  @Input() controlName: string;
}
