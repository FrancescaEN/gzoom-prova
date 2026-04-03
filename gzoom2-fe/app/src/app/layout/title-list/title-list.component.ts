import { ChangeDetectionStrategy, Component, input } from '@angular/core';

@Component({
  selector: 'gzoom-title-list',
  standalone: true,
  template:
    `
    <label
    style="
      font-weight: 400;
      font-size: 18px;
      line-height: 28px;
      margin-bottom: 5px;
    "
    >{{ title() }}</label
  >  
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TitleListComponent {
  title = input<string>();
}
