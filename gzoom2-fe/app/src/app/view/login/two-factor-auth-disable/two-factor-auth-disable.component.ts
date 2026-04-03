import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, ElementRef, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ContactMechService } from 'app/api/service/contact-mech.service';
import { ApiConfig } from 'app/commons/model/api-config';
import { AuthService } from 'app/commons/service/auth.service';
import { ApiClientService } from 'app/commons/service/client.service';
import {Location} from '@angular/common';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { lastValueFrom } from 'rxjs';
import { DynamicDialogRef } from 'primeng/dynamicdialog';

@Component({
  selector: 'gzoom-two-factor-auth-disable',
  templateUrl: './two-factor-auth-disable.component.html',
  styleUrls: ['./two-factor-auth-disable.component.css']
})
export class TwoFactorAuthDisableComponent implements OnInit {

  otp: string;
  error = '';
  visibleDialog: boolean = false;
  visibleDialogDisable: boolean = false;
  visibleDialogEnable: boolean = false;
  visibleDialogDisableConfirm: boolean = false;
  returnUrl: string;

  visible: boolean = true;

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
    // this.loginUrl = `${apiConfig.rootPath}/${LOGIN_ENDPOINT}`;
    
  }

  async ngOnInit(): Promise<void> {

    const qru = this.route.snapshot.queryParams['returnUrl'];
    this.returnUrl = qru && qru !== 'dashboard' && qru !== '/dashboard' ? qru : '/dashboard';
    
    const userPrefValue$ = this.userPreferenceService.getUserPreference('2FAEnabled')
    let twoFAuth :string;
    await lastValueFrom(userPrefValue$).then(
      data => { twoFAuth = data.userPrefValue;
      });  
      
    if(twoFAuth == 'Y'){
      this.visibleDialogDisableConfirm = false;
      this.visibleDialogDisable = true;
    }

    console.log(this.visibleDialogDisable);
    

  }

  disable(){
    this.client
    .get("/twoFAuth/disableTwoFactoryAuth").subscribe({
      next: () => {
          this.visibleDialogDisable = false;
          this.visibleDialogDisableConfirm = true;
      },
      error: (err) => {
        console.log(err)
        this.error = err.error.message;
      },
      complete: () => console.log('Check User Complete') 
    });
  }


  close(){    
    this.ref.close();
    this.visibleDialogDisable = false;
    this.visibleDialogDisableConfirm = false;
   
  }

}
