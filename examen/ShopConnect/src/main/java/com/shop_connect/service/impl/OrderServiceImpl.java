package com.shop_connect.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shop_connect.model.Cart;
import com.shop_connect.model.OrderAddress;
import com.shop_connect.model.OrderRequest;
import com.shop_connect.model.ProductOrder;
import com.shop_connect.repository.CartRepository;
import com.shop_connect.repository.ProductOrderRepository;
import com.shop_connect.service.OrderService;
import com.shop_connect.util.CommonUtil;
import com.shop_connect.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductOrderRepository orderRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CommonUtil commonUtil;

	/**
	 * Sauvegarde les commandes à partir du panier d'un utilisateur.
	 *
	 * @param userid       ID de l'utilisateur.
	 * @param orderRequest Détails de l'adresse et du paiement.
	 * @throws Exception Si une erreur survient lors de l'enregistrement ou de l'envoi d'email.
	 */
	@Override
	public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {
		List<Cart> carts = cartRepository.findByUserId(userid);

		for (Cart cart : carts) {
			ProductOrder order = new ProductOrder();

			// Génère un ID unique pour chaque commande
			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());

			// Associe les informations produit et utilisateur
			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getDiscountPrice());
			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());

			// Définit le statut initial et le mode de paiement
			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			order.setPaymentType(orderRequest.getPaymentType());

			// Enregistre l'adresse de la commande
			OrderAddress address = new OrderAddress();
			address.setFirstName(orderRequest.getFirstName());
			address.setLastName(orderRequest.getLastName());
			address.setEmail(orderRequest.getEmail());
			address.setMobileNo(orderRequest.getMobileNo());
			address.setAddress(orderRequest.getAddress());
			address.setCity(orderRequest.getCity());
			address.setState(orderRequest.getState());
			address.setPincode(orderRequest.getPincode());

			order.setOrderAddress(address);

			// Sauvegarde la commande et envoie un email de confirmation
			ProductOrder savedOrder = orderRepository.save(order);
			commonUtil.sendMailForProductOrder(savedOrder, "success");
		}
	}

	/**
	 * Récupère les commandes passées par un utilisateur spécifique.
	 *
	 * @param userId ID de l'utilisateur.
	 * @return Liste des commandes.
	 */
	@Override
	public List<ProductOrder> getOrdersByUser(Integer userId) {
		return orderRepository.findByUserId(userId);
	}

	/**
	 * Met à jour le statut d'une commande.
	 *
	 * @param id     ID de la commande.
	 * @param status Nouveau statut de la commande.
	 * @return La commande mise à jour.
	 */
	@Override
	public ProductOrder updateOrderStatus(Integer id, String status) {
		Optional<ProductOrder> findById = orderRepository.findById(id);

		if (findById.isPresent()) {
			ProductOrder productOrder = findById.get();
			productOrder.setStatus(status);
			return orderRepository.save(productOrder);
		}

		return null;
	}

	/**
	 * Récupère toutes les commandes disponibles dans le système.
	 *
	 * @return Liste de toutes les commandes.
	 */
	@Override
	public List<ProductOrder> getAllOrders() {
		return orderRepository.findAll();
	}

	/**
	 * Récupère une page de commandes paginée.
	 *
	 * @param pageNo   Numéro de la page.
	 * @param pageSize Taille de la page.
	 * @return Page de commandes.
	 */
	@Override
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return orderRepository.findAll(pageable);
	}

	/**
	 * Récupère une commande par son identifiant unique.
	 *
	 * @param orderId Identifiant de la commande.
	 * @return La commande correspondante ou null si introuvable.
	 */
	@Override
	public ProductOrder getOrdersByOrderId(String orderId) {
		return orderRepository.findByOrderId(orderId);
	}
}
