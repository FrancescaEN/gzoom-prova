import { Component, Input } from '@angular/core';
import { SelectItem } from 'primeng/api';

@Component({
  selector: 'gzoom-chip-filter',
  template: `
      <div
      *ngIf="chipFilter.length > 0"
      style="display: flex; flex-direction: row; gap: 5px; flex-wrap: wrap"
    >
      <ng-template ngFor let-item [ngForOf]="chipFilter" let-i="index">
        <p-chip>
          <div
            style="
              height: 25px;
              display: flex;
              flex-direction: row;
              align-items: center;
            "
          >
            <b>{{ item.label }}:</b> &nbsp;{{ item.value }}
          </div></p-chip
        >
      </ng-template>
    </div>
`,
})
export class GzoomChipFilterComponent {
  @Input() chipFilter: SelectItem<string>[] = [];
}
