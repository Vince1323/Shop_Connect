package com.projet.ShopConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Boutique extends Identified {

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String description;

    private String adresse;
    private String urlSlug;
    private String telephone;

    @ManyToOne
    private User utilisateur;

    @OneToMany(mappedBy = "boutique")
    private List<BoutiqueProduit> produits;

    @OneToMany(mappedBy = "boutique")
    private List<PromotionBoutique> promotions;
}