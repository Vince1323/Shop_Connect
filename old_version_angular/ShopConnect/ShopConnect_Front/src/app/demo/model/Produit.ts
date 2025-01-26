/**
 * Interface représentant un produit dans l'application.
 * @field id - Identifiant unique du produit.
 * @field description - Brève description du produit.
 * @field imageUrl - Lien vers l'image associée au produit.
 * @field nom - Nom du produit.
 * @field prix - Prix du produit.
 * @field quantiteStock - Quantité disponible en stock.
 * @field categorieId - Identifiant de la catégorie à laquelle le produit appartient.
 */
export interface Produit {
  id: number;
  description: string;
  imageUrl: string;
  nom: string;
  prix: number;
  quantiteStock: number;
  categorieId: number;
}
