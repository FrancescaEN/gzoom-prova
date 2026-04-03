import { Component, ElementRef, Input, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { gaugeConfig } from '../gauge/gaugeConfig';

@Component({
  selector: 'app-demo-gauge',
  templateUrl: './demo-gauge.component.html',
  styleUrls: ['./demo-gauge.component.scss']
})
export class DemoGaugeComponent implements OnInit {

  @Input() value: number;
  @Input() indicator: string = "semi"; // line triangle semi
  @Input() gaugeConfig: gaugeConfig;
  @Input() rangeRed: number;
  @Input() rangeOrange: number;
  @Input() rangeGreen: number;
  @Input() valMax: number;
  @Input() valMin: number;
  @ViewChild('canvasGauge', { static: true }) canvasRef: ElementRef;

  colorTheme;


  constructor() {

  }

  x = 50;
  y = 60;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.gaugeConfig) {
      if (this.gaugeConfig.gaugeValue) this.value = this.gaugeConfig.gaugeValue;


      this.valMax = (this.gaugeConfig.gaugeMax) ? this.gaugeConfig.gaugeMax : 100;
      this.valMin = (this.gaugeConfig.gaugeMin) ? this.gaugeConfig.gaugeMin : 0;

      this.rangeRed = this.gaugeConfig.fromValueYellow;
      this.rangeOrange = this.gaugeConfig.fromValueGreen;
      this.rangeGreen = this.valMax;

    }
  }

  ngOnInit(): void {

    const canvas: HTMLCanvasElement = this.canvasRef.nativeElement;

    if (canvas.getContext) {
      const ctx = canvas.getContext("2d");



      ctx.strokeStyle = "rgba(222,222,222,0.5)";
      const radius = 40;
      const degreesStart = 150;
      const degreesEnd = 30;
      const startAngle = (Math.PI / 180) * degreesStart; // Starting point on circle
      const endAngle = (Math.PI / 180) * degreesEnd; // End point on circle
      const counterclockwise = false; // clockwise or counterclockwise

      const endAngleVal = (Math.PI / 180) * (degreesStart + (240 * (this.value - this.valMin)) / (this.valMax - this.valMin));


      //VAL DRAW
      if (this.indicator == "semi") {
        ctx.lineWidth = 5;
        //ctx.lineCap = "round";
        ctx.beginPath();
        ctx.arc(this.x, this.y, radius, startAngle, endAngle, counterclockwise);
        ctx.stroke();

        if (this.rangeOrange <= this.value && this.value <= this.rangeGreen) {

          ctx.strokeStyle = "rgba(103,159,90,1)";
        }
        else if (this.rangeRed <= this.value && this.value <= this.rangeOrange) {

          ctx.strokeStyle = "rgba(255,188,64,1)";
        }
        else if (this.value <= this.rangeRed) {
          ctx.strokeStyle = "rgba(230,25,25,1)";
        }
        else {
          ctx.strokeStyle = "grey";
        }
        ctx.beginPath();
        ctx.arc(this.x, this.y, radius, startAngle, endAngleVal, counterclockwise);
        ctx.stroke();


      }

      ctx.lineWidth = 7;
      ctx.strokeStyle = "rgba(222,222,222,0.6)";
      ctx.beginPath();
      ctx.arc(this.x, this.y, radius - 10, startAngle, endAngle, counterclockwise);
      ctx.stroke();


      ctx.strokeStyle = "rgba(230,25,25,0.3)";
      const endAngleRed = (Math.PI / 180) * (degreesStart + (240 * (this.rangeRed - this.valMin)) / (this.valMax - this.valMin))
      ctx.beginPath();
      ctx.arc(this.x, this.y, radius - 9, startAngle, endAngleRed, counterclockwise);
      ctx.stroke();

      ctx.strokeStyle = "rgba(255,188,64,0.3)";
      const endAngleOrange = (Math.PI / 180) * (degreesStart + (240 * (this.rangeOrange - this.valMin)) / (this.valMax - this.valMin))
      ctx.beginPath();
      ctx.arc(this.x, this.y, radius - 9, endAngleRed, endAngleOrange, counterclockwise);
      ctx.stroke();

      ctx.strokeStyle = "rgba(103,159,90,0.3)";
      const endAngleGreen = (Math.PI / 180) * (degreesStart + (240 * (this.rangeGreen - this.valMin)) / (this.valMax - this.valMin))
      ctx.beginPath();
      ctx.arc(this.x, this.y, radius - 9, endAngleOrange, endAngleGreen, counterclockwise);
      ctx.stroke();

      ctx.font = "20px Arial";
      ctx.textAlign = "center";
      ctx.textBaseline = "middle";
      ctx.fillText(`${this.value.toString().replace(".", ",")}`, this.x, this.y);


      if (this.indicator == "triangle") {

        this.drawTriangle(ctx, endAngleVal, radius);
      }

      if (this.indicator == "line") {
        const lunghezzaLinea = 10;

        var puntoX = this.x + Math.cos(endAngleVal) * (radius - lunghezzaLinea / 1.7);
        var puntoY = this.y + Math.sin(endAngleVal) * (radius - lunghezzaLinea / 1.7);
        ctx.strokeStyle = "gray";
        ctx.lineCap = "round";
        ctx.beginPath();
        ctx.moveTo(puntoX, puntoY);
        ctx.lineTo(puntoX + Math.cos(endAngleVal - Math.PI) * lunghezzaLinea, puntoY + Math.sin(endAngleVal - Math.PI) * lunghezzaLinea);
        ctx.stroke();
      }



      // drawing code here
    } else {
      // canvas-unsupported code here
    }
  }

  drawTriangle(ctx, endAngleVal, radius) {
    const lunghezzaLati = 10;
    var puntoX = this.x + Math.cos(endAngleVal) * (radius - 5);
    var puntoY = this.y + Math.sin(endAngleVal) * (radius - 5);

    ctx.beginPath();
    ctx.moveTo(puntoX, puntoY); // Punto centrale del semicerchio come vertice del triangolo
    ctx.lineTo(puntoX + Math.cos(endAngleVal - Math.PI / 4) * lunghezzaLati, puntoY + Math.sin(endAngleVal - Math.PI / 4) * lunghezzaLati); // Punto sulla circonferenza del semicerchio
    ctx.lineTo(puntoX + Math.cos(endAngleVal + Math.PI / 4) * lunghezzaLati, puntoY + Math.sin(endAngleVal + Math.PI / 4) * lunghezzaLati); // Punto traslato di 90 gradi
    ctx.lineTo(puntoX, puntoY); // Torna al punto di partenza per chiudere il triangolo
    ctx.fill();
  }

}
