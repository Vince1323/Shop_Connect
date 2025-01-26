export interface Auth {
    email: string;
    password: string;
    nom?: string; // Optionnel pour le login
    prenom?: string; // Optionnel pour le login
    adresse?: string; // Optionnel, seulement pour le register
    dateNaissance?: string; // Optionnel, seulement pour le register
    role?: string; // Optionnel : rôle pour l'enregistrement
    language?: string; // Langue préférée (optionnelle)
  }
  