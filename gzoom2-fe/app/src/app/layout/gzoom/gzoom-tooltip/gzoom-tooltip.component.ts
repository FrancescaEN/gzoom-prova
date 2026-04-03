import {Component, ViewEncapsulation, input} from '@angular/core';
import {Button} from "primeng/button";
import {TooltipModule} from "primeng/tooltip";
import {NgStyle} from "@angular/common";
import {GzoomGenericComponentComponent} from "../gzoom-generic/gzoom-generic.component";

@Component({
  selector: 'gzoom-tooltip',
  standalone: true,
  imports: [
    Button,
    TooltipModule,
    NgStyle
  ],
  templateUrl: './gzoom-tooltip.component.html',
  styleUrl: './gzoom-tooltip.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class GzoomTooltipComponent extends GzoomGenericComponentComponent {
  text = input<string>();
  textPosition = input<'left' | 'right'>('left');
}
