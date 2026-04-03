import { CommonModule } from '@angular/common';
import { Component, inject, input, numberAttribute, signal } from '@angular/core';
import { UomRatingScale } from 'app/api/model/uomRatingScale';
import { addDaysFromGiulianDate } from 'app/commons/utils/dateUtils';
import { I18NService } from 'app/i18n/i18n.service';

@Component({
  selector: 'gzoom-uom-amout',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './uom-amout.component.html',
  styleUrl: './uom-amout.component.scss'
})
export class UomAmoutComponent {
  i18n = inject(I18NService);
  secondaryLang = signal(this.i18n.getIsSecondaryLang());

  uomId = input.required<string>();
  uomTypeId = input.required<string>();
  amount = input(null, { transform: numberAttribute });
  decimalScale = input(null, { transform: numberAttribute });
  uomRatingScale = input.required<UomRatingScale[]>();

  julianDate(): Date {
    return addDaysFromGiulianDate(this.amount());
  }

  getUomRatingScaleDescription() {
    const uomRatingScale = this.uomRatingScale()?.find(x => x.uomId === this.uomId() && x.uomRatingValue === this.amount());
    return this.secondaryLang() ? uomRatingScale?.descriptionLang : uomRatingScale?.description;
  }
}
