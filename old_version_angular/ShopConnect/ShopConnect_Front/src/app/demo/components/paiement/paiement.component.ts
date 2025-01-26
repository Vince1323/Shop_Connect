import { Component, OnInit } from '@angular/core';
import { PaiementService } from '../../service/paiement.service';
import { Paiement } from '../../model/Paiement';
import { ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-paiement',
  templateUrl: './paiement.component.html',
  styleUrls: ['./paiement.component.scss'],
  providers: [ConfirmationService]
})
export class PaiementComponent implements OnInit {

  paiements: Paiement[] = [];
  show: boolean = false;
  viewDetailsVisible: boolean = false;
  selectedPaiement?: Paiement;
  new: Paiement = {} as Paiement;

  constructor(
    public paiementService: PaiementService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit(): void {
    this.refreshPaiements();
  }

  viewDetailsPaiement(paiement: Paiement): void {
    this.selectedPaiement = paiement;
    this.viewDetailsVisible = true;
  }

  editPaiement(paiement: Paiement): void {
    this.new = { ...paiement };
    this.show = true;
  }

  confirmDeletePaiement(paiement: Paiement): void {
    this.confirmationService.confirm({
      message: 'Voulez-vous supprimer ce paiement ?',
      accept: () => {
        this.paiementService.deletePaiement(paiement.id).subscribe({
          next: () => {
            this.refreshPaiements();
          },
          error: (error) => {
            console.error('Erreur lors de la suppression du paiement:', error);
          }
        });
      }
    });
  }

  newPaiement(): void {
    this.new = {} as Paiement;
    this.show = true;
  }

  sauver(): void {
    if (this.new.id) {
      this.paiementService.updatePaiement(this.new.id, this.new).subscribe({
        next: () => {
          this.refreshPaiements();
        },
        error: (error) => {
          console.error('Erreur lors de la mise à jour du paiement:', error);
        }
      });
    } else {
      this.paiementService.insertPaiement(this.new).subscribe({
        next: () => {
          this.refreshPaiements();
        },
        error: (error) => {
          console.error('Erreur lors de l\'ajout du paiement:', error);
        }
      });
    }
    this.show = false;
  }

  private refreshPaiements(): void {
    this.paiementService.getAllPaiements().subscribe({
      next: (paiements) => {
        this.paiements = paiements;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des paiements:', error);
      }
    });
  }
}
