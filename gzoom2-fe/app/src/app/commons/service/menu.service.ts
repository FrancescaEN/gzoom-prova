import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { ApiClientService } from './client.service';
import { RootMenu } from '../model/dto';
import { map, tap } from 'rxjs/operators';

const MENU_ENDPOINT = 'menu';

/**
 * API service that operates on menus.
 */
@Injectable()
export class MenuService {
  private rootMenu: RootMenu;

  constructor(private readonly client: ApiClientService) { }

  menu(): Observable<RootMenu> {
    return this.client.get(MENU_ENDPOINT).pipe(tap((values) => {
      this.rootMenu = values;
      localStorage.setItem("menu", JSON.stringify(values));
    }));
  }

  getRootMenu(): Observable<RootMenu> {
    const menu = JSON.parse(localStorage.getItem("menu"));
    return menu ? of(menu) : this.menu();
  }

  getHelpId(contentIdTo: string) {
    return this.client.get('/help/' + contentIdTo).pipe(
      map(json => json as string)
    );
  }

  getMenuPath(contentIdTo: string): Observable<string> {
    return this.client.get(`${MENU_ENDPOINT}/getpath/${contentIdTo}`).pipe(
      map(path => path as string)
    );
  }
}
