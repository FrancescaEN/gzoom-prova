import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MenuItem } from 'app/commons/model/dto';

@Component({
  selector: 'gzoom-tab-menu',
  templateUrl: './gzoom-tab-menu.component.html',
  styleUrls: ['./gzoom-tab-menu.component.scss']
})
export class GzoomTabMenuComponent implements OnInit {
  

  @Input() menuDetails : boolean;
  @Input() activeIndexTabView: number = 0;
  @Input() itemsn : MenuItem;

  @Output() tabChangeEvent = new EventEmitter<any>();
  
  ngOnInit(): void {
    
  }

  tabChange(event) {
    this.activeIndexTabView = event.index;
    this.tabChangeEvent.emit(event);
  }

}
