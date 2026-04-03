import { ChangeDetectionStrategy, Component, computed, DestroyRef, inject, input, NgZone, numberAttribute, signal } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { ActiveElement, Chart, ChartData, ChartDataset, ChartEvent, ChartOptions, ChartType } from 'chart.js';
import { setDatasetsColor } from 'app/commons/utils/colorUtils';
import { splitStringEveryNum } from 'app/commons/utils/string-utils';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { legendMargin } from 'app/commons/utils/pluginsChartjs';
import { catchError, combineLatest, filter, map, of, switchMap, tap } from 'rxjs';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { PeriodArea } from 'app/api/model/dashboards/period';
import { orderBy } from 'lodash';
import { SkeletonModule } from 'primeng/skeleton';

@Component({
  selector: 'gzoom-chart-prevention-state',
  standalone: true,
  imports: [
    ChartModule,
    CommonModule,
    SkeletonModule
  ],
  templateUrl: './chart-prevention-state.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export default class ChartPreventionStateComponent {
  private destroy = inject(DestroyRef);
  ngZone = inject(NgZone);
  router = inject(Router);
  i18n = inject(I18NService);
  activatedRoute = inject(ActivatedRoute);
  riskService = inject(RiskService);
  secondaryLang = this.i18n.getIsSecondaryLang();
  scrollableService = inject(ScrollableTabService);
  //Breakpoint for responsive dashboard
  height = toSignal(this.scrollableService.isScrollableHeight(800).pipe(map(value => value ? '500px' : '400px')));

  plugins = [legendMargin]

  year = input.required({ transform: numberAttribute });
  orgUnitId = input<string>();
  cursorPointer = signal<boolean>(true);
  loading = signal<boolean>(true);

  //preventionStates = signal<PeriodArea[]>([]);
  labels = computed<string[]>(() => {
    return [this.year()?.toString()]
  });

  preventionStates = toSignal(
    combineLatest([
      toObservable(this.year),
      toObservable(this.orgUnitId)
    ]).pipe(
      filter(([year]) => !!year),
      switchMap(([year, orgUnitId]) =>
        this.riskService.getRiskPreventionStateByYear(year, orgUnitId)
          .pipe(
            tap(() => this.loading.set(false)),
            catchError(() => {
              this.loading.set(false);
              return of([] as PeriodArea[]);
            })
          )
      )
    ),
    {
      initialValue: [] as PeriodArea[]
    }
  );

  datasets = computed<ChartDataset[]>(() => {

    if (this.preventionStates()) {

      const areaMap: { [key: string]: { label: string, data: (number | null)[] } } = {};

      this.preventionStates().forEach((period, periodIndex) => {
        const yearStr = period.year.toString();
        period.areas.forEach(area => {
          // Se l'area non è già presente nella mappa, la inizializziamo
          if (!areaMap[area.areaId]) {

            areaMap[area.areaId] = {
              label: this.secondaryLang ? area.areaNameLang : area.areaName,
              data: new Array(this.labels().length).fill(null)
            };
          }

          const index = this.labels().findIndex(x => x === yearStr);
          areaMap[area.areaId].data[index] = area.areaSummary?.measDeltaPct / 100;
        });
      });
      const dataset = Object.values(areaMap);
      return setDatasetsColor(orderBy(dataset, 'label'), 0.7);
    }
  });

  type: ChartType = 'bar';
  data = computed<ChartData>(() => {
    if (this.labels() && this.datasets())
      return {
        labels: this.labels(),
        datasets: this.datasets()
      }
  });

  options = computed<ChartOptions>(() => (
    {
      plugins: {
        legend: {
          display: true, //Nasconde la leggenda
          position: 'top',
          align: 'start',
          labels: { font: { size: 9 } }
        },
        tooltip: {
          bodyFont: {
            size: 11
          }
        },
        title: {
          align: 'start',
          display: true,
          text: `${this.i18n.translate('preventionState')} ${this.i18n.translate('year').toLowerCase()} ${this.year()}`,
          padding: { top: 5, bottom: 10 },
          font: {
            size: 16
          }
        }
      },
      scales: {
        x: {
          position: 'top',
          title: {
            text: this.i18n.translate('year'),
            display: true,
            font: {
              size: 9,
              weight: 'bolder'
            }
          }
        },
        y: {
          title: {
            text: splitStringEveryNum(this.i18n.translate('misDeltaPerc'), 2),
            display: true,
            font: {
              size: 9,
              weight: 'bolder'
            }
          },
          ticks: {
            format: { style: 'percent', minimumSignificantDigits: 2, maximumSignificantDigits: 2 }
          }
        }
      },
      elements: {
        line: {
          hoverBorderWidth: 5
        },
        point: {
          hoverBorderWidth: 5
        }
      },
      hover: {
        includeInvisible: true,
        intersect: false,
        mode: 'dataset'
      },
      onHover: this.onHover.bind(this)
    }

  ))

  selectData(event) {
    const { element } = event;
    const { datasetIndex, index } = element as ActiveElement;
    this.goToPreventionTrendDetail(datasetIndex, index);
  }

  onHover(event: ChartEvent, elements: ActiveElement[], chart: Chart) {
    this.ngZone.run(() => {
      const isHoverElement = (elements.length > 0);
      this.cursorPointer.set(isHoverElement);

      if (isHoverElement && event.type === 'click') {
        const element = elements.pop();
        const { datasetIndex, index } = element;
        this.goToPreventionTrendDetail(datasetIndex, index);
      }
      ;
    })
  }

  goToPreventionTrendDetail(datasetIndex: number, xIndex: number) {
    const year = this.labels()[xIndex];
    const queryParams: any = {}
    if (this.orgUnitId() !== null && this.orgUnitId() !== undefined && this.orgUnitId() !== '') {
      queryParams.orgUnitId = this.orgUnitId();
    }
    this.router.navigate([`prevention-state/${year}`], { queryParams: queryParams, relativeTo: this.activatedRoute })
  }


}
