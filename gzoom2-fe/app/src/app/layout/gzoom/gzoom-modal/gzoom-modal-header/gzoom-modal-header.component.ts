import {Component, ViewEncapsulation} from '@angular/core';
import {GzoomGenericComponentComponent} from "../../gzoom-generic/gzoom-generic.component";

@Component({
  selector: 'gzoom-modal-header',
  standalone: true,
  templateUrl: './gzoom-modal-header.component.html',
  styleUrl: './gzoom-modal-header.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class GzoomModalHeaderComponent extends GzoomGenericComponentComponent {

}
