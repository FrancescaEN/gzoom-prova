import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output, input } from '@angular/core';
import { TooltipOptions } from 'primeng/api/tooltipoptions';


@Component({
  selector: 'gzoom-button-outlined',
  templateUrl: './gzoom-button-outlined.component.html',
  styleUrls: ['./gzoom-button-outlined.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GzoomButtonOutlinedComponent {
  @Input() icon: string;
  @Input() label: string;
  @Input() loading: boolean;
  @Input() iconPos: string | 'left' | 'right' = 'left';
  @Input() rounded: boolean = false;
  @Input() disabled: boolean;
  @Input() show: boolean = true;
  size = input<'small' | 'large' | undefined>();

  @Input() tooltipLabel: string;
  @Input() tooltipOptions: TooltipOptions;
  @Input() tooltipPosition: string | 'left' | 'right' | 'top' | 'bottom';

  @Output() onClick = new EventEmitter<PointerEvent>();

  click(event: PointerEvent) {
    this.onClick.emit(event);
  }
}
