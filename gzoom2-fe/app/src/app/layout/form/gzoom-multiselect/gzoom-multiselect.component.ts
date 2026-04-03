import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormGroupDirective } from '@angular/forms';
import { orderBy } from 'lodash';
import { SelectItem } from 'primeng/api';
@Component({
  selector: 'gzoom-multiselect',
  templateUrl: './gzoom-multiselect.component.html',
  styleUrls: ['./gzoom-multiselect.component.scss']
})
export class GzoomMultiselectComponent implements OnInit {

  @Input() options: SelectItem[];
  @Input() controlName: string;
  @Input() virtualScroll: boolean = false;
  @Input() virtualScrollItemSize: number = 38;
  @Input() showClear: boolean = false;
  @Input() placeholder: string = "Select";

  form: FormGroup;
  get formField() { return this.form.get(this.controlName) }

  constructor(private rootFormGroup: FormGroupDirective) { }

  ngOnInit(): void {
    this.form = this.rootFormGroup.control as FormGroup;
  }

  onSelectionChange(event: { originalEvent: PointerEvent, value: string[], itemValue: string }): void {
    const { originalEvent, value: values, itemValue } = event;
    if (itemValue)
      values?.forEach(value => {
        let selectedItem = this.options.find(x => x.value === value);
        this.options.splice(this.options.indexOf(selectedItem), 1) // remove the item from list
        this.options.unshift(selectedItem)// this will add selected item on the top 
      })
  }

  onClear() {
    this.options = orderBy(this.options, ['label', 'value']);
  }
}
