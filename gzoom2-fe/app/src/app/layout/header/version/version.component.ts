import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ApplicationVersion } from 'app/commons/model/config';
import { DefaultOrderKeyValuePipe } from 'app/commons/pipe/default-order-key-value.pipe';
import { DividerModule } from 'primeng/divider';
import { DynamicDialogConfig } from 'primeng/dynamicdialog';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';

@Component({
  selector: 'gzoom-version',
  standalone: true,
  imports: [DividerModule, TableModule, TagModule, CommonModule, DefaultOrderKeyValuePipe],
  templateUrl: './version.component.html'
})
export class VersionComponent {

  public dialogConfig = inject(DynamicDialogConfig);

  versions = signal<ApplicationVersion[]>(this.dialogConfig.data.versions);
}
