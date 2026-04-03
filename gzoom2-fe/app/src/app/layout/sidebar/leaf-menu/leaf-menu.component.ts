import {
  Component,
  OnInit,
  OnChanges,
  SimpleChanges,
  Input
} from '@angular/core';
import { FolderMenu, LeafMenu } from '../../../commons/model/dto';
import { MenuService } from '../../../shared/menu.service';
import { NavigationEnd, Router } from '@angular/router';

const DEF_ICON = 'fa-angle-right';

@Component({
  selector: 'app-leaf-menu',
  templateUrl: './leaf-menu.component.html',
  styleUrls: ['./leaf-menu.component.scss']
})
export class LeafMenuComponent implements OnInit, OnChanges {
  @Input() menu: FolderMenu;
  @Input() leaf: LeafMenu;
  @Input() context: FolderMenu;
  @Input('tabId') tabId: string;

  classes: string[];
  link: string[];
  disabled: boolean = false;

  constructor(private readonly menuService: MenuService,
    private router: Router
  ) { }

  ngOnInit() {
    this.init();
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.onNavigationEnd(event);
      }
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    this.init();
  }

  toggleSidebar() {
    window.sessionStorage.setItem('tabId', this.tabId); 

    const dom: any = document.querySelector('body');
    const menu: any = document.querySelector('#sidebar');
    if (menu.classList.contains('collapse')) {
      dom.classList.remove('push-right');
    } else {
      dom.classList.add('push-right');
    }
    menu.classList.toggle('collapse');
  }

  private init() {
    this.classes = this.classesOf(this.leaf);
    this.link = this.menuService.stateFor(this.context, this.menu, this.leaf);
  }

  private classesOf(m) {
    return m.classes && m.classes.length ? m.classes : DEF_ICON;
  }

  private onNavigationEnd(event: NavigationEnd) {
    const currentUrl = this.router.url;     
    if (currentUrl.includes(this.link.join('/'))  && !this.link.join('/').includes('legacy') ) {
      this.disabled = true;
    } else {
      this.disabled = false;
    }
    
  }
}
