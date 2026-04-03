import { map } from "rxjs/operators";
import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { UserPreference } from "../../shared/user-preference";
import { provideAnimations } from "@angular/platform-browser/animations";
import { AuthService } from "app/commons/service/auth.service";
import { ThemeService } from "app/commons/service/theme.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
  providers: [provideAnimations()],
})
export class LoginComponent implements OnInit {
  returnUrl: string;
  loading_login: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private themeService: ThemeService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.route.data
      .pipe(map((data: { theme: UserPreference }) => data.theme))
      .subscribe((data) => {
        if (data == undefined) {
          this.loading_login = false;
          return;
        } else {
          this.loading_login = false;
          this.themeService.switchStyle(data.userPrefValue);
        }
      });

    if (this.authService.isLoggedIn()) {
      this.router.navigate(["/c/dashboard"]);
    }
  }
}
