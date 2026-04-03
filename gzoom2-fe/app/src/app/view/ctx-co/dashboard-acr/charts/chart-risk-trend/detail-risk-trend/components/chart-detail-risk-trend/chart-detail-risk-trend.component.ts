import { Component, DestroyRef, computed, inject, input, numberAttribute, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { PeriodProcess } from 'app/api/model/dashboards/period';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { setDatasetsColor } from 'app/commons/utils/colorUtils';
import { legendMargin } from 'app/commons/utils/pluginsChartjs';
import { I18NService } from 'app/i18n/i18n.service';
import { ChartData, ChartDataset, ChartOptions, ChartType } from 'chart.js';
import { orderBy, range } from 'lodash';
import { ChartModule } from 'primeng/chart';
import { SkeletonModule } from 'primeng/skeleton';
import { catchError, combineLatest, filter, map, of, switchMap, tap } from 'rxjs';

@Component({
  selector: 'gzoom-chart-detail-risk-trend',
  standalone: true,
  imports: [ChartModule, SkeletonModule],
  templateUrl: './chart-detail-risk-trend.component.html'
})
export class ChartDetailRiskTrendComponent {
  private destroy = inject(DestroyRef);
  i18n = inject(I18NService);
  riskService = inject(RiskService);
  scrollableService = inject(ScrollableTabService);
  //Breakpoint for responsive dashboard
  height = toSignal(this.scrollableService.isScrollableHeight(800).pipe(map(value => value ? '50vh' : '400px')));

  fromYear = input.required({ transform: numberAttribute });
  year = input.required({ transform: numberAttribute });
  areaId = input.required<string>();
  chartTitle = input.required<string>();
  orgUnitId = input<string>();

  secondaryLang = this.i18n.getIsSecondaryLang();
  loading = signal<boolean>(true);

  labels = computed<string[]>(() => {
    if (this.fromYear() && this.year())
      return range(this.fromYear(), this.year() + 1)?.map(n => n.toString())
  });
  plugins = [legendMargin];

  periodProcess = toSignal(
    combineLatest([
      toObservable(this.year),
      toObservable(this.fromYear),
      toObservable(this.areaId),
      toObservable(this.orgUnitId)
    ]).pipe(
      filter(([year, fromYear, areaId]) => !!fromYear && !!year && !!areaId),
      switchMap(([year, fromYear, areaId, orgUnitId]) =>
        this.riskService.getRiskProcessTrendByArea(fromYear, year, areaId, orgUnitId)
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

  datasets = computed<ChartDataset[]>(() => {

    if (this.periodProcess()) {

      const processMap: { [key: string]: { label: string, data: (number | null)[] } } = {};

      this.periodProcess().forEach((period, periodIndex) => {
        const yearStr = period.year.toString();
        period.processes.forEach(process => {

          if (!processMap[process.processId]) {

            processMap[process.processId] = {
              label: process.processEtch ?? (this.secondaryLang ? process.processNameLang : process.processName),
              data: new Array(this.labels().length).fill(null)
            };

          }

          const index = this.labels().findIndex(x => x === yearStr);
          processMap[process.processId].data[index] = process.proScore;
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

          title: {
            text: this.i18n.translate("year"),
            display: true,
            font: {
              size: 9,
              weight: 'bolder'
            }
          }
        },
        y: {
          title: {
            text: this.i18n.translate("proScore"),
            display: true,
            font: {
              size: 9,
              weight: 'bolder'
            }
          },
        }
      },
      hover: {
        includeInvisible: true,
        intersect: false,
        mode: 'dataset'
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
        tooltip: {
          mode: 'point',
          bodyFont: {
            size: 11
          }
        },
        title: {
          display: true,
          text: this.chartTitle(),
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
