import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { chartConfig } from './chartConfig';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit, OnChanges {
  @Input() chartConfig: chartConfig;
  @Input() chartConfiguration: chartConfig;
  @Input() type: string;

  data = {
    labels: [],
    datasets: []
  };

  datatmp: any;
  // type: string;

  chartOptions: any;

  constructor() { }

  ngOnInit() { }

  selectColor(opacity) {
    let o = Math.round, r = Math.random, s = 255;
    return 'rgba(' + o(r() * s) + ',' + o(r() * s) + ',' + o(r() * s) + ',' + opacity + ')';
  }

  setOpacity(rgbaString, opacity) {
    // Extract the individual RGBA components
    const matches = rgbaString.match(/^rgba\((\d+),\s*(\d+),\s*(\d+),\s*(\d*\.?\d+)\)$/);
    if (!matches) {
      throw new Error('Invalid RGBA string');
    }

    const red = parseInt(matches[1], 10);
    const green = parseInt(matches[2], 10);
    const blue = parseInt(matches[3], 10);
    const alpha = parseFloat(matches[4]);

    // Clamp the opacity value between 0 and 1
    const clampedOpacity = Math.min(Math.max(opacity, 0), 1);

    // Create the modified RGBA string with the updated opacity
    const modifiedRGBA = `rgba(${red}, ${green}, ${blue}, ${clampedOpacity})`;
    return modifiedRGBA;
  }

  setColor(opacity) {
    let backgroundColor = [];
    for (let index = 0; index < this.chartConfig.etch.length; index++) {

      let clr;

      do clr = this.selectColor(opacity);
      while (backgroundColor.includes(clr));

      backgroundColor.push(clr);

    }

    return backgroundColor;

  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes && changes["chartConfiguration"]) {

      if (this.chartConfiguration != undefined) {

        console.log(this.chartConfiguration);
        this.type = this.chartConfiguration.type;

        if (this.type == 'bar' && this.chartConfiguration.responsiveScale == true) {
          this.chartOptions = {
            plugins: {
              title: {
                display: (!!this.chartConfiguration.title),
                text: this.chartConfiguration.title,
              }
            },
            scales: {
              y: {
                beginAtZero: false
              }
            },
            responsive: true,
            maintainAspectRatio: true,
          };
        }
        else {
          this.chartOptions = {
            plugins: {
              title: {
                display: (!!this.chartConfiguration.title),
                text: this.chartConfiguration.title,
              }
            },
            scale: {
              min: this.chartConfiguration.min,
              max: this.chartConfiguration.max,
            },
            responsive: true,
            maintainAspectRatio: (this.type != "radar"),
          };
        }
        //this.chartOptions = this.chartConfiguration.options;
        this.datatmp = {

          labels: this.chartConfiguration.labels,
          datasets: this.chartConfiguration.datasets
        };

        this.datatmp.datasets.forEach(x => {
          let color = this.selectColor(1);

          x.backgroundColor = this.setOpacity(color, 0.5);
          x.borderColor = color;
          x.pointBackgroundColor = color;
          x.pointBorderColor = '#fff';
          x.pointHoverBackgroundColor = '#fff';
          x.pointHoverBorderColor = color;
        })

        this.data = this.datatmp;
      }

    }

    if (changes && changes["chartConfig"]) {

      this.type = this.chartConfig.type;


      let fin = [];

      if (this.chartConfig.showEtchDescr == 'D' || this.chartConfig.showEtchDescr == 'A') {

        if (this.type == "radar") {
          this.chartConfig.labels.forEach((r, index) => {
            let lab = [];
            if (this.chartConfig.showEtchDescr == 'A') r = this.chartConfig.etch[index] + "-" + r;
            let s = r.split(" ");
            let len = s.length;
            let a = "";
            s.forEach((e, index) => {

              if ((index % 3 == 0) || (index == len - 1)) {
                a += " " + e;
                lab.push(a);
                a = "";

              }
              else a += e + " ";

            })

            fin.push(lab);
          })
        }
        else {
          this.chartConfig.labels.forEach((r, index) => {
            if (this.chartConfig.showEtchDescr == 'A') r = this.chartConfig.etch[index] + "-" + r;

            fin.push(r);
          })
        }
      }
      else {
        fin = this.chartConfig.etch;

      }

      if (this.type == 'polarArea') {
        this.chartOptions = {
          animation: { delay: 1000 },
          responsive: true,
          scale: {
            r: {
              pointLabels: {
                display: true,
                centerPointLabels: true,
                font: {
                  size: 12
                }
              }
            },
            min: this.chartConfig.dataMin,
            max: this.chartConfig.dataMax[0],
          },

          //maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'bottom' // or 'left', 'right', 'top'
            }
          }
        };
      }
      else this.chartOptions = {
        animation: { delay: 1000 },

        scale: {
          min: this.chartConfig.dataMin,
          max: this.chartConfig.dataMax[0],
        },

        responsive: true,
        //maintainAspectRatio: false,

        plugins: {
          legend: {
            align: 'right',
            position: 'bottom', // or 'left', 'right', 'top'
            labels: {
              boxWidth: 20
            },
            maxWidt: 100,
          }
        }


      };


      this.datatmp = {

        /* labels: this.chartConfig.labels, */
        labels: fin,
        datasets: []
      };

      let condition = this.type != 'radar' && this.type != 'line' && this.type != 'bar';
      let backgroundColor: string[];
      let color: string[];

      backgroundColor = this.setColor(0.5);
      color = backgroundColor;

      if (!!this.chartConfig.rangeMaxName && !condition) {
        this.datatmp.datasets.push(
          {
            label: this.chartConfig.rangeMaxName,
            backgroundColor: 'rgba(179,181,198,0.2)',
            borderColor: 'rgba(179,181,198,1)',
            pointBackgroundColor: 'rgba(179,181,198,1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(179,181,198,1)',
            data: this.chartConfig.dataMax
          }
        )

      }

      if (!!this.chartConfig.scoreEtch1) {
        if (!condition) {
          backgroundColor = ['rgba(255,99,132,0.2)'];
          color = ['rgba(255,99,132,1)'];
        }

        this.datatmp.datasets.push(
          {
            label: this.chartConfig.scoreEtch1,
            backgroundColor: backgroundColor,
            borderColor: color,
            pointBackgroundColor: color,
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: color,
            data: this.chartConfig.dataSC
          }

        )
      }


      if (!!this.chartConfig.scoreEtch2) {

        if (!condition) {
          backgroundColor = ['rgba(255,165,0,0.2)'];
          color = ['rgba(255,165,0,1)'];
        }

        this.datatmp.datasets.push(
          {
            label: this.chartConfig.scoreEtch2,
            backgroundColor: backgroundColor,
            borderColor: color,
            pointBackgroundColor: color,
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: color,
            data: this.chartConfig.dataST
          }

        )
      }

      if (!!this.chartConfig.scoreEtch3) {

        if (!condition) {
          backgroundColor = ['rgba(60,179,113,0.2)'];
          color = ['rgba(60,179,113,1)'];
        }

        this.datatmp.datasets.push(
          {
            label: this.chartConfig.scoreEtch3,
            backgroundColor: backgroundColor,
            borderColor: color,
            pointBackgroundColor: color,
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: color,
            data: this.chartConfig.dataSC2
          }

        )
      }

      if (!!this.chartConfig.scoreEtch4) {

        if (!condition) {
          backgroundColor = ['rgba(106,90,205,0.2)'];
          color = ['rgba(106,90,205,1)'];
        }

        this.datatmp.datasets.push(
          {
            label: this.chartConfig.scoreEtch4,
            backgroundColor: backgroundColor,
            borderColor: color,
            pointBackgroundColor: color,
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: color,
            data: this.chartConfig.dataST2
          }

        )
      }
      this.data = { ...this.datatmp };

    }
  }

}
