import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, ElementRef, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { ApiConfig } from 'app/commons/model/api-config';
import { AuthService } from 'app/commons/service/auth.service';
import { LdapService } from 'app/commons/service/ldap.service';
import { filter, lastValueFrom } from 'rxjs';

const CHECK_USER_ENDPOINT = 'check-user';
const LOGIN_ENDPOINT = 'login';

@Component({
  selector: 'app-check-user',
  templateUrl: './check-user.component.html',
  styleUrls: ['./check-user.component.css']
})
export class CheckUserComponent implements OnInit {

  model: any = {};
  loading = false;
  private readonly checkUserUrl: string;
  private readonly loginUrl: string;
  returnUrl: string;
  resetUrl: string;
  error = '';
  showDiv2 : boolean = false;
  allowChangePassword: boolean = true;


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private http: HttpClient, 
    private ldap: LdapService,
    private apiConfig: ApiConfig) {
      this.checkUserUrl = `${apiConfig.rootPath}/${CHECK_USER_ENDPOINT}`;
      this.loginUrl = `${apiConfig.rootPath}/${LOGIN_ENDPOINT}`;
  }

  async ngOnInit(): Promise<void> {
    const qru = this.route.snapshot.queryParams['returnUrl'];    
    this.returnUrl = qru && qru !== 'login' && qru !== '/login' ? qru : '/';

    if (this.authService.isLoggedIn()) {
      this.router.navigate([this.returnUrl]);
    }

    this.resetUrl = window.location.href;
    this.resetUrl = this.resetUrl.substring(0, this.resetUrl.lastIndexOf('/'));

  }

   /**
   * Attempts to reset the user in.
   */
   reset() {
    this.loading = true;    
    const body = JSON.stringify({ username: this.model.username, url: this.resetUrl});

    this.http.post(this.checkUserUrl, body, {headers: new HttpHeaders().set('Content-Type', 'application/json')})
    .subscribe({
        next: (data: any) => {
          let token = data.token;
          this.authService.save(token, true); // TODO fix this with this.model.remember
          this.loading = true;
          this.showDiv2 = true;
        },
        error: (err) => {
          console.log(err)
          this.authService.lockout(); // sanity check
          this.error = 'Username or Email is incorrect';
          this.loading = false;
        }, // error
        complete: () => console.log('Check User Complete') // complete
      });

  }

  back(){
    this.router.navigate([this.returnUrl]);
  }

  onKeyUp(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.reset();
    }
  }

}
