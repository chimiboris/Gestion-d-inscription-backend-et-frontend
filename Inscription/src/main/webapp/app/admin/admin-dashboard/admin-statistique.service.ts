import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AdminStatistiqueService {
  private resourceUrl = '/api/admin/statistiques';

  constructor(private http: HttpClient) {}

  getStatistiques(): Observable<Record<string, number>> {
    return this.http.get<Record<string, number>>(this.resourceUrl);
  }
}
