package com.shop_connect.service.impl;

import com.shop_connect.config.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shop_connect.model.UserDtls;
import com.shop_connect.repository.UserRepository;

/**
 * Service pour charger les utilisateurs par email.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDtls user = userRepository.findByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException("Utilisateur non trouvé");
		}
		return new CustomUser(user); // Retourne un utilisateur personnalisé.
	}
}
