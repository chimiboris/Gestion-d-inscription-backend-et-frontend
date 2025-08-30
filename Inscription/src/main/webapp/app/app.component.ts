import { Component, OnInit } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import locale from '@angular/common/locales/en';
import dayjs from 'dayjs/esm';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
// jhipster-needle-angular-add-module-import JHipster will add new module here

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { fontAwesomeIcons } from './config/font-awesome-icons';
import MainComponent from './layouts/main/main.component';
import { TrackerService } from './core/tracker/tracker.service';
import {AccountService} from "./core/auth/account.service";

@Component({
  selector: 'jhi-app',
  standalone: true,
  template: '<jhi-main></jhi-main>',
  imports: [
    MainComponent,
    // jhipster-needle-angular-add-module JHipster will add new module here
  ],
})
export default class AppComponent implements OnInit {

  constructor(
    private accountService: AccountService,
    applicationConfigService: ApplicationConfigService,
    iconLibrary: FaIconLibrary,
    trackerService: TrackerService,
    dpConfig: NgbDatepickerConfig,
  ) {
    trackerService.setup();
    applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
    dpConfig.minDate = { year: dayjs().subtract(100, 'year').year(), month: 1, day: 1 };
  }


  ngOnInit(): void {
    const urlParams = new URLSearchParams(window.location.search);
    const jwt = urlParams.get('jwt');

    if (jwt) {
      localStorage.setItem('authenticationToken', JSON.stringify(jwt)); // ✅ JSON.stringify
      this.accountService.identity(true).subscribe(() => {
        window.history.replaceState({}, document.title, '/');
      });
    }

  }

}
