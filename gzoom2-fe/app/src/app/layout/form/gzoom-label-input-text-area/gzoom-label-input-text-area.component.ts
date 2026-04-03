import { Component, Input } from '@angular/core';

@Component({
  selector: 'gzoom-label-input-text-area',
  templateUrl: './gzoom-label-input-text-area.component.html',
  styleUrls: ['./gzoom-label-input-text-area.component.css']
})
export class GzoomLabelInputTextAreaComponent {

  @Input() minWidth: string = '300px';

  /*Input gzoom-label-input*/
  @Input() label: string;
  @Input() required: boolean;
  @Input() flag: number;

  /*Input gzoom-text-area*/
  @Input() controlName: string;
  @Input() maxLength: number;

}
