import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ContactUrgenceComponent } from './list/contact-urgence.component';
import { ContactUrgenceDetailComponent } from './detail/contact-urgence-detail.component';
import { ContactUrgenceUpdateComponent } from './update/contact-urgence-update.component';
import ContactUrgenceResolve from './route/contact-urgence-routing-resolve.service';

const contactUrgenceRoute: Routes = [
  {
    path: '',
    component: ContactUrgenceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactUrgenceDetailComponent,
    resolve: {
      contactUrgence: ContactUrgenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactUrgenceUpdateComponent,
    resolve: {
      contactUrgence: ContactUrgenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactUrgenceUpdateComponent,
    resolve: {
      contactUrgence: ContactUrgenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contactUrgenceRoute;
