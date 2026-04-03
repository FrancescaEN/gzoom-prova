import { Component, input } from '@angular/core';
import { LayoutModule } from 'app/layout/layout.module';

@Component({
  selector: 'gzoom-message-info',
  standalone: true,
  imports: [LayoutModule],
  templateUrl: './message-info.component.html',
})
export class MessageInfoComponent {
  message = input<string>();
}
