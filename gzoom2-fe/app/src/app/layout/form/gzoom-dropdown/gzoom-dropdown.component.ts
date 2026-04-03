import { Component, Input, OnInit, booleanAttribute, numberAttribute } from '@angular/core';
import { FormGroup, FormGroupDirective } from '@angular/forms';
import { SelectItem } from 'primeng/api';

@Component({
  selector: 'gzoom-dropdown',
  templateUrl: './gzoom-dropdown.component.html',
  styleUrls: ['./gzoom-dropdown.component.css']
})
export class GzoomDropdownComponent implements OnInit {

  @Input() options: SelectItem[];
  @Input() controlName: string;
  @Input({ transform: booleanAttribute }) virtualScroll: boolean = false;
  @Input({ transform: numberAttribute }) virtualScrollItemSize: number = 38;
  @Input({ transform: booleanAttribute }) showClear: boolean = false;
  @Input() placeholder: string = "Select";
  @Input({ transform: booleanAttribute }) filter: boolean = false;
  @Input() filterBy: string = "label";
  @Input() loading: boolean = false;

  form: FormGroup;

  constructor(private rootFormGroup: FormGroupDirective) { }

  ngOnInit(): void {
    this.form = this.rootFormGroup.control as FormGroup;
  }

}
