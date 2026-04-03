import { Component, Input } from '@angular/core';
import { SelectItem } from 'primeng/api';

@Component({
  selector: 'gzoom-label-input-dropdown',
  templateUrl: './gzoom-label-input-dropdown.component.html',
  styleUrls: ['./gzoom-label-input-dropdown.component.css']
})
export class GzoomLabelInputDropdownComponent {
  /*Input gzoom-label-input*/
  @Input() label: string;
  @Input() required: boolean;
  @Input() flag: number;
  @Input() width: string = "100%";

  /*Input gzoom-dropdown*/
  @Input() options: SelectItem[];
  @Input() controlName: string;
  @Input() virtualScroll: boolean = false;
  @Input() virtualScrollItemSize: number = 20;
  @Input() showClear: boolean = false;
  @Input() filter: boolean = true;

}
