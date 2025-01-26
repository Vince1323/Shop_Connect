import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { AvisComponent } from './avis.component';
import { AvisRoutingModule } from './avis-routing.module';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

@NgModule({
    declarations: [AvisComponent],
    imports: [
        CommonModule,
        FormsModule,
        ButtonModule,
        AvisRoutingModule,
        TableModule,
        DialogModule,
        InputTextModule,
        ConfirmDialogModule,
    ],
    providers: [ConfirmationService],
    exports: [AvisComponent]
})
export class AvisModule { }
