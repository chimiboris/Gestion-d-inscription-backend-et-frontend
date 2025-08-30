import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRendezVous, NewRendezVous } from '../rendez-vous.model';

export type PartialUpdateRendezVous = Partial<IRendezVous> & Pick<IRendezVous, 'id'>;

type RestOf<T extends IRendezVous | NewRendezVous> = Omit<T, 'dateRdv'> & {
  dateRdv?: string | null;
};

export type RestRendezVous = RestOf<IRendezVous>;

export type NewRestRendezVous = RestOf<NewRendezVous>;

export type PartialUpdateRestRendezVous = RestOf<PartialUpdateRendezVous>;

export type EntityResponseType = HttpResponse<IRendezVous>;
export type EntityArrayResponseType = HttpResponse<IRendezVous[]>;

@Injectable({ providedIn: 'root' })
export class RendezVousService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rendez-vous');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(rendezVous: NewRendezVous): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rendezVous);
    return this.http
      .post<RestRendezVous>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(rendezVous: IRendezVous): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rendezVous);
    return this.http
      .put<RestRendezVous>(`${this.resourceUrl}/${this.getRendezVousIdentifier(rendezVous)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(rendezVous: PartialUpdateRendezVous): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rendezVous);
    return this.http
      .patch<RestRendezVous>(`${this.resourceUrl}/${this.getRendezVousIdentifier(rendezVous)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRendezVous>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRendezVous[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRendezVousIdentifier(rendezVous: Pick<IRendezVous, 'id'>): number {
    return rendezVous.id;
  }

  compareRendezVous(o1: Pick<IRendezVous, 'id'> | null, o2: Pick<IRendezVous, 'id'> | null): boolean {
    return o1 && o2 ? this.getRendezVousIdentifier(o1) === this.getRendezVousIdentifier(o2) : o1 === o2;
  }

  addRendezVousToCollectionIfMissing<Type extends Pick<IRendezVous, 'id'>>(
    rendezVousCollection: Type[],
    ...rendezVousToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rendezVous: Type[] = rendezVousToCheck.filter(isPresent);
    if (rendezVous.length > 0) {
      const rendezVousCollectionIdentifiers = rendezVousCollection.map(rendezVousItem => this.getRendezVousIdentifier(rendezVousItem)!);
      const rendezVousToAdd = rendezVous.filter(rendezVousItem => {
        const rendezVousIdentifier = this.getRendezVousIdentifier(rendezVousItem);
        if (rendezVousCollectionIdentifiers.includes(rendezVousIdentifier)) {
          return false;
        }
        rendezVousCollectionIdentifiers.push(rendezVousIdentifier);
        return true;
      });
      return [...rendezVousToAdd, ...rendezVousCollection];
    }
    return rendezVousCollection;
  }

  protected convertDateFromClient<T extends IRendezVous | NewRendezVous | PartialUpdateRendezVous>(rendezVous: T): RestOf<T> {
    return {
      ...rendezVous,
      dateRdv: rendezVous.dateRdv?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRendezVous: RestRendezVous): IRendezVous {
    return {
      ...restRendezVous,
      dateRdv: restRendezVous.dateRdv ? dayjs(restRendezVous.dateRdv) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRendezVous>): HttpResponse<IRendezVous> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRendezVous[]>): HttpResponse<IRendezVous[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
