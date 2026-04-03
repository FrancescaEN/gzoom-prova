import { Component, Input, booleanAttribute, input, numberAttribute } from '@angular/core';
import {NgClass, NgIf} from "@angular/common";
import {I18nModule} from "../../../i18n/i18n.module";

@Component({
  selector: 'gzoom-label',
  templateUrl: './gzoom-label.component.html',
  standalone: true,
  imports: [
    NgClass,
    I18nModule,
    NgIf
  ],
  styleUrls: ['./gzoom-label.component.scss']
})
export class GzoomLabelComponent {
  @Input() label: string;
  @Input() labelPos: 'left' | 'top' = 'top';
  @Input({ transform: booleanAttribute }) required: boolean = false;
  @Input({ transform: numberAttribute }) flag: number;
  twoPoints = input(false, { transform: booleanAttribute });

}
