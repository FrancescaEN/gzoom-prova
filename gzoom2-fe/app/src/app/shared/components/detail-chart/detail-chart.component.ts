import { Component, input, output } from '@angular/core';
import { CardModule } from 'primeng/card';
import { ButtonBarTableComponent } from 'app/layout/button/button-bar-table/button-bar-table.component';

@Component({
  selector: 'gzoom-detail-chart',
  standalone: true,
  imports: [CardModule, ButtonBarTableComponent],
  templateUrl: './detail-chart.component.html',
  styles: `
  p-card {
    width: 100%;
    height: 100%;
  }
  `
})
export class DetailChartComponent {
  backLabel = input();
  back = output();
}
