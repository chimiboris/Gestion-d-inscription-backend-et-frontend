import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPieceJustificative, NewPieceJustificative } from '../piece-justificative.model';

export type PartialUpdatePieceJustificative = Partial<IPieceJustificative> & Pick<IPieceJustificative, 'id'>;

type RestOf<T extends IPieceJustificative | NewPieceJustificative> = Omit<T, 'dateUpload'> & {
  dateUpload?: string | null;
};

export type RestPieceJustificative = RestOf<IPieceJustificative>;

export type NewRestPieceJustificative = RestOf<NewPieceJustificative>;

export type PartialUpdateRestPieceJustificative = RestOf<PartialUpdatePieceJustificative>;

export type EntityResponseType = HttpResponse<IPieceJustificative>;
export type EntityArrayResponseType = HttpResponse<IPieceJustificative[]>;

@Injectable({ providedIn: 'root' })
export class PieceJustificativeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/piece-justificatives');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(pieceJustificative: NewPieceJustificative): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pieceJustificative);
    return this.http
      .post<RestPieceJustificative>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pieceJustificative: IPieceJustificative): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pieceJustificative);
    return this.http
      .put<RestPieceJustificative>(`${this.resourceUrl}/${this.getPieceJustificativeIdentifier(pieceJustificative)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pieceJustificative: PartialUpdatePieceJustificative): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pieceJustificative);
    return this.http
      .patch<RestPieceJustificative>(`${this.resourceUrl}/${this.getPieceJustificativeIdentifier(pieceJustificative)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPieceJustificative>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPieceJustificative[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPieceJustificativeIdentifier(pieceJustificative: Pick<IPieceJustificative, 'id'>): number {
    return pieceJustificative.id;
  }

  comparePieceJustificative(o1: Pick<IPieceJustificative, 'id'> | null, o2: Pick<IPieceJustificative, 'id'> | null): boolean {
    return o1 && o2 ? this.getPieceJustificativeIdentifier(o1) === this.getPieceJustificativeIdentifier(o2) : o1 === o2;
  }

  addPieceJustificativeToCollectionIfMissing<Type extends Pick<IPieceJustificative, 'id'>>(
    pieceJustificativeCollection: Type[],
    ...pieceJustificativesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pieceJustificatives: Type[] = pieceJustificativesToCheck.filter(isPresent);
    if (pieceJustificatives.length > 0) {
      const pieceJustificativeCollectionIdentifiers = pieceJustificativeCollection.map(
        pieceJustificativeItem => this.getPieceJustificativeIdentifier(pieceJustificativeItem)!,
      );
      const pieceJustificativesToAdd = pieceJustificatives.filter(pieceJustificativeItem => {
        const pieceJustificativeIdentifier = this.getPieceJustificativeIdentifier(pieceJustificativeItem);
        if (pieceJustificativeCollectionIdentifiers.includes(pieceJustificativeIdentifier)) {
          return false;
        }
        pieceJustificativeCollectionIdentifiers.push(pieceJustificativeIdentifier);
        return true;
      });
      return [...pieceJustificativesToAdd, ...pieceJustificativeCollection];
    }
    return pieceJustificativeCollection;
  }

  protected convertDateFromClient<T extends IPieceJustificative | NewPieceJustificative | PartialUpdatePieceJustificative>(
    pieceJustificative: T,
  ): RestOf<T> {
    return {
      ...pieceJustificative,
      dateUpload: pieceJustificative.dateUpload?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPieceJustificative: RestPieceJustificative): IPieceJustificative {
    return {
      ...restPieceJustificative,
      dateUpload: restPieceJustificative.dateUpload ? dayjs(restPieceJustificative.dateUpload) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPieceJustificative>): HttpResponse<IPieceJustificative> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPieceJustificative[]>): HttpResponse<IPieceJustificative[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
