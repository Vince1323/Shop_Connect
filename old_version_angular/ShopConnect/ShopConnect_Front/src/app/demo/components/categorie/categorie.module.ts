import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CategorieComponent } from './categorie.component';
import { CategorieRoutingModule } from './categorie-routing.module';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';

@NgModule({
  declarations: [CategorieComponent],
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    CategorieRoutingModule,
    TableModule,
    DialogModule,
    InputTextModule,
    ConfirmDialogModule,
    ToastModule
  ],
  providers: [ConfirmationService, MessageService],
  exports: [CategorieComponent]
})
export class CategorieModule { }
