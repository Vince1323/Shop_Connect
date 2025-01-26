import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Promotion } from '../model/Promotion';

@Injectable({
  providedIn: 'root',
})
export class PromotionService {
  private readonly API_URL = 'http://localhost:9292/api';
  private readonly ENDPOINT_PROMOTION = '/promotions';

  constructor(private httpClient: HttpClient) {}

  insertPromotion(promotion: Promotion): Observable<Promotion> {
    return this.httpClient.post<Promotion>(
      `${this.API_URL}${this.ENDPOINT_PROMOTION}`,
      promotion
    ).pipe(catchError(this.handleError));
  }

  getAllPromotions(): Observable<Promotion[]> {
    return this.httpClient.get<Promotion[]>(
      `${this.API_URL}${this.ENDPOINT_PROMOTION}/all`
    ).pipe(catchError(this.handleError));
  }

  updatePromotion(id: number, promotion: Promotion): Observable<Promotion> {
    return this.httpClient.put<Promotion>(
      `${this.API_URL}${this.ENDPOINT_PROMOTION}/${id}`,
      promotion
    ).pipe(catchError(this.handleError));
  }

  deletePromotion(id: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.API_URL}${this.ENDPOINT_PROMOTION}/${id}`
    ).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('HTTP Error', error.message);
    return throwError(() => new Error('Server communication error'));
  }
}
