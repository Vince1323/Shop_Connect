package com.shop_connect.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Classe pour gérer les succès d'authentification.
 */
@Service
public class AuthSucessHandlerImpl implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {

		// Récupère les rôles de l'utilisateur connecté.
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Set<String> roles = AuthorityUtils.authorityListToSet(authorities);

		// Redirection selon le rôle.
		if (roles.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/"); // Redirection pour les administrateurs.
		} else {
			response.sendRedirect("/"); // Redirection pour les utilisateurs normaux.
		}
	}
}
