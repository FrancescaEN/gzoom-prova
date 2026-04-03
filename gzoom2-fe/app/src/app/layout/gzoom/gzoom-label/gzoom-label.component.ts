import {Component, ViewEncapsulation, booleanAttribute, input} from '@angular/core';
import {GzoomGenericComponentComponent} from "../gzoom-generic/gzoom-generic.component";
import {I18nModule} from "../../../i18n/i18n.module";

@Component({
  selector: 'gzoom-label',
  standalone: true,
  templateUrl: './gzoom-label.component.html',
  styleUrl: './gzoom-label.component.scss',
  imports: [
    I18nModule
  ],
  encapsulation: ViewEncapsulation.None
})
export class GzoomLabelComponent extends GzoomGenericComponentComponent {
  label = input<string>();
  for = input<string>();
  colon = input(true, {transform: booleanAttribute});
}
/*
Usage:
<gzoom-label [label]="'Gestione schede'"></gzoom-label>
<gzoom-label [label]="'Gestione schede'" [style]="{'font-size': '40px', 'color':'#808'}"></gzoom-label>
 */
