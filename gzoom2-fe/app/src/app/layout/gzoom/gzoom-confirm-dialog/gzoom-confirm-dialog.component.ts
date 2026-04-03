import { Component, ViewEncapsulation } from '@angular/core';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { GzoomGenericComponentComponent } from '../gzoom-generic/gzoom-generic.component';

@Component({
  selector: 'gzoom-confirm-dialog',
  standalone: true,
  imports: [
    ConfirmDialogModule
  ],
  templateUrl: './gzoom-confirm-dialog.component.html',
  styleUrl: './gzoom-confirm-dialog.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class GzoomConfirmDialogComponent extends GzoomGenericComponentComponent {

}
