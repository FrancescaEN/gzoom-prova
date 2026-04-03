import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output, effect, input } from '@angular/core';
import { TooltipOptions } from 'primeng/api/tooltipoptions';


@Component({
  selector: 'gzoom-button',
  templateUrl: './gzoom-button.component.html',
  styleUrls: ['./gzoom-button.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GzoomButtonComponent {
  @Input() icon: string;
  @Input() label: string;
  @Input() loading: boolean;
  @Input() disabled: boolean;
  @Input() iconPos: string | 'left' | 'right' = 'left';
  @Input() rounded: boolean = false;
  @Input() show: boolean = true;

  @Input() tooltipLabel: string;
  @Input() tooltipOptions: TooltipOptions;
  @Input() tooltipPosition: string | 'left' | 'right' | 'top' | 'bottom';

  @Output() onClick = new EventEmitter<PointerEvent>();

  click(event: PointerEvent) {
    this.onClick.emit(event);
  }

}
