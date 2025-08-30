import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PieceJustificativeComponent } from './list/piece-justificative.component';
import { PieceJustificativeDetailComponent } from './detail/piece-justificative-detail.component';
import { PieceJustificativeUpdateComponent } from './update/piece-justificative-update.component';
import PieceJustificativeResolve from './route/piece-justificative-routing-resolve.service';

const pieceJustificativeRoute: Routes = [
  {
    path: '',
    component: PieceJustificativeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PieceJustificativeDetailComponent,
    resolve: {
      pieceJustificative: PieceJustificativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PieceJustificativeUpdateComponent,
    resolve: {
      pieceJustificative: PieceJustificativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PieceJustificativeUpdateComponent,
    resolve: {
      pieceJustificative: PieceJustificativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pieceJustificativeRoute;
