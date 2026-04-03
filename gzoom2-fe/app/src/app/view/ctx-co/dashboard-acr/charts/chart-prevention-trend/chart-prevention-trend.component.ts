import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, DestroyRef, NgZone, computed, inject, input, numberAttribute, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { PeriodArea } from 'app/api/model/dashboards/period';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { setDatasetsColor } from 'app/commons/utils/colorUtils';
import { legendMargin } from 'app/commons/utils/pluginsChartjs';
import { splitStringEveryNum } from 'app/commons/utils/string-utils';
import { I18NService } from 'app/i18n/i18n.service';
import { ActiveElement, Chart, ChartData, ChartDataset, ChartEvent, ChartOptions, ChartType } from 'chart.js';
import { range } from 'lodash';
import { ChartModule } from 'primeng/chart';
import { SkeletonModule } from 'primeng/skeleton';
import { catchError, combineLatest, filter, map, of, switchMap, tap } from 'rxjs';

@Component({
  selector: '' +
    'gzoom-chart-prevention-trend',
  standalone: true,
  imports: [CommonModule, ChartModule, SkeletonModule],
  templateUrl: './chart-prevention-trend.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export default class ChartPreventionTrendComponent {
  private destroy = inject(DestroyRef);
  ngZone = inject(NgZone);
  router = inject(Router);
  i18n = inject(I18NService);
  activatedRoute = inject(ActivatedRoute);
  riskService = inject(RiskService);
  scrollableService = inject(ScrollableTabService);
  //Breakpoint for responsive dashboard
  height = toSignal(this.scrollableService.isScrollableHeight(800).pipe(map(value => value ? '500px' : '400px')));
  loading = signal<boolean>(true);
  plugins = [legendMargin]

  //input
  fromYear = input.required({ transform: numberAttribute });
  year = input.required({ transform: numberAttribute });
  orgUnitId = input<string>();
  cursorPointer = signal<boolean>(true);
  secondaryLang = this.i18n.getIsSecondaryLang();

  //periodArea = signal<PeriodArea[]>(null);
  labels = computed<string[]>(() => {
    if (this.fromYear() && this.year())
      return range(this.fromYear(), this.year() + 1)?.map(n => n.toString())
  });
  areaIdForDataset: string[] = [];
  goToRiskTrendState = false;

  periodArea = toSignal(
    combineLatest([
      toObservable(this.year),
      toObservable(this.fromYear),
      toObservable(this.orgUnitId)
    ]).pipe(
      filter(([year, fromYear]) => !!fromYear && !!year),
      switchMap(([year, fromYear, orgUnitId]) =>
        this.riskService.getRiskPreventionTrend(fromYear, year, orgUnitId)
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

    if (this.periodArea()) {
      this.areaIdForDataset = [];
      const areaMap: { [key: string]: { label: string, data: (number | null)[] } } = {};

      this.periodArea().forEach((period, periodIndex) => {
        const yearStr = period.year.toString();
        period.areas.forEach(area => {
          // Se l'area non è già presente nella mappa, la inizializziamo
          if (!areaMap[area.areaId]) {

            areaMap[area.areaId] = {
              label: this.secondaryLang ? area.areaNameLang : area.areaName,
              data: new Array(this.labels().length).fill(null)
            };
            this.areaIdForDataset.push(area.areaId);
          }

          const index = this.labels().findIndex(x => x === yearStr);
          areaMap[area.areaId].data[index] = area.areaSummary?.measDeltaPct / 100;
        });
      });
      const dataset = this.areaIdForDataset.map(key => areaMap[key]);
      return setDatasetsColor(dataset);
    }
  });

  type: ChartType = 'line';
  data = computed<ChartData>(() => {
    if (this.labels() && this.datasets())
      return {
        labels: this.labels(),
        datasets: this.datasets()
      }
  });

  options: ChartOptions = {
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
        labels: {
          font: {
            size: 9
          }
        }
      },

      tooltip: {
        mode: 'point',
        bodyFont: {
          size: 11
        }
      },
      title: {
        align: 'start',
        display: true,
        text: this.i18n.translate('preventionTrend'),
        padding: { top: 5, bottom: 10 },
        font: {
          size: 16
        }
      },
      colors: {
        enabled: false,
        forceOverride: true
      }
    },
    onHover: this.onHover.bind(this)
  }

  onHover(event: ChartEvent, elements: ActiveElement[], chart: Chart) {
    this.ngZone.run(() => {
      const isHoverElement = (elements.length > 0);
      this.cursorPointer.set(isHoverElement);

      if (!this.goToRiskTrendState && isHoverElement && event.type === 'click') {
        const element = elements.pop();
        const { datasetIndex, index } = element;
        this.goToPreventionTrendDetail(datasetIndex, index);
      }
      ;
    })
  }

  selectData(event) {
    const { element } = event;
    const { datasetIndex, index } = element as ActiveElement;
    this.goToPreventionTrendDetail(datasetIndex, index);
  }

  goToPreventionTrendDetail(datasetIndex: number, xIndex: number) {
    this.goToRiskTrendState = true;
    const areaId = this.areaIdForDataset[datasetIndex];
    const year = this.labels()[xIndex];
    console.log('goToPreventionTrendDetail organizationUnitId:')
    console.log(this.orgUnitId())

    const queryParams: any = {
      year, fromYear: this.fromYear()
    }
    if (this.orgUnitId() !== null && this.orgUnitId() !== undefined && this.orgUnitId() !== '') {
      queryParams.orgUnitId = this.orgUnitId();
    }

    this.router.navigate([`prevention-trend/${areaId}`],
      { queryParams: queryParams, relativeTo: this.activatedRoute })
  }
}
