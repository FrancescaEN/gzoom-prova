import { Component, ElementRef, ViewEncapsulation, booleanAttribute, input, model, numberAttribute } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SelectItem } from 'primeng/api';
import { DropdownChangeEvent, DropdownModule } from 'primeng/dropdown';
import { MultiSelectChangeEvent, MultiSelectModule } from 'primeng/multiselect';
import { I18nModule } from '../../../../i18n/i18n.module';
import { GzoomGenericInputComponent } from '../gzoom-generic-input/gzoom-generic-input.component';

@Component({
  selector: 'gzoom-dropdown',
  standalone: true,
  imports: [
    DropdownModule,
    MultiSelectModule,
    I18nModule,
    FormsModule
  ],
  templateUrl: './gzoom-dropdown.component.html',
  styleUrl: './gzoom-dropdown.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class GzoomDropdownComponent extends GzoomGenericInputComponent {
  // https://v17.primeng.org/dropdown
  // https://v17.primeng.org/multiselect

  options = input.required<SelectItem[]>();
  selected = model<string | string[]>();

  multiselect = input(false, { transform: booleanAttribute });
  showClear = input(false, { transform: booleanAttribute });
  virtualScroll = input(false, { transform: booleanAttribute });
  virtualScrollItemSize = input(38, { transform: numberAttribute });
  /**
   * @var listPosition
   * Alignment of the dropdown panel with options
   * if set to right --> options panel right edge is aligned with the input right edge
   */
  listPosition = input<'right' | ''>('', { alias: 'options-list-position-align' });
  optionLabel = input<string>('label');
  optionValue = input<string>('value');
  placeholder = input<string>('Select');

  // multiple choice:
  display = input<'comma' | 'chip'>('comma');
  filter = input(false, { transform: booleanAttribute });
  showToggleAll = input(false, { transform: booleanAttribute });
  showHeader = input(false, { transform: booleanAttribute });

  onChange = input<(value: any) => void>(() => { });

  /* Style of the dropdown panel needs to be passed separately in case the panel is attached to body:
  <gzoom-dropdown [panelStyle]="{'--gzoom-dropdown-item-padding': '2px 4px',
                                 '--gzoom-dropdown-item-color': '#003556',
                                 '--gzoom-dropdown-item-selected-background-color': '#0064B4'}" />
   */
  panelStyle = input<{ [propertyName: string]: any; } | null | undefined>(null);
  panelStyleClass = input<string | null | undefined>(null);

  constructor(elem: ElementRef) {
    super(elem)
    // substituted with valueChanged, so it is not triggered on initialization:
    // effect(() => {
    //   if (this.onChange()) {
    //     this.onChange()(this.selected());
    //   }
    // });
  }

  valueChanged(event: DropdownChangeEvent | MultiSelectChangeEvent): void {
    this.onChange()(event.value);
  }
}

/*
Usage:
<!--
cities: SelectItem[] | undefined = [
    { label: 'New York', value: 'NY' },
    { label: 'Rome', value: 'RM' },
    { label: 'London', value: 'LDN' },
    { label: 'Istanbul', value: 'IST' },
    { label: 'Paris', value: 'PRS' }
  ];
-->
<gzoom-dropdown [id]="'dropdown_1'"
                      [options]="cities"
                      placeholder="Choose"
                      virtualScroll
/>
&nbsp;
<!--
  cities2 = [
    { name: 'New York', code: 'NY' },
    { name: 'Rome', code: 'RM' },
    { name: 'London', code: 'LDN' },
    { name: 'Istanbul', code: 'IST' },
    { name: 'Paris', code: 'PRS' }
  ];
selected = 'IST';
-->
<gzoom-dropdown [options]="yearsDropdown"
                [(selected)]="selectedYear"
                [onChange]="yearChangedHandler" //  yearChangedHandler = (value: string) => { ... }
/>
<gzoom-dropdown [id]="'dropdown_2'"
                      [options]="cities2"
                      [(selected)]="selected"
                      showClear
                      optionLabel="name"
                      optionValue="code"
/>
&nbsp;
<!-- selectedValues = ['IST', 'LDN']; -->
<gzoom-dropdown [id]="'dropdown_3'"
                      [options]="cities2"
                      [(selected)]="selectedValues"
                      optionLabel="name"
                      optionValue="code"
                      placeholder="Choose"
                      multiselect
/>
&nbsp;
<gzoom-dropdown [id]="'dropdown_5'"
                      [options]="cities2"
                      [(selected)]="selectedValues"
                      optionLabel="name"
                      optionValue="code"
                      placeholder="Choose"
                      display="chip"
                      virtualScroll
                      multiselect
                      disabled
/>
 */
