package com.shop_connect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant un produit.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation de l'ID.
	private Integer id;

	@Column(length = 500)      // Limite de 500 caractères pour le titre.
	private String title;      // Nom du produit.

	@Column(length = 1500)     // Limite de 1500 caractères pour la description.
	private String description;

	private String category;
	private Double price;
	private int stock;
	private String image;
	private int discount;
	private Double discountPrice; // Prix après application de la réduction.
	private Boolean isActive;
}
