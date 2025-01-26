import { Component, OnInit } from '@angular/core';
import { UtilisateurService } from '../../service/utilisateur.service';
import { Utilisateur } from '../../model/Utilisateur';
import { MessageService } from 'primeng/api';  // Import de MessageService pour les notifications Toast
import * as bcrypt from 'bcryptjs';

@Component({
  selector: 'app-utilisateur',
  templateUrl: './utilisateur.component.html',
  styleUrls: ['./utilisateur.component.scss'],
  providers: [MessageService] // Fournit MessageService au composant
})
export class UtilisateurComponent implements OnInit {
  utilisateurs: Utilisateur[] = []; // Liste des utilisateurs
  new: Utilisateur = {} as Utilisateur; // Nouvel utilisateur à créer ou modifier
  selectedUtilisateur: Utilisateur | null = null; // Utilisateur sélectionné pour affichage des détails
  showCreateDialog = false; // Pour afficher ou masquer la modale de création
  showEditDialog = false;   // Pour afficher ou masquer la modale de modification
  viewDetailsVisible = false; // Pour afficher ou masquer la modale des détails

  // Options pour les rôles
  roles = [
    { label: 'Admin', value: 'Admin' },
    { label: 'Client', value: 'Client' },
    { label: 'Manager', value: 'Manager' }
  ];
  
  // Options pour les langues
  langues = [
    { label: 'Français', value: 'fr' },
    { label: 'Anglais', value: 'en' }
  ];
  
  constructor(
    private utilisateurService: UtilisateurService, 
    private messageService: MessageService  // Injecte MessageService pour afficher les notifications
  ) {}

  ngOnInit(): void {
    this.loadUtilisateurs(); // Charge les utilisateurs au démarrage du composant
  }

  // Fonction pour charger tous les utilisateurs
  loadUtilisateurs(): void {
    this.utilisateurService.getAllUtilisateurs().subscribe({
      next: (data) => {
        this.utilisateurs = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des utilisateurs', err);
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec du chargement des utilisateurs' });
      }
    });
  }

  // Ouvre le dialogue de création d'un nouvel utilisateur
  newUtilisateur(): void {
    this.new = {} as Utilisateur; // Réinitialise l'objet new pour un nouvel utilisateur
    this.showCreateDialog = true; // Affiche la modale de création
  }

  // Ouvre le dialogue de modification pour l'utilisateur sélectionné
  editUtilisateur(utilisateur: Utilisateur): void {
    this.new = { ...utilisateur }; // Cloner l'utilisateur sélectionné
    this.showEditDialog = true;    // Affiche la modale de modification
  }

  // Sauvegarde de l'utilisateur, création ou mise à jour
  saveUtilisateur(): void {
    if (this.new.id) {
      // Mise à jour de l'utilisateur existant
      this.utilisateurService.updateUtilisateur(this.new.id, this.new).subscribe({
        next: () => {
          this.loadUtilisateurs(); // Recharge la liste des utilisateurs
          this.showEditDialog = false; // Ferme la modale de modification
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Utilisateur mis à jour avec succès' });
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour de l\'utilisateur', err);
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la mise à jour de l\'utilisateur' });
        }
      });
    } else {
      // Création d'un nouvel utilisateur
      const salt = bcrypt.genSaltSync(10); // Génère un salt pour le hachage du mot de passe
      this.new.password = bcrypt.hashSync(this.new.password, salt); // Hash du mot de passe

      this.utilisateurService.insertUtilisateur(this.new).subscribe({
        next: () => {
          this.loadUtilisateurs(); // Recharge la liste des utilisateurs
          this.showCreateDialog = false; // Ferme la modale de création
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Utilisateur créé avec succès' });
        },
        error: (err) => {
          console.error('Erreur lors de la création de l\'utilisateur', err);
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la création de l\'utilisateur' });
        }
      });
    }
  }

  // Affiche les détails de l'utilisateur sélectionné
  viewDetailsUtilisateur(utilisateur: Utilisateur): void {
    this.selectedUtilisateur = utilisateur; // Définit l'utilisateur sélectionné
    this.viewDetailsVisible = true; // Affiche la modale des détails
  }

  // Confirme la suppression d'un utilisateur et effectue l'action si confirmé
  confirmDeleteUtilisateur(utilisateur: Utilisateur): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer l'utilisateur ${utilisateur.nom} ?`)) {
      this.utilisateurService.deleteUtilisateur(utilisateur.id).subscribe({
        next: () => {
          this.loadUtilisateurs(); // Recharge la liste des utilisateurs
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Utilisateur supprimé avec succès' });
        },
        error: (err) => {
          console.error('Erreur lors de la suppression de l\'utilisateur', err);
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la suppression de l\'utilisateur' });
        }
      });
    }
  }
}
