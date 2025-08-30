import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ParcoursComponent } from './list/parcours.component';
import { ParcoursDetailComponent } from './detail/parcours-detail.component';
import { ParcoursUpdateComponent } from './update/parcours-update.component';
import ParcoursResolve from './route/parcours-routing-resolve.service';

const parcoursRoute: Routes = [
  {
    path: '',
    component: ParcoursComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParcoursDetailComponent,
    resolve: {
      parcours: ParcoursResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParcoursUpdateComponent,
    resolve: {
      parcours: ParcoursResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParcoursUpdateComponent,
    resolve: {
      parcours: ParcoursResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default parcoursRoute;
