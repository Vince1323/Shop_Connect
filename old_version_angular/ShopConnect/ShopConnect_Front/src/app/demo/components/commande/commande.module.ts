import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CommandeComponent } from './commande.component';
import { CommandeRoutingModule } from './commande-routing.module';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

@NgModule({
    declarations: [CommandeComponent],
    imports: [
        CommonModule,
        FormsModule,
        ButtonModule,
        CommandeRoutingModule,
        TableModule,
        DialogModule,
        InputTextModule,
        ConfirmDialogModule,
    ],
    providers: [ConfirmationService],
    exports: [CommandeComponent]
})
export class CommandeModule { }
