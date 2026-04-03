import { Component, EventEmitter, Input, OnInit, Output, output } from '@angular/core';
import { FormGroup, FormGroupDirective } from '@angular/forms';
import { I18NService } from 'app/i18n/i18n.service';
import { SelectItem } from 'primeng/api';

@Component({
  selector: 'gzoom-search-filter',
  templateUrl: './gzoom-search-filter.component.html',
  styleUrls: ['./gzoom-search-filter.component.scss']
})
export class GzoomSearchFilterComponent implements OnInit {

  @Input() label: string;
  @Input() controlNameSearchField: string;
  @Input() controlNameMachMode: string;
  @Input() placeholder: string = "Search";

  @Output() clearButton = new EventEmitter();
  @Output() keyUpEvent = new EventEmitter();
  onEnter = output();

  constraints: SelectItem[] = [
    { label: this.i18nService.translate("Start with"), value: ConstraintsSearch.START_WITH },
    { label: this.i18nService.translate("Contains"), value: ConstraintsSearch.CONTAINS },
    { label: this.i18nService.translate("Not contains"), value: ConstraintsSearch.NOT_CONTAINS },
    { label: this.i18nService.translate("Ends with"), value: ConstraintsSearch.ENDS_WITH },
    { label: this.i18nService.translate("Equals"), value: ConstraintsSearch.EQUALS },
    { label: this.i18nService.translate("Not equals"), value: ConstraintsSearch.NOT_EQUALS },

  ];

  form: FormGroup;

  get searchField() { return this.form.get(this.controlNameSearchField); }

  constructor(private rootFormGroup: FormGroupDirective,
    private i18nService: I18NService) { }

  ngOnInit(): void {

    this.form = this.rootFormGroup.control as FormGroup;
  }

  clearValue() {
    this.searchField.reset();
    this.clearButton.emit();
  }

  onSubmit(event: SubmitEvent) {
    this.onEnter.emit();
  }

  onSearch(event: SubmitEvent){
    this.keyUpEvent.emit();
    // this.onEnter.emit();
    
  }
}

export enum ConstraintsSearch {
  START_WITH = "startsWith",
  CONTAINS = "contains",
  NOT_CONTAINS = "notContains",
  ENDS_WITH = "endsWith",
  EQUALS = "equals",
  NOT_EQUALS = "notEquals"
}
