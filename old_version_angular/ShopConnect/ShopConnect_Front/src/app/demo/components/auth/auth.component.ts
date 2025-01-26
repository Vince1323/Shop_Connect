import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
  providers: [MessageService],
})
export class AuthComponent implements OnInit {
  loginForm: FormGroup;
  registrationForm: FormGroup;
  showRegistration = false; // Basculer entre Login et Register

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.initForms();
  }

  initForms(): void {
    // Formulaire Login
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });

    // Formulaire Register
    this.registrationForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      birthDate: ['', Validators.required],
      phone: ['', Validators.pattern(/^\+?[0-9]{7,15}$/)], // Numéro de téléphone optionnel mais formaté
      address: this.fb.group({
        street: ['', Validators.required],
        city: ['', Validators.required],
        zipCode: ['', Validators.required],
        country: ['', Validators.required],
      }),
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      hasReadTermsAndConditions: [false, Validators.requiredTrue],
    });
  }

  login(): void {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          console.log('Connexion réussie', response);
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Connexion réussie',
          });
        },
        error: (err) => {
          console.error('Erreur de connexion', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Échec de la connexion',
          });
        },
      });
    }
  }

  register(): void {
    if (this.registrationForm.valid) {
      const registrationData = this.registrationForm.value;
      this.authService.register(registrationData).subscribe({
        next: () => {
          console.log('Inscription réussie');
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Inscription réussie',
          });
          this.showRegistration = false;
        },
        error: (err) => {
          console.error('Erreur d\'inscription', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Échec de l\'inscription',
          });
        },
      });
    }
  }
}
