import { Component, booleanAttribute, computed, inject, input, numberAttribute, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { PeriodArea } from 'app/api/model/dashboards/period';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { setDatasetsColor } from 'app/commons/utils/colorUtils';
import { legendMargin } from 'app/commons/utils/pluginsChartjs';
import { splitStringEveryNum } from 'app/commons/utils/string-utils';
import { I18NService } from 'app/i18n/i18n.service';
import { ChartData, ChartDataset, ChartOptions, ChartType } from 'chart.js';
import { orderBy } from 'lodash';
import { ChartModule } from 'primeng/chart';
import { SkeletonModule } from 'primeng/skeleton';
import { map } from 'rxjs';

@Component({
  selector: 'gzoom-chart-detail-prevention-state',
  standalone: true,
  imports: [ChartModule, SkeletonModule],
  templateUrl: './chart-detail-prevention-state.component.html'
})
export default class ChartDetailPreventionStateComponent {
  i18n = inject(I18NService);
  scrollableService = inject(ScrollableTabService);
  //Breakpoint for responsive dashboard
  height = toSignal(this.scrollableService.isScrollableHeight(800).pipe(map(value => value ? '50vh' : '400px')));
  plugins = [legendMargin];
  loading = input(false, { transform: booleanAttribute });
  year = input.required({ transform: numberAttribute });
  preventionState = input.required<PeriodArea>();
  secondaryLang = this.i18n.getIsSecondaryLang();
  chartTitle = signal<string>(null);
  labels = computed(() => {
    if (this.preventionState()) {

      return [...orderBy(this.preventionState().areas, this.secondaryLang ? 'areaNameLang' : 'areaName').map(x => splitStringEveryNum(this.secondaryLang ? x.areaNameLang : x.areaName, 3))]
    }
  });
  inDatasets: ChartDataset[] = [];
  datasets = computed<ChartDataset[]>(() => {
    if (this.preventionState()) {
      const areas = orderBy(this.preventionState().areas, this.secondaryLang ? 'areaNameLang' : 'areaName');

      this.inDatasets = []
      areas.forEach(item => {

        this.insertValueChart(item.areaSummary?.measDeltaPct / 100, this.i18n.translate('misDeltaPerc'), 'line', 'y2');
        this.insertValueChart(item.areaSummary?.measImplemented, this.i18n.translate('misAttuate'));
        this.insertValueChart(item.areaSummary?.measPlanned, this.i18n.translate('misPreviste'));
      })

      return setDatasetsColor(this.inDatasets);
    }
  });

  type: ChartType = 'bar';
  options = computed<ChartOptions>(() => {
    return {
      responsive: true,
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
          text: `${this.i18n.translate('preventionState')} ${this.i18n.translate('year').toLowerCase()} ${this.year()}`,
          padding: { top: 5, bottom: 10 },
          font: {
            size: 16
          }
        }
      },
      scales: {
        x: {
          ticks: {
            font: {
              size: 9
            }
          },
          title: {
            text: this.i18n.translate('areaRischio'),
            display: true,
            font: {
              size: 9,
              weight: 'bolder'
            }
          }
        },
        y: {
          type: 'linear',
          position: 'left',
          title: {
            text: splitStringEveryNum(this.i18n.translate('misNum'), 2),
            display: true,
            font: {
              size: 9,
              weight: 'bolder'
            }
          }
        },
        y2: {
          type: 'linear',
          position: 'right',
          reverse: true,
          grid: {
            drawOnChartArea: false
          },
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
      }

    }

  });

  data = computed<ChartData>(() => {
    if (this.labels() && this.datasets()) {
      return {
        labels: this.labels(),
        datasets: this.datasets()
      }
    }
  });


  insertValueChart(value: number, label: string, type?, yAxisID?) {

    if (this.inDatasets.filter(x => x.label == label).length == 0) {
      let data: number[] = [];
      data.push(value)
      this.inDatasets.push({ label, data, type, yAxisID });
    } else if (this.inDatasets.filter(x => x.label == label).length > 0) {
      this.inDatasets.filter(x => x.label == label)[0].data.push(value);
    }
  }

}
