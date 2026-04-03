import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { ChartDropdown, chartConfig } from '../chart/chartConfig';

@Component({
  selector: 'app-dialog-chart',
  templateUrl: './dialog-chart.component.html',
  styleUrls: ['./dialog-chart.component.css']
})
export class DialogChartComponent implements OnInit, OnChanges {
  @Input() showDialog: boolean;
  @Input() showDialogTitle: string;
  @Input() chartItemsDialog: ChartDropdown[];
  @Input() loadingDialog: boolean;
  @Input() chartConfigDialog: chartConfig;
  @Input() selectedChartItemDialog: ChartDropdown;

  @Output() dialogClosed = new EventEmitter<any>();

  show: boolean = false;

  constructor() { }
  ngOnChanges(changes: SimpleChanges): void {
    if (changes.showDialog || changes.chartConfigDialog) {
      this.show = this.showDialog;
      this.selectedChartItemDialog = this.chartItemsDialog.filter(x => x.code == this.chartConfigDialog?.type)[0];
    }
  }

  ngOnInit(): void {
  }


  setChartTypeDialog() {
    let tmp = this.chartConfigDialog;
    tmp.type = this.selectedChartItemDialog.code;
    this.chartConfigDialog = { ...tmp };
  }

  hide() {
    this.show = null;
    this.dialogClosed.emit();
  }

}
