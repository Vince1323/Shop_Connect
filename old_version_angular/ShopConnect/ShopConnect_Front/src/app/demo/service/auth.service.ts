import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Auth } from '../model/Auth';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly API_URL = 'http://localhost:9292/api';
  private readonly LOGIN_ENDPOINT = '/auth/login';
  private readonly REGISTER_ENDPOINT = '/auth/register';
  private readonly LOGOUT_ENDPOINT = '/auth/logout';

  constructor(private httpClient: HttpClient) {}

  login(credentials: Auth): Observable<any> {
    return this.httpClient.post(`${this.API_URL}${this.LOGIN_ENDPOINT}`, credentials)
      .pipe(catchError(this.handleError));
  }

  register(userDetails: Auth): Observable<any> {
    return this.httpClient.post(`${this.API_URL}${this.REGISTER_ENDPOINT}`, userDetails)
      .pipe(catchError(this.handleError));
  }

  logout(): Observable<void> {
    return this.httpClient.post<void>(`${this.API_URL}${this.LOGOUT_ENDPOINT}`, {})
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Erreur HTTP', error.message);
    return throwError(() => new Error('Erreur de communication avec le serveur'));
  }
}
