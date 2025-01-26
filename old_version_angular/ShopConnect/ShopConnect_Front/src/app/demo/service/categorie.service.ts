import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Categorie } from '../model/Categorie';

@Injectable({
  providedIn: 'root',
})
export class CategorieService {
  private readonly API_URL = 'http://localhost:9292/api';
  private readonly ENDPOINT_CATEGORIE = '/categories';

  constructor(private httpClient: HttpClient) {}

  insertCategorie(categorie: Categorie): Observable<Categorie> {
    return this.httpClient.post<Categorie>(
      `${this.API_URL}${this.ENDPOINT_CATEGORIE}`,
      categorie
    ).pipe(catchError(this.handleError));
  }

  getAllCategories(): Observable<Categorie[]> {
    return this.httpClient.get<Categorie[]>(
      `${this.API_URL}${this.ENDPOINT_CATEGORIE}`
    ).pipe(catchError(this.handleError));
  }

  updateCategorie(id: number, categorie: Categorie): Observable<Categorie> {
    return this.httpClient.put<Categorie>(
      `${this.API_URL}${this.ENDPOINT_CATEGORIE}/${id}`,
      categorie
    ).pipe(catchError(this.handleError));
  }

  deleteCategorie(id: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.API_URL}${this.ENDPOINT_CATEGORIE}/${id}`
    ).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('HTTP Error', error.message);
    return throwError(() => new Error('Server communication error'));
  }
}
