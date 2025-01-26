import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Boutique } from '../model/Boutique';

@Injectable({
  providedIn: 'root',
})
export class BoutiqueService {
  private readonly API_URL = 'http://localhost:9292/api';
  private readonly ENDPOINT_BOUTIQUE = '/boutiques';

  constructor(private httpClient: HttpClient) {}

  insertBoutique(boutique: Boutique): Observable<Boutique> {
    return this.httpClient.post<Boutique>(
      `${this.API_URL}${this.ENDPOINT_BOUTIQUE}`,
      boutique
    ).pipe(catchError(this.handleError));
  }

  getAllBoutiques(): Observable<Boutique[]> {
    return this.httpClient.get<Boutique[]>(
      `${this.API_URL}${this.ENDPOINT_BOUTIQUE}`
    ).pipe(catchError(this.handleError));
  }

  updateBoutique(id: number, boutique: Boutique): Observable<Boutique> {
    return this.httpClient.put<Boutique>(
      `${this.API_URL}${this.ENDPOINT_BOUTIQUE}/${id}`,
      boutique
    ).pipe(catchError(this.handleError));
  }

  deleteBoutique(id: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.API_URL}${this.ENDPOINT_BOUTIQUE}/${id}`
    ).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('HTTP Error', error.message);
    return throwError(() => new Error('Server communication error'));
  }
}
