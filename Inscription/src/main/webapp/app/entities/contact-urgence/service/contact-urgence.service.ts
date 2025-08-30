import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactUrgence, NewContactUrgence } from '../contact-urgence.model';

export type PartialUpdateContactUrgence = Partial<IContactUrgence> & Pick<IContactUrgence, 'id'>;

export type EntityResponseType = HttpResponse<IContactUrgence>;
export type EntityArrayResponseType = HttpResponse<IContactUrgence[]>;

@Injectable({ providedIn: 'root' })
export class ContactUrgenceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-urgences');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(contactUrgence: NewContactUrgence): Observable<EntityResponseType> {
    return this.http.post<IContactUrgence>(this.resourceUrl, contactUrgence, { observe: 'response' });
  }

  update(contactUrgence: IContactUrgence): Observable<EntityResponseType> {
    return this.http.put<IContactUrgence>(`${this.resourceUrl}/${this.getContactUrgenceIdentifier(contactUrgence)}`, contactUrgence, {
      observe: 'response',
    });
  }

  partialUpdate(contactUrgence: PartialUpdateContactUrgence): Observable<EntityResponseType> {
    return this.http.patch<IContactUrgence>(`${this.resourceUrl}/${this.getContactUrgenceIdentifier(contactUrgence)}`, contactUrgence, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactUrgence>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactUrgence[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContactUrgenceIdentifier(contactUrgence: Pick<IContactUrgence, 'id'>): number {
    return contactUrgence.id;
  }

  compareContactUrgence(o1: Pick<IContactUrgence, 'id'> | null, o2: Pick<IContactUrgence, 'id'> | null): boolean {
    return o1 && o2 ? this.getContactUrgenceIdentifier(o1) === this.getContactUrgenceIdentifier(o2) : o1 === o2;
  }

  addContactUrgenceToCollectionIfMissing<Type extends Pick<IContactUrgence, 'id'>>(
    contactUrgenceCollection: Type[],
    ...contactUrgencesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contactUrgences: Type[] = contactUrgencesToCheck.filter(isPresent);
    if (contactUrgences.length > 0) {
      const contactUrgenceCollectionIdentifiers = contactUrgenceCollection.map(
        contactUrgenceItem => this.getContactUrgenceIdentifier(contactUrgenceItem)!,
      );
      const contactUrgencesToAdd = contactUrgences.filter(contactUrgenceItem => {
        const contactUrgenceIdentifier = this.getContactUrgenceIdentifier(contactUrgenceItem);
        if (contactUrgenceCollectionIdentifiers.includes(contactUrgenceIdentifier)) {
          return false;
        }
        contactUrgenceCollectionIdentifiers.push(contactUrgenceIdentifier);
        return true;
      });
      return [...contactUrgencesToAdd, ...contactUrgenceCollection];
    }
    return contactUrgenceCollection;
  }
}
