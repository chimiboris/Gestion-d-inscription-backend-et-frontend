import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPieceJustificative } from '../piece-justificative.model';
import { PieceJustificativeService } from '../service/piece-justificative.service';

export const pieceJustificativeResolve = (route: ActivatedRouteSnapshot): Observable<null | IPieceJustificative> => {
  const id = route.params['id'];
  if (id) {
    return inject(PieceJustificativeService)
      .find(id)
      .pipe(
        mergeMap((pieceJustificative: HttpResponse<IPieceJustificative>) => {
          if (pieceJustificative.body) {
            return of(pieceJustificative.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default pieceJustificativeResolve;
