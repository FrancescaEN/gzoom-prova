import {
  Component,
  OnInit,
  OnChanges,
  SimpleChanges,
  Input,
  Output,
  EventEmitter,
} from '@angular/core';

import { LeafMenu } from '../../../commons/model/dto';
import { FolderMenu } from '../../../commons/model/dto';

const OPEN_ICON = 'fa-folder-open';
const CLOSE_ICON = 'fa-folder';

@Component({
  selector: 'app-folder-menu',
  templateUrl: './folder-menu.component.html',
  styleUrls: ['./folder-menu.component.scss']
})
export class FolderMenuComponent implements OnInit, OnChanges {
  @Input() menu: FolderMenu;
  @Input() context: FolderMenu;
  @Input('tabId') tabId: string;

  classes: string[];
  @Input() expanded: boolean = true;
  @Input() onTopScroll: boolean = false;

  @Output() tabExpanded = new EventEmitter();

  constructor() { }

  ngOnInit() {
    this.classes = this.classesOf(this.menu);
  }

  ngOnChanges(changes: SimpleChanges) {
    this.classes = this.classesOf(this.menu);
  }

  private classesOf(m) {
    return m.classes && m.classes.length ? m.classes : (this.expanded) ? OPEN_ICON : CLOSE_ICON;
  }

  menuType(item: { children?: any[] }): string {
    return item.children !== undefined && item.children !== null ? 'folder' : 'leaf';
  }

  toggleExpanded(menuId) {

    this.removeClasseExpandFromRootMenu();
    this.expanded = !this.expanded;
    this.classes = this.classesOf(this.menu);
    if (this.expanded) this.tabExpanded.emit(menuId);
  }

  removeClasseExpandFromRootMenu(): void {
    const elementiLi = document.querySelectorAll('nested-menu.li.expand');
    elementiLi.forEach((elemento) => {
      elemento.classList.remove('expand');
    });
  }

  scroll() {
    const element = document.querySelector('#sidebar');
    if (element) {
      element.scrollTop = 0;
    }
  }
}
