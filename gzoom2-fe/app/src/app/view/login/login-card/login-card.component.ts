import {
  AfterContentInit,
  AfterViewInit,
  Component,
  ElementRef,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from "@angular/core";
import {
  catchError,
  distinctUntilChanged,
  filter,
  map,
  switchMap,
} from "rxjs/operators";
import { Router, ActivatedRoute } from "@angular/router";
import { HttpHeaders } from "@angular/common/http";
import { HttpClient } from "@angular/common/http";
import { AuthService } from "app/commons/service/auth.service";
import { ApiConfig } from "app/commons/model/api-config";
import { UserPreference } from "app/shared/user-preference";
import { ApiClientService } from "app/commons/service/client.service";
import { LdapService } from "app/commons/service/ldap.service";
import { Observable, lastValueFrom, of, throwError } from "rxjs";
import {
  animate,
  state,
  style,
  transition,
  trigger,
} from "@angular/animations";
import { UserPreferenceService } from "app/api/service/user-preference.service";
import { DialogService, DynamicDialogRef } from "primeng/dynamicdialog";
import { Login2fauthComponent } from "../login-2fauth/login-2fauth.component";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { I18NService } from "app/i18n/i18n.service";
import { NoteDataService } from "app/api/service/note-data.service";
import { AuthModeService } from "app/commons/service/auth-mode.service";

const LOGIN_ENDPOINT = "login";
const TWOFAUTH_ENDPOINT = "twoFAuthEnable";
const VERIFY_ENDPOINT = "verifyOtp";
const VERIFY_USER_ENDPOINT = "verify-user";

@Component({
  selector: "app-login-card",
  templateUrl: "./login-card.component.html",
  styleUrls: ["./login-card.component.css"],
})
export class LoginCardComponent implements OnInit, OnDestroy {
  model = new FormGroup({
    username: new FormControl("", Validators.required),
    password: new FormControl("", Validators.required),
  });
  loading = false;
  error = "";
  returnUrl: string;
  private readonly loginUrl: string;
  private readonly twoFAuthUrl: string;
  private readonly twoFAuthUrlEnable: string;
  private readonly verifyUserUrl: string;

  node: Node;
  loading_login: boolean = true;
  fieldTextType: boolean;
  enableChangePsw$: Observable<boolean>;
  allowChangePassword$: Observable<boolean>;
  otp: string = "";

  enable2FAuth: boolean;
  enableldap: any;
  visible: boolean = false;
  ref: DynamicDialogRef | undefined;
  organizationSelected: string;
  titleLogin: string = null;

  isKeycloak: boolean = this.authMode.isKeycloakEnabled();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private client: ApiClientService,
    private authService: AuthService,
    private noteDataService: NoteDataService,
    private readonly userPreferenceService: UserPreferenceService,
    private http: HttpClient,
    private apiConfig: ApiConfig,
    private i18nService: I18NService,
    public dialogService: DialogService,
    private authMode: AuthModeService,
  ) {
    this.loginUrl = `${apiConfig.rootPath}/${LOGIN_ENDPOINT}`;
    this.twoFAuthUrl = `${apiConfig.rootPath}/${TWOFAUTH_ENDPOINT}`;
    this.twoFAuthUrlEnable = `${apiConfig.rootPath}/${VERIFY_ENDPOINT}`;
    this.verifyUserUrl = `${apiConfig.rootPath}/${VERIFY_USER_ENDPOINT}`;
  }

  ngOnDestroy(): void {
    if (this.ref) {
      this.ref.destroy();
    }
  }

  async ngOnInit() {
    const qru = this.route.snapshot.queryParams["returnUrl"];
    this.returnUrl = qru && qru !== "login" && qru !== "/login" ? qru : "/";

    this.model.valueChanges.subscribe((changes) => {
      console.log("Model changed:", changes);
      // Perform actions based on changes
    });

    const enableChangePsw$ = this.client
      .get("/profile/enableChangePassword")
      .pipe(map((json) => json as string));
    this.allowChangePassword$ = enableChangePsw$.pipe(
      map((boolResponse) => {
        if (boolResponse) {
          return true;
        }
        return false;
      }),
    );

    const mailHost$ = this.client
      .get("/profile/mail-host")
      .pipe(map((json) => json as string));
    this.enableChangePsw$ = mailHost$.pipe(
      map((mailHost) => {
        if (mailHost && mailHost != "N") {
          this.enable2FAuth = true;
          return true;
        } else {
          return false;
        }
      }),
    );

    const ldap$ = this.client
      .get("/profile/isLdap")
      .pipe(map((json) => json as string));
    await lastValueFrom(ldap$).then((data) => {
      this.enableldap = data;
    });

    if (this.authService.isLoggedIn()) {
      this.router.navigate([this.returnUrl]);
    }

    this.route.data
      .pipe(map((data: { node: Node }) => data.node))
      .subscribe((data) => {
        console.log("login data " + data);
        this.node = data;
      });

    const userPref$ =
      this.userPreferenceService.getUserPreferenceNA("ORGANIZATION_PARTY");
    await lastValueFrom(userPref$).then((data) => {
      if (data && data.userPrefValue && data.userPrefValue !== "DEFAULT") {
        this.organizationSelected = data.userPrefValue;
      }
    });

    const noteData$ = this.noteDataService.getNoteDataByPrefValue(
      this.organizationSelected,
    );
    await lastValueFrom(noteData$).then((data) => {
      if (data) {
        this.titleLogin = data.noteInfo;
      }
    });
  }

  /**
   * Attempts to log the user in.
   */
  login(event?: Event) {
    event?.preventDefault();
    event?.stopPropagation();

    console.log("LOGIN CLICK");
    if (!this.loading) {
      this.loading = true;
      this.checkUser();
    }
  }

  onUsernameChange(event) {
    if (event.length > 0) {
      const elementToRemove = document.getElementById("username-help");
      if (elementToRemove) {
        elementToRemove.remove();
      }
    }
  }

  onPasswordChange(event) {
    if (event.length > 0) {
      const elementToRemove = document.getElementById("password-help");
      if (elementToRemove) {
        elementToRemove.remove();
      }
    }
  }

  checkUser() {
    const body = JSON.stringify({
      username: this.model["username"],
      password: this.model["password"],
    });

    if (this.error) {
      const elementToRemove = document.getElementById("errorDiv");
      elementToRemove.remove();
    }

    this.error = null;

    this.http
      .post(this.verifyUserUrl, body, {
        headers: new HttpHeaders().set("Content-Type", "application/json"),
      })
      .subscribe({
        next: (data: any) => {
          if (
            this.enable2FAuth &&
            !this.enableldap &&
            data.userPrefValue == "Y"
          ) {
            this.twoFAuthVerified();
          } else {
            this.loginConfirm();
          }
        },
        error: (err) => {
          console.log(err);
          this.loading = false;
          // this.error = this.i18nService.translate("Invalid username or password");
          this.error = err.error.message;
        },
        complete: () => console.log("Check User Complete"),
      });
  }

  twoFAuthVerified() {
    this.ref = this.dialogService.open(Login2fauthComponent, {
      header: "Two Factory Authentication",

      styleClass: "min-width p-dialog-width",
      contentStyle: { overflow: "auto" },
      data: {
        model: this.model,
      },
    });

    this.ref.onClose.subscribe(() => {
      this.loading = false;
    });
  }

  loginConfirm() {
    const body = JSON.stringify({
      username: this.model["username"],
      password: this.model["password"],
    });

    this.http
      .post(this.loginUrl, body, {
        headers: new HttpHeaders().set("Content-Type", "application/json"),
      })
      .subscribe({
        next: (data: any) => {
          this.loading = false;
          let token = data.token;
          this.authService.save(token, true);
          this.router.navigate([this.returnUrl]);
        },
        error: (err) => {
          console.log(err);
          this.loading = false;
          this.authService.lockout();
          this.error = "Username or password is incorrect";
        }, // error
        complete: () => console.log("login Complete"),
      });
  }

  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }

  onKeyUp(event: KeyboardEvent) {
    if (event.key === "Enter") {
      this.login();
    }
  }
}
