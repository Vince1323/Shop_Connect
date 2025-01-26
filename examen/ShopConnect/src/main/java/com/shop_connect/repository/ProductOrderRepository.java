package com.shop_connect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop_connect.model.ProductOrder;


/**
 * Repository pour gérer les commandes des utilisateurs.
 */
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {

	List<ProductOrder> findByUserId(Integer userId);
	/**
	 * Récupère toutes les commandes d'un utilisateur donné.
	 * @param userId ID de l'utilisateur.
	 * @return Liste des commandes de l'utilisateur.
	 */
	ProductOrder findByOrderId(String orderId);
	/**
	 * Trouve une commande par son identifiant unique.
	 * @param orderId ID de la commande.
	 * @return La commande correspondante.
	 */
}
