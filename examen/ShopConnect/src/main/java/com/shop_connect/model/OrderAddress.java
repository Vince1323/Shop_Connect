package com.shop_connect.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Classe représentant une adresse associée à une commande.
 */
@Data // Génère les getters, setters, equals, hashCode, et toString.
@Entity
public class OrderAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation de l'ID.
	private Integer id;

	private String firstName; // Prénom du client.
	private String lastName;  // Nom de famille du client.
	private String email;     // Adresse e-mail.
	private String mobileNo;  // Numéro de téléphone.
	private String address;   // Rue et numéro.
	private String city;      // Ville.
	private String state;     // État ou région.
	private String pincode;   // Code postal.
}
