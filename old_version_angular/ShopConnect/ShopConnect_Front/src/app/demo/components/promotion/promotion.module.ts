import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { PromotionComponent } from './promotion.component';
import { PromotionRoutingModule } from './promotion-routing.module';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

@NgModule({
    declarations: [PromotionComponent],
    imports: [
        CommonModule,
        FormsModule,
        ButtonModule,
        PromotionRoutingModule,
        TableModule,
        DialogModule,
        InputTextModule,
        ConfirmDialogModule,
    ],
    providers: [ConfirmationService],
    exports: [PromotionComponent]
})
export class PromotionModule { }
