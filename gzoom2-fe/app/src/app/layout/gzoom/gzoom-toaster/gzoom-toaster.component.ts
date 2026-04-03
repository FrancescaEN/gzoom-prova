import { Component, ViewEncapsulation, computed, input } from '@angular/core';
import { ToastModule } from 'primeng/toast';
import { VarUtils } from '../../../commons/utils/var-utils';

@Component({
  selector: 'gzoom-toaster',
  standalone: true,
  imports: [ToastModule],
  templateUrl: './gzoom-toaster.component.html',
  styleUrl: './gzoom-toaster.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class GzoomToasterComponent {
  private DEFAULT_TIMEOUT_MS = 3000;
  id = input<string>(null);
  timeout = input<number>(this.DEFAULT_TIMEOUT_MS);

  // workaround for stickiness that cannot be set as a tag attribute :(
  life = computed(() => {
    return VarUtils.isDefined(this.timeout())
      ? (this.timeout() > 0 ? this.timeout() : Number.MAX_SAFE_INTEGER / 5)
      : this.DEFAULT_TIMEOUT_MS;
  });
}
