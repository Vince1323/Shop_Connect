package com.shop_connect.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shop_connect.model.Product;
import com.shop_connect.repository.ProductRepository;
import com.shop_connect.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	/**
	 * Sauvegarde un produit dans la base de données.
	 *
	 * @param product Le produit à sauvegarder.
	 * @return Le produit sauvegardé.
	 */
	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	/**
	 * Récupère tous les produits disponibles.
	 *
	 * @return Liste de tous les produits.
	 */
	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	/**
	 * Récupère tous les produits avec pagination.
	 *
	 * @param pageNo   Numéro de la page.
	 * @param pageSize Taille de la page.
	 * @return Une page de produits.
	 */
	@Override
	public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return productRepository.findAll(pageable);
	}

	/**
	 * Supprime un produit par son ID.
	 *
	 * @param id L'identifiant du produit.
	 * @return Vrai si le produit est supprimé, faux sinon.
	 */
	@Override
	public Boolean deleteProduct(Integer id) {
		Product product = productRepository.findById(id).orElse(null);

		if (!ObjectUtils.isEmpty(product)) {
			productRepository.delete(product);
			return true;
		}
		return false;
	}

	/**
	 * Récupère un produit par son ID.
	 *
	 * @param id L'identifiant du produit.
	 * @return Le produit correspondant ou null s'il n'existe pas.
	 */
	@Override
	public Product getProductById(Integer id) {
		return productRepository.findById(id).orElse(null);
	}

	/**
	 * Met à jour un produit existant.
	 *
	 * @param product Le produit avec les nouvelles données.
	 * @param image   L'image associée au produit.
	 * @return Le produit mis à jour.
	 */
	@Override
	public Product updateProduct(Product product, MultipartFile image) {
		Product dbProduct = getProductById(product.getId());

		// Détermine le nom de l'image
		String imageName = image.isEmpty() ? dbProduct.getImage() : image.getOriginalFilename();

		// Met à jour les champs du produit
		dbProduct.setTitle(product.getTitle());
		dbProduct.setDescription(product.getDescription());
		dbProduct.setCategory(product.getCategory());
		dbProduct.setPrice(product.getPrice());
		dbProduct.setStock(product.getStock());
		dbProduct.setImage(imageName);
		dbProduct.setIsActive(product.getIsActive());
		dbProduct.setDiscount(product.getDiscount());

		// Calcule le prix après remise
		Double discountAmount = product.getPrice() * (product.getDiscount() / 100.0);
		Double discountPrice = product.getPrice() - discountAmount;

		// Arrondir à une décimale
		discountPrice = Math.round(discountPrice * 10.0) / 10.0;
		dbProduct.setDiscountPrice(discountPrice);


		Product updatedProduct = productRepository.save(dbProduct);

		// Gère le téléchargement de l'image si elle n'est pas vide
		if (!ObjectUtils.isEmpty(updatedProduct) && !image.isEmpty()) {
			try {
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator + image.getOriginalFilename());
				Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return updatedProduct;
	}

	/**
	 * Récupère tous les produits actifs selon une catégorie.
	 *
	 * @param category La catégorie des produits (peut être vide).
	 * @return Liste des produits actifs.
	 */
	@Override
	public List<Product> getAllActiveProducts(String category) {
		if (ObjectUtils.isEmpty(category)) {
			return productRepository.findByIsActiveTrue();
		} else {
			return productRepository.findByCategory(category);
		}
	}

	/**
	 * Recherche des produits selon un critère.
	 *
	 * @param ch Le critère de recherche.
	 * @return Liste des produits correspondant au critère.
	 */
	@Override
	public List<Product> searchProduct(String ch) {
		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
	}

	/**
	 * Recherche des produits avec pagination.
	 *
	 * @param pageNo   Numéro de la page.
	 * @param pageSize Taille de la page.
	 * @param ch       Critère de recherche.
	 * @return Une page de produits correspondant au critère.
	 */
	@Override
	public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch, pageable);
	}

	/**
	 * Récupère les produits actifs avec pagination selon une catégorie.
	 *
	 * @param pageNo   Numéro de la page.
	 * @param pageSize Taille de la page.
	 * @param category Catégorie des produits.
	 * @return Une page de produits actifs.
	 */
	@Override
	public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		if (ObjectUtils.isEmpty(category)) {
			return productRepository.findByIsActiveTrue(pageable);
		} else {
			return productRepository.findByCategory(pageable, category);
		}
	}

	/**
	 * Recherche des produits actifs avec pagination selon une catégorie et un critère.
	 *
	 * @param pageNo   Numéro de la page.
	 * @param pageSize Taille de la page.
	 * @param category Catégorie des produits.
	 * @param ch       Critère de recherche.
	 * @return Une page de produits correspondant.
	 */
	@Override
	public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String category, String ch) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return productRepository.findByisActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch, pageable);
	}
}
