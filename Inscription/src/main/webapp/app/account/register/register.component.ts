import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormGroup, FormControl, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/config/error.constants';
import SharedModule from 'app/shared/shared.module';
import PasswordStrengthBarComponent from '../password/password-strength-bar/password-strength-bar.component';
import { RegisterService } from './register.service';

@Component({
  selector: 'jhi-register',
  standalone: true,
  imports: [SharedModule, RouterModule, FormsModule, ReactiveFormsModule, PasswordStrengthBarComponent],
  templateUrl: './register.component.html',
})

export default class RegisterComponent implements AfterViewInit {
  @ViewChild('login', { static: false })
  login?: ElementRef;

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;
  recaptchaError = false; // ✅ AJOUT ICI

  registerForm = new FormGroup({
    login: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    // ➕ Champs pour le Candidat
    nom: new FormControl('', Validators.required),
    prenom: new FormControl('', Validators.required),
    sexe: new FormControl('M', Validators.required),
    dateNaissance: new FormControl('', Validators.required),
    nationalite: new FormControl('', Validators.required),
    typePieceIdentite: new FormControl('CNI', Validators.required),
  });

  constructor(
    private translateService: TranslateService,
    private registerService: RegisterService,
  ) {}

  ngAfterViewInit(): void {
    if (this.login) {
      this.login.nativeElement.focus();
    }
  }

  // register(): void {
  //   this.doNotMatch = false;
  //   this.error = false;
  //   this.errorEmailExists = false;
  //   this.errorUserExists = false;
  //
  //   const { password, confirmPassword, login, email } = this.registerForm.getRawValue();
  //   if (password !== confirmPassword) {
  //     this.doNotMatch = true;
  //     return;
  //   }

  register(): void {
    this.doNotMatch = false;
    this.error = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;
    this.recaptchaError = false;
    this.success = false;

    const { password, confirmPassword, login, email } = this.registerForm.getRawValue();
    if (password !== confirmPassword) {
      this.doNotMatch = true;
      return;
    }

    this.registerService
      .save({ login, email, password, langKey: this.translateService.currentLang })
      .subscribe({
        next: () => {
          // ✅ Affiche le message de succès
          this.success = true;
          alert('✅ Votre compte a été créé. Veuillez vérifier vos e-mails pour l’activer.');

          // ✅ Optionnel : on tente de s’authentifier, mais sans erreur JSON
          fetch('/api/authenticate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: login, password }),
          })
            .then(async res => {
              if (!res.ok) {
                if (res.status === 403) {
                  throw new Error('Utilisateur non activé. Veuillez vérifier votre email.');
                }
                throw new Error('Erreur de connexion');
              }

              const auth = await res.json();
              localStorage.setItem('authenticationToken', auth.id_token);
              // Tu peux aussi faire une redirection ici si tu veux
              // this.router.navigate(['']);
            })
            .catch(err => {
              console.warn('⚠ Erreur d’authentification :', err.message);
              this.recaptchaError = err.message.includes('non activé');
              this.error = !this.recaptchaError;
            });
        },

        error: response => this.processError(response),
      });
  }

    // this.registerService
    //   .save({ login, email, password, langKey: this.translateService.currentLang })
    //   .subscribe({
    //     next: () => {
    //       this.success = true;
    //       alert('Votre compte a été créé. Veuillez vérifier vos e-mails pour l’activer.');
    //     },
    //     error: response => this.processError(response),
    //   });


    // Étape 1 : création du User
    // this.registerService
    //   .save({ login, email, password, langKey: this.translateService.currentLang })
    //   .subscribe({
    //     next: () => {
    //       // Étape 2 : authentification rapide
    //       fetch('/api/authenticate', {
    //         method: 'POST',
    //         headers: { 'Content-Type': 'application/json' },
    //         body: JSON.stringify({ username: login, password }),
    //       })
    //         .then(res => res.json())
    //         .then(auth => {
    //           localStorage.setItem('authenticationToken', auth.id_token);
    //           // Étape 3 : récupérer /api/account
    //           fetch('/api/account', {
    //             headers: { Authorization: `Bearer ${auth.id_token}` },
    //           })
    //             .then(res => res.json())
    //             .then(user => {
    //               // Étape 4 : créer le Candidat
    //               const {
    //                 nom,
    //                 prenom,
    //                 sexe,
    //                 dateNaissance,
    //                 nationalite,
    //                 typePieceIdentite,
    //               } = this.registerForm.getRawValue();
    //
    //               fetch('/api/candidats', {
    //                 method: 'POST',
    //                 headers: {
    //                   'Content-Type': 'application/json',
    //                   Authorization: `Bearer ${auth.id_token}`,
    //                 },
    //                 body: JSON.stringify({
    //                   nom,
    //                   prenom,
    //                   sexe,
    //                   dateNaissance,
    //                   nationalite,
    //                   typePieceIdentite,
    //                   user: { login: user.login },
    //                 }),
    //               })
    //                 .then(res => {
    //                   if (res.ok) {
    //                     this.success = true;
    //                   } else {
    //                     this.error = true;
    //                   }
    //                 });
    //             });
    //         });
    //     },
    //     error: response => this.processError(response),
    //   });



  // register(): void {
  //   this.doNotMatch = false;
  //   this.error = false;
  //   this.errorEmailExists = false;
  //   this.errorUserExists = false;
  //
  //   const { password, confirmPassword } = this.registerForm.getRawValue();
  //   if (password !== confirmPassword) {
  //     this.doNotMatch = true;
  //   } else {
  //     const { login, email } = this.registerForm.getRawValue();
  //     this.registerService
  //       .save({ login, email, password, langKey: this.translateService.currentLang })
  //       .subscribe({ next: () => (this.success = true), error: response => this.processError(response) });
  //   }
  // }

  private processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
  }
}
