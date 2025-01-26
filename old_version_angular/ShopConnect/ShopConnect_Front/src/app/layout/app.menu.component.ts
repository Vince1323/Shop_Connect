import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';

@Component({
    selector: 'app-menu',
    templateUrl: './app.menu.component.html',
})
export class AppMenuComponent implements OnInit {
    model: any[] = [];

    constructor(public layoutService: LayoutService) { }

    ngOnInit() {
        this.model = [
            {
                label: 'Accueil',
                items: [
                    {
                        label: 'Accueil',
                        icon: 'pi pi-fw pi-home',
                        routerLink: ['/'],
                    },
                ],
            },
            {
                label: 'Shop Connect',
                items: [
                    {
                        label: 'Avis',
                        icon: 'pi pi-fw pi-comments',
                        routerLink: ['/avis'],
                    },
                    {
                        label: 'Boutique',
                        icon: 'pi pi-fw pi-prime',  
                        routerLink: ['/boutique'],
                    },
                    {
                        label: 'Categorie',
                        icon: 'pi pi-fw pi-tags',
                        routerLink: ['/categorie'],
                    },
                    {
                        label: 'Commande',
                        icon: 'pi pi-fw pi-file',
                        routerLink: ['/commande'],
                    },
                    {
                        label: 'Paiement',
                        icon: 'pi pi-fw pi-money-bill',
                        routerLink: ['/paiement'],
                    },
                    {
                        label: 'Panier',
                        icon: 'pi pi-fw pi-shopping-cart',
                        routerLink: ['/panier'],
                    },
                    {
                        label: 'Produit',
                        icon: 'pi pi-fw pi-box',
                        routerLink: ['/produit'],
                    },
                    {
                        label: 'Promotion',
                        icon: 'pi pi-fw pi-percentage',
                        routerLink: ['/promotion'],
                    },
                    {
                        label: 'Utilisateur',
                        icon: 'pi pi-fw pi-users',
                        routerLink: ['/utilisateur'],
                    },
                ],
            },
         
            {
                label: 'Pages',
                icon: 'pi pi-fw pi-briefcase',
                items: [
                
                    {
                        label: 'Auth',
                        icon: 'pi pi-fw pi-user',
                        items: [
                            {
                                label: 'Login',
                                icon: 'pi pi-fw pi-sign-in',
                                routerLink: ['/auth/login'],
                            },
                            {
                                label: 'Quitter',
                                icon: 'pi pi-fw pi-sign-out',
                                routerLink: ['/auth/logout'],
                            },
                            {
                                label: 'Enregistrer',
                                icon: 'pi pi-fw pi-save',
                                routerLink: ['/auth/register'],
                            },
                        ],
                    },
                
                ],
            },
          
        ];
    }
}
