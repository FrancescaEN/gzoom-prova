import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, DestroyRef, NgZone, computed, effect, inject, input, numberAttribute, signal } from '@angular/core';
import { takeUntilDestroyed, toObservable, toSignal } from '@angular/core/rxjs-interop';
import { Router, ActivatedRoute } from '@angular/router';
import { Factor } from 'app/api/model/dashboards/factor';
import { Level } from 'app/api/model/dashboards/level';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { setDatasetsColor } from 'app/commons/utils/colorUtils';
import { legendMargin } from 'app/commons/utils/pluginsChartjs';
import { splitStringEveryNum } from 'app/commons/utils/string-utils';
import { I18NService } from 'app/i18n/i18n.service';
import { ChartType, ChartData, ChartOptions, ChartEvent, ActiveElement, Chart, ChartDataset, LegendItem } from 'chart.js';
import { orderBy } from 'lodash';
import { SelectItem } from 'primeng/api';
import { ChartModule } from 'primeng/chart';
import { SkeletonModule } from 'primeng/skeleton';
import { catchError, combineLatest, filter, fromEvent, map, of, switchMap, tap } from 'rxjs';

@Component({
  selector: 'gzoom-chart-state-risk-factors',
  standalone: true,
  imports: [CommonModule, ChartModule, SkeletonModule],
  templateUrl: './chart-state-risk-factors.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ChartStateRiskFactorsComponent implements AfterViewInit {
  private destroy = inject(DestroyRef);

  ngZone = inject(NgZone);
  router = inject(Router);
  i18n = inject(I18NService);
  activatedRoute = inject(ActivatedRoute);
  riskService = inject(RiskService);
  scrollableService = inject(ScrollableTabService);
  height = toSignal(this.scrollableService.isScrollableHeight(500).pipe(map(value => value ? '700px' : '400px')));

  //input
  year = input.required({ transform: numberAttribute });
  fromYear = input.required({ transform: numberAttribute });
  orgUnitId = input<string>();
  isMouseOut = signal<boolean>(false);
  tmpChart: Chart = null;
  cursorPointer = signal<boolean>(true);
  secondaryLang = this.i18n.getIsSecondaryLang();

  //factors = signal<Factor[]>(null);
  loading = signal<boolean>(true);
  plugins = [legendMargin];
  hoverIndex: number = null;

  levels = computed<Level[]>(() => [
    ...new Set(
      this.factors()?.map(x => JSON.stringify(x.factorLevel))
    )]
    .map(x => JSON.parse(x))
  );

  riskIdDatasets = computed<SelectItem[]>(() => this.levels().map(x => ({
    label: this.secondaryLang ? x.levelNameLang : x.levelName,
    value: x.levelId
  })));

  labels = computed<string[]>(() => this.levels()?.map(x => this.secondaryLang ? x.levelNameLang : x.levelName));

  factors = toSignal(
    combineLatest([
      toObservable(this.year),
      toObservable(this.orgUnitId)
    ]).pipe(
      filter(([year]) => !!year),
      switchMap(([year, orgUnitId]) =>
        this.riskService.getRiskFactorsByYear(year, orgUnitId)
          .pipe(
            map(period => period?.factors ?? []),
            tap(() => this.loading.set(false)),
            catchError(() => {
              this.loading.set(false);
              return of([] as Factor[]);
            })
          )
      )
    ),
    {
      initialValue: [] as Factor[]
    }
  );


  datasets = computed<ChartDataset[]>(() => {

    if (this.factors()) {

      const factorMap: { [key: string]: { label: string, data: (number | null)[] } } = {};

      this.factors()
        .forEach((factor) => {
          const levelName = this.secondaryLang ? factor.factorLevel.levelNameLang : factor.factorLevel.levelName;
          if (!factorMap[factor.factorId]) {

            factorMap[factor.factorId] = {
              label: this.secondaryLang ? factor.factorNameLang : factor.factorName,
              data: new Array(this.labels().length).fill(null)
            };
          }
          const index = this.labels().findIndex(x => x === levelName);
          factorMap[factor.factorId].data[index] = factor?.factorPct / 100;

        });
      const dataset = Object.values(factorMap);
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
  options = computed<ChartOptions>(() => {
    return {

      scales: {
        x: {
          stacked: true,
          title: {
            text: this.i18n.translate('proRisk'),
            display: true,
            font: {
              size: 9,
              weight: 'bolder'
            }
          },
          ticks: {
            font: {
              weight: (ctx, options) => {
                const { index } = ctx;
                return this.isMouseOut() ? 'normal' :
                  index == this.hoverIndex ? 'bold' : 'normal';
              }

            }
          }
        },
        y: {
          title: {
            text: splitStringEveryNum(`${this.i18n.translate('fatPerc')}`, 2),
            display: true,
            font: {
              size: 9,
              weight: 'bolder'
            }
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
        mode: 'x'
      },
      elements: {

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
          bodyFont: {
            size: 11
          }
        },

        title: {
          align: 'start',
          display: true,
          text: `${this.i18n.translate('stateRiskFactors')} ${this.i18n.translate('year').toLowerCase()} ${this.year()}`,
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
  });

  ngAfterViewInit(): void {
    const ctx = document.getElementById('stateRiskFactors').getElementsByTagName('canvas');

    if (ctx)
      fromEvent(ctx, 'mouseout')
        .pipe(
          takeUntilDestroyed(this.destroy)
        ).subscribe(x => {
        this.isMouseOut.set(true);
        this.tmpChart?.update();
      })
  }

  generateLabels(chart: Chart): LegendItem[] {
    return chart.data.datasets.map(x => {
      return {
        text: x.label,
        fillStyle: '' + x.backgroundColor,
        strokeStyle: '' + x.borderColor
      }
    })
  }

  onHover(event: ChartEvent, elements: ActiveElement[], chart: Chart) {
    this.ngZone.run(() => {
      if (chart)
        this.tmpChart = chart;

      this.isMouseOut.set(false);
      const isHoverElement = (elements.length > 0);
      this.cursorPointer.set(isHoverElement);
      if (isHoverElement) {

        const element = elements[0];
        const { datasetIndex, index } = element;
        this.hoverIndex = index;

        if (isHoverElement && event.type === 'click') {
          this.goToPreventionTrendDetail(datasetIndex, index);
        }
      } else {
        this.hoverIndex = null;
      }

      this.tmpChart.update();
    })
  }

  goToPreventionTrendDetail(datasetIndex: number, xIndex: number) {
    const { value: riskId } = this.riskIdDatasets()[xIndex];

    const queryParams: any = {
      year: this.year(), fromYear: this.fromYear()
    }
    if (this.orgUnitId() !== null && this.orgUnitId() !== undefined && this.orgUnitId() !== '') {
      queryParams.orgUnitId = this.orgUnitId();
    }

    this.router.navigate([`state-risk-factors/${riskId}`], {
      queryParams: queryParams,
      relativeTo: this.activatedRoute
    })
  }
}
