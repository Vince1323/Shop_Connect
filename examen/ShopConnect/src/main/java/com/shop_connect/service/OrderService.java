package com.shop_connect.service;

import java.util.List;
import org.springframework.data.domain.Page;
import com.shop_connect.model.OrderRequest;
import com.shop_connect.model.ProductOrder;

/**
 * Interface pour gérer les commandes.
 */
public interface OrderService {

	/**
	 * Sauvegarde une commande pour un utilisateur donné.
	 * @param userId L'ID de l'utilisateur.
	 * @param orderRequest Détails de la commande.
	 */
	public void saveOrder(Integer userId, OrderRequest orderRequest) throws Exception;

	/**
	 * Récupère les commandes passées par un utilisateur.
	 * @param userId L'ID de l'utilisateur.
	 * @return Liste des commandes.
	 */
	public List<ProductOrder> getOrdersByUser(Integer userId);

	/**
	 * Met à jour le statut d'une commande.
	 * @param id L'ID de la commande.
	 * @param status Nouveau statut.
	 * @return La commande mise à jour.
	 */
	public ProductOrder updateOrderStatus(Integer id, String status);

	/**
	 * Récupère toutes les commandes.
	 */
	public List<ProductOrder> getAllOrders();

	/**
	 * Récupère une commande par son ID.
	 */
	public ProductOrder getOrdersByOrderId(String orderId);

	/**
	 * Récupère une page paginée de commandes.
	 */
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);
}
