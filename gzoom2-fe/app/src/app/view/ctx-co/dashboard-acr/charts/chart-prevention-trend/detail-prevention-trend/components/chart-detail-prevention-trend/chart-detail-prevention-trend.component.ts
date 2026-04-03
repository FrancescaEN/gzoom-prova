import { Component, DestroyRef, computed, inject, input, numberAttribute, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { PeriodProcess } from 'app/api/model/dashboards/period';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { setDatasetsColor } from 'app/commons/utils/colorUtils';
import { legendMargin } from 'app/commons/utils/pluginsChartjs';
import { I18NService } from 'app/i18n/i18n.service';
import { ChartDataset, ChartType, ChartOptions, ChartData } from 'chart.js';
import { orderBy, range } from 'lodash';
import { ChartModule } from 'primeng/chart';
import { ProgressBarModule } from 'primeng/progressbar';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { SkeletonModule } from 'primeng/skeleton';
import { catchError, combineLatest, filter, finalize, map, of, switchMap, tap, timeout } from 'rxjs';

@Component({
  selector: 'gzoom-chart-detail-prevention-trend',
  standalone: true,
  imports: [ChartModule, SkeletonModule, ProgressBarModule, ProgressSpinnerModule],
  templateUrl: './chart-detail-prevention-trend.component.html',
})
export class ChartDetailPreventionTrendComponent {
  private destroy = inject(DestroyRef);
  i18n = inject(I18NService);
  riskService = inject(RiskService);
  scrollableService = inject(ScrollableTabService);
  //Breakpoint for responsive dashboard
  height = toSignal(this.scrollableService.isScrollableHeight(800).pipe(map(value => value ? '50vh' : '400px')));

  fromYear = input.required({ transform: numberAttribute });
  year = input.required({ transform: numberAttribute });
  areaId = input.required<string>();
  orgUnitId = input<string>();

  secondaryLang = this.i18n.getIsSecondaryLang();

  preventionTrend = toSignal(
      combineLatest([
        toObservable(this.year),
        toObservable(this.fromYear),
        toObservable(this.areaId),
        toObservable(this.orgUnitId)
      ]).pipe(
        filter(([year, fromYear, areaId]) => !!fromYear && !!year && !!areaId),
        switchMap(([year, fromYear, areaId, orgUnitId]) =>
          this.riskService.getRiskPreventionTrendProcess(fromYear, year, areaId, orgUnitId)
            .pipe(
              tap(() => this.loading.set(false)),
              catchError(() => {
                this.loading.set(false);
                return of([] as PeriodProcess[]);
              })
            )
        )
      ),
      { 
        initialValue: [] as PeriodProcess[],
      }
    );

  chartTitle = input.required<string>();
  plugins = [legendMargin]

  labels = computed<string[]>(() => {
    if (this.fromYear() && this.year())
      return range(this.fromYear(), this.year() + 1)?.map(n => n.toString())
  });

  loading = signal(true);

  datasets = computed<ChartDataset[]>(() => {

    if (this.preventionTrend()) {

      const processMap: { [key: string]: { label: string, data: (number | null)[] } } = {};

      this.preventionTrend().forEach((period, periodIndex) => {
        const yearStr = period.year.toString();
        period.processes.forEach(process => {

          if (!processMap[process.processId]) {

            processMap[process.processId] = {
              label: process.processEtch ?? (this.secondaryLang ? process.processNameLang : process.processName),
              data: new Array(this.labels().length).fill(null)
            };

          }

          const index = this.labels().findIndex(x => x === yearStr);
          processMap[process.processId].data[index] = process.processSummary?.measNotImplemented;
        });
      });
      const dataset = Object.values(processMap);
      return setDatasetsColor(orderBy(dataset, "label"));
    }
  });

  type: ChartType = 'line';
  options = computed<ChartOptions>(() => {
    return {
      scales: {
        x: {
          position: 'top',
          title: {
            text: this.i18n.translate("year"),
            display: true,
            font: { size: 10, weight: 'bolder' }
          }
        },
        y: {
          title: {
            text: this.i18n.translate("misNoAttuate"),
            display: true,
            font: { size: 10, weight: 'bolder' }
          }
        },
      },
      hover: {
        includeInvisible: true,
        intersect: false,
        mode: 'dataset',
      },
      elements: {
        line: {
          hoverBorderWidth: 5
        },
        point: {
          hoverBorderWidth: 5
        }
      },
      plugins: {
        legend: {
          position: 'top',
          align: 'start',
          labels: { font: { size: 10 } }
        },
        tooltip: { bodyFont: { size: 11 } },
        title: {
          display: true,
          text: `${this.i18n.translate('preventionTrendArea')} ${(this.chartTitle() ?? '')}`,
          align: 'start',
          padding: { top: 5, bottom: 10 },
          font: { size: 16 }
        },
        colors: {
          enabled: false,
          forceOverride: true
        }
      }
    }

  });

  data = computed<ChartData>(() => {
    if (this.labels() && this.datasets()) {
      return {
        labels: this.labels(),
        datasets: this.datasets(),
      }
    }
  });
}
