import { Component, OnInit } from '@angular/core';
import { CommandeService } from '../../service/commande.service';
import { Commande } from '../../model/Commande';  // Modèle

@Component({
  selector: 'app-commande',
  templateUrl: './commande.component.html',
  styleUrls: ['./commande.component.scss']
})
export class CommandeComponent implements OnInit {

  commandes: Commande[] = [];
  show: boolean = false;
  viewDetailsVisible: boolean = false;
  selectedCommande?: Commande;
  new: Commande = {} as Commande;

  constructor(
    public commandeService: CommandeService  // Service pour récupérer les commandes
  ) { }

  ngOnInit(): void {
    this.refreshCommandes();  // Charge les commandes au démarrage
  }

  viewDetailsCommande(commande: Commande): void {
    this.selectedCommande = commande;
    this.viewDetailsVisible = true;
  }

  editCommande(commande: Commande): void {
    this.new = { ...commande };
    this.show = true;
  }

  confirmDeleteCommande(commande: Commande): void {
    this.commandeService.deleteCommande(commande.id).subscribe({
      next: () => {
        this.refreshCommandes();
      },
      error: (error) => {
        console.error('Erreur lors de la suppression de la commande:', error);
      }
    });
  }

  newCommande(): void {
    this.new = {} as Commande;
    this.show = true;
  }

  sauver(): void {
    if (this.new.id) {
      // Mise à jour d'une commande existante
      this.commandeService.updateCommande(this.new.id, this.new).subscribe({
        next: () => {
          this.refreshCommandes();
        },
        error: (error) => {
          console.error('Erreur lors de la mise à jour de la commande:', error);
        }
      });
    } else {
      // Ajout d'une nouvelle commande
      this.commandeService.insertCommande(this.new).subscribe({
        next: () => {
          this.refreshCommandes();
        },
        error: (error) => {
          console.error('Erreur lors de l\'ajout de la commande:', error);
        }
      });
    }
    this.show = false;
  }

  private refreshCommandes(): void {
    // Récupération de la liste des commandes depuis le service
    this.commandeService.getAllCommandes().subscribe({
      next: (commandes) => {
        this.commandes = commandes;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des commandes:', error);
      }
    });
  }
}
