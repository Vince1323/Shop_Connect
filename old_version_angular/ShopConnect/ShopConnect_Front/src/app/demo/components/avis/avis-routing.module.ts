import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AvisComponent } from './avis.component';

@NgModule({
    imports: [
        RouterModule.forChild([{ path: '', component: AvisComponent }]),
    ],
    exports: [RouterModule],
})
export class AvisRoutingModule { }
