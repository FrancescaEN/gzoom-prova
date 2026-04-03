import { Component, Input } from '@angular/core';

@Component({
  selector: 'gzoom-label-input',
  templateUrl: './gzoom-label-input.component.html',
  styleUrls: ['./gzoom-label-input.component.css']
})
export class GzoomLabelInputComponent {
  @Input() label: string;
  @Input() required: boolean = false;
  @Input() flag: number;

}
