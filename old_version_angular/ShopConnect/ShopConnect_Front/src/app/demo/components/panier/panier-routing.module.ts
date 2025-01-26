import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PanierComponent } from './panier.component';

@NgModule({
    imports: [
        RouterModule.forChild([{ path: '', component: PanierComponent }]),
    ],
    exports: [RouterModule],
})
export class PanierRoutingModule { }
