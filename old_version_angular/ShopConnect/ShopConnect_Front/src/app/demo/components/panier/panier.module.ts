import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanierComponent } from './panier.component';
import { PanierRoutingModule } from './panier-routing.module';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { CarouselModule } from 'primeng/carousel';
import { ButtonModule } from 'primeng/button';
import { MessageService, ConfirmationService } from 'primeng/api';

@NgModule({
  declarations: [PanierComponent],
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    PanierRoutingModule,
    TableModule,
    DialogModule,
    InputTextModule,
    ConfirmDialogModule,
    ToastModule,
    CarouselModule,
  ],
  providers: [ConfirmationService, MessageService],
  exports: [PanierComponent],
})
export class PanierModule {}
