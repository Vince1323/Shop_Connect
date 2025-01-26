package com.shop_connect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop_connect.model.UserDtls;

/**
 * Repository pour gérer les opérations CRUD sur les utilisateurs.
 */
public interface UserRepository extends JpaRepository<UserDtls, Integer> {

	/**
	 * Trouve un utilisateur par son email.
	 * @param email Adresse email de l'utilisateur.
	 * @return L'utilisateur correspondant.
	 */
	public UserDtls findByEmail(String email);

	/**
	 * Récupère tous les utilisateurs ayant un rôle spécifique.
	 * @param role Rôle des utilisateurs recherchés.
	 * @return Liste des utilisateurs avec ce rôle.
	 */
	public List<UserDtls> findByRole(String role);

	/**
	 * Trouve un utilisateur par son token de réinitialisation.
	 * @param token Token de réinitialisation.
	 * @return L'utilisateur correspondant.
	 */
	public UserDtls findByResetToken(String token);

	/**
	 * Vérifie si un utilisateur existe par son email.
	 * @param email Adresse email.
	 * @return Vrai si l'utilisateur existe, sinon faux.
	 */
	public Boolean existsByEmail(String email);
}
