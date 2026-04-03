import { Injectable } from "@angular/core";
import { lastValueFrom, Observable } from "rxjs";
import { ApiClientService } from "./client.service";
import { HttpClient } from "@angular/common/http";
import { LockoutService } from "./lockout.service";
import { AuthModeService } from "./auth-mode.service";
import { KeycloakAuthService } from "./keycloak-auth.service";

const LOGOUT_ENDPOINT = "logout";
const LOGIN_METHOD_ENDPOINT = "/rest/api/getLoginMethod";
const ONELOGIN_LOGOUT_URL_ENDPOINT = "/rest/api/getOneLogin-LogoutUrl";
const ONELOGIN_LOGIN_URL_ENDPOINT = "/rest/api/getOneLogin-LoginUrl";

/**
 * Allows user to log out and handles login redirection logic.
 */
@Injectable()
export class LogoutService {
  constructor(
    private readonly client: ApiClientService,
    private http: HttpClient,
    private readonly lockoutSrv: LockoutService,
    private authMode: AuthModeService,
    private keycloakAuth: KeycloakAuthService,
  ) {}

  /**
   * Logs the user out and handles redirection based on the login method.
   * This method ensures that the user is properly redirected or logged out.
   *
   * @return {Promise<boolean>} A promise that resolves to `true` if the user is redirected,
   *                            or `false` if no redirection occurs.
   */
  async logout(): Promise<boolean> {
    try {
      if (this.authMode.isKeycloakEnabled()) {
        // ✅ Nessun redirect manuale, lascia gestire a Keycloak
        await this.keycloakAuth.logout();
        this.lockoutSrv.lockoutLogout();
      } else {
        // Perform the logout operation
        await lastValueFrom(this.client.post(LOGOUT_ENDPOINT));
        this.lockoutSrv.lockoutLogout();

        // Handle redirection based on the login method
        return await this.handleRedirection();
      }
    } catch (err) {
      console.error("Error during logout process:", err);
      return false; // No redirection occurred
    }
  }

  /**
   * Handles login redirection logic based on the login method.
   *
   * @param {string} stateUrl - The URL to redirect to after login.
   * @param {boolean} out - Indicates if the user is logged out.
   * @return {Promise<boolean>} A promise that resolves to `true` if the user is redirected,
   *                            or `false` if no redirection occurs.
   */
  async handleLoginRedirection(
    stateUrl: string,
    out: boolean,
  ): Promise<boolean> {
    try {
      // Handle redirection based on the login method
      return await this.handleRedirection(stateUrl, out);
    } catch (err) {
      console.error("Error during login redirection process:", err);
      return false; // No redirection occurred
    }
  }

  /**
   * Handles redirection logic based on the login method.
   *
   * @param {string} [stateUrl] - Optional URL to redirect to after login.
   * @param {boolean} [out] - Indicates if the user is logged out.
   * @return {Promise<boolean>} A promise that resolves to `true` if the user is redirected,
   *                            or `false` if no redirection occurs.
   */
  private async handleRedirection(
    stateUrl?: string,
    out?: boolean,
  ): Promise<boolean> {
    const loginMethod = await lastValueFrom(this.getLoginMethod());
    console.log("Login method:", loginMethod);

    if (loginMethod === "OneLogin") {
      // Fetch the OneLogin URL (login or logout) and redirect
      const url = stateUrl
        ? await lastValueFrom(this.getOneLoginLoginUrl())
        : await lastValueFrom(this.getOneLoginLogoutUrl());
      console.log("OneLogin URL:", url);
      window.location.href = url;
      return true; // User is redirected
    } else {
      if (out) {
        return true; // User is already logged out
      }
      this.lockoutSrv.returnToLogin();
      return true; // User is redirected
    }
  }

  /**
   * Fetches the login method from the backend.
   *
   * @return {Observable<string>} An observable that emits the login method.
   */
  public getLoginMethod(): Observable<string> {
    return this.http.get<string>(LOGIN_METHOD_ENDPOINT);
  }

  /**
   * Fetches the OneLogin logout URL from the backend.
   *
   * @return {Observable<string>} An observable that emits the OneLogin logout URL.
   */
  private getOneLoginLogoutUrl(): Observable<string> {
    return this.http.get<string>(ONELOGIN_LOGOUT_URL_ENDPOINT);
  }

  /**
   * Fetches the OneLogin login URL from the backend.
   *
   * @return {Observable<string>} An observable that emits the OneLogin login URL.
   */
  private getOneLoginLoginUrl(): Observable<string> {
    return this.http.get<string>(ONELOGIN_LOGIN_URL_ENDPOINT);
  }
}
