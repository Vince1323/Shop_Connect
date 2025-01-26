import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Produit } from '../model/Produit';

@Injectable({
  providedIn: 'root',
})
export class ProduitService {
  // URL de base de l'API backend
  private readonly API_URL = 'http://localhost:9292/api';
  private readonly ENDPOINT_PRODUIT = '/produits';

  constructor(private httpClient: HttpClient) {}

  /**
   * Ajoute un nouveau produit dans la base de données.
   * @param produit - Les informations du produit à insérer.
   * @returns Observable du produit inséré.
   */
  insertProduit(produit: Produit): Observable<Produit> {
    return this.httpClient.post<Produit>(
      `${this.API_URL}${this.ENDPOINT_PRODUIT}`,
      produit
    ).pipe(catchError(this.handleError));
  }

  /**
   * Récupère la liste de tous les produits.
   * @returns Observable contenant une liste de produits.
   */
  getAllProduits(): Observable<Produit[]> {
    return this.httpClient.get<Produit[]>(
      `${this.API_URL}${this.ENDPOINT_PRODUIT}`
    ).pipe(catchError(this.handleError));
  }

  /**
   * Met à jour un produit existant.
   * @param id - Identifiant du produit à mettre à jour.
   * @param produit - Les nouvelles informations du produit.
   * @returns Observable du produit mis à jour.
   */
  updateProduit(id: number, produit: Produit): Observable<Produit> {
    return this.httpClient.put<Produit>(
      `${this.API_URL}${this.ENDPOINT_PRODUIT}/${id}`,
      produit
    ).pipe(catchError(this.handleError));
  }

  /**
   * Supprime un produit par son identifiant.
   * @param id - Identifiant du produit à supprimer.
   * @returns Observable vide indiquant le succès ou l'échec.
   */
  deleteProduit(id: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.API_URL}${this.ENDPOINT_PRODUIT}/${id}`
    ).pipe(catchError(this.handleError));
  }

  /**
   * Gestionnaire d'erreurs pour les requêtes HTTP.
   * @param error - Objet contenant les informations sur l'erreur.
   * @returns Observable contenant un message d'erreur pour l'utilisateur.
   */
  private handleError(error: HttpErrorResponse) {
    console.error('Erreur HTTP :', error.message);
    return throwError(() => new Error('Erreur lors de la communication avec le serveur.'));
  }
}
