import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BoutiqueComponent } from './boutique.component';

@NgModule({
    imports: [
        RouterModule.forChild([{ path: '', component: BoutiqueComponent }]),
    ],
    exports: [RouterModule],
})
export class BoutiqueRoutingModule { }
