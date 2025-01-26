import { Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';

@Component({
    selector: 'app-footer',
    templateUrl: './app.footer.component.html'
})
export class AppFooterComponent {
    // Liste des liens vers les réseaux sociaux
    socialLinks = [
        { url: 'https://www.facebook.com', icon: 'fab fa-facebook-f' },
        { url: 'https://www.twitter.com', icon: 'fab fa-twitter' },
        { url: 'https://www.instagram.com', icon: 'fab fa-instagram' },
        { url: 'https://www.linkedin.com', icon: 'fab fa-linkedin-in' },
        { url: 'https://www.youtube.com', icon: 'fab fa-youtube' }
    ];

    constructor(public layoutService: LayoutService) { }
}
