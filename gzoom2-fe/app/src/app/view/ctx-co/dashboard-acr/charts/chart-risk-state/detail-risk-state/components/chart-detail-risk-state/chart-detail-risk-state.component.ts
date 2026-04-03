import { Component, computed, inject, input, numberAttribute, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { AreaLevel } from 'app/api/model/dashboards/level';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { setDatasetsColor } from 'app/commons/utils/colorUtils';
import { legendMargin } from 'app/commons/utils/pluginsChartjs';
import { I18NService } from 'app/i18n/i18n.service';
import { ChartData, ChartDataset, ChartOptions, ChartType } from 'chart.js';
import { orderBy } from 'lodash';
import { ChartModule } from 'primeng/chart';
import { SkeletonModule } from 'primeng/skeleton';
import { catchError, combineLatest, filter, map, of, switchMap, tap } from 'rxjs';

@Component({
  selector: 'gzoom-chart-detail-risk-state',
  standalone: true,
  imports: [
    ChartModule,
    SkeletonModule
  ],
  templateUrl: './chart-detail-risk-state.component.html',
})
export default class ChartDetailRiskStateComponent {
  /* Services */
  i18n = inject(I18NService);
  scrollableService = inject(ScrollableTabService);
  riskService = inject(RiskService);

  /* Inputs */
  year = input.required({ transform: numberAttribute });
  orgUnitId = input<string>();

  /* Variables */
  //Breakpoint for responsive dashboard
  height = toSignal(this.scrollableService.isScrollableHeight(800).pipe(map(value => value ? '50vh' : '400px')));
  secondaryLang = this.i18n.getIsSecondaryLang();
  loading = signal<boolean>(true);

  labels = computed(() => this.areaLevels()?.map(areaLevel => areaLevel[this.secondaryLang ? 'levelNameLang' : 'levelName']))

  areaLevels = toSignal(
    combineLatest([
      toObservable(this.year),
      toObservable(this.orgUnitId)
    ]).pipe(
      filter(([year]) => !!year ),
      switchMap(([year, orgUnitId]) =>
        this.riskService.getRiskProScoreByYear(year, orgUnitId)
          .pipe(
            tap(() => this.loading.set(false)),
            catchError(() => {
              this.loading.set(false);
              return of([] as AreaLevel[]);
            })
          )
      )
    ),
    { 
      initialValue: [] as AreaLevel[],
    }
  );

  datasets = computed<ChartDataset[]>(
    () => {
      if (this.areaLevels()) {

        // 1. Creare una mappa delle aree con la loro lista di processCount per ogni livello
        const areaMap: { [key: string]: { label: string, data: (number | null)[] } } = {};

        // 2. Scorrere i livelli e per ciascuno scorrere le aree
        this.areaLevels().forEach((level, levelIndex) => {
          level.areas.forEach(area => {
            // Se l'area non è già presente nella mappa, la inizializziamo
            if (!areaMap[area.areaId]) {
              areaMap[area.areaId] = {
                label: area.areaName, // Label corrispondente al nome dell'area
                data: new Array(this.areaLevels().length).fill(null) // Inizializzare l'array data con null per tutti i livelli
              };
            }
            // Inserire il valore di processCount nell'indice corrispondente al livello
            areaMap[area.areaId].data[levelIndex] = area.processCount;
          });
        });

        // 3. Convertire la mappa in un array
        const dataset = Object.values(areaMap);
        return setDatasetsColor(orderBy(dataset, "label"), 0.7);
      }
    }
  );

  plugins = [legendMargin]

  type: ChartType = 'bar';
  options = computed<ChartOptions>(() => {
    return {
      scales: {
        x: {
          title: {
            text: this.i18n.translate("proRisk"),
            display: true,
            font: { size: 10, weight: 'bolder' }
          }
        },
        y: {
          title: {
            text: this.i18n.translate("proNum"),
            display: true,
            font: { size: 10, weight: 'bolder' }
          }
        },
      },
      hover: {
        includeInvisible: true,
        intersect: false,
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
          text: `${this.i18n.translate('riskStatus')} ${this.i18n.translate('year').toLowerCase()} ${this.year()}`,
          align: 'start',
          padding: { top: 5, bottom: 10 },
          font: { size: 16 }
        },
        colors: {
          enabled: false,
          forceOverride: true
        },

      }
    };
  })


  data = computed<ChartData>(() => {
    if (this.labels() && this.datasets())
      return {
        labels: this.labels(),
        datasets:
          this.datasets()
      }
  })

}
