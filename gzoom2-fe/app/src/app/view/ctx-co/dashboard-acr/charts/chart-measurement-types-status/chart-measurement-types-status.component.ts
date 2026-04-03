import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, DestroyRef, NgZone, computed, inject, input, numberAttribute, signal } from '@angular/core';
import { takeUntilDestroyed, toObservable, toSignal } from '@angular/core/rxjs-interop';
import { Router, ActivatedRoute } from '@angular/router';
import { Level } from 'app/api/model/dashboards/level';
import { MeasureType } from 'app/api/model/dashboards/measureType';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { setDatasetsColor } from 'app/commons/utils/colorUtils';
import { legendMargin } from 'app/commons/utils/pluginsChartjs';
import { splitStringEveryNum } from 'app/commons/utils/string-utils';
import { I18NService } from 'app/i18n/i18n.service';
import { ChartDataset, ChartType, ChartData, ChartOptions, Chart, ChartEvent, ActiveElement } from 'chart.js';
import { orderBy } from 'lodash';
import { SelectItem } from 'primeng/api';
import { ChartModule } from 'primeng/chart';
import { SkeletonModule } from 'primeng/skeleton';
import { catchError, combineLatest, filter, fromEvent, map, of, switchMap, tap } from 'rxjs';

@Component({
  selector: 'gzoom-chart-measurement-types-status',
  standalone: true,
  imports: [CommonModule, ChartModule, SkeletonModule],
  templateUrl: './chart-measurement-types-status.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ChartMeasurementTypesStatusComponent implements AfterViewInit {
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
  loading = signal<boolean>(true);
  //measuresType = signal<MeasureType[]>(null);
  riskIdDatasets = computed<SelectItem[]>(() => this.levels().map(x => ({
    label: this.secondaryLang ? x.levelNameLang : x.levelName,
    value: x.levelId
  })));

  levels = computed<Level[]>(() => [
    ...new Set(
      this.measuresType()?.map(x => JSON.stringify(x.measureTypeLevel))
    )]
    .map(x => JSON.parse(x))
  );

  labels = computed<string[]>(() => this.levels()?.map(x => this.secondaryLang ? x.levelNameLang : x.levelName));


  plugins = [legendMargin]
  hoverIndex: number = null;

  measuresType = toSignal(
    combineLatest([
      toObservable(this.year),
      toObservable(this.orgUnitId)
    ]).pipe(
      filter(([year]) => !!year),
      switchMap(([year, orgUnitId]) =>
        this.riskService.getRiskMeasureTypesByYear(year, orgUnitId)
          .pipe(
            map(period => period?.measureTypes ?? []),
            tap(() => this.loading.set(false)),
            catchError(() => {
              this.loading.set(false);
              return of([] as MeasureType[]);
            })
          )
      )
    ),
    {
      initialValue: [] as MeasureType[]
    }
  );

  datasets = computed<ChartDataset[]>(() => {

    if (this.measuresType()) {

      const measMap: { [key: string]: { label: string, data: (number | null)[] } } = {};

      this.measuresType()
        .forEach((meas) => {
          const levelName = this.secondaryLang ? meas.measureTypeLevel.levelNameLang : meas.measureTypeLevel.levelName;
          if (!measMap[meas.measureTypeName]) {

            measMap[meas.measureTypeName] = {
              label: this.secondaryLang ? meas.measureTypeNameLang : meas.measureTypeName,
              data: new Array(this.labels().length).fill(null)
            };
          }
          const index = this.labels().findIndex(x => x === levelName);
          measMap[meas.measureTypeName].data[index] = meas?.measureTypePct / 100;

        });
      const dataset = Object.values(measMap);
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
            text: splitStringEveryNum(`${this.i18n.translate('misPerc')}`, 2),
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
          text: `${this.i18n.translate('measurementTypesStatus')} ${this.i18n.translate('year').toLowerCase()} ${this.year()}`,
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
    const ctx = document.getElementById('measurementTypesStatus').getElementsByTagName('canvas');

    if (ctx)
      fromEvent(ctx, 'mouseout')
        .pipe(
          takeUntilDestroyed(this.destroy)
        ).subscribe(x => {
        this.isMouseOut.set(true);
        this.tmpChart?.update();
      })
  }

  onHover(event: ChartEvent, elements: ActiveElement[], chart: Chart) {
    this.ngZone.run(() => {

      if (chart)
        this.tmpChart = chart;

      this.isMouseOut.set(false);
      const isHoverElement = (elements?.length > 0);
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

    this.router.navigate([`measurement-types-status/${riskId}`], {
      queryParams: queryParams,
      relativeTo: this.activatedRoute
    })
  }
}
