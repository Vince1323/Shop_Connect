package com.shop_connect.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant un panier d'achat.
 */
@Entity
@AllArgsConstructor // Génère un constructeur avec tous les arguments.
@NoArgsConstructor  // Génère un constructeur vide.
@Getter             // Génère les getters pour tous les champs.
@Setter             // Génère les setters pour tous les champs.
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation de l'ID.
	private Integer id;

	@ManyToOne // Relation plusieurs paniers peuvent appartenir à un utilisateur.
	private UserDtls user;

	@ManyToOne // Relation plusieurs paniers peuvent contenir des produits différents.
	private Product product;

	private Integer quantity; // Quantité de ce produit dans le panier.

	@Transient // Non persisté dans la base, calculé dynamiquement.
	private Double totalPrice; // Prix total pour cet article (prix x quantité).

	@Transient // Non persisté dans la base, calculé dynamiquement.
	private Double totalOrderPrice; // Prix total de tous les articles dans le panier.
}
