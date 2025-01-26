import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Commande } from '../model/Commande';

@Injectable({
  providedIn: 'root',
})
export class CommandeService {
  private readonly API_URL = 'http://localhost:9292/api';
  private readonly ENDPOINT_COMMANDE = '/commandes';

  constructor(private httpClient: HttpClient) {}

  insertCommande(commande: Commande): Observable<Commande> {
    return this.httpClient.post<Commande>(
      `${this.API_URL}${this.ENDPOINT_COMMANDE}`,
      commande
    ).pipe(catchError(this.handleError));
  }

  getAllCommandes(): Observable<Commande[]> {
    return this.httpClient.get<Commande[]>(
      `${this.API_URL}${this.ENDPOINT_COMMANDE}/all`
    ).pipe(catchError(this.handleError));
  }

  updateCommande(id: number, commande: Commande): Observable<Commande> {
    return this.httpClient.put<Commande>(
      `${this.API_URL}${this.ENDPOINT_COMMANDE}/${id}`,
      commande
    ).pipe(catchError(this.handleError));
  }

  deleteCommande(id: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.API_URL}${this.ENDPOINT_COMMANDE}/${id}`
    ).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('HTTP Error', error.message);
    return throwError(() => new Error('Server communication error'));
  }
}
