import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Panier } from '../model/Panier';

@Injectable({
  providedIn: 'root',
})
export class PanierService {
  // URL de base de l'API
  private readonly API_URL = 'http://localhost:9292/api';
  // Endpoint pour les paniers
  private readonly ENDPOINT_PANIER = '/paniers';

  constructor(private httpClient: HttpClient) {}

  /**
   * Ajouter un produit à un panier spécifique.
   * @param panierId L'ID du panier auquel ajouter le produit.
   * @param produitId L'ID du produit à ajouter.
   * @param quantite La quantité du produit à ajouter.
   * @returns Un Observable vide, ou une erreur en cas d'échec.
   */
  ajouterProduitAuPanier(
    panierId: number,
    produitId: number,
    quantite: number
  ): Observable<void> {
    return this.httpClient
      .post<void>(`${this.API_URL}${this.ENDPOINT_PANIER}/${panierId}/produits`, {
        produitId,
        quantite,
      })
      .pipe(catchError(this.handleError));
  }

  /**
   * Créer un nouveau panier.
   * @param panier Les détails du panier à créer.
   * @returns Un Observable contenant le panier créé.
   */
  insertPanier(panier: Panier): Observable<Panier> {
    return this.httpClient
      .post<Panier>(`${this.API_URL}${this.ENDPOINT_PANIER}`, panier)
      .pipe(catchError(this.handleError));
  }

  /**
   * Récupérer tous les paniers disponibles.
   * @returns Un Observable contenant une liste de paniers.
   */
  getAllPaniers(): Observable<Panier[]> {
    return this.httpClient
      .get<Panier[]>(`${this.API_URL}${this.ENDPOINT_PANIER}/all`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Mettre à jour un panier existant.
   * @param id L'ID du panier à mettre à jour.
   * @param panier Les nouvelles données du panier.
   * @returns Un Observable contenant le panier mis à jour.
   */
  updatePanier(id: number, panier: Panier): Observable<Panier> {
    return this.httpClient
      .put<Panier>(`${this.API_URL}${this.ENDPOINT_PANIER}/${id}`, panier)
      .pipe(catchError(this.handleError));
  }

  /**
   * Supprimer un panier spécifique.
   * @param id L'ID du panier à supprimer.
   * @returns Un Observable vide en cas de succès.
   */
  deletePanier(id: number): Observable<void> {
    return this.httpClient
      .delete<void>(`${this.API_URL}${this.ENDPOINT_PANIER}/${id}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Gérer les erreurs HTTP.
   * @param error L'erreur reçue lors de la communication HTTP.
   * @returns Un Observable contenant un message d'erreur.
   */
  private handleError(error: HttpErrorResponse) {
    console.error('HTTP Error:', error.message); // Log pour le développeur
    return throwError(() => new Error('Erreur de communication avec le serveur'));
  }
}
