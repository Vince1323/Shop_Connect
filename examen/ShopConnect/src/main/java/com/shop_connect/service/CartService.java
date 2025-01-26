package com.shop_connect.service;

import java.util.List;
import com.shop_connect.model.Cart;

/**
 * Interface pour gérer les fonctionnalités liées au panier.
 */
public interface CartService {

	/**
	 * Enregistre un produit dans le panier de l'utilisateur.
	 * @param productId L'ID du produit.
	 * @param userId L'ID de l'utilisateur.
	 * @return L'objet `Cart` sauvegardé.
	 */
	public Cart saveCart(Integer productId, Integer userId);

	/**
	 * Récupère tous les articles du panier pour un utilisateur donné.
	 * @param userId L'ID de l'utilisateur.
	 * @return Une liste d'objets `Cart`.
	 */
	public List<Cart> getCartsByUser(Integer userId);

	/**
	 * Compte le nombre total d'articles dans le panier d'un utilisateur.
	 * @param userId L'ID de l'utilisateur.
	 * @return Le nombre total d'articles.
	 */
	public Integer getCountCart(Integer userId);

	/**
	 * Met à jour la quantité d'un produit dans le panier.
	 * @param sy Action à effectuer ("incrémenter" ou "décrémenter").
	 * @param cid L'ID du produit dans le panier.
	 */
	public void updateQuantity(String sy, Integer cid);
}
