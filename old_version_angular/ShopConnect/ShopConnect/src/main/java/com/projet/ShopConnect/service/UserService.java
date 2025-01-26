package com.projet.ShopConnect.service;

import com.projet.ShopConnect.auth.model.AuthenticationRequest;
import com.projet.ShopConnect.auth.model.RegistrationRequest;

import com.projet.ShopConnect.model.User;
import com.projet.ShopConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public List<User> getAllUsers() {
        return null;
    }

    public User getUserById(Long userId) {
        return null;
    }


    public Optional<User> findUserByEmail(AuthenticationRequest request) {
        return userRepository.findUserByEmail(request.getEmail());
    }



    public User register(RegistrationRequest request) throws UnsupportedEncodingException {
        return null;
    }


    public void confirmResetPassword(User user) throws UnsupportedEncodingException {

    }


    public void saveUser(User user) {
        userRepository.save(user);
    }

}
