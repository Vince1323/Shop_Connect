package com.shop_connect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shop_connect.model.Category;

/**
 * Repository pour gérer les opérations CRUD sur les catégories.
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {


	/**
	 * Vérifie si une catégorie existe par son nom.
	 * @param name Nom de la catégorie.
	 * @return Vrai si la catégorie existe, sinon faux.
	 */
	public Boolean existsByName(String name);

	/**
	 * Récupère toutes les catégories actives.
	 * @return Liste des catégories actives.
	 */
	public List<Category> findByIsActiveTrue();

}
