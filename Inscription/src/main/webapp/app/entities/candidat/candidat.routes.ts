import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CandidatComponent } from './list/candidat.component';
import { CandidatDetailComponent } from './detail/candidat-detail.component';
import { CandidatUpdateComponent } from './update/candidat-update.component';
import CandidatResolve from './route/candidat-routing-resolve.service';

const candidatRoute: Routes = [
  {
    path: '',
    component: CandidatComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CandidatDetailComponent,
    resolve: {
      candidat: CandidatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CandidatUpdateComponent,
    resolve: {
      candidat: CandidatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CandidatUpdateComponent,
    resolve: {
      candidat: CandidatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default candidatRoute;
