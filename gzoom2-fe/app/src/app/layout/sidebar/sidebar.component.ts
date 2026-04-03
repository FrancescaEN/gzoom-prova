import { ChangeDetectorRef, Component, ElementRef, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';


import { RootMenu, FolderMenu } from '../../commons/model/dto';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  showMenu = '';
  roots: Observable<FolderMenu[]>;
  defaultTabActive: string;

  menuIdOpen;

  constructor(private readonly route: ActivatedRoute,
    private changeDetector: ChangeDetectorRef) { }

  ngOnInit() {
    const idTabSelected = window.sessionStorage.getItem('tabId');

    this.roots = this.route.data.pipe(map((data: { menu: RootMenu }) => {
      if (idTabSelected) {
        let menu = data.menu.children.findIndex(item => item.id === idTabSelected);
        this.defaultTabActive = "ngb-tab-" + menu;  //the id's tab are "ngb-tab-INDEX"
      }

      if (data.menu.children.length == 1 && data.menu.children[0].children.length == 1)
        this.menuIdOpen = data.menu.children[0].children[0].id; //for open the first folder

      return data.menu.children
    }))
  }

  ngAfterViewInit() {
    this.changeDetector.detectChanges();  //detect the "defaultTabActive" change (no throw error)
  }

  addExpandClass(element: any) {
    if (element === this.showMenu) {
      this.showMenu = '0';
    } else {
      this.showMenu = element;
    }
  }

  menuType(item: { children?: any[] }): string {
    return item.children !== undefined && item.children !== null ? 'folder' : 'leaf';
  }

  tabExpanded(menuId) {
    this.menuIdOpen = menuId;
  }

}
