package com.projet.ShopConnect.auth.service.implementation;

import com.projet.ShopConnect.auth.model.AuthenticationRequest;
import com.projet.ShopConnect.auth.model.AuthenticationResponse;
import com.projet.ShopConnect.auth.model.RegistrationRequest;
import com.projet.ShopConnect.auth.model.Token;
import com.projet.ShopConnect.auth.repository.TokenRepository;
import com.projet.ShopConnect.auth.service.AuthenticationService;
import com.projet.ShopConnect.auth.service.JwtService;
import com.projet.ShopConnect.model.Enum;
import com.projet.ShopConnect.repository.RoleRepository;


import com.projet.ShopConnect.service.UserService;
import com.projet.ShopConnect.model.Role;
import com.projet.ShopConnect.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;


    /**
     * @param request
     */
    @Override
    public void register(RegistrationRequest request) {
        Role userRole = roleRepository.findRoleByName(Enum.UserRole.USER.toString())
                .orElseThrow(() -> new NoSuchElementException());// 📌 A REVOIR

        Optional<User> existingUser = userService.findUserByEmail(request);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException(); // 📌 A REVOIR
        }
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .birthDate(LocalDate.parse(request.getBirthDate()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .hasReadTermsAndConditions(request.isHasReadTermsAndConditions())
                .verified(false)
                .build();



        userService.saveUser(user);

    }

    @Override
    public AuthenticationResponse authentication(AuthenticationRequest authenticationRequest) {
        return userService.findUserByEmail(authenticationRequest)
                .map(user -> {
                    if (!user.isVerified() || !user.isHasReadTermsAndConditions()) {
                        throw new AccessDeniedException("erreur1");
                    }
                    return authenticate(authenticationRequest);
                })
                .orElseThrow(() -> new BadCredentialsException("erreur2"));
    }


    private AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userService.findUserByEmail(request);
        revokeAllUserTokens(user.get());
        var jwtToken = jwtService.generateToken(user.get());
        saveUserToken(user.get(), jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .expiredAt(LocalDateTime.now().plus(jwtExpiration, ChronoUnit.MILLIS))
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
