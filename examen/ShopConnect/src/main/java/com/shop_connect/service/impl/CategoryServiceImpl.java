package com.shop_connect.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.shop_connect.model.Category;
import com.shop_connect.repository.CategoryRepository;
import com.shop_connect.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * Sauvegarde une nouvelle catégorie.
	 */
	@Override
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	/**
	 * Récupère toutes les catégories.
	 */
	@Override
	public List<Category> getAllCategory() {
		return categoryRepository.findAll();
	}

	/**
	 * Vérifie si une catégorie exist par son nom.
	 */
	@Override
	public Boolean existCategory(String name) {
		return categoryRepository.existsByName(name);
	}

	/**
	 * Supprime une catégorie par son ID.
	 */
	@Override
	public Boolean deleteCategory(int id) {
		Category category = categoryRepository.findById(id).orElse(null);

		if (!ObjectUtils.isEmpty(category)) {
			categoryRepository.delete(category);
			return true;
		}
		return false;
	}

	/**
	 * Récupère une catégorie par son ID.
	 */
	@Override
	public Category getCategoryById(int id) {
		return categoryRepository.findById(id).orElse(null);
	}

	/**
	 * Récupère toutes les catégories actives.
	 */
	@Override
	public List<Category> getAllActiveCategory() {
		return categoryRepository.findByIsActiveTrue();
	}

	/**
	 * Récupère une page paginée de catégories.
	 */
	@Override
	public Page<Category> getAllCategorPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return categoryRepository.findAll(pageable);
	}
}
