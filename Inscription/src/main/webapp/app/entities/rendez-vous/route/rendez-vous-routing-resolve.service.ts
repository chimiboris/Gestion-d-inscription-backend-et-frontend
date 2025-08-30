import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRendezVous } from '../rendez-vous.model';
import { RendezVousService } from '../service/rendez-vous.service';

export const rendezVousResolve = (route: ActivatedRouteSnapshot): Observable<null | IRendezVous> => {
  const id = route.params['id'];
  if (id) {
    return inject(RendezVousService)
      .find(id)
      .pipe(
        mergeMap((rendezVous: HttpResponse<IRendezVous>) => {
          if (rendezVous.body) {
            return of(rendezVous.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default rendezVousResolve;
