import { Component, ElementRef, EventEmitter, HostBinding, OnInit, Output, booleanAttribute, input } from '@angular/core';

@Component({
  standalone: true,
  template: ''
})
export class GzoomGenericComponentComponent implements OnInit {
  protected element: ElementRef;
  protected readonly selector = '';
  public disabled = input<boolean>();
  public i18n = input<boolean>(true);

  public style = input<Record<string, string>>();
  public outlined = input(false, { transform: booleanAttribute });
  @HostBinding('attr.style') computedStyle = '';

  constructor(elem: ElementRef) {
    this.element = elem;
    this.selector = elem.nativeElement.tagName.toLowerCase();
  }

  ngOnInit(): void {
    if (this.style()) {
      this.computedStyle = Object.entries(this.style())
        .map(([key, value]) => `--${this.selector}-${key}: ${value};`)
        .join(' ');
    }
  }

  // eslint-disable-next-line @angular-eslint/no-output-on-prefix
  @Output() onClick = new EventEmitter<Event>(); // todo remove?
  handleClick(event: Event) {
    this.onClick.emit(event);
  }
}
