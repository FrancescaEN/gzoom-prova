import {Component, ViewEncapsulation} from '@angular/core';
import {GzoomGenericComponentComponent} from "../../gzoom-generic/gzoom-generic.component";

@Component({
  selector: 'gzoom-modal-footer',
  standalone: true,
  imports: [],
  templateUrl: './gzoom-modal-footer.component.html',
  styleUrl: './gzoom-modal-footer.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class GzoomModalFooterComponent extends GzoomGenericComponentComponent {

}
