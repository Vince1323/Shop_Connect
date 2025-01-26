package com.shop_connect.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant une commande de produit.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation de l'ID.
	private Integer id;

	private String orderId; // Identifiant de la commande.
	private LocalDate orderDate;

	@ManyToOne // Relation plusieurs commandes peuvent inclure un même produit.
	private Product product;

	private Double price;
	private Integer quantity;

	@ManyToOne // Relation plusieurs commandes peuvent appartenir à un utilisateur.
	private UserDtls user;

	private String status;
	private String paymentType;

	@OneToOne(cascade = CascadeType.ALL) // Relation avec l'adresse de commande.
	private OrderAddress orderAddress;
}
