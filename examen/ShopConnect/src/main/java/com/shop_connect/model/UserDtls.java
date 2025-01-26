package com.shop_connect.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant un utilisateur.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserDtls {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation de l'ID.
	private Integer id;

	private String name;         // Nom de l'utilisateur.
	private String mobileNumber; // Numéro de téléphone.
	private String email;        // Adresse e-mail.
	private String address;      // Rue et numéro.
	private String city;         // Ville.
	private String state;        // État ou région.
	private String pincode;      // Code postal.
	private String password;     // Mot de passe.
	private String profileImage; // URL de l'image de profil.
	private String role;         // Rôle (admin, utilisateur, etc.).
	private Boolean isEnable;    // Statut actif/inactif.
	private Boolean accountNonLocked; // Indique si le compte est verrouillé.
	private Integer failedAttempt; // Tentatives échouées de connexion.
	private Date lockTime;       // Date et heure du verrouillage.
	private String resetToken;   // Token pour la réinitialisation du mot de passe.

}
