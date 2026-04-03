import { Component, booleanAttribute, inject, input, output } from '@angular/core';
import { I18NService } from 'app/i18n/i18n.service';
import { ButtonBarTableComponent } from 'app/layout/button/button-bar-table/button-bar-table.component';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'gzoom-button-card',
  standalone: true,
  imports: [CardModule, ButtonBarTableComponent],
  templateUrl: './button-card.component.html',
  styles: `
  p-card {
    width: 100%;
    height: 100%;
  }
  `
})
export class ButtonCardComponent {
  i18n = inject(I18NService);
  //Button bar input-output
  backButton = input(false, { transform: booleanAttribute });
  backLabel = input<string>(this.i18n.translate("Back"));
  back = output();

  roundedButtons = input(false, { transform: booleanAttribute });
}
