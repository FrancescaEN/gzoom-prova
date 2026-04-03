// todo enums
type iconDisplay = 'input' | 'button';
type dateSelectionMode = 'single' | 'multiple' | 'range';

export class DateOptions {
  minDate?: Date;
  maxDate?: Date;
  dateFormat: string = 'dd/mm/yy';
  showIcon: boolean = true;
  showClear: boolean = true;
  showButtonBar: boolean = true;
  showOnFocus: boolean = true;
  iconDisplay: iconDisplay = 'input';
  selectionMode: dateSelectionMode = 'single';
  maxDateCount: number | null = null;

  constructor(data?: DateOptions) {
    this.minDate = data?.minDate ?? undefined;
    this.maxDate = data?.maxDate ?? undefined;
    this.dateFormat = data?.dateFormat ?? 'dd/mm/yy';
    this.showIcon = data?.showIcon ?? true;
    this.showClear = data?.showClear ?? true;
    this.showButtonBar = data?.showButtonBar ?? true;
    this.showOnFocus = data?.showOnFocus ?? true;
    this.iconDisplay = data?.iconDisplay ?? 'input';
    this.selectionMode = data?.selectionMode ?? 'single';
    this.maxDateCount = data?.maxDateCount ?? null;
  }
}
