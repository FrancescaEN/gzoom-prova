import { Component, input } from '@angular/core';
import { LayoutModule } from 'app/layout/layout.module';

@Component({
  selector: 'gzoom-message-warn',
  standalone: true,
  imports: [LayoutModule],
  templateUrl: './message-warn.component.html',
})
export class MessageWarnComponent {
  message = input<string>();
}
