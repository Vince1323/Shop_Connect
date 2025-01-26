package com.shop_connect.service;

import java.util.List;
import org.springframework.data.domain.Page;
import com.shop_connect.model.Category;

/**
 * Interface pour gérer les catégories.
 */
public interface CategoryService {

	/**
	 * Sauvegarde une catégorie.
	 * @param category L'objet catégorie à sauvegarder.
	 * @return La catégorie sauvegardée.
	 */
	public Category saveCategory(Category category);

	/**
	 * Vérifie si une catégorie existe par son nom.
	 * @param name Nom de la catégorie.
	 * @return `true` si elle existe, sinon `false`.
	 */
	public Boolean existCategory(String name);

	/**
	 * Récupère toutes les catégories.
	 * @return Une liste de catégories.
	 */
	public List<Category> getAllCategory();

	/**
	 * Supprime une catégorie par son ID.
	 * @param id L'ID de la catégorie.
	 * @return `true` si la suppression a réussi, sinon `false`.
	 */
	public Boolean deleteCategory(int id);

	/**
	 * Récupère une catégorie par son ID.
	 * @param id L'ID de la catégorie.
	 * @return La catégorie correspondante.
	 */
	public Category getCategoryById(int id);

	/**
	 * Récupère toutes les catégories actives.
	 * @return Une liste de catégories actives.
	 */
	public List<Category> getAllActiveCategory();

	/**
	 * Récupère une page paginée de catégories.
	 * @param pageNo Numéro de la page.
	 * @param pageSize Taille de la page.
	 * @return Une page d'objets `Category`.
	 */
	public Page<Category> getAllCategorPagination(Integer pageNo, Integer pageSize);
}
