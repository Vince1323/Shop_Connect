import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Paiement } from '../model/Paiement';

@Injectable({
  providedIn: 'root',
})
export class PaiementService {
  private readonly API_URL = 'http://localhost:9292/api';
  private readonly ENDPOINT_PAIEMENT = '/paiements';

  constructor(private httpClient: HttpClient) {}

  insertPaiement(paiement: Paiement): Observable<Paiement> {
    return this.httpClient.post<Paiement>(
      `${this.API_URL}${this.ENDPOINT_PAIEMENT}`,
      paiement
    ).pipe(catchError(this.handleError));
  }

  getAllPaiements(): Observable<Paiement[]> {
    return this.httpClient.get<Paiement[]>(
      `${this.API_URL}${this.ENDPOINT_PAIEMENT}/all`
    ).pipe(catchError(this.handleError));
  }

  updatePaiement(id: number, paiement: Paiement): Observable<Paiement> {
    return this.httpClient.put<Paiement>(
      `${this.API_URL}${this.ENDPOINT_PAIEMENT}/${id}`,
      paiement
    ).pipe(catchError(this.handleError));
  }

  deletePaiement(id: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.API_URL}${this.ENDPOINT_PAIEMENT}/${id}`
    ).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('HTTP Error', error.message);
    return throwError(() => new Error('Server communication error'));
  }
}
