import { Component, OnInit } from '@angular/core';
import { AvisService } from '../../service/avis.service';
import { Avis } from '../../model/Avis';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-avis',
  templateUrl: './avis.component.html',
  styleUrls: ['./avis.component.scss'],
  providers: [MessageService]
})
export class AvisComponent implements OnInit {
  avis: Avis[] = []; // Liste des avis
  show: boolean = false; // Contrôle du dialogue pour créer/modifier
  viewDetailsVisible: boolean = false; // Contrôle du dialogue pour les détails
  selectedAvis?: Avis; // Avis sélectionné pour les détails
  new: Avis = {} as Avis; // Avis à créer ou modifier

  constructor(private avisService: AvisService, private messageService: MessageService) {}

  ngOnInit(): void {
    // Récupérer la liste des avis au chargement du composant
    this.refreshAvis();
  }

  loadAvis(): void {
    this.avisService.getAllAvis().subscribe({
      next: (data) => {
        this.avis = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des avis', err);
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec du chargement des avis' });
      }
    });
  }
  
  /**
   * Ouvre le formulaire pour créer un nouvel avis
   */
  newAvis(): void {
    this.new = {} as Avis; // Réinitialise l'objet "new"
    this.show = true;
  }

  /**
   * Sauvegarde un avis (création ou mise à jour)
   */
  sauver(): void {
    if (this.new.id) {
      // Mise à jour d'un avis existant
      this.avisService.updateAvis(this.new.id, this.new).subscribe({
        next: () => this.refreshAvis(),
        error: (error) => console.error('Erreur lors de la mise à jour de l\'avis:', error),
      });
    } else {
      // Création d'un nouvel avis
      this.avisService.insertAvis(this.new).subscribe({
        next: () => this.refreshAvis(),
        error: (error) => console.error('Erreur lors de l\'ajout de l\'avis:', error),
      });
    }
    this.show = false; // Ferme le formulaire
  }

  /**
   * Récupère tous les avis
   */
  private refreshAvis(): void {
    this.avisService.getAllAvis().subscribe({
      next: (avis) => (this.avis = avis),
      error: (error) => console.error('Erreur lors de la récupération des avis:', error),
    });
  }

  /**
   * Affiche les détails d'un avis
   * @param avis - L'avis à afficher
   */
  viewDetailsAvis(avis: Avis): void {
    this.selectedAvis = avis;
    this.viewDetailsVisible = true;
  }

  /**
   * Prépare l'édition d'un avis
   * @param avis - L'avis à modifier
   */
  editAvis(avis: Avis): void {
    this.new = { ...avis }; // Copie de l'avis pour modification
    this.show = true;
  }

  /**
   * Confirme et supprime un avis
   * @param avis - L'avis à supprimer
   */
  confirmDeleteAvis(avis: Avis): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer l'avis "${avis.commentaire}" ?`)) {
      this.avisService.deleteAvis(avis.id).subscribe({
        next: () => {
          this.loadAvis();
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Avis supprimé avec succès' });
        },
        error: (err) => {
          console.error('Erreur lors de la suppression de l\'avis', err);
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la suppression de l\'avis' });
        }
      });
    }
  }
}
