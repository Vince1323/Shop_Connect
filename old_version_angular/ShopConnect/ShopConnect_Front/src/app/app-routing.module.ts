import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { AppLayoutComponent } from './layout/app.layout.component';

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                {
                    path: '',
                    component: AppLayoutComponent,
                    children: [
                        { path: 'avis', loadChildren: () => import('./demo/components/avis/avis.module').then(m => m.AvisModule) },
                        { path: 'boutique', loadChildren: () => import('./demo/components/boutique/boutique.module').then(m => m.BoutiqueModule) },
                        { path: 'categorie', loadChildren: () => import('./demo/components/categorie/categorie.module').then(m => m.CategorieModule) },
                        { path: 'commande', loadChildren: () => import('./demo/components/commande/commande.module').then(m => m.CommandeModule) },
                        { path: 'paiement', loadChildren: () => import('./demo/components/paiement/paiement.module').then(m => m.PaiementModule) },
                        { path: 'panier', loadChildren: () => import('./demo/components/panier/panier.module').then(m => m.PanierModule) },
                        { path: 'produit', loadChildren: () => import('./demo/components/produit/produit.module').then(m => m.ProduitModule) },
                        { path: 'promotion', loadChildren: () => import('./demo/components/promotion/promotion.module').then(m => m.PromotionModule) },
                        { path: 'utilisateur', loadChildren: () => import('./demo/components/utilisateur/utilisateur.module').then(m => m.UtilisateurModule) },
                    ],
                },
                {
                    path: 'auth', // Ajout de la route pour AuthModule
                    loadChildren: () => import('./demo/components/auth/auth.module').then(m => m.AuthModule),
                },
                {
                    path: '**',
                    redirectTo: '', // Redirige les chemins invalides vers l'accueil
                },
            ],
            {
                scrollPositionRestoration: 'enabled',
                anchorScrolling: 'enabled',
                onSameUrlNavigation: 'reload',
            }
        ),
    ],
    exports: [RouterModule],
})
export class AppRoutingModule {}
