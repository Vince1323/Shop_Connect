import { Component, OnInit } from '@angular/core';
import { PanierService } from '../../service/panier.service';
import { ProduitService } from '../../service/produit.service';
import { Panier } from '../../model/Panier';
import { Produit } from '../../model/Produit';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.scss'],
  providers: [ConfirmationService, MessageService],
})
export class PanierComponent implements OnInit {
  paniers: Panier[] = []; // Liste des paniers
  produits: Produit[] = []; // Liste des produits pour le carrousel
  responsiveOptions: any; // Options pour le carrousel
  show: boolean = false; // Contrôle l'affichage du dialogue de création/modification
  viewDetailsVisible: boolean = false; // Contrôle l'affichage du dialogue de détails
  selectedPanier?: Panier; // Panier sélectionné pour voir les détails
  new: Panier = {} as Panier; // Nouveau panier ou panier en cours de modification

  constructor(
    public panierService: PanierService,
    public produitService: ProduitService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {
    this.responsiveOptions = [
      { breakpoint: '1024px', numVisible: 3, numScroll: 3 },
      { breakpoint: '768px', numVisible: 2, numScroll: 2 },
      { breakpoint: '560px', numVisible: 1, numScroll: 1 },
    ];
  }

  ngOnInit(): void {
    this.refreshPaniers();
    this.loadProduits();
  }

  /**
   * Charger la liste des produits pour le carrousel.
   */
  loadProduits(): void {
    this.produitService.getAllProduits().subscribe({
      next: (data) => {
        this.produits = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des produits:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Erreur',
          detail: 'Échec du chargement des produits.',
        });
      },
    });
  }

  /**
   * Ajouter un produit au panier existant.
   * @param produit Le produit à ajouter.
   */
  ajouterProduitAuPanier(produit: Produit): void {
    const panierId = 1; // Remplacez par l'ID du panier actuel
    const quantite = 1; // Quantité par défaut

    this.panierService.ajouterProduitAuPanier(panierId, produit.id, quantite).subscribe({
      next: () => {
        this.refreshPaniers();
        this.messageService.add({
          severity: 'success',
          summary: 'Succès',
          detail: 'Produit ajouté au panier avec succès',
        });
      },
      error: (err) => {
        console.error('Erreur lors de l\'ajout au panier:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Erreur',
          detail: 'Échec de l\'ajout au panier',
        });
      },
    });
  }

  /**
   * Afficher les détails d'un panier.
   * @param panier Le panier sélectionné.
   */
  viewDetailsPanier(panier: Panier): void {
    this.selectedPanier = panier;
    this.viewDetailsVisible = true;
  }

  /**
   * Préparer l'édition d'un panier.
   * @param panier Le panier à modifier.
   */
  editPanier(panier: Panier): void {
    this.new = { ...panier };
    this.show = true;
  }

  /**
   * Confirmer la suppression d'un panier.
   * @param panier Le panier à supprimer.
   */
  confirmDeletePanier(panier: Panier): void {
    this.confirmationService.confirm({
      message: 'Voulez-vous supprimer ce panier ?',
      accept: () => {
        this.panierService.deletePanier(panier.id).subscribe({
          next: () => {
            this.refreshPaniers();
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Panier supprimé avec succès',
            });
          },
          error: (error) => {
            console.error('Erreur lors de la suppression du panier:', error);
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la suppression du panier',
            });
          },
        });
      },
    });
  }

  /**
   * Créer un nouveau panier.
   */
  newPanier(): void {
    this.new = {} as Panier;
    this.show = true;
  }

  /**
   * Sauvegarder un nouveau panier ou mettre à jour un panier existant.
   */
  sauver(): void {
    if (this.new.id) {
      this.panierService.updatePanier(this.new.id, this.new).subscribe({
        next: () => {
          this.refreshPaniers();
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Panier mis à jour avec succès',
          });
        },
        error: (error) => {
          console.error('Erreur lors de la mise à jour du panier:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Échec de la mise à jour du panier',
          });
        },
      });
    } else {
      this.panierService.insertPanier(this.new).subscribe({
        next: () => {
          this.refreshPaniers();
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Panier ajouté avec succès',
          });
        },
        error: (error) => {
          console.error('Erreur lors de l\'ajout du panier:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Échec de l\'ajout du panier',
          });
        },
      });
    }
    this.show = false;
  }

  /**
   * Rafraîchir la liste des paniers.
   */
  private refreshPaniers(): void {
    this.panierService.getAllPaniers().subscribe({
      next: (paniers) => {
        this.paniers = paniers;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des paniers:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Erreur',
          detail: 'Échec du chargement des paniers',
        });
      },
    });
  }
}
