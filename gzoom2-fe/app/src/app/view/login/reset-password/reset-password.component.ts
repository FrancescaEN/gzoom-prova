import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, ElementRef, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiConfig } from 'app/commons/model/api-config';
import { AuthService } from 'app/commons/service/auth.service';
import { I18NService } from 'app/i18n/i18n.service';

const LOGIN_ENDPOINT = 'login';
const RESET_ENDPOINT = 'reset-password';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  model: any = {};
  loading = false;
  private readonly loginUrl: string;
  private readonly resetUrl: string;
  returnUrl: string;
  error = '';
  token = '';
  showConfirm = false;
  showError = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private http: HttpClient, 
    private readonly i18nService: I18NService,
    private elementRef: ElementRef,
    private apiConfig: ApiConfig) {
      this.loginUrl = `${apiConfig.rootPath}/${LOGIN_ENDPOINT}`;
      this.resetUrl = `${apiConfig.rootPath}/${RESET_ENDPOINT}`;
  }

  ngOnInit(): void {
    const qru = this.route.snapshot.queryParams['returnUrl'];
    this.returnUrl = qru && qru !== 'login' && qru !== '/login' ? qru : '/';

    this.route.queryParams
      .subscribe(params => {
        this.token = params.token;
        this.model.username = params.username;
      }
      );
  }

   /**
   * Attempts to reset the user in.
   */
   reset() {
    this.loading = true;
    if(this.model.newPassword == this.model.confirmPassword){
      
      const body = JSON.stringify({ username: this.model.username,  newPassword : this.model.newPassword, token: this.token });

      this.http.post(this.resetUrl, body, {headers: new HttpHeaders().set('Content-Type', 'application/json')})
      .subscribe({
          next: () => {
            this.loading = false;
            // this.router.navigate([this.returnUrl]);
            this.showConfirm = true;
            const div = this.elementRef.nativeElement.querySelector('#div1');
            div.style.display = 'none';
          },
          error: (err) => {
            console.log(err)
            this.authService.lockout(); // sanity check
            this.error = 'Token is Expired';
            this.loading = false;
            this.showError = true;
            const div = this.elementRef.nativeElement.querySelector('#div1');
            div.style.display = 'none';
          }, // error
          complete: () => console.log('login Complete') // complete
        });
    }else{
      this.error = this.i18nService.translate('The two Password not match');
      this.loading = false;
    }
  }

  onKeyUp(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.reset();
    }
  }

}
