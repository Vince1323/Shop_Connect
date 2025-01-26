package com.shop_connect.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shop_connect.model.Cart;

/**
 * Repository pour gérer les opérations CRUD sur les paniers.
 */
public interface CartRepository extends JpaRepository<Cart, Integer> {

	/**
	 * Trouve un panier par l'ID du produit et l'ID de l'utilisateur.
	 * @param productId ID du produit.
	 * @param userId ID de l'utilisateur.
	 * @return Le panier correspondant.
	 */
	public Cart findByProductIdAndUserId(Integer productId, Integer userId);

	/**
	 * Compte le nombre d'articles dans le panier d'un utilisateur.
	 * @param userId ID de l'utilisateur.
	 * @return Le nombre d'articles dans le panier.
	 */
	public Integer countByUserId(Integer userId);

	/**
	 * Récupère tous les paniers d'un utilisateur donné.
	 * @param userId ID de l'utilisateur.
	 * @return Liste des paniers de l'utilisateur.
	 */
	public List<Cart> findByUserId(Integer userId);

}
