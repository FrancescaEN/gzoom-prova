import { Component, computed, effect, OnInit, signal } from "@angular/core";
import {
  ActivatedRoute,
  Router,
  NavigationEnd,
  Event,
  RouterEvent,
} from "@angular/router";
import { filter, distinctUntilChanged } from "rxjs/operators";
import { MenuItem } from "primeng/api";
import { I18NService } from "../../i18n/i18n.service";
import { BreadCrumbs } from "./breadcrumb";

@Component({
  selector: "app-breadcrumb",
  templateUrl: "./breadcrumb.component.html",
  styleUrls: ["./breadcrumb.component.scss"],
})
export class BreadcrumbComponent implements OnInit {
  
  public home: BreadCrumbs = { icon: 'pi pi-home', url: null};
  public breadcrumbs = signal<BreadCrumbs[]> ([]);
  
  breadcrumbsLenght = computed<number>( () => this.breadcrumbs().length )
  breadcrumbsLast = computed( () => { return this.breadcrumbs().map((x,index) => {
    if( index === this.breadcrumbsLenght() - 1) {
      x.leaf = true
    }
    return x
    })})

  constructor(
    private router: Router,
    public readonly i18nService: I18NService,
    private activatedRoute: ActivatedRoute,
  ) {
    this.breadcrumbs.set( this.buildBreadCrumb(this.activatedRoute.root))
  }

  ngOnInit() {
    this.router.events
      .pipe(
        filter((event: Event | RouterEvent) => event instanceof NavigationEnd),
        distinctUntilChanged(),
      )
      .subscribe(() => {
        this.breadcrumbs.set( this.buildBreadCrumb(this.activatedRoute.root));
      });
  }

  insertBreadCrumb(breadcrumbs: BreadCrumbs[] = [], label) {
    let breadcrumb: BreadCrumbs;
    breadcrumb = {
      label: this.i18nService.translate(label),
    };

    if (breadcrumb.label) {
      breadcrumbs.push(breadcrumb);
    }
  }

  /**
   * Recursively build breadcrumb according to activated route.
   * @param route
   * @param url
   * @param breadcrumbs
   */

  buildBreadCrumb(
    route: ActivatedRoute,
    breadcrumbs: BreadCrumbs[] = []
  ): BreadCrumbs[] {

    if (route.routeConfig && route.routeConfig.data?.breadcrumb) {           
      this.insertBreadCrumb(breadcrumbs, route.routeConfig.data.breadcrumb);
    }else if(route.routeConfig && route.routeConfig.path == 'legacy'){
      let nextUrl = this.router.url;
      let tempArrayNextUrl = nextUrl.split("/").splice(3, 5);

      this.insertBreadCrumb(breadcrumbs, tempArrayNextUrl[0]);
      this.insertBreadCrumb(breadcrumbs, tempArrayNextUrl[1]);
      this.insertBreadCrumb(breadcrumbs, tempArrayNextUrl[2]);
      
    }

    if (route.firstChild) {
      return this.buildBreadCrumb(route.firstChild, breadcrumbs);
    }
    
    return breadcrumbs;
  }
}
