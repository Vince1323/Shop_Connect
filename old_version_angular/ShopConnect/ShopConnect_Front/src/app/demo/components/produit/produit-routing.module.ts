import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProduitComponent } from './produit.component'; 

const routes: Routes = [
  { path: '', component: ProduitComponent }, // Route pour afficher les produits
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProduitRoutingModule {}
