import { Component, OnInit } from '@angular/core';
import { ProduitService } from '../../service/produit.service';
import { Produit } from '../../model/Produit';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-produit',
  templateUrl: './produit.component.html',
  styleUrls: ['./produit.component.scss'],
  providers: [MessageService]
})
export class ProduitComponent implements OnInit {
  produits: Produit[] = []; // Liste des produits récupérés depuis le backend
  new: Produit = {} as Produit; // Produit en cours d'ajout ou de modification
  selectedProduit: Produit | null = null; // Produit sélectionné pour les détails
  showCreateDialog = false; // Contrôle l'affichage du dialogue de création
  showEditDialog = false; // Contrôle l'affichage du dialogue de modification
  viewDetailsVisible = false; // Contrôle l'affichage des détails

  constructor(
    private produitService: ProduitService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadProduits(); // Charger la liste des produits lors de l'initialisation
  }

  /**
   * Charge tous les produits depuis le backend.
   */
  loadProduits(): void {
    this.produitService.getAllProduits().subscribe({
      next: (data) => {
        this.produits = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des produits', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Erreur',
          detail: 'Échec du chargement des produits'
        });
      }
    });
  }

  /**
   * Ouvre le dialogue pour créer un nouveau produit.
   */
  newProduit(): void {
    this.new = {} as Produit;
    this.showCreateDialog = true;
  }

  /**
   * Ouvre le dialogue pour modifier un produit existant.
   * @param produit - Le produit à modifier.
   */
  editProduit(produit: Produit): void {
    this.new = { ...produit }; // Clone les données du produit sélectionné
    this.showEditDialog = true;
  }

  /**
   * Enregistre un produit (création ou mise à jour).
   */
  saveProduit(): void {
    if (this.new.id) {
      // Mise à jour
      this.produitService.updateProduit(this.new.id, this.new).subscribe({
        next: () => {
          this.loadProduits();
          this.showEditDialog = false;
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Produit mis à jour avec succès'
          });
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour du produit', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Échec de la mise à jour du produit'
          });
        }
      });
    } else {
      // Création
      this.produitService.insertProduit(this.new).subscribe({
        next: () => {
          this.loadProduits();
          this.showCreateDialog = false;
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Produit créé avec succès'
          });
        },
        error: (err) => {
          console.error('Erreur lors de la création du produit', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Échec de la création du produit'
          });
        }
      });
    }
  }

  /**
   * Affiche les détails d'un produit.
   * @param produit - Le produit dont on veut voir les détails.
   */
  viewDetailsProduit(produit: Produit): void {
    this.selectedProduit = produit;
    this.viewDetailsVisible = true;
  }

  /**
   * Supprime un produit après confirmation.
   * @param produit - Le produit à supprimer.
   */
  confirmDeleteProduit(produit: Produit): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer le produit ${produit.nom} ?`)) {
      this.produitService.deleteProduit(produit.id).subscribe({
        next: () => {
          this.loadProduits();
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Produit supprimé avec succès'
          });
        },
        error: (err) => {
          console.error('Erreur lors de la suppression du produit', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Échec de la suppression du produit'
          });
        }
      });
    }
  }
}
