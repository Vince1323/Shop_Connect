package com.projet.ShopConnect.auth.model;

import com.projet.ShopConnect.model.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor

    public class RegistrationRequest extends AuthenticationRequest {

        private String firstname;
        private String lastname;
        private String birthDate;
        private String phone;
        @NonNull
        private Address address;
        private boolean hasReadTermsAndConditions;

    }
