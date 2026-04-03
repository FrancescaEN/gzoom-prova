import { Component, DestroyRef, computed, effect, inject, input, numberAttribute, signal } from '@angular/core';
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
import { SkeletonModule } from 'primeng/skeleton';
import { catchError, combineLatest, filter, finalize, map, of, switchMap, tap } from 'rxjs';

@Component({
  selector: 'gzoom-chart-detail-state-risk-factors',
  standalone: true,
  imports: [ChartModule, SkeletonModule],
  templateUrl: './chart-detail-state-risk-factors.component.html',
})
export class ChartDetailStateRiskFactorsComponent {
  private destroy = inject(DestroyRef);
  i18n = inject(I18NService);
  riskService = inject(RiskService);
  scrollableService = inject(ScrollableTabService);
  //Breakpoint for responsive dashboard
  height = toSignal(this.scrollableService.isScrollableHeight(800).pipe(map(value => value ? '50vh' : '400px')));

  riskLabel = input<string>();

  secondaryLang = this.i18n.getIsSecondaryLang();
  loading = signal<boolean>(true);
  year = input.required({ transform: numberAttribute });
  fromYear = input.required({ transform: numberAttribute });
  levelId = input.required({ transform: numberAttribute });
  orgUnitId = input<string>();

  chartTitle = signal<string>(null);

  labels = computed<string[]>(() => {
    if (this.fromYear() && this.year())
      return range(this.fromYear(), this.year() + 1)?.map(n => n.toString())
  });
  plugins = [legendMargin];

  factorsTrend = toSignal(
      combineLatest([
        toObservable(this.year),
        toObservable(this.fromYear),
        toObservable(this.levelId),
        toObservable(this.orgUnitId)
      ]).pipe(
        filter(([year, fromYear, levelId]) => !!fromYear && !!year && !!levelId),
        switchMap(([year, fromYear, levelId, orgUnitId]) =>
          this.riskService.getRiskFactorsTrendByLevelId(fromYear, year, levelId, orgUnitId)
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

    if (this.factorsTrend()) {

      const factorMap: { [key: string]: { label: string, data: (number | null)[] } } = {};

      this.factorsTrend().forEach((period, periodIndex) => {
        const yearStr = period.year.toString();
        period.factors.forEach(factor => {

          if (!factorMap[factor.factorId]) {

            factorMap[factor.factorId] = {
              label: this.secondaryLang ? factor.factorNameLang : factor.factorName,
              data: new Array(this.labels().length).fill(null)
            };
          }

          const index = this.labels().findIndex(x => x === yearStr);
          factorMap[factor.factorId].data[index] = factor.factorPct / 100;
        });
      });
      const dataset = Object.values(factorMap);
      return setDatasetsColor(orderBy(dataset, "label"), 0.7);
    }
  });


  type: ChartType = 'bar';
  options = computed<ChartOptions>(() => {
    return {
      scales: {
        x: {
          stacked: true,
          title: {
            text: this.i18n.translate("year"),
            display: true,
            font: { size: 10, weight: 'bolder' }
          }
        },
        y: {
          title: {
            text: this.i18n.translate("fatPerc"),
            display: true,
            font: { size: 10, weight: 'bolder' }
          },
          ticks: {
            format: { style: 'percent', minimumSignificantDigits: 2, maximumSignificantDigits: 2 }
          },
          min: 0,
          max: 1,
          stacked: true
        }
      },
      hover: {
        includeInvisible: true,
        intersect: false,
        mode: 'x',
      },
      plugins: {
        legend: {
          position: 'top',
          align: 'start',
          labels: { font: { size: 10 } }
        },
        tooltip: {
          bodyFont: {
            size: 11
          }
        },
        title: {
          display: true,
          text: `${this.i18n.translate('trendRiskFactors')} - ${this.i18n.translate('proRisk')} ${(this.riskLabel() ?? '')}`,
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

  dataChart = computed<ChartData>(() => {
    if (this.labels() && this.datasets()) {
      return {
        labels: this.labels(),
        datasets: this.datasets(),
      }
    }
  });

}
