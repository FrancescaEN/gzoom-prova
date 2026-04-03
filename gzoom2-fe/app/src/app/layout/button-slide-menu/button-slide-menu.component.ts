import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { MenuItem } from 'app/commons/model/dto';

@Component({
  selector: 'app-button-slide-menu',
  templateUrl: './button-slide-menu.component.html',
  styleUrls: ['./button-slide-menu.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ButtonSlideMenuComponent {

  @Input() items: MenuItem[];
  @Input() disabled: boolean;
  @Input() icon: String;

}
