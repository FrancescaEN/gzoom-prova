import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, NgZone, computed, inject, input, numberAttribute, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { Area } from 'app/api/model/dashboards/area';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { getColorFromGzoomColorChart, setOpacityByHex } from 'app/commons/utils/colorUtils';
import { legendMargin } from 'app/commons/utils/pluginsChartjs';
import { splitStringEveryNum } from 'app/commons/utils/string-utils';
import { I18NService } from 'app/i18n/i18n.service';
import { ActiveElement, Chart, ChartData, ChartEvent, ChartOptions, ChartType } from 'chart.js';
import { ChartModule } from 'primeng/chart';
import { SkeletonModule } from 'primeng/skeleton';
import { catchError, combineLatest, filter, map, of, switchMap, tap } from 'rxjs';

@Component({
  selector: 'gzoom-risk-state',
  standalone: true,
  imports: [
    ChartModule,
    CommonModule,
    SkeletonModule
  ],
  templateUrl: './chart-risk-state.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export default class RiskStateComponent {
  /* Services */
  ngZone = inject(NgZone);
  router = inject(Router);
  i18n = inject(I18NService);
  activatedRoute = inject(ActivatedRoute);
  riskService = inject(RiskService);
  scrollableService = inject(ScrollableTabService);

  /* Inputs */
  year = input.required({ transform: numberAttribute });
  orgUnitId = input<string>();

  /* Variables */
  //Breakpoint for responsive dashboard
  height = toSignal(this.scrollableService.isScrollableHeight(800).pipe(map(value => value ? '500px' : '400px')));
  secondaryLang = this.i18n.getIsSecondaryLang();
  loading = signal<boolean>(true);
  cursorPointer = signal<boolean>(true);

  //areas = signal<Area[]>([])

  areas = toSignal(
    combineLatest([
      toObservable(this.year),
      toObservable(this.orgUnitId)
    ]).pipe(
      filter(([year]) => !!year),
      switchMap(([year, orgUnitId]) =>
        this.riskService.getRiskAreaByYear(year, orgUnitId)
          .pipe(
            map((areas: Area[] = []) => {
              const padded = [...areas];
              while (padded.length < 7) {
                padded.push({} as Area); // or create a placeholder object if needed
              }
              return padded.slice(0, 7);
            }),
            tap(() => this.loading.set(false)),
            catchError(() => {
              this.loading.set(false);
              return of(Array(7).fill({} as Area));
            })
          )
      )
    ),
    {
      initialValue: Array(7).fill({} as Area)
    }
  );

  riskAreas = computed(() => this.areas()?.map(x => splitStringEveryNum(this.secondaryLang ? x.areaNameLang : x.areaName, 3)));
  scoreAreas = computed(() => this.areas()?.map(x => x.areaScore))
  plugins = [legendMargin]

  type: ChartType = 'radar';
  data = computed<ChartData>(() => {
    const color = getColorFromGzoomColorChart(0);
    return {
      labels: this.riskAreas(),
      datasets: [{
        label: this.i18n.translate(`areScore`),
        data: this.scoreAreas(),
        borderColor: color,
        backgroundColor: setOpacityByHex(color, 0.5)
      }
      ]
    }
  });

  // borderWidth = signal<number>(3);
  options = computed<ChartOptions>(() => {
    return {
      hover: {
        includeInvisible: true,
        intersect: false,
        mode: 'dataset'
      },

      elements: {
        line: {
          hoverBorderWidth: 4
        },
        point: {
          //pointStyle: false,
          hoverBorderWidth: 4
        }
      },

      plugins: {
        tooltip: {
          mode: 'point',
          bodyFont: {
            size: 11
          }
        },
        title: {
          align: 'start',
          display: true,
          text: `${this.i18n.translate('risk')} ${this.i18n.translate('year').toLowerCase()} ${this.year()}`,
          padding: { top: 5, bottom: 10 },
          font: {
            size: 16
          }
        },
        legend: {
          display: true, //Nasconde la leggenda
          position: 'top',
          align: 'start',
          labels: { font: { size: 9 } }
        },
        colors: {
          enabled: false,
          forceOverride: true
        }
      },

      onHover: this.onHover.bind(this)
    }
  });

  onHover(event: ChartEvent, elements: ActiveElement[], chart: Chart) {
    this.ngZone.run(() => {
      const isHoverElement = (elements.length > 0)

      this.cursorPointer.set(isHoverElement);
      if (isHoverElement && event.type === 'click') this.goToRiskStateDetail();
    })
  }

  goToRiskStateDetail() {
    const queryParams: any = {}
    if (this.orgUnitId() !== null && this.orgUnitId() !== undefined && this.orgUnitId() !== '') {
      queryParams.orgUnitId = this.orgUnitId();
    }
    this.router.navigate([`risk-state/${this.year()}`], { queryParams: queryParams, relativeTo: this.activatedRoute })
  }

}
