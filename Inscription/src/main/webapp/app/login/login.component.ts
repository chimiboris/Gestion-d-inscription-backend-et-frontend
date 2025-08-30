import { Component, ViewChild, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { FormGroup, FormControl, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import {RecaptchaModule} from "ng-recaptcha";

@Component({
  selector: 'jhi-login',
  standalone: true,
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterModule, RecaptchaModule],
  templateUrl: './login.component.html',
})
export default class LoginComponent implements OnInit, AfterViewInit {
  @ViewChild('username', { static: false })
  username!: ElementRef;

  authenticationError = false;

  recaptchaError = false;

  loginForm = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    rememberMe: new FormControl(false, { nonNullable: true, validators: [Validators.required] }),
  });

  constructor(
    private accountService: AccountService,
    private loginService: LoginService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']);
      }
    });
  }

  // loginWithOAuth(provider: string): void {
  //   window.location.href = `/oauth2/authorization/${provider}`;
  // }

  captchaToken: string | null = null;

  onCaptchaResolved(token: string | null): void {
    this.captchaToken = token;
  }


  ngAfterViewInit(): void {
    this.username.nativeElement.focus();
  }

  loginWithGoogle(): void {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  }

  loginWithMicrosoft(): void {
    window.location.href = 'http://localhost:8080/oauth2/authorization/azure';
  }


  login(): void {
    const loginPayload: any = this.loginForm.getRawValue();

    if (!loginPayload.username.includes('@google.com')) {
      loginPayload.recaptchaToken = this.captchaToken;
    }

    this.loginService.login(loginPayload).subscribe({
      next: () => {
        this.authenticationError = false;
        this.recaptchaError = false;
        this.router.navigate(['']);
      },
      error: err => {
        this.authenticationError = true;
        this.recaptchaError = err.status === 403;
      },
    });
  }

}
