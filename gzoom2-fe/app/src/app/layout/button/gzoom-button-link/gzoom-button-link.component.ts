import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { TooltipOptions } from 'primeng/api/tooltipoptions';


@Component({
  selector: 'gzoom-button-link',
  templateUrl: './gzoom-button-link.component.html',
  styleUrls: ['./gzoom-button-link.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GzoomButtonLinkComponent {
  @Input() icon: string;
  @Input() label: string;
  @Input() loading: boolean;
  @Input() disabled: boolean;
  @Input() iconPos: string | 'left' | 'right' = 'left';
  @Input() rounded: boolean = false;
  @Input() buttonStyle: string;

  @Input() tooltipLabel: string;
  @Input() tooltipOptions: TooltipOptions;
  @Input() tooltipPosition: string | 'left' | 'right' | 'top' | 'bottom';

  @Output() onClick = new EventEmitter<PointerEvent>();

  click(event: PointerEvent) {
    this.onClick.emit(event);
  }

}
