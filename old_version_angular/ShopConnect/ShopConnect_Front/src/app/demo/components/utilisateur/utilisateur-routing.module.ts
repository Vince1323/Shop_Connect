import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UtilisateurComponent } from './utilisateur.component'; // Renommage du composant

@NgModule({
    imports: [
        RouterModule.forChild([{ path: '', component: UtilisateurComponent }]),
    ],
    exports: [RouterModule],
})
export class UtilisateursRoutingModule { } // Renommage du module
