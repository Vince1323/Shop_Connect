package com.shop_connect.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.shop_connect.model.Cart;
import com.shop_connect.model.Product;
import com.shop_connect.model.UserDtls;
import com.shop_connect.repository.CartRepository;
import com.shop_connect.repository.ProductRepository;
import com.shop_connect.repository.UserRepository;
import com.shop_connect.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	/**
	 * Ajoute un produit au panier de l'utilisateur.
	 */
	@Override
	public Cart saveCart(Integer productId, Integer userId) {
		UserDtls user = userRepository.findById(userId).orElse(null);
		Product product = productRepository.findById(productId).orElse(null);
		Cart cart = cartRepository.findByProductIdAndUserId(productId, userId);

		if (ObjectUtils.isEmpty(cart)) {
			cart = new Cart();
			cart.setProduct(product);
			cart.setUser(user);
			cart.setQuantity(1);
			cart.setTotalPrice(product.getDiscountPrice());
		} else {
			cart.setQuantity(cart.getQuantity() + 1);
			cart.setTotalPrice(cart.getQuantity() * product.getDiscountPrice());
		}

		return cartRepository.save(cart);
	}

	/**
	 * Récupère les produits du panier d'un utilisateur.
	 */
	@Override
	public List<Cart> getCartsByUser(Integer userId) {
		List<Cart> carts = cartRepository.findByUserId(userId);
		Double totalOrderPrice = 0.0;
		List<Cart> updatedCarts = new ArrayList<>();

		for (Cart cart : carts) {
			// Calcul du prix total pour le produit
			Double totalPrice = cart.getQuantity() * cart.getProduct().getDiscountPrice();
			totalPrice = Math.round(totalPrice * 10.0) / 10.0; // Arrondir à une décimale
			cart.setTotalPrice(totalPrice);

			// Mise à jour du prix total de la commande
			totalOrderPrice += totalPrice;
			totalOrderPrice = Math.round(totalOrderPrice * 10.0) / 10.0; // Arrondir à une décimale
			cart.setTotalOrderPrice(totalOrderPrice);

			updatedCarts.add(cart);
		}

		return updatedCarts;
	}

	/**
	 * Compte le nombre d'articles dans le panier d'un utilisateur.
	 */
	@Override
	public Integer getCountCart(Integer userId) {
		return cartRepository.countByUserId(userId);
	}

	/**
	 * Met à jour la quantité d'un produit dans le panier.
	 */
	@Override
	public void updateQuantity(String action, Integer cartId) {
		Cart cart = cartRepository.findById(cartId).orElse(null);

		if ("de".equalsIgnoreCase(action)) {
			int updatedQuantity = cart.getQuantity() - 1;

			if (updatedQuantity <= 0) {
				cartRepository.delete(cart);
			} else {
				cart.setQuantity(updatedQuantity);
				cartRepository.save(cart);
			}
		} else {
			cart.setQuantity(cart.getQuantity() + 1);
			cartRepository.save(cart);
		}
	}
}
