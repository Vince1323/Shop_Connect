package com.projet.ShopConnect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projet.ShopConnect.auth.model.Token;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
@EntityListeners({AuditingEntityListener.class})
public class User extends Identified implements UserDetails, Principal{

    private String firstname;
    private String lastname;
    private LocalDate birthDate;
    private String phone;
    @Column(unique = true)
    private String email;
    private String password;
    @ColumnDefault("false")
    private boolean verified;
    @ColumnDefault("false")
    private boolean validEmail;
    private boolean hasReadTermsAndConditions;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Address.class)
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)

    private List<Token> tokens;


    /**
     * @return l'email de ce Principal.
     */
    @Override
    public String getName() {
        return email;
    }

    /**
     * Renvoie la collection des autorisations accordées à l'utilisateur.
     *
     * @return La collection des autorisations, représentées par des instances `GrantedAuthority`.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role.getName()));
    }

    /**
     * @return le mot de passe de cet utilisateur.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @return l'email de cet utilisateur.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * @return 'true' si le compte n'est pas expiré, 'false' sinon.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return 'true' si les informations d'identification de l'utilisateur sont valides et non périmées, 'false' sinon.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return 'true' si le compte est activé, 'false' sinon.
     */
    @Override
    public boolean isEnabled() {
        return verified;
    }

    public String getFullName() {
        return firstname.substring(0, 1).toUpperCase() + firstname.substring(1).toLowerCase() + " " + lastname.toUpperCase();
    }

}
