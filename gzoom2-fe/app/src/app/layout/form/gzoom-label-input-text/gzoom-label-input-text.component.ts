import { Component, Input } from '@angular/core';

@Component({
  selector: 'gzoom-label-input-text',
  templateUrl: './gzoom-label-input-text.component.html',
  styleUrls: ['./gzoom-label-input-text.component.css']
})
export class GzoomLabelInputTextComponent {

  @Input() width: string = '240px;'
  /*Input gzoom-label-input*/
  @Input() label: string;
  @Input() required: boolean;
  @Input() flag: number;

  /*Input gzoom-text*/
  @Input() controlName: string;
  @Input() maxlength: string;

}
