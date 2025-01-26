package com.shop_connect.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop_connect.model.Product;


/**
 * Repository pour gérer les opérations CRUD sur les produits.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {

	/**
	 * Récupère tous les produits actifs.
	 * @return Liste des produits actifs.
	 */
	List<Product> findByIsActiveTrue();


	/**
	 * Récupère une page de produits actifs.
	 * @param pageable Objet pour la pagination.
	 * @return Page de produits actifs.
	 */
	Page<Product> findByIsActiveTrue(Pageable pageable);


	/**
	 * Récupère tous les produits d'une catégorie donnée.
	 * @param category Catégorie des produits.
	 * @return Liste des produits de la catégorie.
	 */
	List<Product> findByCategory(String category);


	/**
	 * Recherche des produits par titre ou catégorie (ignorer les majuscules).
	 * @param ch Mot-clé de recherche.
	 * @param ch2 Deuxième mot-clé (catégorie).
	 * @return Liste des produits correspondants.
	 */
	List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);

	/**
	 * Recherche paginée par catégorie.
	 * @param pageable Objet pour la pagination.
	 * @param category Catégorie recherchée.
	 * @return Page de produits correspondant.
	 */
	Page<Product> findByCategory(Pageable pageable, String category);

	/**
	 * Recherche des produits par titre ou catégorie (ignorer les majuscules).
	 * @param ch Mot-clé de recherche.
	 * @param ch2 Deuxième mot-clé (catégorie).
	 * @return Liste des produits correspondants.
	 */
	Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
			Pageable pageable);

	/**
	 * Recherche paginée par titre ou catégorie avec filtre d'activation.
	 * @param ch Mot-clé de recherche.
	 * @param ch2 Deuxième mot-clé.
	 * @param pageable Objet pour la pagination.
	 * @return Page de produits correspondants.
	 */

	Page<Product> findByisActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
			Pageable pageable);
}
