package com.projet.ShopConnect.auth.service;

import com.projet.ShopConnect.auth.model.AuthenticationRequest;
import com.projet.ShopConnect.auth.model.AuthenticationResponse;
import com.projet.ShopConnect.auth.model.RegistrationRequest;

public interface AuthenticationService {

    void register(RegistrationRequest request);

    AuthenticationResponse authentication(AuthenticationRequest authenticationRequest);

}
