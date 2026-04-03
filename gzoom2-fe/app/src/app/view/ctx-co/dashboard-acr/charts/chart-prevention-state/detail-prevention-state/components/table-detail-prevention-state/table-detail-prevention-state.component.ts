import { CommonModule } from '@angular/common';
import { Component, booleanAttribute, computed, inject, input } from '@angular/core';
import { PeriodArea } from 'app/api/model/dashboards/period';
import { I18NService } from 'app/i18n/i18n.service';
import { TableDetailChartComponent, TableDetailChartHeader } from 'app/shared/components/table-detail-chart/table-detail-chart.component';
import { orderBy } from 'lodash';

export interface TableDetailPreventionState {
  areaName: string,
  measPlanned: number,
  measImplemented: number,
  measDeltaPct: number
}
@Component({
  selector: 'gzoom-table-detail-prevention-state',
  standalone: true,
  imports: [CommonModule, TableDetailChartComponent],
  templateUrl: './table-detail-prevention-state.component.html',
})
export default class TableDetailPreventionStateComponent {

  i18n = inject(I18NService);
  secondaryLang = this.i18n.getIsSecondaryLang();
  defaultYear = new Date().getFullYear();
  loading = input(false, { transform: booleanAttribute });

  preventionState = input.required<PeriodArea>();
  header: TableDetailChartHeader = {
    labels: ["areaRischio", "misAttuate", "misPreviste", "misDeltaPerc"],
    keys: [
      "areaName",
      "measImplemented",
      "measPlanned",
      "measDeltaPct"]
  }

  data$ = computed<TableDetailPreventionState[]>(() => {
    if (this.preventionState()) {
      return orderBy(this.preventionState().areas?.map(area => ({
        areaName: this.secondaryLang ? area.areaNameLang : area.areaName,
        measPlanned: area.areaSummary?.measPlanned,
        measImplemented: area.areaSummary?.measImplemented,
        measDeltaPct: area.areaSummary?.measDeltaPct
      })), "areaName")
    }
  })
}
