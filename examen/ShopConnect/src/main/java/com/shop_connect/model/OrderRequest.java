package com.shop_connect.model;

import lombok.Data;
import lombok.ToString;

/**
 * Classe représentant une requête pour créer une commande.
 */
@ToString // Génère la méthode toString pour cette classe.
@Data      // Génère les getters, setters, equals, hashCode, et toString.
public class OrderRequest {

	private String firstName;  // Prénom du client.
	private String lastName;   // Nom de famille.
	private String email;      // Adresse e-mail.
	private String mobileNo;   // Numéro de téléphone.
	private String address;    // Rue et numéro.
	private String city;       // Ville.
	private String state;      // État ou région.
	private String pincode;    // Code postal.
	private String paymentType; // Mode de paiement.
}
