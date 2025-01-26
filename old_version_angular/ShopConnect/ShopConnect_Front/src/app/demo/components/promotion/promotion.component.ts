import { Component, OnInit } from '@angular/core';
import { PromotionService } from '../../service/promotion.service';
import { Promotion } from '../../model/Promotion';
import { ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-promotion',
  templateUrl: './promotion.component.html',
  styleUrls: ['./promotion.component.scss'],
  providers: [ConfirmationService]
})
export class PromotionComponent implements OnInit {

  promotions: Promotion[] = [];
  show: boolean = false;
  viewDetailsVisible: boolean = false;
  selectedPromotion?: Promotion;
  new: Promotion = {} as Promotion;

  constructor(
    public promotionService: PromotionService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit(): void {
    this.refreshPromotions();
  }

  viewDetailsPromotion(promotion: Promotion): void {
    this.selectedPromotion = promotion;
    this.viewDetailsVisible = true;
  }

  editPromotion(promotion: Promotion): void {
    this.new = { ...promotion };
    this.show = true;
  }

  confirmDeletePromotion(promotion: Promotion): void {
    this.confirmationService.confirm({
      message: 'Voulez-vous supprimer cette promotion ?',
      accept: () => {
        this.promotionService.deletePromotion(promotion.id).subscribe({
          next: () => {
            this.refreshPromotions();
          },
          error: (error) => {
            console.error('Erreur lors de la suppression de la promotion:', error);
          }
        });
      }
    });
  }

  newPromotion(): void {
    this.new = {} as Promotion;
    this.show = true;
  }

  sauver(): void {
    if (this.new.id) {
      this.promotionService.updatePromotion(this.new.id, this.new).subscribe({
        next: () => {
          this.refreshPromotions();
        },
        error: (error) => {
          console.error('Erreur lors de la mise à jour de la promotion:', error);
        }
      });
    } else {
      this.promotionService.insertPromotion(this.new).subscribe({
        next: () => {
          this.refreshPromotions();
        },
        error: (error) => {
          console.error('Erreur lors de l\'ajout de la promotion:', error);
        }
      });
    }
    this.show = false;
  }

  private refreshPromotions(): void {
    this.promotionService.getAllPromotions().subscribe({
      next: (promotions) => {
        this.promotions = promotions;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des promotions:', error);
      }
    });
  }
}
