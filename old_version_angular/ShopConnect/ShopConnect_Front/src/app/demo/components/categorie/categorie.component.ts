import { Component, OnInit } from '@angular/core';
import { CategorieService } from '../../service/categorie.service';
import { Categorie } from '../../model/Categorie';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-categorie',
  templateUrl: './categorie.component.html',
  styleUrls: ['./categorie.component.scss'],
  providers: [MessageService]
})
export class CategorieComponent implements OnInit {
  categories: Categorie[] = [];
  new: Categorie = {} as Categorie;
  selectedCategorie: Categorie | null = null;
  showCreateDialog = false;
  showEditDialog = false;
  viewDetailsVisible = false;

  constructor(private categorieService: CategorieService, private messageService: MessageService) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categorieService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des catégories', err);
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec du chargement des catégories' });
      }
    });
  }

  newCategorie(): void {
    this.new = {} as Categorie;
    this.showCreateDialog = true;
  }

  editCategorie(categorie: Categorie): void {
    this.new = { ...categorie };
    this.showEditDialog = true;
  }

  saveCategorie(): void {
    if (this.new.id) {
      this.categorieService.updateCategorie(this.new.id, this.new).subscribe({
        next: () => {
          this.loadCategories();
          this.showEditDialog = false;
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Catégorie mise à jour avec succès' });
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour de la catégorie', err);
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la mise à jour de la catégorie' });
        }
      });
    } else {
      this.categorieService.insertCategorie(this.new).subscribe({
        next: () => {
          this.loadCategories();
          this.showCreateDialog = false;
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Catégorie créée avec succès' });
        },
        error: (err) => {
          console.error('Erreur lors de la création de la catégorie', err);
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la création de la catégorie' });
        }
      });
    }
  }

  viewDetailsCategorie(categorie: Categorie): void {
    this.selectedCategorie = categorie;
    this.viewDetailsVisible = true;
  }

  confirmDeleteCategorie(categorie: Categorie): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer la catégorie ${categorie.nom} ?`)) {
      this.categorieService.deleteCategorie(categorie.id).subscribe({
        next: () => {
          this.loadCategories();
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Catégorie supprimée avec succès' });
        },
        error: (err) => {
          console.error('Erreur lors de la suppression de la catégorie', err);
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la suppression de la catégorie' });
        }
      });
    }
  }
}
