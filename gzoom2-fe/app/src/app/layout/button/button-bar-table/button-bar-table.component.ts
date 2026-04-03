import { ChangeDetectionStrategy, Component, booleanAttribute, inject, input, output } from '@angular/core';
import { I18NService } from 'app/i18n/i18n.service';
import { LayoutModule } from 'app/layout/layout.module';

@Component({
  selector: 'gzoom-button-bar-table',
  standalone: true,
  imports: [LayoutModule],
  templateUrl: './button-bar-table.component.html',
  styleUrl: './button-bar-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ButtonBarTableComponent {
  i18n = inject(I18NService);

  backButton = input(false, { transform: booleanAttribute });
  backLabel = input(this.i18n.translate("Back"), { transform: (value: string) => value ?? this.i18n.translate("Back") });
  disabledBack = input(false, { transform: booleanAttribute });
  loadingBack = input(false, { transform: booleanAttribute });
  back = output();

  addButton = input(false, { transform: booleanAttribute });
  addLabel = input<string>(this.i18n.translate("New"));
  disabledAdd = input(false, { transform: booleanAttribute });
  loadingAdd = input(false, { transform: booleanAttribute });
  add = output();

  deleteButton = input(false, { transform: booleanAttribute });
  deleteLabel = input<string>(this.i18n.translate("Delete"));
  disabledDelete = input(false, { transform: booleanAttribute });
  loadingDelete = input(false, { transform: booleanAttribute });
  delete = output();

  resetButton = input(false, { transform: booleanAttribute });
  resetLabel = input<string>(this.i18n.translate("Reset"));
  disabledReset = input(false, { transform: booleanAttribute });
  loadingReset = input(false, { transform: booleanAttribute });
  reset = output();

  saveButton = input(false, { transform: booleanAttribute });
  saveLabel = input<string>(this.i18n.translate("Save"));
  disabledSave = input(false, { transform: booleanAttribute });
  loadingSave = input(false, { transform: booleanAttribute });
  save = output();

  rounded = input(false, { transform: booleanAttribute });

}
