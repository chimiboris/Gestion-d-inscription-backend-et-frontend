import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParcours } from '../parcours.model';
import { ParcoursService } from '../service/parcours.service';

export const parcoursResolve = (route: ActivatedRouteSnapshot): Observable<null | IParcours> => {
  const id = route.params['id'];
  if (id) {
    return inject(ParcoursService)
      .find(id)
      .pipe(
        mergeMap((parcours: HttpResponse<IParcours>) => {
          if (parcours.body) {
            return of(parcours.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default parcoursResolve;
