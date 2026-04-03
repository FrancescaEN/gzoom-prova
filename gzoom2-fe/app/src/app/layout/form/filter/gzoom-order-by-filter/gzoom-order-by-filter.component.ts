import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, FormGroupDirective } from '@angular/forms';
import { PrimeIcons, SelectItem } from 'primeng/api';
import { Subscription, distinctUntilChanged, tap } from 'rxjs';

@Component({
  selector: 'gzoom-order-by-filter',
  templateUrl: './gzoom-order-by-filter.component.html',
  styleUrls: ['./gzoom-order-by-filter.component.scss']
})
export class GzoomOrderByFilterComponent implements OnInit, OnDestroy {
  @Input() orderByColumn: SelectItem[];
  @Input() controlNameOrderBy: string;
  @Input() controlNameOrderType: string;

  iconOrder: string = PrimeIcons.SORT_ALT;
  form: FormGroup;
  changeIcon$: Subscription;

  get orderBy() { return this.form.get(this.controlNameOrderBy); }
  get orderType() { return this.form.get(this.controlNameOrderType); }

  constructor(private rootFormGroup: FormGroupDirective) { }

  ngOnDestroy(): void {
    this.changeIcon$?.unsubscribe();
  }

  ngOnInit(): void {

    this.form = this.rootFormGroup.control as FormGroup;

    if (this.orderType.value)
      this.iconOrder = this.orderType.value == 'asc' ? PrimeIcons.SORT_ALPHA_DOWN : PrimeIcons.SORT_ALPHA_UP

    this.changeIcon$ = this.orderBy.valueChanges
      .pipe(
        distinctUntilChanged(),
        tap(value => {
          if (value == null) {
            this.iconOrder = PrimeIcons.SORT_ALT;
          }
          else if (this.iconOrder === PrimeIcons.SORT_ALT && value) {
            this.iconOrder = PrimeIcons.SORT_ALPHA_DOWN;
            this.orderType.setValue('asc');
          }
        })
      )
      .subscribe();
  }

  clickOrder(event: PointerEvent) {
    switch (this.iconOrder) {
      case PrimeIcons.SORT_ALT:
        this.iconOrder = PrimeIcons.SORT_ALPHA_UP;
        break;
      case PrimeIcons.SORT_ALPHA_UP:
        this.iconOrder = PrimeIcons.SORT_ALPHA_DOWN;
        this.orderType.setValue('asc');
        this.form.markAsDirty();
        break;
      case PrimeIcons.SORT_ALPHA_DOWN:
        this.iconOrder = PrimeIcons.SORT_ALPHA_UP;
        this.orderType.setValue('desc');
        this.form.markAsDirty();
        break;
    }

  }

}
