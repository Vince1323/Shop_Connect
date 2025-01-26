import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Avis } from '../model/Avis';

@Injectable({
  providedIn: 'root',
})
export class AvisService {
  private readonly API_URL = 'http://localhost:9292/api';
  private readonly ENDPOINT_AVIS = '/avis';

  constructor(private httpClient: HttpClient) {}

  insertAvis(avis: Avis): Observable<Avis> {
    return this.httpClient.post<Avis>(
      `${this.API_URL}${this.ENDPOINT_AVIS}`,
      avis
    ).pipe(catchError(this.handleError));
  }

  getAllAvis(): Observable<Avis[]> {
    return this.httpClient.get<Avis[]>(
      `${this.API_URL}${this.ENDPOINT_AVIS}`
    ).pipe(catchError(this.handleError));
  }

  updateAvis(id: number, avis: Avis): Observable<Avis> {
    return this.httpClient.put<Avis>(
      `${this.API_URL}${this.ENDPOINT_AVIS}/${id}`,
      avis
    ).pipe(catchError(this.handleError));
  }

  deleteAvis(id: number): Observable<void> {
    const url = `${this.API_URL}${this.ENDPOINT_AVIS}/${id}`;
    console.log('Suppression de l\'avis avec URL :', url); // Vérifie l'URL construite
    return this.httpClient.delete<void>(url).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Erreur HTTP :', error.message);
    return throwError(() => new Error('Erreur lors de la communication avec le serveur.'));
  }
}
