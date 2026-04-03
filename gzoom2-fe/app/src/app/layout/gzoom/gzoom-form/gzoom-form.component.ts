import {Component, ViewEncapsulation} from '@angular/core';
import {GzoomGenericComponentComponent} from "../gzoom-generic/gzoom-generic.component";

@Component({
  selector: 'gzoom-form',
  standalone: true,
  imports: [],
  templateUrl: './gzoom-form.component.html',
  styleUrl: './gzoom-form.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class GzoomFormComponent extends GzoomGenericComponentComponent {

}
