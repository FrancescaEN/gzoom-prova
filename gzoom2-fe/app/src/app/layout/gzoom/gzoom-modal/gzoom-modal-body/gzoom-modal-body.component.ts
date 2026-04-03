import {Component, ViewEncapsulation} from '@angular/core';
import {GzoomGenericComponentComponent} from "../../gzoom-generic/gzoom-generic.component";

@Component({
  selector: 'gzoom-modal-body',
  standalone: true,
  imports: [],
  templateUrl: './gzoom-modal-body.component.html',
  styleUrl: './gzoom-modal-body.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class GzoomModalBodyComponent extends GzoomGenericComponentComponent {

}
