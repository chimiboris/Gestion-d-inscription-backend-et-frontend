import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParcours, NewParcours } from '../parcours.model';

export type PartialUpdateParcours = Partial<IParcours> & Pick<IParcours, 'id'>;

type RestOf<T extends IParcours | NewParcours> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestParcours = RestOf<IParcours>;

export type NewRestParcours = RestOf<NewParcours>;

export type PartialUpdateRestParcours = RestOf<PartialUpdateParcours>;

export type EntityResponseType = HttpResponse<IParcours>;
export type EntityArrayResponseType = HttpResponse<IParcours[]>;

@Injectable({ providedIn: 'root' })
export class ParcoursService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parcours');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(parcours: NewParcours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(parcours);
    return this.http
      .post<RestParcours>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(parcours: IParcours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(parcours);
    return this.http
      .put<RestParcours>(`${this.resourceUrl}/${this.getParcoursIdentifier(parcours)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(parcours: PartialUpdateParcours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(parcours);
    return this.http
      .patch<RestParcours>(`${this.resourceUrl}/${this.getParcoursIdentifier(parcours)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestParcours>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestParcours[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getParcoursIdentifier(parcours: Pick<IParcours, 'id'>): number {
    return parcours.id;
  }

  compareParcours(o1: Pick<IParcours, 'id'> | null, o2: Pick<IParcours, 'id'> | null): boolean {
    return o1 && o2 ? this.getParcoursIdentifier(o1) === this.getParcoursIdentifier(o2) : o1 === o2;
  }

  addParcoursToCollectionIfMissing<Type extends Pick<IParcours, 'id'>>(
    parcoursCollection: Type[],
    ...parcoursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const parcours: Type[] = parcoursToCheck.filter(isPresent);
    if (parcours.length > 0) {
      const parcoursCollectionIdentifiers = parcoursCollection.map(parcoursItem => this.getParcoursIdentifier(parcoursItem)!);
      const parcoursToAdd = parcours.filter(parcoursItem => {
        const parcoursIdentifier = this.getParcoursIdentifier(parcoursItem);
        if (parcoursCollectionIdentifiers.includes(parcoursIdentifier)) {
          return false;
        }
        parcoursCollectionIdentifiers.push(parcoursIdentifier);
        return true;
      });
      return [...parcoursToAdd, ...parcoursCollection];
    }
    return parcoursCollection;
  }

  protected convertDateFromClient<T extends IParcours | NewParcours | PartialUpdateParcours>(parcours: T): RestOf<T> {
    return {
      ...parcours,
      dateDebut: parcours.dateDebut?.toJSON() ?? null,
      dateFin: parcours.dateFin?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restParcours: RestParcours): IParcours {
    return {
      ...restParcours,
      dateDebut: restParcours.dateDebut ? dayjs(restParcours.dateDebut) : undefined,
      dateFin: restParcours.dateFin ? dayjs(restParcours.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestParcours>): HttpResponse<IParcours> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestParcours[]>): HttpResponse<IParcours[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
