import { Component, Input, OnInit } from '@angular/core';
import { PrimeIcons } from 'primeng/api';

@Component({
  selector: 'gzoom-message',
  templateUrl: './gzoom-message.component.html',
  styles:
    `
  .p-inline-message.p-inline-message-info {
    background: #8dcdff5c ;
  }
  `
})
export class GzoomMessageComponent implements OnInit {

  @Input() severity: 'warn' | 'info' | 'error' | 'success' = 'info';
  @Input() icon: string;
  @Input() message: string;
  @Input() labelLink: string;
  @Input() routerLink: string;

  ngOnInit(): void {
    if (!this.icon) {
      switch (this.severity) {
        case 'info':
          this.icon = PrimeIcons.INFO_CIRCLE;
          break;
        case 'warn':
          this.icon = PrimeIcons.EXCLAMATION_TRIANGLE;
          break;
        case 'success':
          this.icon = PrimeIcons.CHECK;
          break;
        case 'error':
          this.icon = PrimeIcons.TIMES_CIRCLE;
          break;
      }
    }
  }
}
