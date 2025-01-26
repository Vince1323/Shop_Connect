import { Component, OnInit } from '@angular/core';
import { BoutiqueService } from '../../service/boutique.service';
import { Boutique } from '../../model/Boutique';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-boutique',
  templateUrl: './boutique.component.html',
  styleUrls: ['./boutique.component.scss'],
  providers: [MessageService]
})
export class BoutiqueComponent implements OnInit {
  boutiques: Boutique[] = [];
  new: Boutique = {} as Boutique;
  selectedBoutique: Boutique | null = null;
  showCreateDialog = false;
  showEditDialog = false;
  viewDetailsVisible = false;

  constructor(private boutiqueService: BoutiqueService, private messageService: MessageService) { }

  ngOnInit(): void {
    this.loadBoutiques();
  }

  loadBoutiques(): void {
    this.boutiqueService.getAllBoutiques().subscribe({
      next: (data) => {
        this.boutiques = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des boutiques', err);
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec du chargement des boutiques' });
      }
    });
  }

  newBoutique(): void {
    this.new = {} as Boutique;
    this.showCreateDialog = true;
  }

  editBoutique(boutique: Boutique): void {
    this.new = { ...boutique };
    this.showEditDialog = true;
  }

  saveBoutique(): void {
    if (this.new.id) {
      this.boutiqueService.updateBoutique(this.new.id, this.new).subscribe({
        next: () => {
          this.loadBoutiques();
          this.showEditDialog = false;
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Boutique mise à jour avec succès' });
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour de la boutique', err);
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la mise à jour de la boutique' });
        }
      });
    } else {
      this.boutiqueService.insertBoutique(this.new).subscribe({
        next: () => {
          this.loadBoutiques();
          this.showCreateDialog = false;
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Boutique créée avec succès' });
        },
        error: (err) => {
          console.error('Erreur lors de la création de la boutique', err);
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la création de la boutique' });
        }
      });
    }
  }

  viewDetailsBoutique(boutique: Boutique): void {
    this.selectedBoutique = boutique;
    this.viewDetailsVisible = true;
  }

  confirmDeleteBoutique(boutique: Boutique): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer la boutique ${boutique.nom} ?`)) {
      this.boutiqueService.deleteBoutique(boutique.id).subscribe({
        next: () => {
          this.loadBoutiques();
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Boutique supprimée avec succès' });
        },
        error: (err) => {
          console.error('Erreur lors de la suppression de la boutique', err);
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la suppression de la boutique' });
        }
      });
    }
  }
}
