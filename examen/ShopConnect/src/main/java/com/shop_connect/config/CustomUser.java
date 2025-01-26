package com.shop_connect.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shop_connect.model.UserDtls;

/**
 * Classe personnalisée pour les détails des utilisateurs.
 */
public class CustomUser implements UserDetails {

	private UserDtls user;

	public CustomUser(UserDtls user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Retourne les rôles de l'utilisateur.
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
		return Arrays.asList(authority);
	}

	@Override
	public String getPassword() {
		return user.getPassword(); // Retourne le mot de passe.
	}

	@Override
	public String getUsername() {
		return user.getEmail(); // Retourne l'email comme nom d'utilisateur.
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Compte toujours valide.
	}

	@Override
	public boolean isAccountNonLocked() {
		return user.getAccountNonLocked(); // Vérifie si le compte est verrouillé.
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Les informations d'identification ne sont jamais expirées.
	}

	@Override
	public boolean isEnabled() {
		return user.getIsEnable(); // Vérifie si le compte est actif.
	}
}
