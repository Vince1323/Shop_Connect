import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Utilisateur } from '../model/Utilisateur';

@Injectable({
  providedIn: 'root',
})
export class UtilisateurService {
  private readonly API_URL = 'http://localhost:9292/api';
  private readonly ENDPOINT_UTILISATEUR = '/utilisateurs';

  constructor(private httpClient: HttpClient) {}

  insertUtilisateur(utilisateur: Utilisateur): Observable<Utilisateur> {
    return this.httpClient.post<Utilisateur>(
      `${this.API_URL}${this.ENDPOINT_UTILISATEUR}`,
      utilisateur
    ).pipe(catchError(this.handleError));
  }

  getAllUtilisateurs(): Observable<Utilisateur[]> {
    return this.httpClient.get<Utilisateur[]>(
      `${this.API_URL}${this.ENDPOINT_UTILISATEUR}`
    ).pipe(catchError(this.handleError));
  }

  updateUtilisateur(id: number, utilisateur: Utilisateur): Observable<Utilisateur> {
    return this.httpClient.put<Utilisateur>(
      `${this.API_URL}${this.ENDPOINT_UTILISATEUR}/${id}`,
      utilisateur
    ).pipe(catchError(this.handleError));
  }

  deleteUtilisateur(id: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.API_URL}${this.ENDPOINT_UTILISATEUR}/${id}`
    ).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('HTTP Error', error.message);
    return throwError(() => new Error('Server communication error'));
  }
}

