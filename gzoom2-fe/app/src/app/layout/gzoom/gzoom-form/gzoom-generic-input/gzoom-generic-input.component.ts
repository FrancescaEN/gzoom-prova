import { Component, booleanAttribute, input, model } from '@angular/core';
import { GzoomGenericComponentComponent } from '../../gzoom-generic/gzoom-generic.component';

@Component({
  standalone: true,
  template: ''
})
export class GzoomGenericInputComponent extends GzoomGenericComponentComponent {
  id = input<string>('');
  value = model<string>('');
  required = input(false, { transform: booleanAttribute });
  showInfo = input(false, { transform: booleanAttribute });
  showInfoMiddle = input(false, { transform: booleanAttribute });

  onValueChange(event: Event) {
    this.value.set((event.target as HTMLTextAreaElement).value);
  }
}
