package com.shop_connect.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.shop_connect.model.UserDtls;
import com.shop_connect.repository.UserRepository;
import com.shop_connect.service.UserService;
import com.shop_connect.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Classe pour gérer les échecs d'authentification.
 */
@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
										AuthenticationException exception) throws IOException, ServletException {

		// Récupère l'email de l'utilisateur depuis le formulaire de connexion.
		String email = request.getParameter("username");

		UserDtls userDtls = userRepository.findByEmail(email);

		if (userDtls != null) {
			if (userDtls.getIsEnable()) {
				if (userDtls.getAccountNonLocked()) {
					if (userDtls.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
						// Incrémente les tentatives échouées.
						userService.increaseFailedAttempt(userDtls);
					} else {
						// Verrouille le compte après plusieurs échecs.
						userService.userAccountLock(userDtls);
						exception = new LockedException("Votre compte est verrouillé après 3 tentatives.");
					}
				} else {
					// Gestion du déverrouillage selon le temps écoulé.
					if (userService.unlockAccountTimeExpired(userDtls)) {
						exception = new LockedException("Votre compte est déverrouillé. Essayez à nouveau.");
					} else {
						exception = new LockedException("Votre compte est verrouillé. Réessayez plus tard.");
					}
				}
			} else {
				exception = new LockedException("Votre compte est inactif.");
			}
		} else {
			exception = new LockedException("Email ou mot de passe invalide.");
		}

		// Redirection vers la page de connexion avec un message d'erreur.
		super.setDefaultFailureUrl("/signin?error");
		super.onAuthenticationFailure(request, response, exception);
	}
}
