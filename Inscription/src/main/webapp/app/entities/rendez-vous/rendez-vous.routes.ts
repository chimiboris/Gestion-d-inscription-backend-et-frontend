import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RendezVousComponent } from './list/rendez-vous.component';
import { RendezVousDetailComponent } from './detail/rendez-vous-detail.component';
import { RendezVousUpdateComponent } from './update/rendez-vous-update.component';
import RendezVousResolve from './route/rendez-vous-routing-resolve.service';

const rendezVousRoute: Routes = [
  {
    path: '',
    component: RendezVousComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RendezVousDetailComponent,
    resolve: {
      rendezVous: RendezVousResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RendezVousUpdateComponent,
    resolve: {
      rendezVous: RendezVousResolve,
    },
    //canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RendezVousUpdateComponent,
    resolve: {
      rendezVous: RendezVousResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default rendezVousRoute;
