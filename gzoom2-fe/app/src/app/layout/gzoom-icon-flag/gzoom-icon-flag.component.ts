import { Component, Input } from '@angular/core';

@Component({
  selector: 'gzoom-icon-flag',
  templateUrl: './gzoom-icon-flag.component.html',
  styleUrls: ['./gzoom-icon-flag.component.scss']
})
export class GzoomIconFlagComponent {
  @Input() flag: 0 | 1 = 0; //0 per la lingua primaria, 1 per la secondaria
}
