import { Injectable, inject } from "@angular/core";
import {
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  CanActivateFn,
  CanActivateChildFn,
  UrlTree,
} from "@angular/router";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable, of, mergeMap, map, reduce, catchError } from "rxjs";
import { trimStart } from "lodash";

import { AuthService } from "./auth.service";
import { LockoutService } from "./lockout.service";
import { UserPermissionService } from "app/shared/user-permission.service";
import { MenuService as MenuServiceGpMenu } from "app/shared/menu.service";
import { MenuService } from "./menu.service";
import { Location } from "@angular/common";
import { LogoutService } from "./logout.service";
import { KeycloakAuthService } from "./keycloak-auth.service";
import { FolderMenu } from "../model/dto";
import { TRISTATECHECKBOX_VALUE_ACCESSOR } from "primeng/tristatecheckbox";
import { AuthModeService } from "./auth-mode.service";

declare function jwt_decode(token: string): any;
const TOKEN_KEY = "auth-token";
const HTTP_HEADERS = new HttpHeaders();
@Injectable({ providedIn: "root" })
export class AuthGuard {
  private useKeycloak: boolean;

  userPermissionService = inject(UserPermissionService);
  router = inject(Router);
  MenuServiceGpMenu = inject(MenuServiceGpMenu);
  menuService = inject(MenuService);
  location = inject(Location);
  logoutSrv = inject(LogoutService);

  constructor(
    private authService: AuthService,
    private http: HttpClient,
    private lockout: LockoutService,
    private keycloakService: KeycloakAuthService,
    private authMode: AuthModeService,
  ) {
    this.useKeycloak = this.authMode.isKeycloakEnabled();
  }
  /**
   * Determines if a route can be activated.
   * Checks if the user has a valid token and redirects to the login page if not.
   *
   * @param route - The activated route snapshot.
   * @param state - The router state snapshot.
   * @param out - Optional flag indicating if the user is logged out.
   * @returns A boolean or a promise resolving to a boolean.
   */
  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
    out?: boolean,
  ): Promise<boolean> {
    const from = route.queryParamMap.get("from");

    if (from === "kc-update-password") {
      if (this.authMode.isKeycloakEnabled()) {
        await this.keycloakService.logout();
      }
      return false;
    }

    if (this.authMode.isKeycloakEnabled()) {
      // Modalità Keycloak
      const loggedIn = await this.keycloakService.isLoggedIn();
      if (!loggedIn) {
        await this.keycloakService.login();
        return false;
      }
      return true;
    }

    // Modalità nativa
    const token = localStorage.getItem("auth-token");

    if (token) {
      try {
        const expire = jwt_decode(token).exp;
        if (Date.now() < expire * 1000) {
          return true;
        }
      } catch {
        console.warn("Token non valido");
      }
    }
    console.log("Token not found or expired!");
    // Redirect to login if the token is invalid or missing
    return this.logoutSrv
      .handleLoginRedirection(state.url, out)
      .then((result) => {
        return result;
      });
  }

  /**
   * Determines if a child route can be activated.
   * Delegates the check to the `canActivate` method.
   *
   * @param route - The activated route snapshot.
   * @param state - The router state snapshot.
   * @returns A boolean or a promise resolving to a boolean.
   */
  canActivateChild(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
    out?: boolean,
  ): boolean | Promise<boolean> {
    return this.canActivate(route, state, out);
  }

  /**
   * Checks if the user has the required permissions to activate a route.
   * Redirects to the dashboard if the user lacks permissions.
   *
   * @param route - The activated route snapshot.
   * @param state - The router state snapshot.
   * @returns An observable resolving to a boolean or a UrlTree.
   */
  canActivateCheckPermission(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): Observable<boolean | UrlTree> {
    const contextKey = route.data.context;

    return this.userPermissionService.hasAnyPermission(contextKey).pipe(
      map((hasPermission) => {
        if (hasPermission) {
          return true; // User has the required permissions
        } else {
          return this.router.parseUrl("/dashboard"); // Redirect to dashboard
        }
      }),
      catchError((err) => {
        console.error("Error while checking permissions:", err);
        this.lockout.lockout(); // Lockout the user if an error occurs
        return of(false);
      }),
    );
  }

  /**
   * Checks if the user has access to a specific menu item.
   * Redirects to the dashboard if the user lacks access.
   *
   * @param route - The activated route snapshot.
   * @param state - The router state snapshot.
   * @returns An observable resolving to a boolean or a UrlTree.
   */
  canActivateCheckPermissionItem(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): Observable<boolean | UrlTree> {
    const nextRouteWithoutRoot = trimStart(state.url, "/c/");
    const gpMenu = this.MenuServiceGpMenu.getGP_MENU([nextRouteWithoutRoot]);

    return this.menuService.getRootMenu().pipe(
      mergeMap((menu) => menu.children),
      mergeMap((child0) => child0.children),
      mergeMap((child1: FolderMenu) => child1.children),
      mergeMap((child2: FolderMenu) => child2.children),
      map((child3) => child3.id === gpMenu),
      reduce((acc, currentValue) => acc || currentValue, false), // Check if any child matches the ID
      map((hasAccess) =>
        hasAccess ? true : this.router.parseUrl("/c/dashboard"),
      ),
    );
  }
}

/**
 * Angular route guards for protecting routes and child routes.
 */
export const canActivateAuthGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
) => {
  return inject(AuthGuard).canActivate(route, state, true);
};

export const canActivateChildAuthGuard: CanActivateChildFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
) => {
  return inject(AuthGuard).canActivateChild(route, state, true);
};

export const canActivateLoginGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
) => {
  return inject(AuthGuard).canActivate(route, state, true);
};

export const canActivateCheckPermissionGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
) => {
  return inject(AuthGuard).canActivateCheckPermission(route, state);
};

export const canActivateCheckPermissionItemGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
) => {
  return inject(AuthGuard).canActivateCheckPermissionItem(route, state);
};
