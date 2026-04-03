import { Component, inject, input } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { LayoutModule } from 'app/layout/layout.module';
export interface TableDetailChartHeader {
  labels?: string[],
  keys: string[]
}


@Component({
  selector: 'gzoom-table-detail-chart',
  standalone: true,
  imports: [
    LayoutModule
  ],
  templateUrl: './table-detail-chart.component.html',
  styleUrl: './table-detail-chart.component.scss'
})
export class TableDetailChartComponent {
  item = input.required();
  header = input.required<TableDetailChartHeader>();
  loading = input<boolean>(false);
  scrollableTabService = inject(ScrollableTabService)

  scrollable = toSignal(this.scrollableTabService.isScrollableWidth(960));


  getTypeof(value): string {
    return typeof value;
  }
}
