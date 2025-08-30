import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactUrgence } from '../contact-urgence.model';
import { ContactUrgenceService } from '../service/contact-urgence.service';

export const contactUrgenceResolve = (route: ActivatedRouteSnapshot): Observable<null | IContactUrgence> => {
  const id = route.params['id'];
  if (id) {
    return inject(ContactUrgenceService)
      .find(id)
      .pipe(
        mergeMap((contactUrgence: HttpResponse<IContactUrgence>) => {
          if (contactUrgence.body) {
            return of(contactUrgence.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default contactUrgenceResolve;
