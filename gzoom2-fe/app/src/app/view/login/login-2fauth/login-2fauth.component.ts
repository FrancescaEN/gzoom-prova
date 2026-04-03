import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, ElementRef, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ContactMechService } from 'app/api/service/contact-mech.service';
import { ApiConfig } from 'app/commons/model/api-config';
import { AuthService } from 'app/commons/service/auth.service';
import { ApiClientService } from 'app/commons/service/client.service';
import {Location} from '@angular/common';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { lastValueFrom } from 'rxjs';
import { DialogService, DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';

const LOGIN_ENDPOINT = 'login';
const TWOFAUTH_ENDPOINT = 'twoFAuthEnable';
const VERIFY_ENDPOINT = 'verifyOtp';
const VERIFY_USER_ENDPOINT = 'verify-user';

@Component({
  selector: 'gzoom-login-2fauth',
  templateUrl: './login-2fauth.component.html',
  styleUrls: ['./login-2fauth.component.css']
})
export class Login2fauthComponent implements OnInit {

  otp: string;
  error = '';
  visibleDialog: boolean = false;
  visibleDialogEnable: boolean = false;
  returnUrl: string;
  dialogConfig = inject(DynamicDialogConfig);

  model: any = {};

  private readonly loginUrl: string;
  private readonly twoFAuthUrl: string;
  private readonly twoFAuthUrlEnable: string;
  private readonly verifyUserUrl: string;

  constructor(
    public ref: DynamicDialogRef,
    private route: ActivatedRoute,
    private router: Router,
    private client: ApiClientService,
    private authService: AuthService,
    private userPreferenceService: UserPreferenceService,
    private location: Location,
    private http: HttpClient, 
    private apiConfig: ApiConfig) {
      this.loginUrl = `${apiConfig.rootPath}/${LOGIN_ENDPOINT}`;
      this.twoFAuthUrl = `${apiConfig.rootPath}/${TWOFAUTH_ENDPOINT}`;
      this.twoFAuthUrlEnable = `${apiConfig.rootPath}/${VERIFY_ENDPOINT}`;
      this.verifyUserUrl = `${apiConfig.rootPath}/${VERIFY_USER_ENDPOINT}`;
    
  }

  async ngOnInit(): Promise<void> {

    const qru = this.route.snapshot.queryParams['returnUrl'];
    this.returnUrl = qru && qru !== 'dashboard' && qru !== '/dashboard' ? qru : '/dashboard';

    this.model = this.dialogConfig.data.model;
    this.twoFAuthVerified();
  }

  twoFAuthVerified(){
    const body = JSON.stringify({ username: this.model.username });

    this.http
      .post(this.twoFAuthUrl, body, {
        headers: new HttpHeaders().set('Content-Type', 'application/json'),
      }).subscribe({
        next: (data: any) => {          
        },
        error: (err) => {
          console.log(err);
          this.error = err.error.message;
          
        }, // error
        complete: () => console.log('twoFAuthVerified Complete') 
      });
  }
  
  verifyOTP(){
    this.error = '';
    const body = JSON.stringify({ username: this.model.username, token: this.otp });

    this.http
      .post(this.twoFAuthUrlEnable, body, {
        headers: new HttpHeaders().set('Content-Type', 'application/json'),
      }).subscribe({
        next: () => {
          this.loginConfirm();
        },
        error: (err) => {
          console.log(err);
          this.error = err.error.message;
        }, // error
        complete: () => console.log('verifyOTP Complete') 
      });
  }

  loginConfirm(){

    const body = JSON.stringify({ username: this.model.username, password: this.model.password });

        this.http
      .post(this.loginUrl, body, {
        headers: new HttpHeaders().set('Content-Type', 'application/json'),
      }).subscribe({
        next: (data: any) => {
          let token = data.token;
          this.authService.save(token, true); 
          this.ref.close();
          this.router.navigate([this.returnUrl]);
        },
        error: (err) => {
          console.log(err)
          this.authService.lockout(); 
          this.error = 'Username or password is incorrect';
        }, // error
        complete: () => console.log('login Complete') 
      });
  }

  close(){    
    this.router.navigate([this.returnUrl]);
  }

}
