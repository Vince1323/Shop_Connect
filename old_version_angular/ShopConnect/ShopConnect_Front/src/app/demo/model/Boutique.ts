export interface Boutique {
  id: number;
  nom: string;
  adresse: string;
  telephone: string;
  description: string;
  url_slug: string;
  utilisateur_id: number;  // Relation avec un utilisateur
}
